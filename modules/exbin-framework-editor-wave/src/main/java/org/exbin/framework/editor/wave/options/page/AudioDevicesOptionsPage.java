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
package org.exbin.framework.editor.wave.options.page;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.editor.wave.options.AudioDevicesOptions;
import org.exbin.framework.editor.wave.options.gui.AudioDevicesOptionsPanel;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.options.api.DefaultOptionsPage;
import org.exbin.framework.options.api.DefaultOptionsStorage;
import org.exbin.framework.options.api.OptionsComponent;
import org.exbin.framework.preferences.api.OptionsStorage;

/**
 * Audio devices options page.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AudioDevicesOptionsPage implements DefaultOptionsPage<AudioDevicesOptions> {
    
    public static final String PAGE_ID = "audioDevices";

    @Nonnull
    @Override
    public String getId() {
        return PAGE_ID;
    }

    @Nonnull
    @Override
    public OptionsComponent<AudioDevicesOptions> createPanel() {
        return new AudioDevicesOptionsPanel();
    }

    @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return App.getModule(LanguageModuleApi.class).getBundle(AudioDevicesOptionsPanel.class);
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
    }
}
