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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.context.api.ContextChangeRegistration;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.document.api.ContextDocument;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.editor.picture.gui.ImageResizePanel;
import org.exbin.framework.editor.picture.ImageDocument;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.controller.DefaultControlController;
import org.exbin.framework.window.api.controller.DefaultControlController.ControlActionType;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.window.api.WindowHandler;

/**
 * Picture operation actions.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class PictureOperationActions {

    private ResourceBundle resourceBundle;

    public PictureOperationActions() {
    }

    public void setup(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public ImageResizeAction createRevertAction() {
        ImageResizeAction imageResizeAction = new ImageResizeAction();
        imageResizeAction.setup(resourceBundle);
        return imageResizeAction;
    }

    @ParametersAreNonnullByDefault
    public static class ImageResizeAction extends AbstractAction {

        public static final String ACTION_ID = "imageResizeAction";

        private ImageDocument imageDocument;

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_DIALOG_MODE, true);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerUpdateListener(ContextDocument.class, (instance) -> {
                        imageDocument = instance instanceof ImageDocument ? (ImageDocument) instance : null;
                        setEnabled(imageDocument != null);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImagePanel imagePanel = imageDocument.getComponent();
            final ImageResizePanel imageResizePanel = new ImageResizePanel();
            imageResizePanel.setResolution(imagePanel.getImageSize());
            DefaultControlPanel controlPanel = new DefaultControlPanel(imageResizePanel.getResourceBundle());
            WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
            final WindowHandler dialog = windowModule.createDialog(imageResizePanel, controlPanel);
            windowModule.addHeaderPanel(dialog.getWindow(), imageResizePanel.getClass(), imageResizePanel.getResourceBundle());
            windowModule.setWindowTitle(dialog, imageResizePanel.getResourceBundle());
            controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
                if (actionType == ControlActionType.OK) {
                    Point point = imageResizePanel.getResolution();
                    int width = (int) (point.getX());
                    int height = (int) (point.getY());
                    imagePanel.performResize(width, height);
                }

                dialog.close();
            });
            dialog.showCentered((Component) e.getSource());
            dialog.dispose();
        }
    }
}
