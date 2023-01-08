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
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.component.api.toolbar.SideToolBar;
import org.exbin.framework.xbup.catalog.item.file.action.AddFileAction;
import org.exbin.framework.xbup.catalog.item.file.action.RenameFileAction;
import org.exbin.framework.xbup.catalog.item.file.action.ReplaceFileContentAction;
import org.exbin.framework.xbup.catalog.item.file.action.SaveFileContentAsAction;
import org.exbin.framework.xbup.catalog.item.file.gui.CatalogItemEditFilesPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Catalog editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogFilesEditor {

    private final CatalogItemEditFilesPanel catalogEditorPanel;
    private final DefaultEditItemActions fileActions;
    private XBApplication application;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private XBCNode node;
    
    private AddFileAction addFileAction = new AddFileAction();
    private RenameFileAction renameFileAction = new RenameFileAction();
    private ReplaceFileContentAction replaceFileContentAction = new ReplaceFileContentAction();
    private SaveFileContentAsAction saveFileContentAsAction = new SaveFileContentAsAction();

    public CatalogFilesEditor() {
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
        catalogEditorPanel.setPanelPopup(popupMenu);

        catalogEditorPanel.addFileActions((SideToolBar sideToolBar) -> {
            sideToolBar.addAction(addFileAction);
            sideToolBar.addAction(renameFileAction);
        });
    }

    @Nonnull
    public CatalogItemEditFilesPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        catalogEditorPanel.setApplication(application);

        addFileAction.setup(application);
        renameFileAction.setup(application);
        replaceFileContentAction.setup(application);
        saveFileContentAsAction.setup(application);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);
        
        addFileAction.setCatalog(catalog);
        renameFileAction.setCatalog(catalog);
        replaceFileContentAction.setCatalog(catalog);
        saveFileContentAsAction.setCatalog(catalog);

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

    public void setNode(XBCNode node) {
        this.node = node;
        catalogEditorPanel.setNode(node);
    }

    public void setMenuManagement(MenuManagement menuManagement) {
        catalogEditorPanel.setMenuManagement(menuManagement);
    }
    
    public void persist() {
        catalogEditorPanel.persist();
    }
}
