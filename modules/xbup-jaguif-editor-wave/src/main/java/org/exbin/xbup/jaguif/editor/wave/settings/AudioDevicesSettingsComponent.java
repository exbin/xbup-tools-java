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
package org.exbin.xbup.jaguif.editor.wave.settings;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.jaguif.App;
import org.exbin.xbup.jaguif.editor.wave.settings.AudioDevicesOptions;
import org.exbin.xbup.jaguif.editor.wave.settings.gui.AudioDevicesSettingsPanel;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.options.settings.api.SettingsComponent;
import org.exbin.jaguif.options.settings.api.SettingsOptionsProvider;
import org.exbin.jaguif.options.api.OptionsStorage;
import org.exbin.jaguif.options.settings.api.SettingsComponentProvider;

/**
 * Audio devices options page.
 */
@ParametersAreNonnullByDefault
public class AudioDevicesSettingsComponent implements SettingsComponentProvider {
    
    public static final String PAGE_ID = "audioDevices";

    @Nonnull
    @Override
    public SettingsComponent createComponent() {
        return new AudioDevicesSettingsPanel();
    }

    /* @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return App.getModule(LanguageModuleApi.class).getBundle(AudioDevicesSettingsPanel.class);
    }

    @Nonnull
    @Override
    public AudioDevicesOptions createOptions() {
        return new AudioDevicesOptions(new DefaultOptionsStorage());
    }

    @Override
    public void loadFromPreferences(OptionsStorage preferences, AudioDevicesOptions options) {
        new AudioDevicesOptions(preferences).copyTo(options);
    }

    @Override
    public void saveToPreferences(OptionsStorage preferences, AudioDevicesOptions options) {
        options.copyTo(new AudioDevicesOptions(preferences));
    }

    @Override
    public void applyPreferencesChanges(AudioDevicesOptions options) {
        // TODO
    } */
}
