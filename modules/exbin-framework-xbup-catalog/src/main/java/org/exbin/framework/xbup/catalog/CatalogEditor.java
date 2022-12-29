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
package org.exbin.framework.xbup.catalog;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.xbup.catalog.gui.CatalogEditorPanel;
import org.exbin.framework.xbup.catalog.item.action.AddCatalogItemAction;
import org.exbin.framework.xbup.catalog.item.action.DeleteCatalogItemAction;
import org.exbin.framework.xbup.catalog.item.action.EditCatalogItemAction;
import org.exbin.framework.xbup.catalog.item.action.ExportItemAction;
import org.exbin.framework.xbup.catalog.item.action.ImportItemAction;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalog editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogEditor {

    private final CatalogEditorPanel catalogEditorPanel;
    private final DefaultEditItemActions treeActions;
    private final DefaultEditItemActions itemActions;
    private XBApplication application;
    private XBACatalog catalog;
    private JPopupMenu catalogTreePopupMenu;
    private JPopupMenu catalogItemPopupMenu;

    private AddCatalogItemAction addCatalogItemAction = new AddCatalogItemAction();
    private EditCatalogItemAction editCatalogItemAction = new EditCatalogItemAction();
    private DeleteCatalogItemAction deleteCatalogItemAction = new DeleteCatalogItemAction();
    private ExportItemAction exportItemAction = new ExportItemAction();
    private ImportItemAction importItemAction = new ImportItemAction();

    public CatalogEditor() {
        catalogEditorPanel = new CatalogEditorPanel();

        addCatalogItemAction.setParentComponent(catalogEditorPanel);
        editCatalogItemAction.setParentComponent(catalogEditorPanel);
        deleteCatalogItemAction.setParentComponent(catalogEditorPanel);
        exportItemAction.setParentComponent(catalogEditorPanel);
        importItemAction.setParentComponent(catalogEditorPanel);

        treeActions = new DefaultEditItemActions();
        treeActions.setEditItemActionsHandler(new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addCatalogItemAction.actionPerformed(null);
                XBCItem resultItem = addCatalogItemAction.getResultItem();
                if (resultItem != null) {
                    catalogEditorPanel.reloadNodesTree();
                    catalogEditorPanel.setNode(resultItem instanceof XBCNode ? (XBCNode) resultItem : catalogEditorPanel.getSpecsNode());
                    catalogEditorPanel.selectSpecTableRow(resultItem);
                }
            }

            @Override
            public void performEditItem() {
                editCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                editCatalogItemAction.actionPerformed(null);
                catalogEditorPanel.reloadNodesTree();
            }

            @Override
            public void performDeleteItem() {
                deleteCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                deleteCatalogItemAction.actionPerformed(null);
            }

            @Override
            public boolean isSelection() {
                return catalogEditorPanel.getSelectedTreeItem() != null;
            }

            @Override
            public boolean isEditable() {
                XBCItem item = catalogEditorPanel.getSelectedTreeItem();
                return item != null;
            }

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                catalogEditorPanel.addTreeSelectionListener(updateListener);
            }
        });
        itemActions = new DefaultEditItemActions();
        itemActions.setEditItemActionsHandler(new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addCatalogItemAction.actionPerformed(null);
                XBCItem resultItem = addCatalogItemAction.getResultItem();
                if (resultItem != null) {
                    catalogEditorPanel.setItem(resultItem);
                    catalogEditorPanel.setSpecsNode(catalogEditorPanel.getSpecsNode());
                    catalogEditorPanel.selectSpecTableRow(resultItem);
                }
            }

            @Override
            public void performEditItem() {
                editCatalogItemAction.setCurrentItem(catalogEditorPanel.getCurrentItem());
                editCatalogItemAction.actionPerformed(null);
                XBCItem resultItem = editCatalogItemAction.getResultItem();
                if (resultItem != null) {
                    catalogEditorPanel.setItem(resultItem);
                    catalogEditorPanel.setSpecsNode(catalogEditorPanel.getSpecsNode());
                    catalogEditorPanel.selectSpecTableRow(resultItem);
                }
            }

            @Override
            public void performDeleteItem() {
                deleteCatalogItemAction.setCurrentItem(catalogEditorPanel.getCurrentItem());
                deleteCatalogItemAction.actionPerformed(null);
            }

            @Override
            public boolean isSelection() {
                return catalogEditorPanel.getCurrentItem() != null;
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                catalogEditorPanel.addItemSelectionListener(updateListener);
            }
        });
        
        catalogTreePopupMenu = new JPopupMenu();
        catalogTreePopupMenu.add(treeActions.getAddItemAction());
        catalogTreePopupMenu.add(treeActions.getEditItemAction());
        catalogTreePopupMenu.addSeparator();
        catalogTreePopupMenu.addSeparator();
        catalogTreePopupMenu.add(exportItemAction);
        catalogTreePopupMenu.add(importItemAction);
        catalogEditorPanel.setTreePanelPopup(catalogTreePopupMenu);

        catalogItemPopupMenu = new JPopupMenu();
        catalogItemPopupMenu.add(itemActions.getAddItemAction());
        catalogItemPopupMenu.add(itemActions.getEditItemAction());
        catalogItemPopupMenu.addSeparator();
        catalogItemPopupMenu.addSeparator();
        catalogItemPopupMenu.add(exportItemAction);
        catalogItemPopupMenu.add(importItemAction);
        catalogEditorPanel.setItemPanelPopup(catalogItemPopupMenu);

        catalogEditorPanel.addTreeActions(treeActions);
        catalogEditorPanel.addItemActions(itemActions);
    }

    @Nonnull
    public CatalogEditorPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        catalogEditorPanel.setApplication(application);

        addCatalogItemAction.setup(application);
        editCatalogItemAction.setup(application);
        deleteCatalogItemAction.setup(application);

        XbupCatalogModule managerModule = application.getModuleRepository().getModuleByInterface(XbupCatalogModule.class);
        MenuManagement menuManagement = managerModule.getDefaultMenuManagement();
        menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 3);
        menuManagement.insertMainPopupMenu(catalogItemPopupMenu, 3);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        addCatalogItemAction.setCatalog(catalog);
        editCatalogItemAction.setCatalog(catalog);
        deleteCatalogItemAction.setCatalog(catalog);
        exportItemAction.setCatalog(catalog);
        importItemAction.setCatalog(catalog);
    }

    public void setCatalogRoot(XBCRoot root) {
        catalogEditorPanel.setCatalogRoot(root);
    }
}
