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
package org.exbin.jaguif.editor.wave.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.api.ActionType;
import org.exbin.jaguif.document.api.ContextDocument;
import org.exbin.jaguif.editor.wave.AudioDocument;
import org.exbin.jaguif.editor.wave.gui.AudioPanel;
import org.exbin.xbup.audio.swing.XBWavePanel;

/**
 * Drawing mode control actions.
 */
@ParametersAreNonnullByDefault
public class DrawingControlActions {

    public static final String DRAWING_RADIO_GROUP_ID = "drawingRadioGroup";

    private ResourceBundle resourceBundle;

    public DrawingControlActions() {
    }

    public void init(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public DotsModeAction createDotsModeAction() {
        DotsModeAction dotsModeAction = new DotsModeAction();
        dotsModeAction.init(resourceBundle);
        return dotsModeAction;
    }

    @Nonnull
    public LineModeAction createLineModeAction() {
        LineModeAction lineModeAction = new LineModeAction();
        lineModeAction.init(resourceBundle);
        return lineModeAction;
    }

    @Nonnull
    public IntegralModeAction createIntegralModeAction() {
        IntegralModeAction integralModeAction = new IntegralModeAction();
        integralModeAction.init(resourceBundle);
        return integralModeAction;
    }

    @ParametersAreNonnullByDefault
    public static class DotsModeAction extends AbstractAction {

        public static final String ACTION_ID = "dotsMode";

        private AudioDocument audioDocument;

        public DotsModeAction() {
        }

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                        putValue(Action.SELECTED_KEY, audioDocument.getComponent().getDrawMode() == XBWavePanel.DrawMode.LINE_MODE);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = audioDocument.getComponent();
            audioPanel.setDrawMode(XBWavePanel.DrawMode.DOTS_MODE);
        }
    }

    @ParametersAreNonnullByDefault
    public static class LineModeAction extends AbstractAction {

        public static final String ACTION_ID = "lineMode";

        private AudioDocument audioDocument;

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                        putValue(Action.SELECTED_KEY, audioDocument.getComponent().getDrawMode() == XBWavePanel.DrawMode.LINE_MODE);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = audioDocument.getComponent();
            audioPanel.setDrawMode(XBWavePanel.DrawMode.LINE_MODE);
        }
    }

    @ParametersAreNonnullByDefault
    public static class IntegralModeAction extends AbstractAction {

        public static final String ACTION_ID = "integralMode";

        private AudioDocument audioDocument;

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                        putValue(Action.SELECTED_KEY, audioDocument.getComponent().getDrawMode() == XBWavePanel.DrawMode.LINE_MODE);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = audioDocument.getComponent();
            audioPanel.setDrawMode(XBWavePanel.DrawMode.INTEGRAL_MODE);
        }
    }
}
