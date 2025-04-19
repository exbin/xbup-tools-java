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
package org.exbin.framework.editor.wave.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeManager;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.editor.wave.gui.AudioPanel;
import org.exbin.framework.editor.wave.options.gui.WaveColorPanel;
import org.exbin.framework.editor.wave.AudioFileHandler;
import org.exbin.framework.editor.wave.EditorWaveModule;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.window.api.handler.DefaultControlHandler.ControlActionType;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.window.api.WindowHandler;

/**
 * Tools options action handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class WaveColorAction extends AbstractAction {

    public static final String ACTION_ID = "toolsSetColorAction";

    private FileHandler fileHandler;

    public WaveColorAction() {
    }

    public void setup(ResourceBundle resourceBundle) {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ActionContextChangeManager manager) {
                manager.registerUpdateListener(FileHandler.class, (instance) -> {
                    fileHandler = instance;
                    setEnabled(fileHandler instanceof AudioFileHandler);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AudioPanel audioPanel = ((AudioFileHandler) fileHandler).getComponent();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);

        EditorWaveModule editorWaveModule = App.getModule(EditorWaveModule.class);

        final WaveColorPanel waveColorPanel = new WaveColorPanel();
        waveColorPanel.setWaveColorService(editorWaveModule.getWaveColorService());
        waveColorPanel.setWaveColorsFromArray(audioPanel.getAudioPanelColors());
        DefaultControlPanel controlPanel = new DefaultControlPanel(waveColorPanel.getResourceBundle());
        final WindowHandler dialog = windowModule.createDialog(waveColorPanel, controlPanel);
        windowModule.addHeaderPanel(dialog.getWindow(), waveColorPanel.getClass(), waveColorPanel.getResourceBundle());
        windowModule.setWindowTitle(dialog, waveColorPanel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == ControlActionType.OK) {
                audioPanel.setAudioPanelColors(waveColorPanel.getWaveColorsAsArray());
            }

            dialog.close();
        });
        dialog.showCentered((Component) e.getSource());
        dialog.dispose();
    }
}
