/*
 * Copyright (C) ExBin Project, https://exbin.org
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
package org.exbin.xbup.jaguif.catalog.item.plugin;

import org.jspecify.annotations.NullMarked;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.component.action.AddItemAction;
import org.exbin.jaguif.menu.api.MenuManagement;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.DeleteItemAction;
import org.exbin.jaguif.component.action.EditItemAction;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.component.api.ContextMoveItem;
import org.exbin.jaguif.context.api.ContextStateManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.toolbar.api.ActionToolBarContribution;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.jaguif.catalog.item.plugin.gui.CatalogItemEditPluginsPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Catalog plugins editor.
 */
@NullMarked
public class CatalogPluginsEditor {

    public static final String TOOLBAR_ID = "CatalogPluginsEditor.toolBar";

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogPluginsEditor.class);

    protected final CatalogItemEditPluginsPanel catalogEditorPanel;
    protected final DefaultEditItemActions editActions;
    protected XBACatalog catalog;
    protected JPopupMenu popupMenu;
    protected XBCNode node;

    protected ContextStateManagement itemContextStateManager;

    private ContextMoveItem contextMoveItem;

    public CatalogPluginsEditor() {
        catalogEditorPanel = new CatalogItemEditPluginsPanel();
        editActions = new DefaultEditItemActions(EditItemMode.DIALOG);
        init();
    }
    
    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        itemContextStateManager = contextModule.createStateManager();
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Override
            public Action createAction() {
                return editActions.createAddItemAction();
            }

            @Override
            public String getContributionId() {
                return AddItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Override
            public Action createAction() {
                return editActions.createEditItemAction();
            }

            @Override
            public String getContributionId() {
                return EditItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Override
            public Action createAction() {
                return editActions.createDeleteItemAction();
            }

            @Override
            public String getContributionId() {
                return DeleteItemAction.ACTION_ID;
            }
        });
        CatalogPluginsEditorController itemController = new CatalogPluginsEditorController(catalogEditorPanel);
        itemContextStateManager.changeActiveState(ContextEditItem.class, itemController);
        itemContextStateManager.changeActiveState(DialogParentComponent.class, (DialogParentComponent) () -> catalogEditorPanel);
        catalogEditorPanel.addSelectionListener((lse) -> {
            itemContextStateManager.changeActiveState(ContextEditItem.class, itemController);        
        });
        ContextMonitoringRegistration monitoringRegistrar = contextModule.createMonitoringRegistrator();
        toolBarManager.buildIconToolBar(catalogEditorPanel.getToolBar(), TOOLBAR_ID, monitoringRegistrar);

        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        popupMenu = new JPopupMenu();
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addPluginMenuItem = menuModule.actionToMenuItem(editActions.createAddItemAction());
        addPluginMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addPluginMenuItem.text"));
        popupMenu.add(addPluginMenuItem);
        JMenuItem editPluginMenuItem = menuModule.actionToMenuItem(editActions.createEditItemAction());
        editPluginMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editPluginMenuItem.text"));
        popupMenu.add(editPluginMenuItem);

        catalogEditorPanel.setPanelPopup(popupMenu);

        itemController.registerMonitoring(monitoringRegistrar);
        monitoringRegistrar.finish();

        // TODO catalogEditorPanel.addFileActions(editActions);
    }

    public CatalogItemEditPluginsPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        itemContextStateManager.changeActiveState(XBACatalog.class, catalog);
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
