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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.options.settings.api.SettingsApplier;
import org.exbin.framework.options.settings.api.SettingsOptionsProvider;
import org.exbin.framework.editor.wave.WaveColorState;

/**
 * Wave editor color settings applier.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class WaveColorSettingsApplier implements SettingsApplier {

    public static final String APPLIER_ID = "waveColor";

    @Override
    public void applySettings(Object instance, SettingsOptionsProvider settingsOptionsProvider) {
        if (!(instance instanceof WaveColorState)) {
            return;
        }

        WaveColorState waveColorState = (WaveColorState) instance;
        WaveColorOptions options = settingsOptionsProvider.getSettingsOptions(WaveColorOptions.class);
        if (options.isUseDefaultColors()) {
            waveColorState.setCurrentWaveColors(waveColorState.getCurrentWaveColors());
        } else {
            Color[] colors = new Color[6];
            colors[0] = intToColor(options.getWaveColor());
            colors[1] = intToColor(options.getWaveFillColor());
            colors[2] = intToColor(options.getWaveCursorColor());
            colors[3] = intToColor(options.getWaveCursorWaveColor());
            colors[4] = intToColor(options.getWaveBackgroundColor());
            colors[5] = intToColor(options.getWaveSelectionColor());
            waveColorState.setCurrentWaveColors(colors);
        }
    }

    @Nullable
    private Color intToColor(@Nullable Integer intValue) {
        return intValue == null ? null : new Color(intValue);
    }
}
