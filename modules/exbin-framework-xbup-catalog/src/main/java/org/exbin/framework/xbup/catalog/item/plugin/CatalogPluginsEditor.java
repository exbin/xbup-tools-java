/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.framework.xbup.catalog.item.plugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.DefaultActionContextService;
import org.exbin.framework.menu.api.MenuManagement;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.toolbar.api.ToolBarManager;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.framework.xbup.catalog.item.plugin.ation.AddItemPluginAction;
import org.exbin.framework.xbup.catalog.item.plugin.ation.EditItemPluginAction;
import org.exbin.framework.xbup.catalog.item.plugin.gui.CatalogItemEditPluginsPanel;
import org.exbin.framework.xbup.catalog.item.plugin.gui.CatalogPluginsTableModel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Catalog plugins editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogPluginsEditor {

    public static final String TOOLBAR_ID = "CatalogPluginsEditor.toolBar";

    private final CatalogItemEditPluginsPanel catalogEditorPanel;
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private XBCNode node;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogPluginsEditor.class);

    private AddItemPluginAction addPluginAction = new AddItemPluginAction();
    private EditItemPluginAction editPluginAction = new EditItemPluginAction();

    public CatalogPluginsEditor() {
        catalogEditorPanel = new CatalogItemEditPluginsPanel();
        editActions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        init();
    }
    
    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManager toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");
        DefaultActionContextService actionContextService = new DefaultActionContextService();
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createAddItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createEditItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createDeleteItemAction());
        EditItemActionsHandler editItemActionsHandler = new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addPluginAction.setCurrentNode(node);
                addPluginAction.actionPerformed(null);
                AddItemPluginAction.ResultData resultData = addPluginAction.getResultData();
                if (resultData != null) {
                    CatalogPluginsTableModel pluginsModel = catalogEditorPanel.getPluginsModel();
                    pluginsModel.addItem(resultData.plugin, resultData.file, resultData.rowEditorsCount, resultData.panelViewersCount, resultData.panelEditorsCount);
//                    catalogEditorPanel.reloadNodesTree();
                }
            }

            @Override
            public void performEditItem() {
                editPluginAction.setCurrentPlugin(catalogEditorPanel.getSelectedPlugin());
                editPluginAction.actionPerformed(null);
            }

            @Override
            public void performDeleteItem() {
//                deleteCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
//                deleteCatalogItemAction.actionPerformed(null);
            }

            @Override
            public boolean canAddItem() {
                return true;
            }

            @Override
            public boolean canEditItem() {
                XBCXPlugin plugin = catalogEditorPanel.getSelectedPlugin();
                return plugin != null;
            }

            @Override
            public boolean canDeleteItem() {
                return false;
//                XBCNode node = catalogEditorPanel.getSelectedTreeItem();
//                return node != null && node.getParent().isPresent();
            }
        };
        actionContextService.updated(EditItemActionsHandler.class, editItemActionsHandler);
        catalogEditorPanel.addSelectionListener((lse) -> {
            actionContextService.updated(EditItemActionsHandler.class, editItemActionsHandler);        
        });
        toolBarManager.buildIconToolBar(catalogEditorPanel.getToolBar(), TOOLBAR_ID, actionContextService);

        addPluginAction.setParentComponent(catalogEditorPanel);
        editPluginAction.setParentComponent(catalogEditorPanel);

        popupMenu = new JPopupMenu();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addPluginMenuItem = actionModule.actionToMenuItem(editActions.createAddItemAction());
        addPluginMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addPluginMenuItem.text"));
        popupMenu.add(addPluginMenuItem);
        JMenuItem editPluginMenuItem = actionModule.actionToMenuItem(editActions.createEditItemAction());
        editPluginMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editPluginMenuItem.text"));
        popupMenu.add(editPluginMenuItem);

        catalogEditorPanel.setPanelPopup(popupMenu);

        catalogEditorPanel.addFileActions(editActions);
    }

    @Nonnull
    public CatalogItemEditPluginsPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        addPluginAction.setCatalog(catalog);
        editPluginAction.setCatalog(catalog);
    }

    public void setNode(XBCNode node) {
        this.node = node;
        catalogEditorPanel.setNode(node);
    }

    public void setMenuManagement(MenuManagement menuManagement) {

    }

//    public void persist() {
//        catalogEditorPanel.persist();
//    }
}
