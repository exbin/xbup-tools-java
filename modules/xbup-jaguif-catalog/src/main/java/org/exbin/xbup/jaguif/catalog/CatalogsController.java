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

import java.util.Optional;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.jaguif.catalog.action.AddCatalogAction;
import org.exbin.xbup.jaguif.catalog.action.DeleteCatalogAction;
import org.exbin.xbup.jaguif.catalog.action.EditCatalogAction;
import org.exbin.xbup.jaguif.catalog.gui.CatalogsManagerPanel;
import org.jspecify.annotations.NullMarked;

/**
 * Catalogs controller.
 */
@NullMarked
public class CatalogsController implements ContextEditItem {

    protected final CatalogsManagerPanel catalogsManagerPanel;

    public CatalogsController(CatalogsManagerPanel catalogsManagerPanel) {
        this.catalogsManagerPanel = catalogsManagerPanel;
    }

    @Override
    public void performAddItem() {
        AddCatalogAction action = new AddCatalogAction();
        action.init();
        action.setParentComponent(catalogsManagerPanel);
        action.actionPerformed(null);
        Optional<XBCRoot> resultRoot = action.getResultRoot();
        if (resultRoot.isPresent()) {
            catalogsManagerPanel.reload();
        }
    }

    @Override
    public void performEditItem() {
        EditCatalogAction action = new EditCatalogAction();
        action.init();
        action.setParentComponent(catalogsManagerPanel);
        action.setActiveItem(catalogsManagerPanel.getSelectedItem());
        action.actionPerformed(null);
        catalogsManagerPanel.reload();
    }

    @Override
    public void performDeleteItem() {
        DeleteCatalogAction action = new DeleteCatalogAction();
        action.init();
        action.setParentComponent(catalogsManagerPanel);
        action.setActiveItem(catalogsManagerPanel.getSelectedItem());
        action.actionPerformed(null);
    }

    @Override
    public boolean canAddItem() {
        return true;
    }

    @Override
    public boolean canDeleteItem() {
        return catalogsManagerPanel.hasSelection();
    }

    @Override
    public boolean canEditItem() {
        return catalogsManagerPanel.hasSelection();
    }
}
