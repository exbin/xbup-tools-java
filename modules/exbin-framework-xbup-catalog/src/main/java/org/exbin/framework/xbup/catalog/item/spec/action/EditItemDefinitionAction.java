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
package org.exbin.framework.xbup.catalog.item.spec.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.data.model.CatalogDefsTableItem;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.xbup.catalog.item.spec.gui.CatalogSpecDefEditorPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.framework.window.api.controller.DefaultControlController;

/**
 * Add new definition record action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditItemDefinitionAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogItemDefinitionAction";
    
    private XBACatalog catalog;

    private Component parentComponent;
    private XBCSpec currentSpec;
    private CatalogDefsTableItem currentDefinition;
    private CatalogDefsTableItem resultDefinition;

    public EditItemDefinitionAction() {
    }

    public void setup() {
    }

    public XBCSpec getCurrentSpec() {
        return currentSpec;
    }

    public void setCurrentSpec(XBCSpec currentSpec) {
        this.currentSpec = currentSpec;
    }

    @Nullable
    public CatalogDefsTableItem getCurrentDefinition() {
        return currentDefinition;
    }

    public void setCurrentDefinition(CatalogDefsTableItem currentDefinition) {
        this.currentDefinition = currentDefinition;
    }

    @Nullable
    public CatalogDefsTableItem getResultDefinition() {
        return resultDefinition;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultDefinition = null;
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogSpecDefEditorPanel panel = new CatalogSpecDefEditorPanel();
        panel.setCatalog(catalog);
        panel.setSpec(currentSpec);
        panel.setDefItem(currentDefinition);
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                resultDefinition = panel.getDefItem();
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
