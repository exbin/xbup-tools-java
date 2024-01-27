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
import org.exbin.framework.App;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.ComponentModuleApi;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.component.api.toolbar.MoveItemActions;
import org.exbin.framework.component.api.toolbar.MoveItemActionsHandler;
import org.exbin.framework.component.api.toolbar.MoveItemActionsUpdateListener;
import org.exbin.framework.data.model.CatalogDefsTableItem;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.language.api.LanguageModuleApi;
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

    private final CatalogItemEditDefinitionPanel catalogEditorPanel;
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    
    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogDefinitionEditor.class);

    private MoveItemActionsHandler moveItemActionsHandler;
    private AddItemDefinitionAction addDefinitionAction = new AddItemDefinitionAction();
    private EditItemDefinitionAction editDefinitionAction = new EditItemDefinitionAction();
    private RemoveItemDefinitionAction removeDefinitionAction = new RemoveItemDefinitionAction();

    public CatalogDefinitionEditor() {
        catalogEditorPanel = new CatalogItemEditDefinitionPanel();

        editActions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        editActions.setEditItemActionsHandler(new EditItemActionsHandler() {
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

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                catalogEditorPanel.addSelectionListener(updateListener);
            }
        });
        
        moveItemActionsHandler = new MoveItemActionsHandler() {
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

            @Override
            public void setUpdateListener(MoveItemActionsUpdateListener updateListener) {
                // TODO catalogEditorPanel.addSelectionListener(updateListener);
            }
        };

        popupMenu = new JPopupMenu();
        JMenuItem addDefinitionMenuItem = ActionUtils.actionToMenuItem(editActions.getAddItemAction());
        addDefinitionMenuItem.setText(resourceBundle.getString("addDefinitionMenuItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
        popupMenu.add(addDefinitionMenuItem);
        JMenuItem editDefinitionMenuItem = ActionUtils.actionToMenuItem(editActions.getEditItemAction());
        editDefinitionMenuItem.setText(resourceBundle.getString("editDefinitionMenuItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
        popupMenu.add(editDefinitionMenuItem);

        catalogEditorPanel.setPanelPopup(popupMenu);

        catalogEditorPanel.addToolbarActions(editActions);

        addDefinitionAction.setParentComponent(catalogEditorPanel);
        editDefinitionAction.setParentComponent(catalogEditorPanel);
        removeDefinitionAction.setParentComponent(catalogEditorPanel);

        ComponentModuleApi componentModule = App.getModule(ComponentModuleApi.class);
        MoveItemActions moveItemActions = componentModule.createMoveItemActions(moveItemActionsHandler);
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
