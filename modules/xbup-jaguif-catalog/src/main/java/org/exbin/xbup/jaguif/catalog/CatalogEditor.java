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
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ContextStateManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.jaguif.context.api.ContextMonitoringManagement;
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

    protected ContextStateManagement treeContextStateManager;
    protected ContextStateManagement itemContextStateManager;

    protected ExportItemAction exportItemAction;
    protected ImportItemAction importItemAction;
    protected ExportItemAction exportTreeItemAction;
    protected ImportItemAction importTreeItemAction;
    protected XBCRoot catalogRoot;

    public CatalogEditor() {
        catalogEditorPanel = new CatalogEditorPanel();

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        treeContextStateManager = contextModule.createStateManager();
        itemContextStateManager = contextModule.createStateManager();

        exportItemAction = new ExportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getCurrentItem());
                super.actionPerformed(event);
            }
        };
        exportItemAction.init();
        importItemAction = new ImportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getCurrentItem());
                super.actionPerformed(event);
            }
        };
        importItemAction.init();
        exportTreeItemAction = new ExportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                super.actionPerformed(event);
            }
        };
        exportTreeItemAction.init();
        importTreeItemAction = new ImportItemAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                super.actionPerformed(event);
            }
        };
        importTreeItemAction.init();

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
            treeContextStateManager.changeActiveState(ContextEditItem.class, treeController);
            treeContextStateManager.changeActiveState(DialogParentComponent.class, (DialogParentComponent) () -> catalogEditorPanel);
            catalogEditorPanel.addTreeSelectionListener((arg0) -> {
                treeContextStateManager.updateActiveState(ContextEditItem.class, treeController, ContextEditItem.UpdateType.EDIT_STATE);
            });
            ContextMonitoringManagement monitoringManagement = contextModule.createMonitoringManager(treeContextStateManager);
            ContextMonitoringRegistration monitoringRegistrar = contextModule.createMonitoringRegistrator(monitoringManagement, treeContextStateManager);

            ToolBarDefinitionManagement toolBarDefinition = toolBarModule.createToolBarDefinition(toolBarManager, TREE_TOOLBAR_ID, XbupCatalogModule.MODULE_ID);
            DefaultEditItemActions editItemActions = new DefaultEditItemActions();
            editItemActions.registerToolBarContributions(toolBarDefinition);
            toolBarManager.buildIconToolBar(catalogEditorPanel.getTreeToolBar(), TREE_TOOLBAR_ID, monitoringRegistrar);
            treeController.registerMonitoring(monitoringRegistrar);
            monitoringRegistrar.finish();
        }

        {
            CatalogEditorItemController itemController = new CatalogEditorItemController(catalogEditorPanel);
            itemContextStateManager.changeActiveState(ContextEditItem.class, itemController);
            itemContextStateManager.changeActiveState(DialogParentComponent.class, (DialogParentComponent) () -> catalogEditorPanel);
            catalogEditorPanel.addItemSelectionListener((arg0) -> {
                itemContextStateManager.updateActiveState(ContextEditItem.class, itemController, ContextEditItem.UpdateType.EDIT_STATE);
            });
            ContextMonitoringManagement monitoringManagement = contextModule.createMonitoringManager(itemContextStateManager);
            ContextMonitoringRegistration monitoringRegistrar = contextModule.createMonitoringRegistrator(monitoringManagement, itemContextStateManager);

            ToolBarDefinitionManagement toolBarDefinition = toolBarModule.createToolBarDefinition(toolBarManager, ITEM_TOOLBAR_ID, XbupCatalogModule.MODULE_ID);
            DefaultEditItemActions editItemActions = new DefaultEditItemActions();
            editItemActions.registerToolBarContributions(toolBarDefinition);
            toolBarManager.buildIconToolBar(catalogEditorPanel.addItemToolBar(), ITEM_TOOLBAR_ID, monitoringRegistrar);
            itemController.registerMonitoring(monitoringRegistrar);
            monitoringRegistrar.finish();
        }
    }

    public CatalogEditorPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        treeContextStateManager.changeActiveState(XBACatalog.class, catalog);
        itemContextStateManager.changeActiveState(XBACatalog.class, catalog);

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
