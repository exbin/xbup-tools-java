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
package org.exbin.framework.xbup.catalog.item.property.gui;

import java.awt.event.ActionEvent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.RemovalControlPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.framework.window.api.controller.RemovalControlController;

/**
 * Catalog big icon property cell panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogBIconPropertyTableCellPanel extends CatalogPropertyTableCellPanel {

    private XBACatalog catalog;
    private byte[] icon;

    public CatalogBIconPropertyTableCellPanel(XBACatalog catalog) {
        super();
        this.catalog = catalog;
        init();
    }

    private void init() {
        setEditorAction((ActionEvent e) -> {
            performEditorAction();
        });
    }

    public void performEditorAction() {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogEditIconPanel iconPanel = new CatalogEditIconPanel(catalog, icon);
        RemovalControlPanel controlPanel = new RemovalControlPanel();
        final WindowHandler dialog = windowModule.createDialog(iconPanel, controlPanel);
        windowModule.setWindowTitle(dialog, iconPanel.getResourceBundle());
        controlPanel.setController((RemovalControlController.ControlActionType actionType) -> {
            switch (actionType) {
                case OK: {
                    icon = iconPanel.getIcon();
                    setPropertyLabel();
                    break;
                }
                case CANCEL: {
                    break;
                }
                case REMOVE: {
                    icon = new byte[0];
                    setPropertyLabel();
                    break;
                }
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }

    public void setCatalogItem(XBCItem catalogItem) {
        XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
        icon = iconService.getDefaultBigIconData(catalogItem);
        setPropertyLabel();
    }

    private void setPropertyLabel() {
        setPropertyText(icon == null || icon.length == 0 ? "" : "[" + icon.length + " bytes]");
    }

    @Nullable
    public byte[] getIcon() {
        return icon;
    }

    @Nonnull
    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
