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
package org.exbin.xbup.jaguif.catalog.item.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.xbup.jaguif.catalog.item.gui.CatalogEditItemPanel;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.jaguif.window.api.controller.DefaultControlController;

/**
 * Edit item to catalog action.
 */
@NullMarked
public class EditCatalogItemAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogItem";

    private XBACatalog catalog;

    private Component parentComponent;
    private XBCItem currentItem;
    private XBCItem resultItem;

    public EditCatalogItemAction() {
    }

    public void init() {
    }

    @Nullable
    public XBCItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(@Nullable XBCItem currentItem) {
        this.currentItem = currentItem;
    }

    @Nullable
    public XBCItem getResultItem() {
        return resultItem;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultItem = null;
        if (currentItem != null) {
            WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
            CatalogEditItemPanel editPanel = new CatalogEditItemPanel();
            editPanel.setCatalog(catalog);
            editPanel.setCatalogItem(currentItem);
            editPanel.setVisible(true);

            DefaultControlPanel controlPanel = new DefaultControlPanel();
            final WindowHandler dialog = windowModule.createDialog(editPanel, controlPanel);
            windowModule.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
            windowModule.setWindowTitle(dialog, editPanel.getResourceBundle());
            controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
                if (actionType == DefaultControlController.ControlActionType.OK) {
                    EntityManager em = ((XBECatalog) catalog).getEntityManager();
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    editPanel.persist();
                    em.flush();
                    transaction.commit();
                    resultItem = currentItem;
                }
                dialog.close();
            });
            dialog.showCentered(parentComponent);
            dialog.dispose();
        }
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
