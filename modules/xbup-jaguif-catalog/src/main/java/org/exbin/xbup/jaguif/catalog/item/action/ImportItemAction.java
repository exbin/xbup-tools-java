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
package org.exbin.xbup.jaguif.catalog.item.action;

import java.awt.event.ActionEvent;
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
import org.exbin.xbup.jaguif.catalog.YamlFileType;
import org.exbin.xbup.catalog.convert.XBCatalogYaml;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Import catalog item action.
 */
@NullMarked
public class ImportItemAction extends AbstractAction {

    public static final String ACTION_ID = "importItem";

    protected final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ImportItemAction.class);

    protected @Nullable XBACatalog catalog;

    protected final XBCatalogYaml catalogYaml = new XBCatalogYaml();

    protected @Nullable DialogParentComponent parentComponent;
    protected XBCItem currentItem;

    public ImportItemAction() {
    }

    public void init() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
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
    public XBCItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(@Nullable XBCItem currentItem) {
        this.currentItem = currentItem;
    }

    public void setParentComponent(DialogParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if ((currentItem != null) && (currentItem instanceof XBCNode)) {
            JFileChooser importFileChooser = new JFileChooser();
            importFileChooser.addChoosableFileFilter(new YamlFileType());
            importFileChooser.setAcceptAllFileFilterUsed(true);
            if (importFileChooser.showOpenDialog(parentComponent.getComponent()) == JFileChooser.APPROVE_OPTION) {
                FileInputStream fileStream;
                try {
                    fileStream = new FileInputStream(importFileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        catalogYaml.importCatalogItem(fileStream, (XBENode) currentItem);
                    } finally {
                        fileStream.close();
                    }
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                    Logger.getLogger(ImportItemAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void setCatalog(XBACatalog catalog) {
        catalogYaml.setCatalog(catalog);
    }
}
