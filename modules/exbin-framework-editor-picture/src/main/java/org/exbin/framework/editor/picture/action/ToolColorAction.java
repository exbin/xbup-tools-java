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
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeManager;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.editor.picture.gui.ToolColorPanel;
import org.exbin.framework.editor.picture.ImageFileHandler;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.window.api.handler.DefaultControlHandler.ControlActionType;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.window.api.WindowHandler;

/**
 * Tools color action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ToolColorAction extends AbstractAction {

    public static final String ACTION_ID = "toolsSetColorAction";

    private FileHandler fileHandler;

    public ToolColorAction() {
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
                    setEnabled(fileHandler instanceof ImageFileHandler);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel imagePanel = ((ImageFileHandler) fileHandler).getComponent();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);

        final ToolColorPanel toolColorPanel = new ToolColorPanel();
        toolColorPanel.setToolColor(imagePanel.getToolColor());
        toolColorPanel.setSelectionColor(imagePanel.getSelectionColor());
        DefaultControlPanel controlPanel = new DefaultControlPanel(toolColorPanel.getResourceBundle());
        final WindowHandler dialog = windowModule.createDialog(toolColorPanel, controlPanel);
        windowModule.addHeaderPanel(dialog.getWindow(), toolColorPanel.getClass(), toolColorPanel.getResourceBundle());
        windowModule.setWindowTitle(dialog, toolColorPanel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == ControlActionType.OK) {
                imagePanel.setToolColor(toolColorPanel.getToolColor());
                imagePanel.setSelectionColor(toolColorPanel.getSelectionColor());
            }

            dialog.close();
        });
        dialog.showCentered((Component) e.getSource());
        dialog.dispose();
    }
}
