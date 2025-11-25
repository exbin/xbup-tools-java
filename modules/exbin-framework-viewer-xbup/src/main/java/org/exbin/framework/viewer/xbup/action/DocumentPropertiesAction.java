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
package org.exbin.framework.viewer.xbup.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.context.api.ContextChangeRegistration;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.document.api.ContextDocument;
import org.exbin.framework.viewer.xbup.gui.DocumentPropertiesPanel;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.viewer.xbup.document.XbupTreeDocument;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.CloseControlPanel;

/**
 * Document properties action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class DocumentPropertiesAction extends AbstractAction {

    public static final String ACTION_ID = "propertiesAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(DocumentPropertiesAction.class);

    protected XbupTreeDocument xbupDocument;

    public DocumentPropertiesAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        setEnabled(false);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerUpdateListener(ContextDocument.class, (instance) -> {
                    xbupDocument = instance instanceof XbupTreeDocument ? (XbupTreeDocument) instance : null;
                    setEnabled(xbupDocument != null);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        DocumentPropertiesPanel propertiesPanel = new DocumentPropertiesPanel();
        propertiesPanel.setDocument(xbupDocument.getDocument());
        propertiesPanel.setDocumentUri(xbupDocument.getFileUri().orElse(null));
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(propertiesPanel, controlPanel);
        windowModule.addHeaderPanel(dialog.getWindow(), propertiesPanel.getClass(), propertiesPanel.getResourceBundle());
        windowModule.setWindowTitle(dialog, propertiesPanel.getResourceBundle());
        controlPanel.setController(() -> {
            dialog.close();
            dialog.dispose();
        });
        dialog.showCentered((Component) e.getSource());
    }
}
