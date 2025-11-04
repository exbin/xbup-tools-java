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
package org.exbin.framework.editor.picture.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeRegistration;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.editor.picture.ImageFileHandler;
import org.exbin.framework.file.api.FileHandler;

/**
 * Zoom mode control actions.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ZoomControlActions {

    public static final String ZOOM_RADIO_GROUP_ID = "zoomRadioGroup";

    private ResourceBundle resourceBundle;

    public ZoomControlActions() {
    }

    public void setup(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

    }

    @Nonnull
    public NormalZoomAction createNormalZoomAction() {
        NormalZoomAction normalZoomAction = new NormalZoomAction();
        normalZoomAction.setup(resourceBundle);
        return normalZoomAction;
    }

    @Nonnull
    public ZoomUpAction createZoomUpAction() {
        ZoomUpAction zoomUpAction = new ZoomUpAction();
        zoomUpAction.setup(resourceBundle);
        return zoomUpAction;
    }

    @Nonnull
    public ZoomDownAction createZoomDownAction() {
        ZoomDownAction zoomDownAction = new ZoomDownAction();
        zoomDownAction.setup(resourceBundle);
        return zoomDownAction;
    }

    @ParametersAreNonnullByDefault
    public static class NormalZoomAction extends AbstractAction {

        public static final String ACTION_ID = "normalZoomAction";

        private FileHandler fileHandler;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                        fileHandler = instance;
                        setEnabled(fileHandler instanceof ImageFileHandler);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImagePanel imagePanel = ((ImageFileHandler) fileHandler).getComponent();
            imagePanel.setScale(1);
        }
    }

    @ParametersAreNonnullByDefault
    public static class ZoomUpAction extends AbstractAction {

        public static final String ACTION_ID = "zoomUpAction";

        private FileHandler fileHandler;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                        fileHandler = instance;
                        setEnabled(fileHandler instanceof ImageFileHandler);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImagePanel imagePanel = ((ImageFileHandler) fileHandler).getComponent();
            imagePanel.setScale(imagePanel.getScale() / 2);
        }
    }

    @ParametersAreNonnullByDefault
    public static class ZoomDownAction extends AbstractAction {

        public static final String ACTION_ID = "zoomDownAction";

        private FileHandler fileHandler;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                        fileHandler = instance;
                        setEnabled(fileHandler instanceof ImageFileHandler);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImagePanel imagePanel = ((ImageFileHandler) fileHandler).getComponent();
            imagePanel.setScale(imagePanel.getScale() * 2);
        }
    }
}
