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
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.document.api.ContextDocument;
import org.exbin.jaguif.editor.wave.gui.AudioPanel;
import org.exbin.jaguif.editor.wave.AudioDocument;

/**
 * Zoom mode control actions.
 */
@ParametersAreNonnullByDefault
public class ZoomControlActions {

    public static final String ZOOM_RADIO_GROUP_ID = "zoomRadioGroup";

    private ResourceBundle resourceBundle;

    public ZoomControlActions() {
    }

    public void init(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public NormalZoomAction createNormalZoomAction() {
        NormalZoomAction normalZoomAction = new NormalZoomAction();
        normalZoomAction.init(resourceBundle);
        return normalZoomAction;
    }

    @Nonnull
    public ZoomUpAction createZoomUpAction() {
        ZoomUpAction zoomUpAction = new ZoomUpAction();
        zoomUpAction.init(resourceBundle);
        return zoomUpAction;
    }

    @Nonnull
    public ZoomDownAction createZoomDownAction() {
        ZoomDownAction zoomDownAction = new ZoomDownAction();
        zoomDownAction.init(resourceBundle);
        return zoomDownAction;
    }

    @ParametersAreNonnullByDefault
    public static class NormalZoomAction extends AbstractAction {

        public static final String ACTION_ID = "normalZoom";

        private AudioDocument audioDocument;

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = ((AudioDocument) audioDocument).getComponent();
            audioPanel.scaleAndSeek(1);
        }
    }

    @ParametersAreNonnullByDefault
    public static class ZoomUpAction extends AbstractAction {

        public static final String ACTION_ID = "zoomUp";

        private AudioDocument audioDocument;

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = ((AudioDocument) audioDocument).getComponent();
            audioPanel.scaleAndSeek(audioPanel.getScale() / 2);
        }
    }

    @ParametersAreNonnullByDefault
    public static class ZoomDownAction extends AbstractAction {

        public static final String ACTION_ID = "zoomDown";

        private AudioDocument audioDocument;

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        audioDocument = instance instanceof AudioDocument ? (AudioDocument) instance : null;
                        setEnabled(audioDocument != null);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AudioPanel audioPanel = audioDocument.getComponent();
            audioPanel.scaleAndSeek(audioPanel.getScale() * 2);
        }
    }
}
