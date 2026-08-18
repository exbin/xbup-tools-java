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
import org.exbin.xbup.jaguif.catalog.gui.CatalogEditorPanel;
import org.exbin.xbup.jaguif.catalog.item.action.AddCatalogItemAction;
import org.exbin.xbup.jaguif.catalog.item.action.DeleteCatalogItemAction;
import org.exbin.xbup.jaguif.catalog.item.action.EditCatalogItemAction;
import org.jspecify.annotations.NullMarked;

/**
 * Catalog editor tree controller.
 */
@NullMarked
public class CatalogEditorItemController implements ContextEditItem {

    protected final AddCatalogItemAction addCatalogItemAction = new AddCatalogItemAction();
    protected final EditCatalogItemAction editCatalogItemAction = new EditCatalogItemAction();
    protected final DeleteCatalogItemAction deleteCatalogItemAction = new DeleteCatalogItemAction();

    protected final CatalogEditorPanel catalogEditorPanel;

    public CatalogEditorItemController(CatalogEditorPanel catalogEditorPanel) {
        this.catalogEditorPanel = catalogEditorPanel;
        addCatalogItemAction.setParentComponent(catalogEditorPanel);
        editCatalogItemAction.setParentComponent(catalogEditorPanel);
        deleteCatalogItemAction.setParentComponent(catalogEditorPanel);

        addCatalogItemAction.init();
        editCatalogItemAction.init();
        deleteCatalogItemAction.init();
    }

    @Override
    public void performAddItem() {
        addCatalogItemAction.setCurrentItem(catalogEditorPanel.getCurrentItem());
        addCatalogItemAction.actionPerformed(null);
        XBCItem resultItem = addCatalogItemAction.getResultItem();
        if (resultItem != null) {
            catalogEditorPanel.setItem(resultItem);
            catalogEditorPanel.setSpecsNode(catalogEditorPanel.getSpecsNode());
            catalogEditorPanel.selectSpecTableRow(resultItem);
        }
    }

    @Override
    public void performEditItem() {
        editCatalogItemAction.setCurrentItem(catalogEditorPanel.getCurrentItem());
        editCatalogItemAction.actionPerformed(null);
        XBCItem resultItem = editCatalogItemAction.getResultItem();
        if (resultItem != null) {
            catalogEditorPanel.setItem(resultItem);
            catalogEditorPanel.setSpecsNode(catalogEditorPanel.getSpecsNode());
            catalogEditorPanel.selectSpecTableRow(resultItem);
        }
    }

    @Override
    public void performDeleteItem() {
        deleteCatalogItemAction.setCurrentItem(catalogEditorPanel.getCurrentItem());
        deleteCatalogItemAction.actionPerformed(null);
    }

    @Override
    public boolean canAddItem() {
        return catalogEditorPanel.hasTreeSelection();
    }

    @Override
    public boolean canDeleteItem() {
        XBCItem currentItem = catalogEditorPanel.getCurrentItem();
        return currentItem != null && (currentItem != catalogEditorPanel.getSelectedTreeItem());
    }

    @Override
    public boolean canEditItem() {
        return catalogEditorPanel.hasItemSelection();
    }
}
