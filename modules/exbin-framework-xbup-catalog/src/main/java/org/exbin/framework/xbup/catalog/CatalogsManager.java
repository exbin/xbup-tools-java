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

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.xbup.catalog.action.AddCatalogAction;
import org.exbin.framework.xbup.catalog.action.DeleteCatalogAction;
import org.exbin.framework.xbup.catalog.action.EditCatalogAction;
import org.exbin.framework.xbup.catalog.gui.CatalogsManagerPanel;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalogs manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogsManager {

    private final CatalogsManagerPanel catalogsManagerPanel;
    private final DefaultEditItemActions actions;
    private XBACatalog catalog;

    public CatalogsManager() {
        catalogsManagerPanel = new CatalogsManagerPanel();
        actions = new DefaultEditItemActions();
        actions.setEditItemActionsHandler(new EditItemActionsHandler() {
            @Override
            public void performAddItem() {
                AddCatalogAction action = new AddCatalogAction();
                action.setup();
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
                action.setup();
                action.setParentComponent(catalogsManagerPanel);
                action.setActiveItem(catalogsManagerPanel.getSelectedItem());
                action.actionPerformed(null);
                catalogsManagerPanel.reload();
            }

            @Override
            public void performDeleteItem() {
                DeleteCatalogAction action = new DeleteCatalogAction();
                action.setup();
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

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                catalogsManagerPanel.addRowSelectionListener((arg0) -> {
                    updateListener.stateChanged();
                });
            }
        });
        init();
    }

    private void init() {
        catalogsManagerPanel.addActions(actions);
    }

    @Nonnull
    public CatalogsManagerPanel getCatalogsManagerPanel() {
        return catalogsManagerPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogsManagerPanel.setCatalog(catalog);
    }
}
