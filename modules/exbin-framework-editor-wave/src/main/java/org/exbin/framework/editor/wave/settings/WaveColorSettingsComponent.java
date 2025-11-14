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
package org.exbin.framework.editor.wave.settings;

import java.awt.Color;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.editor.wave.settings.WaveColorOptions;
import org.exbin.framework.editor.wave.settings.gui.WaveColorSettingsPanel;
import org.exbin.framework.options.settings.api.SettingsComponent;
import org.exbin.framework.options.settings.api.SettingsComponentProvider;
import org.exbin.framework.editor.wave.WaveColorState;

/**
 * Wave editor color options page.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class WaveColorSettingsComponent implements SettingsComponentProvider {
    
    public static final String PAGE_ID = "waveColor";

    private WaveColorState waveColorService;

    public void setWaveColorService(WaveColorState waveColorService) {
        this.waveColorService = waveColorService;
    }
    
    @Nonnull
    @Override
    public SettingsComponent createComponent() {
        WaveColorSettingsPanel panel = new WaveColorSettingsPanel();
        panel.setWaveColorService(waveColorService);
        return panel;
    }

    /* @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return App.getModule(LanguageModuleApi.class).getBundle(WaveColorSettingsPanel.class);
    }

    @Nonnull
    @Override
    public WaveColorOptions createOptions() {
        return new WaveColorOptions(new DefaultOptionsStorage());
    }

    @Override
    public void loadFromPreferences(OptionsStorage preferences, WaveColorOptions options) {
        new WaveColorOptions(preferences).copyTo(options);
    }

    @Override
    public void saveToPreferences(OptionsStorage preferences, WaveColorOptions options) {
        options.copyTo(new WaveColorOptions(preferences));
    }

    @Override
    public void applyPreferencesChanges(WaveColorOptions options) {
        if (options.isUseDefaultColors()) {
            waveColorService.setCurrentWaveColors(waveColorService.getCurrentWaveColors());
        } else {
            Color[] colors = new Color[6];
            colors[0] = intToColor(options.getWaveColor());
            colors[1] = intToColor(options.getWaveFillColor());
            colors[2] = intToColor(options.getWaveCursorColor());
            colors[3] = intToColor(options.getWaveCursorWaveColor());
            colors[4] = intToColor(options.getWaveBackgroundColor());
            colors[5] = intToColor(options.getWaveSelectionColor());
            waveColorService.setCurrentWaveColors(colors);
        }
    } */

    @Nullable
    private Color intToColor(@Nullable Integer intValue) {
        return intValue == null ? null : new Color(intValue);
    }
}
