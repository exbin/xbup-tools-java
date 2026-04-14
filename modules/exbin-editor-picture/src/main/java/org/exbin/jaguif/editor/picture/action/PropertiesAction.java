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
package org.exbin.jaguif.editor.picture.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.document.api.ContextDocument;
import org.exbin.jaguif.editor.picture.gui.PropertiesPanel;
import org.exbin.jaguif.editor.picture.ImageDocument;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.jaguif.window.api.gui.CloseControlPanel;
import org.exbin.jaguif.window.api.WindowHandler;

/**
 * Properties action.
 */
@ParametersAreNonnullByDefault
public class PropertiesAction extends AbstractAction {

    public static final String ACTION_ID = "properties";

    private ImageDocument imageDocument;

    public PropertiesAction() {
    }

    public void init(ResourceBundle resourceBundle) {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                    imageDocument = instance instanceof ImageDocument ? (ImageDocument) instance : null;
                    setEnabled(imageDocument != null);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);

        PropertiesPanel propertiesPanel = new PropertiesPanel();
        propertiesPanel.setDocument(imageDocument);
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(propertiesPanel, controlPanel);
        windowModule.addHeaderPanel(dialog.getWindow(), propertiesPanel.getClass(), propertiesPanel.getResourceBundle());
        windowModule.setWindowTitle(dialog, propertiesPanel.getResourceBundle());
        controlPanel.setController(dialog::close);
        dialog.showCentered((Component) e.getSource());
        dialog.dispose();
    }
}
