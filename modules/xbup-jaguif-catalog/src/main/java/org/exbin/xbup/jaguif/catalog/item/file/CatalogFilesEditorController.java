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
package org.exbin.xbup.jaguif.catalog.item.file;

import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.jaguif.catalog.item.file.action.AddFileAction;
import org.exbin.xbup.jaguif.catalog.item.file.action.DeleteFileAction;
import org.exbin.xbup.jaguif.catalog.item.file.action.RenameFileAction;
import org.exbin.xbup.jaguif.catalog.item.file.gui.CatalogFilesTableModel;
import org.exbin.xbup.jaguif.catalog.item.file.gui.CatalogItemEditFilesPanel;
import org.jspecify.annotations.NullMarked;

/**
 * Catalog definitions editor controller.
 */
@NullMarked
public class CatalogFilesEditorController implements ContextEditItem {

    protected AddFileAction addFileAction = new AddFileAction();
    protected RenameFileAction renameFileAction = new RenameFileAction();
    protected DeleteFileAction deleteFileAction = new DeleteFileAction();

    protected final CatalogItemEditFilesPanel catalogEditorPanel;

    public CatalogFilesEditorController(CatalogItemEditFilesPanel catalogEditorPanel) {
        this.catalogEditorPanel = catalogEditorPanel;
        addFileAction.init();
        renameFileAction.init();
        deleteFileAction.init();
    }

    @Override
    public void performAddItem() {
        addFileAction.actionPerformed(null);
        String resultName = addFileAction.getResultName();
        if (resultName != null) {
            byte[] resultData = addFileAction.getResultData();
            CatalogFilesTableModel filesModel = catalogEditorPanel.getFilesModel();
            filesModel.addItem(resultName, resultData);
            // TODO catalogEditorPanel.reloadNodesTree();
        }
    }

    @Override
    public void performEditItem() {
        renameFileAction.setCurrentFile(catalogEditorPanel.getSelectedFile());
        renameFileAction.actionPerformed(null);
    }

    @Override
    public void performDeleteItem() {
        int selectedIndex = catalogEditorPanel.getSelectedIndex();
        // deleteFileAction.setCurrentIndex(selectedIndex);
        deleteFileAction.actionPerformed(null);
        CatalogFilesTableModel filesModel = catalogEditorPanel.getFilesModel();
        filesModel.removeItem(selectedIndex);
    }

    @Override
    public boolean canAddItem() {
        return true;
    }

    @Override
    public boolean canEditItem() {
        XBCXFile file = catalogEditorPanel.getSelectedFile();
        return file != null;
    }

    @Override
    public boolean canDeleteItem() {
        return false;
//                XBCNode node = catalogEditorPanel.getSelectedTreeItem();
//                return node != null && node.getParent().isPresent();
    }

    public void registerMonitoring(ContextMonitoringRegistration monitoringRegistrar) {
        monitoringRegistrar.registerContextMonitoring(addFileAction);
        monitoringRegistrar.registerContextMonitoring(renameFileAction);
        monitoringRegistrar.registerContextMonitoring(deleteFileAction);
    }
}
