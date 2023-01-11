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
import javax.swing.JPopupMenu;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.data.model.CatalogRevsTableItem;
import org.exbin.framework.xbup.catalog.item.revision.action.AddItemRevisionAction;
import org.exbin.framework.xbup.catalog.item.revision.action.EditItemRevisionAction;
import org.exbin.framework.xbup.catalog.item.revision.action.RemoveItemRevisionAction;
import org.exbin.framework.xbup.catalog.item.revision.gui.CatalogItemEditRevsPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Catalog editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogRevisionsEditor {

    private final CatalogItemEditRevsPanel catalogEditorPanel;
    private final DefaultEditItemActions editActions;
    private XBApplication application;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    
    private AddItemRevisionAction addRevisionAction = new AddItemRevisionAction();
    private EditItemRevisionAction editRevisionAction = new EditItemRevisionAction();
    private RemoveItemRevisionAction removeRevisionAction = new RemoveItemRevisionAction();

    public CatalogRevisionsEditor() {
        catalogEditorPanel = new CatalogItemEditRevsPanel();

        editActions = new DefaultEditItemActions(DefaultEditItemActions.MODE.DIALOG);
        editActions.setEditItemActionsHandler(new EditItemActionsHandler() {
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

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                catalogEditorPanel.addSelectionListener(updateListener);
            }
        });

        popupMenu = new JPopupMenu();
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

    public void setApplication(XBApplication application) {
        this.application = application;
        catalogEditorPanel.setApplication(application);

        addRevisionAction.setup(application);
        editRevisionAction.setup(application);
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
