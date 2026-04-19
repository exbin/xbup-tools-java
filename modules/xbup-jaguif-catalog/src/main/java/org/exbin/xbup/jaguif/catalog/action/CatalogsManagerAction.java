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
package org.exbin.xbup.jaguif.catalog.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.xbup.jaguif.catalog.CatalogsManager;
import org.exbin.xbup.jaguif.catalog.gui.CatalogsManagerPanel;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.gui.CloseControlPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.jaguif.context.api.ContextChangeRegistration;

/**
 * Catalogs manager action.
 */
@ParametersAreNonnullByDefault
public class CatalogsManagerAction extends AbstractAction {

    public static final String ACTION_ID = "catalogsManager";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogsManagerAction.class);
    private XBACatalog catalog;

    public CatalogsManagerAction() {
    }

    public void init() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(XBACatalog.class, (instance) -> {
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
