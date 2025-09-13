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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.DefaultActionContextService;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.data.model.CatalogRevsTableItem;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.toolbar.api.ToolBarManager;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.framework.xbup.catalog.item.revision.action.AddItemRevisionAction;
import org.exbin.framework.xbup.catalog.item.revision.action.EditItemRevisionAction;
import org.exbin.framework.xbup.catalog.item.revision.action.RemoveItemRevisionAction;
import org.exbin.framework.xbup.catalog.item.revision.gui.CatalogItemEditRevsPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Catalog revisions editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogRevisionsEditor {

    public static final String TOOLBAR_ID = "CatalogRevisionsEditor.toolBar";

    private final CatalogItemEditRevsPanel catalogEditorPanel;
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogRevisionsEditor.class);

    private AddItemRevisionAction addRevisionAction = new AddItemRevisionAction();
    private EditItemRevisionAction editRevisionAction = new EditItemRevisionAction();
    private RemoveItemRevisionAction removeRevisionAction = new RemoveItemRevisionAction();

    public CatalogRevisionsEditor() {
        catalogEditorPanel = new CatalogItemEditRevsPanel();
        editActions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        init();
    }
    
    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManager toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");
        DefaultActionContextService actionContextService = new DefaultActionContextService();
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createAddItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createEditItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createDeleteItemAction());
        EditItemActionsHandler editItemActionsHandler = new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                addRevisionAction.actionPerformed(null);
                CatalogRevsTableItem resultRevision = addRevisionAction.getResultRevision();
                if (resultRevision != null) {
                    catalogEditorPanel.revisionAdded(resultRevision);
                }
            }

            @Override
            public void performEditItem() {
                editRevisionAction.setCurrentRevision(catalogEditorPanel.getSelectedRevision());
                editRevisionAction.actionPerformed(null);
                CatalogRevsTableItem resultRevision = editRevisionAction.getResultRevision();
                if (resultRevision != null) {
                    catalogEditorPanel.revisionEdited(resultRevision);
                }
            }

            @Override
            public void performDeleteItem() {
                removeRevisionAction.setCurrentRevision(catalogEditorPanel.getSelectedRevision());
                removeRevisionAction.actionPerformed(null);
                CatalogRevsTableItem resultRevision = editRevisionAction.getResultRevision();
                if (resultRevision != null) {
                    catalogEditorPanel.revisionRemoved(resultRevision);
                }
            }

            @Override
            public boolean canAddItem() {
                return true;
            }

            @Override
            public boolean canEditItem() {
                CatalogRevsTableItem revision = catalogEditorPanel.getSelectedRevision();
                return revision != null;
            }

            @Override
            public boolean canDeleteItem() {
                CatalogRevsTableItem revision = catalogEditorPanel.getSelectedRevision();
                return revision != null;
            }
        };
        actionContextService.updated(EditItemActionsHandler.class, editItemActionsHandler);
        catalogEditorPanel.addSelectionListener((lse) -> {
            actionContextService.updated(EditItemActionsHandler.class, editItemActionsHandler);        
        });
        toolBarManager.buildIconToolBar(catalogEditorPanel.getToolBar(), TOOLBAR_ID, actionContextService);

        popupMenu = new JPopupMenu();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addRevisionMenuItem = actionModule.actionToMenuItem(editActions.createAddItemAction());
        addRevisionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addRevisionMenuItem.text"));
        popupMenu.add(addRevisionMenuItem);
        JMenuItem editRevisionMenuItem = actionModule.actionToMenuItem(editActions.createEditItemAction());
        editRevisionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editRevisionMenuItem.text"));
        popupMenu.add(editRevisionMenuItem);

        catalogEditorPanel.setPanelPopup(popupMenu);

        catalogEditorPanel.addFileActions(editActions);

        addRevisionAction.setParentComponent(catalogEditorPanel);
        editRevisionAction.setParentComponent(catalogEditorPanel);
        removeRevisionAction.setParentComponent(catalogEditorPanel);
    }

    public void setCatalogItem(XBCItem item) {
        catalogEditorPanel.setCatalogItem(item);
    }

    public void setDefsModel(CatalogDefsTableModel defsTableModel) {
        catalogEditorPanel.setDefsModel(defsTableModel);
    }

    @Nonnull
    public CatalogItemEditRevsPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        addRevisionAction.setCatalog(catalog);
        editRevisionAction.setCatalog(catalog);
    }

    public void persist() {
        catalogEditorPanel.persist();
    }
}
