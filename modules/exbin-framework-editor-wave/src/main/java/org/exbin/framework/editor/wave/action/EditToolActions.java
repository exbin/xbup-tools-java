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
import org.exbin.framework.context.api.ContextChangeRegistration;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.ActionType;
import org.exbin.framework.document.api.ContextDocument;
import org.exbin.framework.editor.wave.gui.AudioPanel;
import org.exbin.framework.editor.wave.AudioDocument;
import org.exbin.xbup.audio.swing.XBWavePanel;

/**
 * Edit tool actions.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditToolActions {

    public static final String TOOLS_SELECTION_RADIO_GROUP_ID = "toolsSelectionRadioGroup";

    private ResourceBundle resourceBundle;

    public EditToolActions() {
    }

    public void setup(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public SelectionToolAction createSelectionToolAction() {
        SelectionToolAction selectionToolAction = new SelectionToolAction();
        selectionToolAction.setup(resourceBundle);
        return selectionToolAction;
    }

    @Nonnull
    public PencilToolAction createPencilToolAction() {
        PencilToolAction pencilToolAction = new PencilToolAction();
        pencilToolAction.setup(resourceBundle);
        return pencilToolAction;
    }

    @ParametersAreNonnullByDefault
    public static class SelectionToolAction extends AbstractAction {

        public static final String ACTION_ID = "selectionToolAction";

        private AudioDocument audioDocument;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_RADIO_GROUP, TOOLS_SELECTION_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                        if (audioDocument != null) {
                            putValue(Action.SELECTED_KEY, audioDocument.getComponent().getDrawMode() == XBWavePanel.DrawMode.LINE_MODE);
                        }
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = audioDocument.getComponent();
            audioPanel.setToolMode(XBWavePanel.ToolMode.SELECTION);
        }
    }

    @ParametersAreNonnullByDefault
    public static class PencilToolAction extends AbstractAction {

        public static final String ACTION_ID = "pencilToolAction";

        private AudioDocument audioDocument;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_TYPE, ActionType.RADIO);
            putValue(ActionConsts.ACTION_RADIO_GROUP, TOOLS_SELECTION_RADIO_GROUP_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                        if (audioDocument != null) {
                            putValue(Action.SELECTED_KEY, audioDocument.getComponent().getDrawMode() == XBWavePanel.DrawMode.LINE_MODE);
                        }
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = audioDocument.getComponent();
            audioPanel.setToolMode(XBWavePanel.ToolMode.PENCIL);
        }
    }
}
