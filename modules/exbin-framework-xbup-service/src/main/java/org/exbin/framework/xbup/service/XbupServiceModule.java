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
package org.exbin.framework.xbup.service;

import java.awt.Component;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.xbup.service.gui.ConnectionPanel;
import org.exbin.framework.xbup.service.gui.ServiceManagerPanel;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.options.api.OptionsStorage;

/**
 * XBUP catalog service module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupServiceModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(XbupServiceModule.class);

    private ServiceManagerPanel servicePanel;
    private OptionsStorage preferences;

    public XbupServiceModule() {
    }

    public void openConnectionDialog(Component parentComponent) {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        ConnectionPanel panel = new ConnectionPanel();
        panel.loadConnectionList(preferences);
        final WindowHandler dialog = windowModule.createDialog(panel);
        WindowUtils.assignGlobalKeyListener(dialog.getWindow(), panel.getCloseButton());
        dialog.showCentered(parentComponent);
        dialog.dispose();
        panel.saveConnectionList(preferences);
        getServicePanel().setService(panel.getService());
    }

    @Nonnull
    public ServiceManagerPanel getServicePanel() {
        if (servicePanel == null) {
            servicePanel = new ServiceManagerPanel();
        }

        return servicePanel;
    }

    public void setPreferences(OptionsStorage preferences) {
        this.preferences = preferences;
    }
}
