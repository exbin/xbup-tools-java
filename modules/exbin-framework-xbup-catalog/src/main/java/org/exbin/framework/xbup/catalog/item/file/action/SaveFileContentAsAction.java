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
package org.exbin.framework.xbup.catalog.item.file.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemPanel;
import org.exbin.xbup.catalog.convert.XBCatalogYaml;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Save catalog file content action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SaveFileContentAsAction extends AbstractAction {

    public static final String ACTION_ID = "saveCatalogItemFileContentAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(SaveFileContentAsAction.class);

    private XBACatalog catalog;

    private final XBCatalogYaml catalogYaml = new XBCatalogYaml();

    private Component parentComponent;
    private XBCXFile currentFile;

    public SaveFileContentAsAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
    }

    @Nullable
    public XBCXFile getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(@Nullable XBCXFile currentFile) {
        this.currentFile = currentFile;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (currentFile != null) {
            JFileChooser saveFileChooser = new JFileChooser(currentFile.getFilename());
            saveFileChooser.setAcceptAllFileFilterUsed(true);
            if (saveFileChooser.showSaveDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fileStream;
                try {
                    fileStream = new FileOutputStream(saveFileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        fileStream.write(currentFile.getContent());
                    } finally {
                        fileStream.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CatalogItemPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(CatalogItemPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void setCatalog(XBACatalog catalog) {
        catalogYaml.setCatalog(catalog);
    }
}
