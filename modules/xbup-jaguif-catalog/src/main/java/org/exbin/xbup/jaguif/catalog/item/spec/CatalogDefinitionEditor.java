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
package org.exbin.xbup.jaguif.catalog.item.spec;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import org.exbin.jaguif.App;
import org.exbin.jaguif.component.action.AddItemAction;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.DefaultMoveItemActions;
import org.exbin.jaguif.component.action.DeleteItemAction;
import org.exbin.jaguif.component.action.EditItemAction;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.component.action.MoveBottomAction;
import org.exbin.jaguif.component.action.MoveDownAction;
import org.exbin.jaguif.component.action.MoveTopAction;
import org.exbin.jaguif.component.action.MoveUpAction;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.component.api.action.MoveItemActions;
import org.exbin.jaguif.component.api.ContextMoveItem;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextRegistration;
import org.exbin.xbup.jaguif.data.model.CatalogDefsTableItem;
import org.exbin.xbup.jaguif.data.model.CatalogDefsTableModel;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.toolbar.api.ActionToolBarContribution;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.jaguif.catalog.item.spec.action.AddItemDefinitionAction;
import org.exbin.xbup.jaguif.catalog.item.spec.action.EditItemDefinitionAction;
import org.exbin.xbup.jaguif.catalog.item.spec.action.RemoveItemDefinitionAction;
import org.exbin.xbup.jaguif.catalog.item.spec.gui.CatalogItemEditDefinitionPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Catalog definition editor.
 */
@ParametersAreNonnullByDefault
public class CatalogDefinitionEditor {

    public static final String TOOLBAR_ID = "CatalogDefinitionEditor.toolBar";

    private final CatalogItemEditDefinitionPanel catalogEditorPanel;
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogDefinitionEditor.class);

    private ContextMoveItem contextMoveItem;
    private AddItemDefinitionAction addDefinitionAction = new AddItemDefinitionAction();
    private EditItemDefinitionAction editDefinitionAction = new EditItemDefinitionAction();
    private RemoveItemDefinitionAction removeDefinitionAction = new RemoveItemDefinitionAction();

    public CatalogDefinitionEditor() {
        catalogEditorPanel = new CatalogItemEditDefinitionPanel();
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
                addDefinitionAction.actionPerformed(null);
                CatalogDefsTableItem resultDefinition = addDefinitionAction.getResultDefinition();
                if (resultDefinition != null) {
                    catalogEditorPanel.definitionAdded(resultDefinition);
                }
            }

            @Override
            public void performEditItem() {
                editDefinitionAction.setCurrentDefinition(catalogEditorPanel.getSelectedDefinition());
                editDefinitionAction.actionPerformed(null);
                CatalogDefsTableItem resultDefinition = editDefinitionAction.getResultDefinition();
                if (resultDefinition != null) {
                    catalogEditorPanel.definitionEdited(resultDefinition);
                }
            }

            @Override
            public void performDeleteItem() {
                removeDefinitionAction.setCurrentDefinition(catalogEditorPanel.getSelectedDefinition());
                removeDefinitionAction.actionPerformed(null);
                CatalogDefsTableItem resultDefinition = editDefinitionAction.getResultDefinition();
                if (resultDefinition != null) {
                    catalogEditorPanel.definitionRemoved(resultDefinition);
                }
            }

            @Override
            public boolean canAddItem() {
                return true;
            }

            @Override
            public boolean canEditItem() {
                CatalogDefsTableItem revision = catalogEditorPanel.getSelectedDefinition();
                return revision != null;
            }

            @Override
            public boolean canDeleteItem() {
                CatalogDefsTableItem revision = catalogEditorPanel.getSelectedDefinition();
                return revision != null;
            }
        };
        contextManager.changeActiveState(ContextEditItem.class, contextEditItem);

        contextMoveItem = new ContextMoveItem() {
            @Override
            public void performMoveUp() {
                int selectedDefinitionIndex = catalogEditorPanel.getSelectedDefinitionIndex();
                catalogEditorPanel.definitionMovedUp(selectedDefinitionIndex);
            }

            @Override
            public void performMoveDown() {
                int selectedDefinitionIndex = catalogEditorPanel.getSelectedDefinitionIndex();
                catalogEditorPanel.definitionMovedDown(selectedDefinitionIndex);
            }

            @Override
            public void performMoveTop() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void performMoveBottom() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSelection() {
                return true;
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        };
        contextManager.changeActiveState(ContextMoveItem.class, contextMoveItem);

        MoveItemActions moveItemActions = new DefaultMoveItemActions();
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return moveItemActions.createMoveTopAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return MoveTopAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return moveItemActions.createMoveUpAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return MoveUpAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return moveItemActions.createMoveDownAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return MoveDownAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return moveItemActions.createMoveBottomAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return MoveBottomAction.ACTION_ID;
            }
        });
        catalogEditorPanel.addSelectionListener((ListSelectionEvent lse) -> {
            contextManager.changeActiveState(ContextEditItem.class, contextEditItem);
            contextManager.changeActiveState(ContextMoveItem.class, contextMoveItem);
        });
        ContextRegistration contextRegistrar = contextModule.createContextRegistrator();
        toolBarManager.buildIconToolBar(catalogEditorPanel.getToolBar(), TOOLBAR_ID, contextRegistrar);

        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        popupMenu = new JPopupMenu();
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addDefinitionMenuItem = menuModule.actionToMenuItem(editActions.createAddItemAction());
        addDefinitionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addDefinitionMenuItem.text"));
        popupMenu.add(addDefinitionMenuItem);
        JMenuItem editDefinitionMenuItem = menuModule.actionToMenuItem(editActions.createEditItemAction());
        editDefinitionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editDefinitionMenuItem.text"));
        popupMenu.add(editDefinitionMenuItem);

        catalogEditorPanel.setPanelPopup(popupMenu);

        catalogEditorPanel.addToolbarActions(editActions);

        addDefinitionAction.setParentComponent(catalogEditorPanel);
        editDefinitionAction.setParentComponent(catalogEditorPanel);
        removeDefinitionAction.setParentComponent(catalogEditorPanel);

        catalogEditorPanel.addToolbarActions((sideToolBar) -> {
            sideToolBar.addSeparator();
        });
        catalogEditorPanel.addToolbarActions(moveItemActions);
    }

    public void setCatalogItem(XBCItem item) {
        catalogEditorPanel.setCatalogItem(item);
    }

    public void setDefsModel(CatalogDefsTableModel defsTableModel) {
        catalogEditorPanel.setDefsModel(defsTableModel);
    }

    @Nonnull
    public CatalogItemEditDefinitionPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        addDefinitionAction.setCatalog(catalog);
        editDefinitionAction.setCatalog(catalog);
        removeDefinitionAction.setCatalog(catalog);
    }

    public void persist() {
        catalogEditorPanel.persist();
    }
}
