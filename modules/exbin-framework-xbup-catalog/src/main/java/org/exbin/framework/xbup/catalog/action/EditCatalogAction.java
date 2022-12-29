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
package org.exbin.framework.xbup.catalog.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.component.api.toolbar.SideToolBar;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.xbup.catalog.XbupCatalogModule;
import org.exbin.framework.xbup.catalog.item.action.AddCatalogItemAction;
import org.exbin.framework.xbup.catalog.item.action.EditCatalogItemAction;
import org.exbin.framework.xbup.catalog.item.action.ExportItemAction;
import org.exbin.framework.xbup.catalog.item.action.ImportItemAction;
import org.exbin.framework.xbup.catalog.gui.CatalogEditorPanel;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.gui.CloseControlPanel;
import org.exbin.framework.xbup.catalog.CatalogEditor;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Edit catalog action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditCatalogAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogAction";

    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(EditCatalogAction.class);

    private XBApplication application;
    private XBACatalog catalog;

    private Component parentComponent;
    private XBCRoot activeItem;

    public EditCatalogAction() {
    }

    public void setup(XBApplication application) {
        this.application = application;

        ActionUtils.setupAction(this, resourceBundle, ACTION_ID);
        putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public void setActiveItem(XBCRoot activeItem) {
        this.activeItem = activeItem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
        XbupCatalogModule managerModule = application.getModuleRepository().getModuleByInterface(XbupCatalogModule.class);
        MenuManagement menuManagement = managerModule.getDefaultMenuManagement();
        CatalogEditor catalogEditor = new CatalogEditor();

//        DefaultEditItemActions treeActions = new DefaultEditItemActions();
//        treeActions.setEditItemActionsHandler(new EditItemActionsHandler() {
//            @Override
//            public void performAddItem() {
//                AddCatalogItemAction addItemAction = new AddCatalogItemAction() {
//                    @Override
//                    public void actionPerformed(ActionEvent event) {
//                        super.actionPerformed(event);
//                        XBCItem resultItem = getResultItem();
//                        if (resultItem != null) {
//                            catalogEditor.reloadNodesTree();
//                            catalogEditor.setNode(resultItem instanceof XBCNode ? (XBCNode) resultItem : catalogEditor.getSpecsNode());
//                            catalogEditor.selectSpecTableRow(resultItem);
//                        }
//                    }
//                };
//                addItemAction.setup(application);
//                addItemAction.setCatalog(catalog);
//                addItemAction.setParentComponent(parentComponent);
//                XBCItem currentItem = catalogEditor.getSelectedTreeItem();
//                addItemAction.setCurrentItem(currentItem);
//                addItemAction.actionPerformed(null);
//                XBCItem resultItem = addItemAction.getResultItem();
//                if (resultItem != null) {
//                    catalogEditor.setItem(currentItem);
//                    catalogEditor.setSpecsNode(catalogEditor.getSpecsNode());
//                    catalogEditor.selectSpecTableRow(currentItem);
//                }
//            }
//
//            @Override
//            public void performEditItem() {
//                EditCatalogItemAction editItemAction = new EditCatalogItemAction();
//                editItemAction.setup(application);
//                editItemAction.setCatalog(catalog);
//                editItemAction.setMenuManagement(menuManagement);
//                editItemAction.setParentComponent(parentComponent);
//                XBCItem currentItem = catalogEditor.getSelectedTreeItem();
//                editItemAction.setCurrentItem(currentItem);
//                editItemAction.actionPerformed(null);
//                XBCItem resultItem = editItemAction.getResultItem();
//                if (resultItem != null) {
//                    catalogEditor.setItem(currentItem);
//                    catalogEditor.setSpecsNode(catalogEditor.getSpecsNode());
//                    catalogEditor.selectSpecTableRow(currentItem);
//                }
//            }
//
//            @Override
//            public void performDeleteItem() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            @Override
//            public boolean isSelection() {
//                return catalogEditor.getSelectedTreeItem() != null;
//            }
//
//            @Override
//            public boolean isEditable() {
//                XBCItem item = catalogEditor.getSelectedTreeItem();
//                return item != null;
//            }
//
//            @Override
//            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
//                catalogEditor.addTreeSelectionListener(updateListener);
//            }
//        });


//        ExportItemAction exportItemAction = new ExportItemAction();
//        exportItemAction.setCatalog(catalog);
//        exportItemAction.setParentComponent(parentComponent);
//
//        ImportItemAction importItemAction = new ImportItemAction();
//        importItemAction.setCatalog(catalog);
//        importItemAction.setParentComponent(parentComponent);

        
//        catalogEditor.addTreeActions((SideToolBar sideToolBar) -> {
//            treeActions.registerActions(sideToolBar);
//        });

        catalogEditor.setApplication(application);
//        catalogEditor.setMenuManagement(menuManagement);
        catalogEditor.setCatalog(catalog);
        catalogEditor.setCatalogRoot(activeItem);
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowUtils.DialogWrapper dialog = frameModule.createDialog(catalogEditor.getCatalogEditorPanel(), controlPanel);
        controlPanel.setHandler(() -> {
            dialog.close();
            dialog.dispose();
        });
        frameModule.setDialogTitle(dialog, catalogEditor.getCatalogEditorPanel().getResourceBundle());
        dialog.showCentered(parentComponent);
    }
}
