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
package org.exbin.framework.xbup.catalog.item.spec;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionContextRegistration;
import org.exbin.framework.action.api.ActionManagement;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.action.DefaultMoveItemActions;
import org.exbin.framework.component.action.EditItemMode;
import org.exbin.framework.component.api.ContextEditItem;
import org.exbin.framework.component.api.action.MoveItemActions;
import org.exbin.framework.component.api.ContextMoveItem;
import org.exbin.framework.context.api.ActiveContextManagement;
import org.exbin.framework.context.api.ContextModuleApi;
import org.exbin.framework.data.model.CatalogDefsTableItem;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.toolbar.api.ToolBarManagement;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.framework.xbup.catalog.item.spec.action.AddItemDefinitionAction;
import org.exbin.framework.xbup.catalog.item.spec.action.EditItemDefinitionAction;
import org.exbin.framework.xbup.catalog.item.spec.action.RemoveItemDefinitionAction;
import org.exbin.framework.xbup.catalog.item.spec.gui.CatalogItemEditDefinitionPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Catalog definition editor.
 *
 * @author ExBin Project (https://exbin.org)
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
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        ActionManagement actionManager = actionModule.createActionManager(contextManager);
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createAddItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createEditItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createDeleteItemAction());
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
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", moveItemActions.createMoveTopAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", moveItemActions.createMoveUpAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", moveItemActions.createMoveDownAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", moveItemActions.createMoveBottomAction());
        catalogEditorPanel.addSelectionListener((ListSelectionEvent lse) -> {
            contextManager.changeActiveState(ContextEditItem.class, contextEditItem);
            contextManager.changeActiveState(ContextMoveItem.class, contextMoveItem);
        });
        ActionContextRegistration actionContextRegistrar = actionModule.createActionContextRegistrar(actionManager);
        toolBarManager.buildIconToolBar(catalogEditorPanel.getToolBar(), TOOLBAR_ID, actionContextRegistrar);

        popupMenu = new JPopupMenu();
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addDefinitionMenuItem = actionModule.actionToMenuItem(editActions.createAddItemAction());
        addDefinitionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addDefinitionMenuItem.text"));
        popupMenu.add(addDefinitionMenuItem);
        JMenuItem editDefinitionMenuItem = actionModule.actionToMenuItem(editActions.createEditItemAction());
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
