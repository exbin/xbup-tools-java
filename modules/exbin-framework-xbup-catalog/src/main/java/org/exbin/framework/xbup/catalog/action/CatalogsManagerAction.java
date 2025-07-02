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
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeManager;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.xbup.catalog.CatalogsManager;
import org.exbin.framework.xbup.catalog.gui.CatalogsManagerPanel;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.CloseControlPanel;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Catalogs manager action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogsManagerAction extends AbstractAction {

    public static final String ACTION_ID = "catalogsManagerAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogsManagerAction.class);
    private XBACatalog catalog;

    public CatalogsManagerAction() {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        CatalogsManager catalogsBrowser = new CatalogsManager();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        catalogsBrowser.setCatalog(catalog);
        CatalogsManagerPanel panel = catalogsBrowser.getCatalogsManagerPanel();
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        windowModule.addHeaderPanel(dialog.getWindow(), CatalogsManagerPanel.class, panel.getResourceBundle());
        controlPanel.setController(() -> {
            dialog.close();
            dialog.dispose();
        });
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        dialog.showCentered((Component) e.getSource());
    }
}
