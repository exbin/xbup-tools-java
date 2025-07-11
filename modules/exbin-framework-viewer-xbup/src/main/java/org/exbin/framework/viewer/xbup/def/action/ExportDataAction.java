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
package org.exbin.framework.viewer.xbup.def.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Export data action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ExportDataAction extends AbstractAction {

    public static final String ACTION_ID = "exportDataAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ExportDataAction.class);

    private Component parentComponent;
    private File resultFile;

    public ExportDataAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Nullable
    public File getResultFile() {
        return resultFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resultFile = null;
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setAcceptAllFileFilterUsed(true);
        if (saveFileChooser.showSaveDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
            resultFile = saveFileChooser.getSelectedFile();
            // binaryDataFile.saveToFile(file.toURI(), null);
        }
    }
}
