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
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.xbup.catalog.item.property.action.EditBlockPaneAction;
import org.exbin.xbup.catalog.entity.XBEXBlockUi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;

/**
 * Catalog panel editor property cell panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogPEditorPropertyTableCellPanel extends CatalogPropertyTableCellPanel {

    private XBApplication application;
    private XBACatalog catalog;
    private long paneId;
    private XBCBlockRev blockRev;
    private XBCXPlugUi plugUi;
    private EditBlockPaneAction editBlockPaneAction = new EditBlockPaneAction();

    public CatalogPEditorPropertyTableCellPanel(XBACatalog catalog) {
        super();
        this.catalog = catalog;
        init();
    }

    private void init() {
        setEditorAction((ActionEvent e) -> {
            performEditorAction(e);
        });
    }

    public void setApplication(XBApplication application) {
        this.application = application;
    }

    public void performEditorAction(ActionEvent event) {
        editBlockPaneAction.actionPerformed(event);
        XBEXBlockUi resultBlockPane = editBlockPaneAction.getResultBlockPane();
        if (resultBlockPane != null) {
            paneId = resultBlockPane.getId();
            setPropertyLabel();
        }
    }

    public void setCatalogItem(XBCItem catalogItem) {
        XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
        XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
        long maxRev = revService.findMaxRevXB((XBCBlockSpec) catalogItem);
        blockRev = (XBCBlockRev) revService.findRevByXB((XBCBlockSpec) catalogItem, maxRev);
        XBCXBlockUi blockUi = uiService.findUiByPR(blockRev, XBPlugUiType.PANEL_EDITOR, 0);
        plugUi = blockUi == null ? null : blockUi.getUi();
        paneId = blockUi == null ? 0 : blockUi.getId();
        editBlockPaneAction.setCurrentBlockRev(blockRev);
        editBlockPaneAction.setCurrentPlugUi(plugUi);

        setPropertyLabel();
    }

    private void setPropertyLabel() {
        setPropertyText(paneId > 0 ? String.valueOf(paneId) : "");
    }

    public long getPaneId() {
        return paneId;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
