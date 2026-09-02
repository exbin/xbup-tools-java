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

import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.component.api.ContextMoveItem;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.xbup.jaguif.catalog.item.spec.action.AddItemDefinitionAction;
import org.exbin.xbup.jaguif.catalog.item.spec.action.EditItemDefinitionAction;
import org.exbin.xbup.jaguif.catalog.item.spec.action.RemoveItemDefinitionAction;
import org.exbin.xbup.jaguif.catalog.item.spec.gui.CatalogItemEditDefinitionPanel;
import org.exbin.xbup.jaguif.catalog.model.CatalogDefsTableItem;
import org.jspecify.annotations.NullMarked;

/**
 * Catalog definitions editor controller.
 */
@NullMarked
public class CatalogDefinitionEditorController implements ContextEditItem, ContextMoveItem {

    protected final AddItemDefinitionAction addDefinitionAction = new AddItemDefinitionAction();
    protected final EditItemDefinitionAction editDefinitionAction = new EditItemDefinitionAction();
    protected final RemoveItemDefinitionAction removeDefinitionAction = new RemoveItemDefinitionAction();

    protected final CatalogItemEditDefinitionPanel catalogEditorPanel;

    public CatalogDefinitionEditorController(CatalogItemEditDefinitionPanel catalogEditorPanel) {
        this.catalogEditorPanel = catalogEditorPanel;
        addDefinitionAction.init();
        editDefinitionAction.init();
        removeDefinitionAction.init();
    }

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
        int selectedDefinitionIndex = catalogEditorPanel.getSelectedDefinitionIndex();
        catalogEditorPanel.definitionMovedTop(selectedDefinitionIndex);
    }

    @Override
    public void performMoveBottom() {
        int selectedDefinitionIndex = catalogEditorPanel.getSelectedDefinitionIndex();
        catalogEditorPanel.definitionMovedBottom(selectedDefinitionIndex);
    }

    @Override
    public boolean isSelection() {
        CatalogDefsTableItem revision = catalogEditorPanel.getSelectedDefinition();
        return revision != null;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    public void registerMonitoring(ContextMonitoringRegistration monitoringRegistrar) {
        monitoringRegistrar.registerContextMonitoring(addDefinitionAction);
        monitoringRegistrar.registerContextMonitoring(editDefinitionAction);
        monitoringRegistrar.registerContextMonitoring(removeDefinitionAction);
    }
}
