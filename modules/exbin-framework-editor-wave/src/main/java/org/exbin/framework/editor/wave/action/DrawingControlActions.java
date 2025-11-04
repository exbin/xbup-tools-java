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

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeRegistration;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.ActionType;
import org.exbin.framework.editor.wave.gui.AudioPanel;
import org.exbin.framework.editor.wave.AudioFileHandler;
import org.exbin.xbup.audio.swing.XBWavePanel;
import org.exbin.framework.file.api.FileHandler;

/**
 * Drawing mode control actions.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class DrawingControlActions {

    public static final String DRAWING_RADIO_GROUP_ID = "drawingRadioGroup";

    private ResourceBundle resourceBundle;

    public DrawingControlActions() {
    }

    public void setup(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public DotsModeAction createDotsModeAction() {
        DotsModeAction dotsModeAction = new DotsModeAction();
        dotsModeAction.setup(resourceBundle);
        return dotsModeAction;
    }

    @Nonnull
    public LineModeAction createLineModeAction() {
        LineModeAction lineModeAction = new LineModeAction();
        lineModeAction.setup(resourceBundle);
        return lineModeAction;
    }

    @Nonnull
    public IntegralModeAction createIntegralModeAction() {
        IntegralModeAction integralModeAction = new IntegralModeAction();
        integralModeAction.setup(resourceBundle);
        return integralModeAction;
    }

    @ParametersAreNonnullByDefault
    public static class DotsModeAction extends AbstractAction {

        public static final String ACTION_ID = "dotsModeAction";

        private FileHandler fileHandler;

        public DotsModeAction() {
        }

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                        fileHandler = instance;
                        setEnabled(fileHandler instanceof AudioFileHandler);
                        putValue(Action.SELECTED_KEY, ((AudioFileHandler) fileHandler).getComponent().getDrawMode() == XBWavePanel.DrawMode.DOTS_MODE);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = ((AudioFileHandler) fileHandler).getComponent();
            audioPanel.setDrawMode(XBWavePanel.DrawMode.DOTS_MODE);
        }
    }

    @ParametersAreNonnullByDefault
    public static class LineModeAction extends AbstractAction {

        public static final String ACTION_ID = "lineModeAction";

        private FileHandler fileHandler;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                        fileHandler = instance;
                        setEnabled(fileHandler instanceof AudioFileHandler);
                        putValue(Action.SELECTED_KEY, ((AudioFileHandler) fileHandler).getComponent().getDrawMode() == XBWavePanel.DrawMode.LINE_MODE);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = ((AudioFileHandler) fileHandler).getComponent();
            audioPanel.setDrawMode(XBWavePanel.DrawMode.LINE_MODE);
        }
    }

    @ParametersAreNonnullByDefault
    public static class IntegralModeAction extends AbstractAction {

        public static final String ACTION_ID = "integralModeAction";

        private FileHandler fileHandler;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                        fileHandler = instance;
                        setEnabled(fileHandler instanceof AudioFileHandler);
                        putValue(Action.SELECTED_KEY, ((AudioFileHandler) fileHandler).getComponent().getDrawMode() == XBWavePanel.DrawMode.INTEGRAL_MODE);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = ((AudioFileHandler) fileHandler).getComponent();
            audioPanel.setDrawMode(XBWavePanel.DrawMode.INTEGRAL_MODE);
        }
    }
}
