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
package org.exbin.framework.viewer.xbup.settings;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.viewer.xbup.settings.gui.ServiceConnectionPanel;
import org.exbin.framework.options.settings.api.SettingsComponent;
import org.exbin.framework.options.settings.api.SettingsComponentProvider;

/**
 * XBUP service settings component.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ServiceConnectionSettingsComponent implements SettingsComponentProvider<ServiceConnectionOptions> {

    public static final String PAGE_ID = "serviceConnection";

    private ServiceConnectionPanel panel;

    @Nonnull
    @Override
    public SettingsComponent<ServiceConnectionOptions> createComponent() {
        if (panel == null) {
            panel = new ServiceConnectionPanel();
        }
        return panel;
    }

    /* @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return App.getModule(LanguageModuleApi.class).getBundle(ServiceConnectionPanel.class);
    }

    @Nonnull
    @Override
    public ServiceConnectionOptions createOptions() {
        return new ServiceConnectionOptions(new DefaultOptionsStorage());
    }

    @Override
    public void loadFromPreferences(OptionsStorage preferences, ServiceConnectionOptions options) {
        new ServiceConnectionOptions(preferences).copyTo(options);
    }

    @Override
    public void saveToPreferences(OptionsStorage preferences, ServiceConnectionOptions options) {
        options.copyTo(new ServiceConnectionOptions(preferences));
    }

    @Override
    public void applyPreferencesChanges(ServiceConnectionOptions options) {
        // options.getCatalogUpdateUrl();
    } */
}
