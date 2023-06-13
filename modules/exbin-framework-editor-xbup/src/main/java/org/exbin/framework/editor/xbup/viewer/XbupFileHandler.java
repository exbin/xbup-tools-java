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

import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.gui.XBDocTreeTransferHandler;
import org.exbin.framework.file.api.FileType;
import org.exbin.framework.utils.ClipboardActionsHandler;
import org.exbin.framework.utils.ClipboardActionsUpdateListener;
import org.exbin.framework.utils.ClipboardUtils;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.operation.Operation;
import org.exbin.xbup.operation.OperationEvent;
import org.exbin.xbup.operation.OperationListener;
import org.exbin.xbup.operation.XBTDocOperation;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.xbup.operation.undo.XBTLinearUndo;

/**
 * XBUP file handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupFileHandler implements FileHandler {

    private XbupDocumentViewer documentViewer = new XbupDocumentViewer();
    private final XBTTreeDocument treeDocument;
    private XBUndoHandler undoHandler;
    private int id = 0;
    private XBApplication application;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;

    private ClipboardActionsUpdateListener clipboardActionsUpdateListener;
//    private ClipboardActionsHandler activeHandler;

    private URI fileUri = null;
    private FileType fileType = null;

    public XbupFileHandler() {
        treeDocument = new XBTTreeDocument();
        undoHandler = new XBTLinearUndo(treeDocument);
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
        File file = new File(fileUri);
        try (FileInputStream fileStream = new FileInputStream(file)) {
            getDoc().fromStreamUB(fileStream);
            getDoc().processSpec();
            documentViewer.setAddressText(fileUri.toASCIIString());
            notifyFileChanged();
            undoHandler.clear();
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
    public void saveToFile(URI fileUri, FileType fileType) {
        File file = new File(fileUri);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            getDoc().toStreamUB(fileOutputStream);
            documentViewer.setAddressText(fileUri.toASCIIString());
            undoHandler.setSyncPoint();
            getDoc().setModified(false);
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
        undoHandler.clear();
        getDoc().clear();
        documentViewer.setAddressText("");
        notifyFileChanged();
//        updateItem();
    }

    @Nonnull
    @Override
    public Optional<String> getFileName() {
        if (fileUri != null) {
            String path = fileUri.getPath();
            int lastSegment = path.lastIndexOf("/");
            return Optional.of(lastSegment < 0 ? path : path.substring(lastSegment + 1));
        }

        return Optional.empty();
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
        return getDoc().wasModified();
    }

    public void setUndoHandler(XBUndoHandler undoHandler) {
        this.undoHandler = undoHandler;
    }

    public XBTTreeDocument getDoc() {
        return treeDocument;
    }

    public void loadFromResourcePath(Class<?> classInstance, String resourcePath) {
        try {
            treeDocument.fromStreamUB(classInstance.getResourceAsStream(resourcePath));
            treeDocument.processSpec();
            documentViewer.setAddressText("classpath:" + resourcePath);
            notifyFileChanged();
            undoHandler.clear();
        } catch (IOException ex) {
            Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        treeDocument.setCatalog(catalog);
        treeDocument.processSpec();
        documentViewer.setCatalog(catalog);
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        documentViewer.setApplication(application);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
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
        return Optional.empty(); // Optional.ofNullable(selectedItem);
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

    public XBUndoHandler getUndoHandler() {
        return undoHandler;
    }

    public void itemWasModified(XBTTreeNode newNode) {
        notifyFileChanged();
        treeDocument.setModified(true);
        treeDocument.processSpec();
        // TODO updateItemStatus();
    }

    public boolean isSaveSupported() {
        return true;
    }

    public boolean isEditable() {
        return documentViewer.isEditable();
    }
/*
    @ParametersAreNonnullByDefault
    private class TreeDocument extends XBTTreeDocument implements OperationListener, ClipboardActionsHandler {

        public TreeDocument() {
            super((XBCatalog) null);
        }

        public TreeDocument(@Nullable XBCatalog catalog) {
            super(catalog);
        }

        @Override
        public void notifyChange(OperationEvent event) {
            Operation operation = event.getOperation();
            // TODO Consolidate
            processSpec();
            notifyFileChanged();
            // getDoc().setModified(true);
//            updateItem();
//            updateActionStatus(null);
//            if (clipboardActionsUpdateListener != null) {
//                clipboardActionsUpdateListener.stateChanged();
//            }

            if (operation instanceof XBTDocOperation) {
                // setSelectedTab(ViewerTab.VIEW);
            } else {
                // TODO
            }
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
            // documentPanel.performSelectAll();
        }

        @Override
        public boolean isSelection() {
            return false; // documentPanel.hasSelection();
        }

        @Override
        public boolean isEditable() {
            return false; // documentPanel.hasSelection();
        }

        @Override
        public boolean canSelectAll() {
            return true;
        }

        @Override
        public boolean canPaste() {
            Clipboard clipboard = ClipboardUtils.getClipboard();
            return clipboard.isDataFlavorAvailable(XBDocTreeTransferHandler.XB_DATA_FLAVOR);
        }

        @Override
        public boolean canDelete() {
            return false; // documentPanel.hasSelection();
        }

        @Override
        public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
//            documentPanel.addUpdateListener((e) -> {
//                updateListener.stateChanged();
//            });
        }
    }
*/
}
