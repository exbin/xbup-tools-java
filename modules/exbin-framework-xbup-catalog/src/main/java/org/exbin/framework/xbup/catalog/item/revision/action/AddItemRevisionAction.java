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
package org.exbin.framework.xbup.catalog.item.revision.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.data.model.CatalogRevsTableItem;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.xbup.catalog.item.revision.gui.CatalogSpecRevEditorPanel;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Add new revision action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AddItemRevisionAction extends AbstractAction {

    public static final String ACTION_ID = "addCatalogItemRevisionAction";
    
    private XBACatalog catalog;

    private Component parentComponent;
    private CatalogRevsTableItem resultRevision;

    public AddItemRevisionAction() {
    }

    @Nullable
    public CatalogRevsTableItem getResultRevision() {
        return resultRevision;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultRevision = null;
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogSpecRevEditorPanel panel = new CatalogSpecRevEditorPanel();
        panel.setRevItem(new CatalogRevsTableItem());
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                resultRevision = panel.getRevItem();
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
