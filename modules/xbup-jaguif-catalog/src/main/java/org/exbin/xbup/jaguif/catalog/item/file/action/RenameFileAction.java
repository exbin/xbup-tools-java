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
import java.util.ResourceBundle;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.jaguif.catalog.item.gui.RenamePanel;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.jaguif.window.api.controller.DefaultControlController;

/**
 * Rename file action.
 */
@NullMarked
public class RenameFileAction extends AbstractAction {

    public static final String ACTION_ID = "renameCatalogItemFile";

    protected final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(RenameFileAction.class);

    protected @Nullable XBACatalog catalog;

    protected @Nullable DialogParentComponent parentComponent;
    protected @Nullable XBCXFile currentFile;
    protected @Nullable String resultName;

    public RenameFileAction() {
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

    public void setParentComponent(DialogParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultName = null;
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        RenamePanel renamePanel = new RenamePanel();
        renamePanel.setNameText(currentFile.getFilename());

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(renamePanel, controlPanel);
        //        windowModule.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                String fileName = renamePanel.getNameText();
                ((XBEXFile) currentFile).setFilename(fileName);

                EntityManager em = ((XBECatalog) catalog).getEntityManager();
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.persist(currentFile);
                em.flush();
                transaction.commit();
                resultName = fileName;
//                filesModel.setFileName(selectedRow, fileName);
            }
            dialog.close();
        });
        windowModule.setWindowTitle(dialog, resourceBundle);
        dialog.showCentered(parentComponent.getComponent());
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
