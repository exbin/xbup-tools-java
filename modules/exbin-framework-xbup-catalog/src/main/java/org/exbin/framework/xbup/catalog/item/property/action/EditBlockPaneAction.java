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
package org.exbin.framework.xbup.catalog.item.property.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.xbup.catalog.item.plugin.gui.CatalogSelectComponentEditorPanel;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEXBlockUi;
import org.exbin.xbup.catalog.entity.XBEXPlugUi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;

/**
 * Edit block pane action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditBlockPaneAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogItemBlockPaneAction";
    
    private XBACatalog catalog;

    private Component parentComponent;
    private XBCBlockRev currentBlockRev;
    private XBCXPlugUi currentPlugUi;
    private XBEXBlockUi resultBlockPane;

    public EditBlockPaneAction() {
    }

    public void setup() {
    }

    @Nullable
    public XBCXPlugUi getCurrentPlugUi() {
        return currentPlugUi;
    }

    public void setCurrentPlugUi(XBCXPlugUi currentPlugUi) {
        this.currentPlugUi = currentPlugUi;
    }

    public XBCBlockRev getCurrentBlockRev() {
        return currentBlockRev;
    }

    public void setCurrentBlockRev(XBCBlockRev currentBlockRev) {
        this.currentBlockRev = currentBlockRev;
    }

    public XBEXBlockUi getResultBlockPane() {
        return resultBlockPane;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultBlockPane = null;
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogSelectComponentEditorPanel panelSelectPanel = new CatalogSelectComponentEditorPanel();
        panelSelectPanel.setCatalog(catalog);
        panelSelectPanel.setPlugUi(currentPlugUi);
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panelSelectPanel, controlPanel);
//        frameModule.setDialogTitle(dialog, paneSelectPanel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            switch (actionType) {
                case OK: {
                    currentPlugUi = panelSelectPanel.getPlugUi();

                    XBEXBlockUi blockPane = new XBEXBlockUi();
                    blockPane.setBlockRev((XBEBlockRev) currentBlockRev);
                    blockPane.setUi((XBEXPlugUi) currentPlugUi);
                    blockPane.setPriority(0L);

                    EntityManager em = ((XBECatalog) catalog).getEntityManager();
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.persist(blockPane);

                    em.flush();
                    transaction.commit();

                    resultBlockPane = blockPane;
//                    paneId = blockPane.getId();
//                    setPropertyLabel();
                    break;
                }
                case CANCEL: {
                    break;
                }
            }
            dialog.close();
        });
        dialog.showCentered(parentComponent);
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
