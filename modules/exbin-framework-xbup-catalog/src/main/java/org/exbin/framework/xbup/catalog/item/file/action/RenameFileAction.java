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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.AbstractAction;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.gui.DefaultControlPanel;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.framework.xbup.catalog.item.gui.RenamePanel;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Rename file action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class RenameFileAction extends AbstractAction {

    public static final String ACTION_ID = "renameCatalogItemFileAction";

    private XBApplication application;
    private XBACatalog catalog;

    private Component parentComponent;
    private XBCXFile currentFile;
    private String resultName;

    public RenameFileAction() {
    }

    public void setup(XBApplication application) {
        this.application = application;
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

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultName = null;
        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
        RenamePanel renamePanel = new RenamePanel();
        renamePanel.setNameText(currentFile.getFilename());

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowUtils.DialogWrapper dialog = frameModule.createDialog(renamePanel, controlPanel);
        //        WindowUtils.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
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
        dialog.showCentered(parentComponent);
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
