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
package org.exbin.framework.xbup.catalog.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeManager;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.xbup.catalog.gui.AddCatalogPanel;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.catalog.modifiable.XBMRoot;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCRootManager;
import org.exbin.framework.window.api.controller.DefaultControlController;

/**
 * Add catalog root action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AddCatalogAction extends AbstractAction {

    public static final String ACTION_ID = "addCatalogAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AddCatalogAction.class);

    private XBACatalog catalog;

    private XBCRoot resultRoot;

    private Component parentComponent;

    public AddCatalogAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ActionContextChangeManager manager) {
                manager.registerUpdateListener(XBACatalog.class, (instance) -> {
                    catalog = instance;
                });
            }
        });
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Nonnull
    public Optional<XBCRoot> getResultRoot() {
        return Optional.ofNullable(resultRoot);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        resultRoot = null;
        AddCatalogPanel panel = new AddCatalogPanel();
        panel.setCatalog(catalog);
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        controlPanel.setController((actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                XBCRootManager rootManager = catalog.getCatalogManager(XBCRootManager.class);
                resultRoot = (XBMRoot) rootManager.createEmptyRoot(panel.getCatalogUrl());
            }
            dialog.close();
            dialog.dispose();
        });
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        dialog.showCentered(parentComponent);
    }
}
