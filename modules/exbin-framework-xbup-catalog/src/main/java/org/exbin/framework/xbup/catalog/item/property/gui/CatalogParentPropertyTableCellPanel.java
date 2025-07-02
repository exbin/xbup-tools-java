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

import org.exbin.framework.xbup.catalog.item.spec.gui.CatalogSelectSpecPanel;
import java.awt.event.ActionEvent;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JOptionPane;
import org.exbin.framework.App;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.framework.window.api.controller.DefaultControlController;

/**
 * Catalog parent cell panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogParentPropertyTableCellPanel extends CatalogPropertyTableCellPanel {

    private XBACatalog catalog;
    private XBCNode parent;

    public CatalogParentPropertyTableCellPanel(XBACatalog catalog) {
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
        if (parent == null) {
            JOptionPane.showMessageDialog(this, "You cannot move root node", "Editing not allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogSelectSpecPanel panel = new CatalogSelectSpecPanel(CatalogItemType.NODE);
        panel.setCatalog(catalog);
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        panel.setSelectionListener((XBCItem item) -> {
            controlPanel.setActionEnabled(DefaultControlController.ControlActionType.OK, item != null);
        });
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                parent = (XBCNode) panel.getSpec();
                setNodeLabel();
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }

    public void setCatalogItem(XBCItem catalogItem) {
        parent = (XBCNode) catalogItem.getParentItem().orElse(null);
        setNodeLabel();
    }

    private void setNodeLabel() {
        XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
        String targetCaption = parent == null ? null : nameService.getItemNamePath(parent);
        if (targetCaption == null) {
            targetCaption = "";
        } else {
            targetCaption += " ";
        }

        if (parent != null) {
            targetCaption += "(" + Long.toString(parent.getId()) + ")";
        }

        setPropertyText(targetCaption);
    }

    @Nonnull
    public Optional<XBCNode> getParentNode() {
        return Optional.ofNullable(parent);
    }

    @Nonnull
    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
