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
package org.exbin.xbup.jaguif.catalog;

import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.jaguif.catalog.gui.CatalogEditorPanel;
import org.exbin.xbup.jaguif.catalog.item.action.AddCatalogItemAction;
import org.exbin.xbup.jaguif.catalog.item.action.DeleteCatalogItemAction;
import org.exbin.xbup.jaguif.catalog.item.action.EditCatalogItemAction;
import org.jspecify.annotations.NullMarked;

/**
 * Catalog editor tree controller.
 */
@NullMarked
public class CatalogEditorTreeController implements ContextEditItem {

    protected final AddCatalogItemAction addCatalogItemAction = new AddCatalogItemAction();
    protected final EditCatalogItemAction editCatalogItemAction = new EditCatalogItemAction();
    protected final DeleteCatalogItemAction deleteCatalogItemAction = new DeleteCatalogItemAction();

    protected final CatalogEditorPanel catalogEditorPanel;

    public CatalogEditorTreeController(CatalogEditorPanel catalogEditorPanel) {
        this.catalogEditorPanel = catalogEditorPanel;
        addCatalogItemAction.init();
        editCatalogItemAction.init();
        deleteCatalogItemAction.init();
    }

    @Override
    public void performAddItem() {
        addCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
        addCatalogItemAction.actionPerformed(null);
        XBCItem resultItem = addCatalogItemAction.getResultItem();
        if (resultItem != null) {
            catalogEditorPanel.reloadNodesTree();
            catalogEditorPanel.setNode(resultItem instanceof XBCNode ? (XBCNode) resultItem : catalogEditorPanel.getSpecsNode());
            catalogEditorPanel.selectSpecTableRow(resultItem);
        }
    }

    @Override
    public void performEditItem() {
        editCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
        editCatalogItemAction.actionPerformed(null);
        catalogEditorPanel.reloadNodesTree();
    }

    @Override
    public void performDeleteItem() {
        deleteCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
        deleteCatalogItemAction.actionPerformed(null);
        catalogEditorPanel.reloadNodesTree();
    }

    @Override
    public boolean canAddItem() {
        return true;
    }

    @Override
    public boolean canDeleteItem() {
        XBCNode node = catalogEditorPanel.getSelectedTreeItem();
        return node != null && node.getParent().isPresent();
    }

    @Override
    public boolean canEditItem() {
        return catalogEditorPanel.hasTreeSelection();
    }
}
