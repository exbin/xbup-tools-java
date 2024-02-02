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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Replace catalog file content action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ReplaceFileContentAction extends AbstractAction {

    public static final String ACTION_ID = "replaceCatalogItemFileContentAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ReplaceFileContentAction.class);

    private XBACatalog catalog;

    private Component parentComponent;
    private XBCXFile currentFile;
    private String resultName;
    private byte[] resultData;

    public ReplaceFileContentAction() {
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

    public void setCurrentFile(XBCXFile currentFile) {
        this.currentFile = currentFile;
    }

    @Nullable
    public String getResultName() {
        return resultName;
    }

    @Nullable
    public byte[] getResultData() {
        return resultData;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultName = null;
        resultData = null;
        if (currentFile != null) {
            JFileChooser importFileChooser = new JFileChooser();
            if (importFileChooser.showOpenDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
                FileInputStream fileStream;
                try {
                    fileStream = new FileInputStream(importFileChooser.getSelectedFile().getAbsolutePath());
                    byte[] fileData = new byte[(int) (new File(importFileChooser.getSelectedFile().getAbsolutePath())).length()];
                    DataInputStream dataIs = new DataInputStream(fileStream);
                    dataIs.readFully(fileData);
                    resultName = importFileChooser.getSelectedFile().getName();
                    resultData = fileData;
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                    Logger.getLogger(ReplaceFileContentAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
