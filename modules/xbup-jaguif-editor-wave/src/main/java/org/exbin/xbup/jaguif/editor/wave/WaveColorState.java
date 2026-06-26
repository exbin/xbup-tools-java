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
package org.exbin.xbup.jaguif.editor.wave;

import java.awt.Color;
import org.jspecify.annotations.NullMarked;
import org.exbin.jaguif.context.api.StateUpdateType;

/**
 * Wave color state.
 */
@NullMarked
public interface WaveColorState {

    /**
     * Returns current colors used in application frame.
     *
     * @return array of 4 colors.
     */
    Color[] getCurrentWaveColors();

    /**
     * Returns default colors used in application frame.
     *
     * @return array of 4 colors.
     */
    Color[] getDefaultWaveColors();

    /**
     * Sets current colors used in application frame.
     *
     * @param colors
     */
    void setCurrentWaveColors(Color[] colors);

    public enum UpdateType implements StateUpdateType {
        WAVE_COLOR_STATE
    }
}
