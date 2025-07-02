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
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.RemovalControlPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.framework.window.api.controller.RemovalControlController;

/**
 * Catalog hDoc property cell panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogDocPropertyTableCellPanel extends CatalogPropertyTableCellPanel {

    private XBACatalog catalog;
    private String doc;

    public CatalogDocPropertyTableCellPanel(XBACatalog catalog) {
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
        CatalogEditDocumentationPanel docPanel = new CatalogEditDocumentationPanel();
        docPanel.setDocumentation(doc);
        RemovalControlPanel controlPanel = new RemovalControlPanel();
        final WindowHandler dialog = windowModule.createDialog(docPanel, controlPanel);
        windowModule.setWindowTitle(dialog, docPanel.getResourceBundle());
        controlPanel.setController((RemovalControlController.ControlActionType actionType) -> {
            switch (actionType) {
                case OK: {
                    doc = docPanel.getDocumentation();
                    setDocLabel();
                    break;
                }
                case CANCEL: {
                    break;
                }
                case REMOVE: {
                    doc = "";
                    setDocLabel();
                    break;
                }
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }

    public void setCatalogItem(XBCItem catalogItem) {
        XBCXHDocService hDocService = catalog.getCatalogService(XBCXHDocService.class);
        doc = hDocService.getDocumentationText(catalogItem);
        setDocLabel();
    }

    private void setDocLabel() {
        setPropertyText(doc == null || doc.isEmpty() ? "" : "[" + doc.length() + " bytes]");
    }

    public String getDocument() {
        return doc;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
