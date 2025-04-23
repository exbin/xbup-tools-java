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

import java.awt.event.ActionEvent;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.language.api.LanguageModuleApi;
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

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogEditor.class);

    private final CatalogEditorPanel catalogEditorPanel;
    private final DefaultEditItemActions treeActions;
    private final DefaultEditItemActions itemActions;
    private XBACatalog catalog;
    private JPopupMenu catalogTreePopupMenu;
    private JPopupMenu catalogItemPopupMenu;

    private AddCatalogItemAction addCatalogItemAction = new AddCatalogItemAction();
    private EditCatalogItemAction editCatalogItemAction = new EditCatalogItemAction();
    private DeleteCatalogItemAction deleteCatalogItemAction = new DeleteCatalogItemAction();
    private ExportItemAction exportItemAction;
    private ImportItemAction importItemAction;
    private ExportItemAction exportTreeItemAction;
    private ImportItemAction importTreeItemAction;
    private XBCRoot catalogRoot;

    public CatalogEditor() {
        catalogEditorPanel = new CatalogEditorPanel();

        addCatalogItemAction.setParentComponent(catalogEditorPanel);
        editCatalogItemAction.setParentComponent(catalogEditorPanel);
        deleteCatalogItemAction.setParentComponent(catalogEditorPanel);
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

        treeActions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        EditItemActionsHandler editItemActionsHandler = new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
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
                catalogEditorPanel.reloadNodesTree();
            }

            @Override
            public boolean canAddItem() {
                return true;
//                XBCItem item = catalogEditorPanel.getSelectedTreeItem();
//                return item != null;
            }

            @Override
            public boolean canEditItem() {
                XBCItem item = catalogEditorPanel.getSelectedTreeItem();
                return item != null;
            }

            @Override
            public boolean canDeleteItem() {
                XBCNode node = catalogEditorPanel.getSelectedTreeItem();
                return node != null && node.getParent().isPresent();
            }
        };
        itemActions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        EditItemActionsHandler editItemActionsHandler2 = new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addCatalogItemAction.setCurrentItem(catalogEditorPanel.getCurrentItem());
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
            public boolean canAddItem() {
                return catalogEditorPanel.getSelectedTreeItem() != null;
            }

            @Override
            public boolean canEditItem() {
                return catalogEditorPanel.getCurrentItem() != null;
            }

            @Override
            public boolean canDeleteItem() {
                XBCItem currentItem = catalogEditorPanel.getCurrentItem();
                return currentItem != null && (currentItem != catalogEditorPanel.getSelectedTreeItem());
            }
        };

        catalogTreePopupMenu = new JPopupMenu();
        catalogEditorPanel.setTreePanelPopup(catalogTreePopupMenu);

        catalogItemPopupMenu = new JPopupMenu();
        catalogEditorPanel.setItemPanelPopup(catalogItemPopupMenu);

        catalogEditorPanel.addTreeActions(treeActions);
        catalogEditorPanel.addItemActions(itemActions);
    }

    @Nonnull
    public CatalogEditorPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void initApplication() {
        addCatalogItemAction.setup();
        editCatalogItemAction.setup();
        deleteCatalogItemAction.setup();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        addCatalogItemAction.setCatalog(catalog);
        editCatalogItemAction.setCatalog(catalog);
        deleteCatalogItemAction.setCatalog(catalog);
        exportItemAction.setup(catalog);
        importItemAction.setup(catalog);
        exportTreeItemAction.setup(catalog);
        importTreeItemAction.setup(catalog);

        XbupCatalogModule managerModule = App.getModule(XbupCatalogModule.class);
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);

        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        if (catalogTreePopupMenu.getComponentCount() == 0) {
            JMenuItem addTreeItem = actionModule.actionToMenuItem(treeActions.createAddItemAction());
            addTreeItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addTreeItem.text"));
            catalogTreePopupMenu.add(addTreeItem);
            JMenuItem editTreeItem = actionModule.actionToMenuItem(treeActions.createEditItemAction());
            editTreeItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editTreeItem.text"));
            catalogTreePopupMenu.add(editTreeItem);
            catalogTreePopupMenu.addSeparator();
            catalogTreePopupMenu.addSeparator();
            catalogTreePopupMenu.add(actionModule.actionToMenuItem(exportTreeItemAction));
            catalogTreePopupMenu.add(actionModule.actionToMenuItem(importTreeItemAction));
            // menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 3);
        }
        if (catalogItemPopupMenu.getComponentCount() == 0) {
            JMenuItem addCatalogItem = actionModule.actionToMenuItem(itemActions.createAddItemAction());
            addCatalogItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addCatalogItem.text"));
            catalogItemPopupMenu.add(addCatalogItem);
            JMenuItem editCatalogItem = actionModule.actionToMenuItem(itemActions.createEditItemAction());
            editCatalogItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editCatalogItem.text"));
            catalogItemPopupMenu.add(editCatalogItem);
            catalogItemPopupMenu.addSeparator();
            catalogItemPopupMenu.addSeparator();
            catalogItemPopupMenu.add(actionModule.actionToMenuItem(exportItemAction));
            catalogItemPopupMenu.add(actionModule.actionToMenuItem(importItemAction));
            // menuManagement.insertMainPopupMenu(catalogItemPopupMenu, 3);
        }
    }

    public void setCatalogRoot(XBCRoot catalogRoot) {
        this.catalogRoot = catalogRoot;
        catalogEditorPanel.setCatalogRoot(catalogRoot);
    }
}
