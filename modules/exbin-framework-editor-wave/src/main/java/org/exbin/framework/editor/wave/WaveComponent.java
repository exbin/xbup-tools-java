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
package org.exbin.framework.editor.wave;

import java.awt.Color;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.editor.wave.gui.AudioPanel;

/**
 * Wave component.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class WaveComponent implements WaveColorState { // ContextDocument, 

    protected AudioPanel audioPanel;

    public WaveComponent() {
    }

    @Nonnull
    @Override
    public Color[] getCurrentWaveColors() {
        return audioPanel.getAudioPanelColors();
    }

    @Nonnull
    @Override
    public Color[] getDefaultWaveColors() {
        return audioPanel.getDefaultColors();
    }

    @Override
    public void setCurrentWaveColors(Color[] colors) {
        audioPanel.setAudioPanelColors(colors);
    }
}
