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
package org.exbin.framework.xbup.catalog.item.file;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.xbup.catalog.item.file.gui.CatalogItemEditFilesPanel;
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
public class CatalogFileEditor {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CatalogFileEditor.class);

    private final CatalogItemEditFilesPanel catalogEditorPanel;
    private final DefaultEditItemActions fileActions;
    private XBApplication application;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    public CatalogFileEditor() {
        catalogEditorPanel = new CatalogItemEditFilesPanel();

        fileActions = new DefaultEditItemActions(DefaultEditItemActions.MODE.DIALOG);
        fileActions.setEditItemActionsHandler(new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
//                addCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
//                addCatalogItemAction.actionPerformed(null);
//                XBCItem resultItem = addCatalogItemAction.getResultItem();
//                if (resultItem != null) {
//                    catalogEditorPanel.reloadNodesTree();
//                    catalogEditorPanel.setNode(resultItem instanceof XBCNode ? (XBCNode) resultItem : catalogEditorPanel.getSpecsNode());
//                    catalogEditorPanel.selectSpecTableRow(resultItem);
//                }
            }

            @Override
            public void performEditItem() {
//                editCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
//                editCatalogItemAction.actionPerformed(null);
//                catalogEditorPanel.reloadNodesTree();
            }

            @Override
            public void performDeleteItem() {
//                deleteCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
//                deleteCatalogItemAction.actionPerformed(null);
            }

            @Override
            public boolean canAddItem() {
                return true;
//                XBCItem item = catalogEditorPanel.getSelectedTreeItem();
//                return item != null;
            }

            @Override
            public boolean canEditItem() {
                return false;
//                XBCItem item = catalogEditorPanel.getSelectedTreeItem();
//                return item != null;
            }

            @Override
            public boolean canDeleteItem() {
                return false;
//                XBCNode node = catalogEditorPanel.getSelectedTreeItem();
//                return node != null && node.getParent().isPresent();
            }

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
//                catalogEditorPanel.addTreeSelectionListener(updateListener);
            }
        });

        popupMenu = new JPopupMenu();
//        catalogEditorPanel.setPanelPopup(popupMenu);

//        catalogEditorPanel.addFileActions(fileActions);
    }

    @Nonnull
    public CatalogItemEditFilesPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        catalogEditorPanel.setApplication(application);

//        addCatalogItemAction.setup(application);
//        editCatalogItemAction.setup(application);
//        deleteCatalogItemAction.setup(application);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

//        addCatalogItemAction.setCatalog(catalog);
//        editCatalogItemAction.setCatalog(catalog);
//        deleteCatalogItemAction.setCatalog(catalog);
//        exportItemAction.setup(application, catalog);
//        importItemAction.setup(application, catalog);
//        exportTreeItemAction.setup(application, catalog);
//        importTreeItemAction.setup(application, catalog);
//        
//        XbupCatalogModule managerModule = application.getModuleRepository().getModuleByInterface(XbupCatalogModule.class);
//        MenuManagement menuManagement = managerModule.getDefaultMenuManagement();
//
//        if (catalogTreePopupMenu.getComponentCount() == 0) {
//            JMenuItem addTreeItem = ActionUtils.actionToMenuItem(treeActions.getAddItemAction());
//            addTreeItem.setText(resourceBundle.getString("addTreeItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
//            catalogTreePopupMenu.add(addTreeItem);
//            JMenuItem editTreeItem = ActionUtils.actionToMenuItem(treeActions.getEditItemAction());
//            editTreeItem.setText(resourceBundle.getString("editTreeItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
//            catalogTreePopupMenu.add(editTreeItem);
//            catalogTreePopupMenu.addSeparator();
//            catalogTreePopupMenu.addSeparator();
//            catalogTreePopupMenu.add(ActionUtils.actionToMenuItem(exportTreeItemAction));
//            catalogTreePopupMenu.add(ActionUtils.actionToMenuItem(importTreeItemAction));
//            menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 3);
//        }
//        if (catalogItemPopupMenu.getComponentCount() == 0) {
//            JMenuItem addCatalogItem = ActionUtils.actionToMenuItem(itemActions.getAddItemAction());
//            addCatalogItem.setText(resourceBundle.getString("addCatalogItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
//            catalogItemPopupMenu.add(addCatalogItem);
//            JMenuItem editCatalogItem = ActionUtils.actionToMenuItem(itemActions.getEditItemAction());
//            editCatalogItem.setText(resourceBundle.getString("editCatalogItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
//            catalogItemPopupMenu.add(editCatalogItem);
//            catalogItemPopupMenu.addSeparator();
//            catalogItemPopupMenu.addSeparator();
//            catalogItemPopupMenu.add(ActionUtils.actionToMenuItem(exportItemAction));
//            catalogItemPopupMenu.add(ActionUtils.actionToMenuItem(importItemAction));
//            menuManagement.insertMainPopupMenu(catalogItemPopupMenu, 3);
//        }
    }

    public void setCatalogRoot(XBCRoot catalogRoot) {
//        this.catalogRoot = catalogRoot;
//        catalogEditorPanel.setCatalogRoot(catalogRoot);
    }
}
