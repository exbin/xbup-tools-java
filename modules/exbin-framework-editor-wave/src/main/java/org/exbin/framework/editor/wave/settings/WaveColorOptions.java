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

import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.options.settings.api.SettingsOptions;
import org.exbin.framework.options.api.OptionsStorage;

/**
 * Wave editor color options.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class WaveColorOptions implements SettingsOptions {

    public static final String KEY_WAVE_COLOR_DEFAULT = "waveColor.default";
    public static final String KEY_WAVE_COLOR_WAVE = "waveColor.wave";
    public static final String KEY_WAVE_COLOR_WAVE_FILL = "waveColor.waveFill";
    public static final String KEY_WAVE_COLOR_BACKGROUND = "waveColor.background";
    public static final String KEY_WAVE_COLOR_SELECTION = "waveColor.selection";
    public static final String KEY_WAVE_COLOR_CURSOR = "waveColor.cursor";
    public static final String KEY_WAVE_COLOR_CURSOR_WAVE = "waveColor.cursorWave";

    private final OptionsStorage storage;

    public WaveColorOptions(OptionsStorage storage) {
        this.storage = storage;
    }

    public boolean isUseDefaultColors() {
        return storage.getBoolean(KEY_WAVE_COLOR_DEFAULT, true);
    }

    public void setUseDefaultColors(boolean useDefault) {
        storage.putBoolean(KEY_WAVE_COLOR_DEFAULT, useDefault);
    }

    @Nullable
    public Integer getWaveColor() {
        return getColorAsInt(KEY_WAVE_COLOR_WAVE);
    }

    @Nullable
    public Integer getWaveFillColor() {
        return getColorAsInt(KEY_WAVE_COLOR_WAVE_FILL);
    }

    @Nullable
    public Integer getWaveBackgroundColor() {
        return getColorAsInt(KEY_WAVE_COLOR_BACKGROUND);
    }

    @Nullable
    public Integer getWaveSelectionColor() {
        return getColorAsInt(KEY_WAVE_COLOR_SELECTION);
    }

    @Nullable
    public Integer getWaveCursorColor() {
        return getColorAsInt(KEY_WAVE_COLOR_CURSOR);
    }

    @Nullable
    public Integer getWaveCursorWaveColor() {
        return getColorAsInt(KEY_WAVE_COLOR_CURSOR_WAVE);
    }

    @Nullable
    private Integer getColorAsInt(String key) {
        Optional<String> value = storage.get(key);
        return value.isPresent() ? Integer.valueOf(value.get()) : null;
    }

    public void setWaveColor(@Nullable Integer color) {
        setColor(KEY_WAVE_COLOR_WAVE, color);
    }

    public void setWaveColor(int color) {
        storage.putInt(KEY_WAVE_COLOR_WAVE, color);
    }

    public void setWaveFillColor(@Nullable Integer color) {
        setColor(KEY_WAVE_COLOR_WAVE_FILL, color);
    }

    public void setWaveFillColor(int color) {
        storage.putInt(KEY_WAVE_COLOR_WAVE_FILL, color);
    }

    public void setWaveBackgroundColor(@Nullable Integer color) {
        setColor(KEY_WAVE_COLOR_BACKGROUND, color);
    }

    public void setWaveBackgroundColor(int color) {
        storage.putInt(KEY_WAVE_COLOR_BACKGROUND, color);
    }

    public void setWaveSelectionColor(@Nullable Integer color) {
        setColor(KEY_WAVE_COLOR_SELECTION, color);
    }

    public void setWaveSelectionColor(int color) {
        storage.putInt(KEY_WAVE_COLOR_SELECTION, color);
    }

    public void setWaveCursorColor(@Nullable Integer color) {
        setColor(KEY_WAVE_COLOR_CURSOR, color);
    }

    public void setWaveCursorColor(int color) {
        storage.putInt(KEY_WAVE_COLOR_CURSOR, color);
    }

    public void setWaveCursorWaveColor(@Nullable Integer color) {
        setColor(KEY_WAVE_COLOR_CURSOR_WAVE, color);
    }

    public void setWaveCursorWaveColor(int color) {
        storage.putInt(KEY_WAVE_COLOR_CURSOR_WAVE, color);
    }

    private void setColor(String preferenceName, @Nullable Integer color) {
        if (color == null) {
            storage.remove(preferenceName);
        } else {
            storage.putInt(preferenceName, color);
        }
    }

    @Override
    public void copyTo(SettingsOptions options) {
        WaveColorOptions with = (WaveColorOptions) options;
        with.setUseDefaultColors(isUseDefaultColors());
        with.setWaveBackgroundColor(getWaveBackgroundColor());
        with.setWaveColor(getWaveColor());
        with.setWaveCursorColor(getWaveCursorColor());
        with.setWaveCursorWaveColor(getWaveCursorWaveColor());
        with.setWaveFillColor(getWaveFillColor());
        with.setWaveSelectionColor(getWaveSelectionColor());
    }
}
