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
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.DefaultActionContextService;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.toolbar.ToolBarManager;
import org.exbin.framework.xbup.catalog.item.file.action.AddFileAction;
import org.exbin.framework.xbup.catalog.item.file.action.DeleteFileAction;
import org.exbin.framework.xbup.catalog.item.file.action.RenameFileAction;
import org.exbin.framework.xbup.catalog.item.file.action.ReplaceFileContentAction;
import org.exbin.framework.xbup.catalog.item.file.action.SaveFileContentAsAction;
import org.exbin.framework.xbup.catalog.item.file.gui.CatalogFilesTableModel;
import org.exbin.framework.xbup.catalog.item.file.gui.CatalogItemEditFilesPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Catalog files editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogFilesEditor {

    private final CatalogItemEditFilesPanel catalogEditorPanel;
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private XBCNode node;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogFilesEditor.class);

    private AddFileAction addFileAction = new AddFileAction();
    private RenameFileAction renameFileAction = new RenameFileAction();
    private DeleteFileAction deleteFileAction = new DeleteFileAction();
    private SaveFileContentAsAction saveFileContentAsAction = new SaveFileContentAsAction();
    private ReplaceFileContentAction replaceFileContentAction = new ReplaceFileContentAction();

    public CatalogFilesEditor() {
        catalogEditorPanel = new CatalogItemEditFilesPanel();
        ToolBarManager toolBarManager = new ToolBarManager();
        DefaultActionContextService actionContextService = new DefaultActionContextService();
        editActions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        toolBarManager.registerToolBarItem("", "", editActions.createAddItemAction());
        toolBarManager.registerToolBarItem("", "", editActions.createEditItemAction());
        toolBarManager.registerToolBarItem("", "", editActions.createDeleteItemAction());
        EditItemActionsHandler editItemActionsHandler = new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addFileAction.setCurrentNode(node);
                addFileAction.actionPerformed(null);
                String resultName = addFileAction.getResultName();
                if (resultName != null) {
                    byte[] resultData = addFileAction.getResultData();
                    CatalogFilesTableModel filesModel = catalogEditorPanel.getFilesModel();
                    filesModel.addItem(resultName, resultData);
                    // TODO catalogEditorPanel.reloadNodesTree();
                }
            }

            @Override
            public void performEditItem() {
                renameFileAction.setCurrentFile(catalogEditorPanel.getSelectedFile());
                renameFileAction.actionPerformed(null);
            }

            @Override
            public void performDeleteItem() {
                int selectedIndex = catalogEditorPanel.getSelectedIndex();
                // deleteFileAction.setCurrentIndex(selectedIndex);
                deleteFileAction.actionPerformed(null);
                CatalogFilesTableModel filesModel = catalogEditorPanel.getFilesModel();
                filesModel.removeItem(selectedIndex);
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
        };
        actionContextService.updated(EditItemActionsHandler.class, editItemActionsHandler);
        catalogEditorPanel.addSelectionListener((lse) -> {
            actionContextService.updated(EditItemActionsHandler.class, editItemActionsHandler);        
        });
        toolBarManager.buildToolBar(catalogEditorPanel.getToolBar(), "", actionContextService);

        addFileAction.setParentComponent(catalogEditorPanel);
        renameFileAction.setParentComponent(catalogEditorPanel);
        saveFileContentAsAction.setParentComponent(catalogEditorPanel);
        replaceFileContentAction.setParentComponent(catalogEditorPanel);

        catalogEditorPanel.addFileActions(editActions);
    }

    @Nonnull
    public CatalogItemEditFilesPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setup() {
        saveFileContentAsAction.setup();
        replaceFileContentAction.setup();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        addFileAction.setCatalog(catalog);
        renameFileAction.setCatalog(catalog);
        saveFileContentAsAction.setCatalog(catalog);
        replaceFileContentAction.setCatalog(catalog);

        popupMenu = new JPopupMenu();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addFileMenuItem = actionModule.actionToMenuItem(editActions.createAddItemAction());
        addFileMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addFileMenuItem.text"));
        popupMenu.add(addFileMenuItem);
        JMenuItem editFileMenuItem = actionModule.actionToMenuItem(editActions.createEditItemAction());
        editFileMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editFileMenuItem.text"));
        popupMenu.add(editFileMenuItem);
        popupMenu.addSeparator();
        JMenuItem saveFileContentAsMenuItem = actionModule.actionToMenuItem(saveFileContentAsAction);
        saveFileContentAsMenuItem.removeActionListener(saveFileContentAsAction);
        saveFileContentAsMenuItem.addActionListener((event) -> {
            saveFileContentAsAction.setCurrentFile(catalogEditorPanel.getSelectedFile());
            saveFileContentAsAction.actionPerformed(event);
        });
        saveFileContentAsMenuItem.setText((String) saveFileContentAsAction.getValue(Action.NAME));
        popupMenu.add(saveFileContentAsMenuItem);
        JMenuItem replaceFileContentMenuItem = actionModule.actionToMenuItem(replaceFileContentAction);
        replaceFileContentMenuItem.removeActionListener(replaceFileContentAction);
        replaceFileContentMenuItem.addActionListener((event) -> {
            int selectedIndex = catalogEditorPanel.getSelectedIndex();
            replaceFileContentAction.setCurrentFile(catalogEditorPanel.getSelectedFile());
            replaceFileContentAction.actionPerformed(event);
            String resultName = replaceFileContentAction.getResultName();
            if (resultName != null) {
                byte[] resultData = replaceFileContentAction.getResultData();
                CatalogFilesTableModel filesModel = catalogEditorPanel.getFilesModel();
                filesModel.setItemData(selectedIndex, resultData);
            }
        });
        replaceFileContentMenuItem.setText((String) replaceFileContentAction.getValue(Action.NAME));
        popupMenu.add(replaceFileContentMenuItem);

        catalogEditorPanel.setPanelPopup(popupMenu);
    }

    public void setNode(XBCNode node) {
        this.node = node;
        catalogEditorPanel.setNode(node);
    }

    public void persist() {
        catalogEditorPanel.persist();
    }
}
