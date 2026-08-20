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
package org.exbin.xbup.jaguif.catalog;

import java.awt.event.ActionEvent;
import org.jspecify.annotations.NullMarked;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.jaguif.App;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextRegistration;
import org.exbin.jaguif.context.api.ContextUpdateManagement;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.toolbar.api.ToolBarDefinitionManagement;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.jaguif.catalog.gui.CatalogEditorPanel;
import org.exbin.xbup.jaguif.catalog.item.action.ExportItemAction;
import org.exbin.xbup.jaguif.catalog.item.action.ImportItemAction;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalog editor.
 */
@NullMarked
public class CatalogEditor {

    public static final String TREE_TOOLBAR_ID = "CatalogEditor.treeToolBar";
    public static final String ITEM_TOOLBAR_ID = "CatalogEditor.itemToolBar";

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogEditor.class);

    protected final CatalogEditorPanel catalogEditorPanel;
    protected final DefaultEditItemActions treeActions;
    protected final DefaultEditItemActions itemActions;
    protected XBACatalog catalog;
    protected JPopupMenu catalogTreePopupMenu;
    protected JPopupMenu catalogItemPopupMenu;

    protected ActiveContextManagement treeContextManager;
    protected ActiveContextManagement itemContextManager;

    protected ExportItemAction exportItemAction;
    protected ImportItemAction importItemAction;
    protected ExportItemAction exportTreeItemAction;
    protected ImportItemAction importTreeItemAction;
    protected XBCRoot catalogRoot;

    public CatalogEditor() {
        catalogEditorPanel = new CatalogEditorPanel();

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        treeContextManager = contextModule.createContextManager();
        itemContextManager = contextModule.createContextManager();

        exportItemAction = new ExportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getCurrentItem());
                super.actionPerformed(event);
            }
        };
        exportItemAction.setParentComponent(catalogEditorPanel);
        importItemAction = new ImportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getCurrentItem());
                super.actionPerformed(event);
            }
        };
        importItemAction.setParentComponent(catalogEditorPanel);
        exportTreeItemAction = new ExportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                super.actionPerformed(event);
            }
        };
        exportTreeItemAction.setParentComponent(catalogEditorPanel);
        importTreeItemAction = new ImportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                super.actionPerformed(event);
            }
        };
        importTreeItemAction.setParentComponent(catalogEditorPanel);

        treeActions = new DefaultEditItemActions(EditItemMode.DIALOG);
        itemActions = new DefaultEditItemActions(EditItemMode.DIALOG);

        catalogTreePopupMenu = new JPopupMenu();
        catalogEditorPanel.setTreePanelPopup(catalogTreePopupMenu);

        catalogItemPopupMenu = new JPopupMenu();
        catalogEditorPanel.setItemPanelPopup(catalogItemPopupMenu);

        init();
    }

    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TREE_TOOLBAR_ID, "");
        toolBarManager.registerToolBar(ITEM_TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        {
            CatalogEditorTreeController treeController = new CatalogEditorTreeController(catalogEditorPanel);
            treeContextManager.changeActiveState(ContextEditItem.class, treeController);
            catalogEditorPanel.addTreeSelectionListener((arg0) -> {
                treeContextManager.updateActiveState(ContextEditItem.class, treeController, ContextEditItem.UpdateType.EDIT_STATE);
            });
            ContextUpdateManagement updateManagement = contextModule.createContextUpdateManagement(treeContextManager);
            ContextRegistration contextRegistrar = contextModule.createContextRegistrator("", updateManagement, treeContextManager);

            ToolBarDefinitionManagement toolBarDefinition = toolBarModule.createToolBarDefinition(toolBarManager, TREE_TOOLBAR_ID, XbupCatalogModule.MODULE_ID);
            DefaultEditItemActions editItemActions = new DefaultEditItemActions();
            editItemActions.registerToolBarContributions(toolBarDefinition);
            toolBarManager.buildIconToolBar(catalogEditorPanel.getTreeToolBar(), TREE_TOOLBAR_ID, contextRegistrar);
        }

        {
            CatalogEditorItemController itemController = new CatalogEditorItemController(catalogEditorPanel);
            itemContextManager.changeActiveState(ContextEditItem.class, itemController);
            catalogEditorPanel.addItemSelectionListener((arg0) -> {
                itemContextManager.updateActiveState(ContextEditItem.class, itemController, ContextEditItem.UpdateType.EDIT_STATE);
            });
            ContextUpdateManagement updateManagement = contextModule.createContextUpdateManagement(itemContextManager);
            ContextRegistration contextRegistrar = contextModule.createContextRegistrator("", updateManagement, itemContextManager);

            ToolBarDefinitionManagement toolBarDefinition = toolBarModule.createToolBarDefinition(toolBarManager, ITEM_TOOLBAR_ID, XbupCatalogModule.MODULE_ID);
            DefaultEditItemActions editItemActions = new DefaultEditItemActions();
            editItemActions.registerToolBarContributions(toolBarDefinition);
            toolBarManager.buildIconToolBar(catalogEditorPanel.addItemToolBar(), ITEM_TOOLBAR_ID, contextRegistrar);
        }
    }

    public CatalogEditorPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        exportItemAction.init(catalog);
        importItemAction.init(catalog);
        exportTreeItemAction.init(catalog);
        importTreeItemAction.init(catalog);
        treeContextManager.changeActiveState(XBACatalog.class, catalog);
        itemContextManager.changeActiveState(XBACatalog.class, catalog);

        XbupCatalogModule managerModule = App.getModule(XbupCatalogModule.class);
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);

        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        if (catalogTreePopupMenu.getComponentCount() == 0) {
            JMenuItem addTreeItem = menuModule.actionToMenuItem(treeActions.createAddItemAction());
            addTreeItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addTreeItem.text"));
            catalogTreePopupMenu.add(addTreeItem);
            JMenuItem editTreeItem = menuModule.actionToMenuItem(treeActions.createEditItemAction());
            editTreeItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editTreeItem.text"));
            catalogTreePopupMenu.add(editTreeItem);
            catalogTreePopupMenu.addSeparator();
            catalogTreePopupMenu.addSeparator();
            catalogTreePopupMenu.add(menuModule.actionToMenuItem(exportTreeItemAction));
            catalogTreePopupMenu.add(menuModule.actionToMenuItem(importTreeItemAction));
            // menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 3);
        }
        if (catalogItemPopupMenu.getComponentCount() == 0) {
            JMenuItem addCatalogItem = menuModule.actionToMenuItem(itemActions.createAddItemAction());
            addCatalogItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addCatalogItem.text"));
            catalogItemPopupMenu.add(addCatalogItem);
            JMenuItem editCatalogItem = menuModule.actionToMenuItem(itemActions.createEditItemAction());
            editCatalogItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editCatalogItem.text"));
            catalogItemPopupMenu.add(editCatalogItem);
            catalogItemPopupMenu.addSeparator();
            catalogItemPopupMenu.addSeparator();
            catalogItemPopupMenu.add(menuModule.actionToMenuItem(exportItemAction));
            catalogItemPopupMenu.add(menuModule.actionToMenuItem(importItemAction));
            // menuManagement.insertMainPopupMenu(catalogItemPopupMenu, 3);
        }
    }

    public void setCatalogRoot(XBCRoot catalogRoot) {
        this.catalogRoot = catalogRoot;
        catalogEditorPanel.setCatalogRoot(catalogRoot);
    }
}
