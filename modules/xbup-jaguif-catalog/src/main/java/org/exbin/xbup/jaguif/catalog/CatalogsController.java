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

import org.exbin.jaguif.App;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.jaguif.window.api.controller.DefaultControlController;
import org.exbin.jaguif.window.api.gui.CloseControlPanel;
import org.exbin.jaguif.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.catalog.modifiable.XBMRoot;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCRootManager;
import org.exbin.xbup.jaguif.catalog.gui.AddCatalogPanel;
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
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        AddCatalogPanel panel = new AddCatalogPanel();
        XBACatalog catalog = catalogsManagerPanel.getCatalog();
        panel.setCatalog(catalog);
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        controlPanel.setController((actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                XBCRootManager rootManager = catalog.getCatalogManager(XBCRootManager.class);
                XBCRoot resultRoot = (XBMRoot) rootManager.createEmptyRoot(panel.getCatalogUrl());
                if (resultRoot != null) {
                    catalogsManagerPanel.reload();
                }
            }
            dialog.close();
            dialog.dispose();
        });
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        dialog.showCentered(catalogsManagerPanel);
    }

    @Override
    public void performEditItem() {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);

        CatalogEditor catalogEditor = new CatalogEditor();
        catalogEditor.setCatalog(catalogsManagerPanel.getCatalog());
        catalogEditor.setCatalogRoot(catalogsManagerPanel.getSelectedItem());
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(catalogEditor.getCatalogEditorPanel(), controlPanel);
        controlPanel.setController(() -> {
            dialog.close();
            dialog.dispose();
            catalogsManagerPanel.reload();
        });
        windowModule.setWindowTitle(dialog, catalogEditor.getCatalogEditorPanel().getResourceBundle());
        dialog.showCentered(catalogsManagerPanel);
    }

    @Override
    public void performDeleteItem() {
        throw new UnsupportedOperationException("Not supported yet.");
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
