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
package org.exbin.xbup.jaguif.catalog.item.file.action;

import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Add new file action.
 */
@NullMarked
public class AddFileAction extends AbstractAction {

    public static final String ACTION_ID = "addCatalogItemFile";
    
    protected final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AddFileAction.class);
    protected @Nullable XBACatalog catalog;

    protected @Nullable DialogParentComponent parentComponent;
    protected @Nullable XBCNode currentNode;
    protected @Nullable String resultName;
    protected @Nullable byte[] resultData;

    public AddFileAction() {
    }

    public void init() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(DialogParentComponent.class, (instance) -> {
                    parentComponent = instance;
                });
                registrar.registerChangeListener(XBACatalog.class, (instance) -> {
                    catalog = instance;
                });
            }
        });
    }

    @Nullable
    public XBCNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(XBCNode currentNode) {
        this.currentNode = currentNode;
    }

    @Nullable
    public String getResultName() {
        return resultName;
    }

    @Nullable
    public byte[] getResultData() {
        return resultData;
    }

    public void setParentComponent(DialogParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultName = null;
        resultData = null;
        if (currentNode != null) {
            JFileChooser importFileChooser = new JFileChooser();
            if (importFileChooser.showOpenDialog(parentComponent.getComponent()) == JFileChooser.APPROVE_OPTION) {
                FileInputStream fileStream;
                try {
                    fileStream = new FileInputStream(importFileChooser.getSelectedFile().getAbsolutePath());
                    byte[] fileData = new byte[(int) (new File(importFileChooser.getSelectedFile().getAbsolutePath())).length()];
                    DataInputStream dataIs = new DataInputStream(fileStream);
                    dataIs.readFully(fileData);
                    resultName = importFileChooser.getSelectedFile().getName();
                    resultData = fileData;
//                    filesModel.addItem(resultName, fileData);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                    Logger.getLogger(AddFileAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
