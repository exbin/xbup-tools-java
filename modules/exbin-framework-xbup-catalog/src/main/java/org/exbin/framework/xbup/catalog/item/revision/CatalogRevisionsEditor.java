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
package org.exbin.framework.xbup.catalog.item.revision;

import org.exbin.framework.xbup.catalog.item.plugin.*;
import org.exbin.framework.xbup.catalog.item.file.*;
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
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Catalog editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogRevisionsEditor {

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

    public CatalogRevisionsEditor() {
        catalogEditorPanel = new CatalogItemEditFilesPanel();

        fileActions = new DefaultEditItemActions(DefaultEditItemActions.MODE.DIALOG);
        fileActions.setEditItemActionsHandler(new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addFileAction.setCurrentNode(node);
                addFileAction.actionPerformed(null);
                String resultName = addFileAction.getResultName();
                if (resultName != null) {
                    byte[] resultData = addFileAction.getResultData();
                    throw new IllegalStateException();
//                    filesModel.addItem(resultName, fileData);
//                    catalogEditorPanel.reloadNodesTree();
                }
            }

            @Override
            public void performEditItem() {
                renameFileAction.setCurrentFile(catalogEditorPanel.getSelectedFile());
                renameFileAction.actionPerformed(null);
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
                XBCXFile file = catalogEditorPanel.getSelectedFile();
                return file != null;
            }

            @Override
            public boolean canDeleteItem() {
                return false;
//                XBCNode node = catalogEditorPanel.getSelectedTreeItem();
//                return node != null && node.getParent().isPresent();
            }

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                catalogEditorPanel.addSelectionListener(updateListener);
            }
        });

        popupMenu = new JPopupMenu();
        catalogEditorPanel.setPanelPopup(popupMenu);

        catalogEditorPanel.addFileActions(fileActions);
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
