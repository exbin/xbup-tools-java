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
package org.exbin.framework.editor.xbup.viewer;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.bined.swing.CodeAreaCore;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ComponentActivationListener;
import org.exbin.framework.editor.xbup.gui.BlockPropertiesPanel;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.file.api.EditableFileHandler;
import org.exbin.framework.file.api.FileType;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.ClipboardActionsHandler;
import org.exbin.framework.utils.ClipboardActionsUpdateListener;
import org.exbin.framework.window.api.gui.CloseControlPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.window.api.WindowHandler;

/**
 * Viewer provider.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupSingleEditorProvider implements XbupEditorProvider, ClipboardActionsHandler {

    private XBACatalog catalog;
    private PropertyChangeListener propertyChangeListener = null;

    private ClipboardActionsHandler activeHandler;

    private XBPluginRepository pluginRepository;
    private final List<DocumentItemSelectionListener> itemSelectionListeners = new ArrayList<>();
    private ClipboardActionsUpdateListener clipboardActionsUpdateListener;
    private XbupFileHandler activeFile;
    private boolean devMode = false;
    @Nullable
    private File lastUsedDirectory;
    private ComponentActivationListener componentActivationListener;

    public XbupSingleEditorProvider() {
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        componentActivationListener = frameModule.getFrameHandler().getComponentActivationListener();
        activeFile = new XbupFileHandler();
        activeFile.addItemSelectionListener((block) -> {
            itemSelectionListeners.forEach(listener -> {
                listener.itemSelected(block);
            });
        });

        componentActivationListener.updated(EditorProvider.class, this);
        activeFileChanged();
    }

    private void activeFileChanged() {
        componentActivationListener.updated(FileHandler.class, activeFile);
        componentActivationListener.updated(CodeAreaCore.class, null);
    }

    @Nonnull
    @Override
    public JComponent getEditorComponent() {
        return activeFile.getComponent();
    }

    @Nonnull
    @Override
    public Optional<FileHandler> getActiveFile() {
        return Optional.of(activeFile);
    }

    @Nonnull
    @Override
    public Optional<File> getLastUsedDirectory() {
        return Optional.ofNullable(lastUsedDirectory);
    }

    @Override
    public void setLastUsedDirectory(@Nullable File directory) {
        lastUsedDirectory = directory;
    }

    @Override
    public void updateRecentFilesList(URI fileUri, FileType fileType) {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        fileModule.updateRecentFilesList(fileUri, fileType);
    }

    @Override
    public XBACatalog getCatalog() {
        return catalog;
    }

    @Override
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        activeFile.setCatalog(catalog);
    }

    @Override
    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    @Override
    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
        activeFile.setPluginRepository(pluginRepository);
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
        activeFile.setDevMode(devMode);
    }

    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeListener = propertyChangeListener;
        // activeFile.getComponent().setPropertyChangeListener(propertyChangeListener);
    }

    @Nonnull
    @Override
    public String getWindowTitle(String frameTitle) {
        XBTTreeDocument treeDocument = activeFile.getDocument();
        String fileName = treeDocument.getFileName();
        if (!"".equals(fileName)) {
            int pos;
            int newpos = 0;
            do {
                pos = newpos;
                newpos = fileName.indexOf(File.separatorChar, pos) + 1;
            } while (newpos > 0);
            return fileName.substring(pos) + " - " + frameTitle;
        }

        return frameTitle;
    }

    @Override
    public void performCut() {
        activeHandler.performCut();
    }

    @Override
    public void performCopy() {
        activeHandler.performCopy();
    }

    @Override
    public void performPaste() {
        activeHandler.performPaste();
    }

    @Override
    public void performDelete() {
        activeHandler.performDelete();
    }

    @Override
    public void performSelectAll() {
        activeHandler.performSelectAll();
    }

    @Override
    public boolean isSelection() {
        return activeHandler.isSelection();
    }

    @Override
    public boolean isEditable() {
        return activeHandler.isEditable();
    }

    @Override
    public boolean canSelectAll() {
        return activeHandler.canSelectAll();
    }

    @Override
    public boolean canPaste() {
        return activeHandler.canPaste();
    }

    @Override
    public boolean canDelete() {
        return activeHandler.canDelete();
    }

    @Override
    public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
        clipboardActionsUpdateListener = updateListener;
//        activeFile.setUpdateListener(updateListener);
    }

    public void actionItemProperties() {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        BlockPropertiesPanel panel = new BlockPropertiesPanel();
        panel.setCatalog(catalog);
        panel.setBlock(activeFile.getSelectedItem().orElse(null));
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        controlPanel.setHandler(() -> {
            dialog.close();
            dialog.dispose();
        });
        dialog.showCentered(null);
    }

    @Override
    public void addItemSelectionListener(DocumentItemSelectionListener listener) {
        itemSelectionListeners.add(listener);
    }

    @Override
    public void removeItemSelectionListener(DocumentItemSelectionListener listener) {
        itemSelectionListeners.remove(listener);
    }

    @Override
    public void newFile() {
        activeFile.clearFile();
    }

    @Override
    public void openFile(URI fileUri, FileType fileType) {
        activeFile.loadFromFile(fileUri, fileType);
    }

    @Override
    public void openFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadFromFile(String fileName) throws URISyntaxException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadFromFile(URI fileUri, FileType fileType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public void saveFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveAsFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean releaseFile(FileHandler fileHandler) {
        if (((EditableFileHandler) fileHandler).isModified()) {
            FileModuleApi fileModule = App.getModule(FileModuleApi.class);
            return fileModule.getFileActions().showAskForSaveDialog(fileHandler, null, this);
        }

        return true;
    }

    @Override
    public boolean releaseAllFiles() {
        return releaseFile(activeFile);
    }
}
