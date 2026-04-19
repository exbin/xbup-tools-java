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
package org.exbin.xbup.jaguif.catalog.item.revision;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.jaguif.App;
import org.exbin.jaguif.component.action.AddItemAction;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.DeleteItemAction;
import org.exbin.jaguif.component.action.EditItemAction;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextRegistration;
import org.exbin.xbup.jaguif.data.model.CatalogDefsTableModel;
import org.exbin.xbup.jaguif.data.model.CatalogRevsTableItem;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.toolbar.api.ActionToolBarContribution;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.jaguif.catalog.item.revision.action.AddItemRevisionAction;
import org.exbin.xbup.jaguif.catalog.item.revision.action.EditItemRevisionAction;
import org.exbin.xbup.jaguif.catalog.item.revision.action.RemoveItemRevisionAction;
import org.exbin.xbup.jaguif.catalog.item.revision.gui.CatalogItemEditRevsPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Catalog revisions editor.
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
        editActions = new DefaultEditItemActions(EditItemMode.DIALOG);
        init();
    }
    
    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ActiveContextManagement contextManager = contextModule.createContextManager();
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createAddItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return AddItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createEditItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return EditItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createDeleteItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return DeleteItemAction.ACTION_ID;
            }
        });
        ContextEditItem contextEditItem = new ContextEditItem() {
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
        contextManager.changeActiveState(ContextEditItem.class, contextEditItem);
        catalogEditorPanel.addSelectionListener((lse) -> {
            contextManager.changeActiveState(ContextEditItem.class, contextEditItem);        
        });
        ContextRegistration contextRegistrar = contextModule.createContextRegistrator();
        toolBarManager.buildIconToolBar(catalogEditorPanel.getToolBar(), TOOLBAR_ID, contextRegistrar);

        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        popupMenu = new JPopupMenu();
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addRevisionMenuItem = menuModule.actionToMenuItem(editActions.createAddItemAction());
        addRevisionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addRevisionMenuItem.text"));
        popupMenu.add(addRevisionMenuItem);
        JMenuItem editRevisionMenuItem = menuModule.actionToMenuItem(editActions.createEditItemAction());
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
