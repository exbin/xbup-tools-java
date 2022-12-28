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
package org.exbin.framework.xbup.catalog;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.xbup.catalog.gui.CatalogEditorPanel;
import org.exbin.framework.xbup.catalog.item.action.AddCatalogItemAction;
import org.exbin.framework.xbup.catalog.item.action.DeleteCatalogItemAction;
import org.exbin.framework.xbup.catalog.item.action.EditCatalogItemAction;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalog editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogEditor {

    private final CatalogEditorPanel catalogEditorPanel;
    private final DefaultEditItemActions treeActions;
    private final DefaultEditItemActions itemActions;
    private XBApplication application;
    private XBACatalog catalog;

    public CatalogEditor() {
        catalogEditorPanel = new CatalogEditorPanel();
        treeActions = new DefaultEditItemActions();
        treeActions.setEditItemActionsHandler(new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                AddCatalogItemAction action = new AddCatalogItemAction();
                action.setup(application);
                action.setCatalog(catalog);
                action.setParentComponent(catalogEditorPanel);
                action.actionPerformed(null);
                XBCItem resultItem = action.getResultItem();
                if (resultItem != null) {
                    catalogEditorPanel.reloadNodesTree();
                }
            }

            @Override
            public void performEditItem() {
                EditCatalogItemAction action = new EditCatalogItemAction();
                action.setup(application);
                action.setCatalog(catalog);
                action.setParentComponent(catalogEditorPanel);
                action.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                action.actionPerformed(null);
                catalogEditorPanel.reloadNodesTree();
            }

            @Override
            public void performDeleteItem() {
                DeleteCatalogItemAction action = new DeleteCatalogItemAction();
                action.setup(application, catalog);
                action.setParentComponent(catalogEditorPanel);
                action.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
                action.actionPerformed(null);
            }

            @Override
            public boolean isSelection() {
                return catalogEditorPanel.getSelectedTreeItem() != null;
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                catalogEditorPanel.addTreeSelectionListener(updateListener);
            }
        });
        itemActions = new DefaultEditItemActions();
        init();
    }

    private void init() {
        catalogEditorPanel.addTreeActions(treeActions);
        catalogEditorPanel.addItemActions(itemActions);
    }

    @Nonnull
    public CatalogEditorPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        catalogEditorPanel.setApplication(application);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);
    }

    public void setCatalogRoot(XBCRoot root) {

    }
}
