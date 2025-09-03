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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.action.api.ComponentActivationListener;
import org.exbin.framework.action.api.ActionContextService;
import org.exbin.framework.file.api.EditableFileHandler;
import org.exbin.framework.file.api.FileType;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.action.api.ComponentActivationProvider;
import org.exbin.framework.action.api.DefaultActionContextService;
import org.exbin.framework.action.api.DialogParentComponent;
import org.exbin.framework.editor.api.EditorFileHandler;
import org.exbin.framework.operation.undo.api.UndoRedoFileHandler;
import org.exbin.framework.operation.undo.api.UndoRedoState;
import org.exbin.xbup.operation.undo.UndoRedoControl;

/**
 * XBUP file handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupFileHandler implements EditableFileHandler, EditorFileHandler, ComponentActivationProvider, UndoRedoFileHandler {

    private XbupDocumentView documentViewer = new XbupDocumentView();
    private final XbupTreeDocument treeDocument = new XbupTreeDocument();
    private String title;
    private int id = 0;
    private DefaultActionContextService actionContextService = new DefaultActionContextService();

    private UndoRedoControl undoRedo = null;
    private ComponentActivationListener componentActivationListener;
    private DialogParentComponent dialogParentComponent;
//    private ClipboardActionsHandler activeHandler;

    private URI fileUri = null;
    private FileType fileType = null;

    public XbupFileHandler() {
        init();
    }

    public XbupFileHandler(int id) {
        this();
        this.id = id;
    }

    private void init() {
        documentViewer.setTreeDocument(treeDocument);
        actionContextService.updated(XbupTreeDocument.class, treeDocument);
    }

    public void registerUndoHandler() {
        UndoRedo undoHandler = treeDocument.getUndoRedo();
        documentViewer.setUndoHandler(undoHandler);
        undoRedo = new UndoRedoControl() {
            @Override
            public boolean canUndo() {
                return undoHandler.canUndo();
            }

            @Override
            public boolean canRedo() {
                return undoHandler.canRedo();
            }

            @Override
            public void performUndo() {
                try {
                    undoHandler.performUndo();
                    notifyUndoChanged();
                } catch (Exception ex) {
                    Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void performRedo() {
                try {
                    undoHandler.performRedo();
                    notifyUndoChanged();
                } catch (Exception ex) {
                    Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        notifyUndoChanged();
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
    public boolean canSave() {
        return isEditable();
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
    public void clearFile() {
        treeDocument.newFile();
        documentViewer.setAddressText("");
        notifyFileChanged();
//        updateItem();
    }

    @Nonnull
    @Override
    public String getTitle() {
        if (fileUri != null) {
            String path = fileUri.getPath();
            int lastSegment = path.lastIndexOf("/");
            String fileName = lastSegment < 0 ? path : path.substring(lastSegment + 1);
            return fileName == null ? "" : fileName;
        }

        return title == null ? "" : title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
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
            treeDocument.getUndoRedo().clear();
        } catch (IOException ex) {
            Logger.getLogger(XbupFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public XBACatalog getCatalog() {
        return treeDocument.getCatalog();
    }
    
    public XBPluginRepository getPluginRepository() {
        return treeDocument.getPluginRepository();
    }

    public void setCatalog(XBACatalog catalog) {
        treeDocument.setCatalog(catalog);
        documentViewer.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        treeDocument.setPluginRepository(pluginRepository);
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
//            DocumentTab currentTab = getCurrentViewer();
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
    @Override
    public Optional<UndoRedoState> getUndoRedo() {
        return Optional.of(new UndoRedoState() {
            @Override
            public boolean canUndo() {
                return treeDocument.getUndoRedo().canUndo();
            }

            @Override
            public boolean canRedo() {
                return treeDocument.getUndoRedo().canRedo();
            }
        });
    }

    public void notifyItemModified(XBTTreeNode block) {
        notifyFileChanged();
        treeDocument.notifyItemModified(block);
        // TODO updateItemStatus();
    }

    public boolean isEditable() {
        return documentViewer.isEditable();
    }

    @Override
    public void componentActivated(ComponentActivationListener componentActivationListener) {
        this.componentActivationListener = componentActivationListener;
        componentActivationListener.updated(org.exbin.framework.operation.undo.api.UndoRedoState.class, getUndoRedo().orElse(null));
//        componentActivationListener.updated(ClipboardActionsHandler.class, this);
    }

    @Override
    public void componentDeactivated(ComponentActivationListener componentActivationListener) {
        this.componentActivationListener = null;
        componentActivationListener.updated(org.exbin.framework.operation.undo.api.UndoRedoState.class, null);
//        componentActivationListener.updated(ClipboardActionsHandler.class, null);
    }

    private void notifyUndoChanged() {
        if (undoRedo != null) {
            actionContextService.updated(UndoRedoControl.class, undoRedo);
        }
    }

    @Nonnull
    @Override
    public ActionContextService getActionContextService() {
        return actionContextService;
    }

    @Override
    public void setDialogParentComponent(DialogParentComponent dialogParentComponent) {
        this.dialogParentComponent = dialogParentComponent;
    }
}
