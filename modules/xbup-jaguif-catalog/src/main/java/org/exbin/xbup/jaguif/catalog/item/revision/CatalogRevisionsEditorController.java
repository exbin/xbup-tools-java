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

import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.xbup.jaguif.catalog.item.revision.action.AddItemRevisionAction;
import org.exbin.xbup.jaguif.catalog.item.revision.action.EditItemRevisionAction;
import org.exbin.xbup.jaguif.catalog.item.revision.action.RemoveItemRevisionAction;
import org.exbin.xbup.jaguif.catalog.item.revision.gui.CatalogItemEditRevsPanel;
import org.exbin.xbup.jaguif.catalog.model.CatalogRevsTableItem;
import org.jspecify.annotations.NullMarked;

/**
 * Catalog revisions editor controller.
 */
@NullMarked
public class CatalogRevisionsEditorController implements ContextEditItem {

    protected final AddItemRevisionAction addRevisionAction = new AddItemRevisionAction();
    protected final EditItemRevisionAction editRevisionAction = new EditItemRevisionAction();
    protected final RemoveItemRevisionAction removeRevisionAction = new RemoveItemRevisionAction();

    protected final CatalogItemEditRevsPanel catalogEditorPanel;

    public CatalogRevisionsEditorController(CatalogItemEditRevsPanel catalogEditorPanel) {
        this.catalogEditorPanel = catalogEditorPanel;
        addRevisionAction.init();
        editRevisionAction.init();
        removeRevisionAction.init();
    }

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
    public boolean canDeleteItem() {
        CatalogRevsTableItem revision = catalogEditorPanel.getSelectedRevision();
        return revision != null;
    }

    @Override
    public boolean canEditItem() {
        CatalogRevsTableItem revision = catalogEditorPanel.getSelectedRevision();
        return revision != null;
    }

    public void registerMonitoring(ContextMonitoringRegistration monitoringRegistrar) {
        monitoringRegistrar.registerContextMonitoring(addRevisionAction);
        monitoringRegistrar.registerContextMonitoring(editRevisionAction);
        monitoringRegistrar.registerContextMonitoring(removeRevisionAction);
    }
}
