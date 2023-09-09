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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.file.api.FileType;
import org.exbin.framework.utils.ClipboardActionsUpdateListener;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.file.api.FileHandler;

/**
 * XBUP file handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupFileHandler implements FileHandler {

    private XbupDocumentViewer documentViewer = new XbupDocumentViewer();
    private final XbupTreeDocument treeDocument;
    private int id = 0;

    private ClipboardActionsUpdateListener clipboardActionsUpdateListener;
//    private ClipboardActionsHandler activeHandler;

    private URI fileUri = null;
    private FileType fileType = null;

    public XbupFileHandler() {
        treeDocument = new XbupTreeDocument();
        documentViewer.setTreeDocument(treeDocument);
    }

    public XbupFileHandler(int id) {
        this();
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return documentViewer.getComponent();
    }

    @Override
    public void loadFromFile(URI fileUri, FileType fileType) {
        try {
            treeDocument.loadFromFile(fileUri, fileType);
            documentViewer.setAddressText(fileUri.toASCIIString());
            notifyFileChanged();
            this.fileUri = fileUri;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedOperationException("Not supported yet.");
            // TODO JOptionPane.showMessageDialog(WindowUtils.getFrame(this), ex.getMessage(), "Parsing Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void saveFile() {
        saveToFile(fileUri, fileType);
    }

    @Override
    public void saveToFile(URI fileUri, FileType fileType) {
        try {
            treeDocument.saveToFile(fileUri, fileType);
            documentViewer.setAddressText(fileUri.toASCIIString());
            this.fileUri = fileUri;
        } catch (IOException ex) {
            Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Nonnull
    @Override
    public Optional<URI> getFileUri() {
        return Optional.ofNullable(fileUri);
    }

    @Override
    public void newFile() {
        treeDocument.newFile();
        documentViewer.setAddressText("");
        notifyFileChanged();
//        updateItem();
    }

    @Nonnull
    @Override
    public String getFileName() {
        if (fileUri != null) {
            String path = fileUri.getPath();
            int lastSegment = path.lastIndexOf("/");
            return lastSegment < 0 ? path : path.substring(lastSegment + 1);
        }

        return "";
    }

    @Nonnull
    @Override
    public Optional<FileType> getFileType() {
        return Optional.ofNullable(fileType);
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean isModified() {
        return treeDocument.wasModified();
    }

    @Nonnull
    public XBTTreeDocument getDocument() {
        return treeDocument.getDocument();
    }

    public void loadFromResourcePath(Class<?> classInstance, String resourcePath) {
        try {
            treeDocument.loadFromResourcePath(classInstance, resourcePath);
            documentViewer.setAddressText("classpath:" + resourcePath);
            notifyFileChanged();
            treeDocument.getUndoHandler().clear();
        } catch (IOException ex) {
            Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCatalog(XBACatalog catalog) {
        treeDocument.setCatalog(catalog);
        documentViewer.setCatalog(catalog);
    }

    public void setApplication(XBApplication application) {
        treeDocument.setApplication(application);
        documentViewer.setApplication(application);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        documentViewer.setPluginRepository(pluginRepository);
    }

    public void setDevMode(boolean devMode) {
        documentViewer.setDevMode(devMode);
    }

    public void notifyFileChanged() {
        documentViewer.notifyFileChanged();
    }

    public void postWindowOpened() {
        documentViewer.postWindowOpened();
    }

    @Nonnull
    public Optional<XBTBlock> getSelectedItem() {
        return documentViewer.getSelectedItem();
    }

//    public void setSelectedTab(ViewerTab selectedTab) {
//        if (this.selectedTab != selectedTab) {
//            this.selectedTab = selectedTab;
//            notifySelectedItem();
//            notifyItemSelectionChanged();
//            DocumentTab currentTab = getCurrentTab();
//            if (activeHandler != currentTab && activeHandler != treeDocument) {
//                activeHandler = treeDocument;
//                notifyActiveChanged();
//            }
//
//            mainFrame.getEditFindAction().setEnabled(mode != PanelMode.TREE);
//            mainFrame.getEditFindAgainAction().setEnabled(mode == PanelMode.TEXT);
//            mainFrame.getEditGotoAction().setEnabled(mode == PanelMode.TEXT);
//            mainFrame.getEditReplaceAction().setEnabled(mode == PanelMode.TEXT);
//            mainFrame.getItemAddAction().setEnabled(false);
//            mainFrame.getItemModifyAction().setEnabled(false);
//            showPanel();
//            updateItem();
//            updateActionStatus(null);
//            if (clipboardActionsUpdateListener != null) {
//                clipboardActionsUpdateListener.stateChanged();
//            }
//        }
//    }
//    public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
//        clipboardActionsUpdateListener = updateListener;
//        treeDocument.setUpdateListener(updateListener);
//    }
//    private void notifyActiveChanged() {
//        if (clipboardActionsUpdateListener != null) {
//            clipboardActionsUpdateListener.stateChanged();
//        }
//    }

    public void addItemSelectionListener(DocumentItemSelectionListener listener) {
        documentViewer.addItemSelectionListener(listener);
    }

    public void removeItemSelectionListener(DocumentItemSelectionListener listener) {
        documentViewer.removeItemSelectionListener(listener);
    }

    @Nonnull
    public XBUndoHandler getUndoHandler() {
        return treeDocument.getUndoHandler();
    }

    public void notifyItemModified(XBTTreeNode block) {
        notifyFileChanged();
        treeDocument.notifyItemModified(block);
        // TODO updateItemStatus();
    }

    public boolean isSaveSupported() {
        return true;
    }

    public boolean isEditable() {
        return documentViewer.isEditable();
    }
}
