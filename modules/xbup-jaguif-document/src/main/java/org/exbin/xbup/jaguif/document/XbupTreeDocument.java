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
package org.exbin.xbup.jaguif.document;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextActivable;
import org.exbin.jaguif.document.api.ComponentDocument;
import org.exbin.jaguif.document.api.DocumentSource;
import org.exbin.jaguif.document.api.EditableDocument;
import org.exbin.jaguif.document.api.EmptyDocumentSource;
import org.exbin.jaguif.document.api.StreamDocumentSource;
import org.exbin.jaguif.file.api.FileDocument;
import org.exbin.jaguif.file.api.FileDocumentSource;
import org.exbin.jaguif.file.api.FileType;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.jaguif.component.XbupEditableTree;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.editor.XbupEditor;
import org.exbin.xbup.operation.undo.XBTLinearUndo;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeDocument;

/**
 * XBUP tree document.
 */
@ParametersAreNonnullByDefault
public class XbupTreeDocument implements XbupDocument, ComponentDocument, FileDocument, EditableDocument, ContextActivable {

    protected XbupEditor xbupEditor;
    protected DocumentSource documentSource = null;
    protected final XbupTree xbupTree;
    protected ActiveContextManagement activeContextManagement;
    protected UndoRedo undoRedo;

    protected final Map<Long, String> captionCache = new HashMap<>();
    protected final Map<Long, ImageIcon> iconCache = new HashMap<>();

    public XbupTreeDocument(XbupTree xbupTree) {
        this.xbupTree = xbupTree;
        undoRedo = new XBTLinearUndo(new XBTTreeDocument()); // TODO xbupTree
    }

    @Nonnull
    public UndoRedo getUndoRedo() {
        return undoRedo;
    }

    @Nonnull
    @Override
    public Optional<URI> getFileUri() {
        if (!(documentSource instanceof FileDocumentSource)) {
            return Optional.empty();
        }
        return Optional.of(((FileDocumentSource) documentSource).getFile().toURI());
    }

    @Nonnull
    @Override
    public String getDocumentName() {
        if (documentSource instanceof FileDocumentSource) {
            return ((FileDocumentSource) documentSource).getFile().getName();
        }

        if (documentSource instanceof StreamDocumentSource) {
            return ((StreamDocumentSource) documentSource).getDocumentTitle();
        }

        if (documentSource instanceof EmptyDocumentSource) {
            return ((EmptyDocumentSource) documentSource).getDocumentTitle();
        }

        return "";
    }

    @Nonnull
    @Override
    public Component getComponent() {
        return xbupEditor.getComponent();
    }

    public void setXbupEditor(XbupEditor xbupEditor) {
        this.xbupEditor = xbupEditor;
    }

    @Override
    public void notifyActivated(ActiveContextManagement contextManagement) {
        activeContextManagement = contextManagement;
        // TODO
    }

    @Override
    public void notifyDeactivated(ActiveContextManagement contextManagement) {
        activeContextManagement = null;
        // TODO
    }

    public void notifyModified() {
        // TODO Replace with content update messaging
        xbupEditor.setXbupTree(xbupTree);
    }

    /*
    @Override
    public void notifyChange(OperationEvent event) {
        Operation operation = event.getOperation();
        // TODO Consolidate
//        processSpec();
//        notifyFileChanged();
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
    } */
//    @Override
//    public boolean canPaste() {
//        Clipboard clipboard = ClipboardUtils.getClipboard();
//        return clipboard.isDataFlavorAvailable(XBDocTreeTransferHandler.XB_DATA_FLAVOR);
//    }
    @Nullable
    public XBTBlock getRoot() {
        return xbupTree.getRootBlock().orElse(null);
    }

    @Nonnull
    public Optional<XBTBlock> getRootBlock() {
        return xbupTree.getRootBlock();
    }

    public void loadFromResourcePath(Class<?> classInstance, String resourcePath) throws IOException {
        ((XbupEditableTree) xbupTree).fromStreamUB(classInstance.getResourceAsStream(resourcePath));
        // TODO xbupTree.processSpec();
        undoRedo.clear();
        notifyModified();
    }

    public void newFile() {
        ((XbupEditableTree) xbupTree).clear();
        undoRedo.clear();
    }

    @Override
    public boolean isModified() {
        return false; // TODO xbupTree.wasModified();
    }

    @Override
    public void clearFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public void saveTo(DocumentSource documentSource) {
        if (!(documentSource instanceof FileDocumentSource)) {
            throw new UnsupportedOperationException();
        }

        File file = ((FileDocumentSource) documentSource).getFile();
        try {
            saveToFile(file.toURI(), null);
        } catch (IOException ex) {
            Logger.getLogger(XbupTreeDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Nonnull
    @Override
    public Optional<DocumentSource> getDocumentSource() {
        return Optional.empty();
    }

    @Override
    public void loadFrom(DocumentSource documentSource) {
        if (documentSource instanceof EmptyDocumentSource) {
            return;
        }

        if (documentSource instanceof FileDocumentSource) {
            try {
                loadFromFile(((FileDocumentSource) documentSource).getFile().toURI(), null);
            } catch (IOException ex) {
                Logger.getLogger(XbupTreeDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadFromFile(URI fileUri, FileType fileType) throws FileNotFoundException, IOException {
        File file = new File(fileUri);
        FileInputStream fileStream = new FileInputStream(file);
        ((XbupEditableTree) xbupTree).fromStreamUB(fileStream);
        // TODO xbupTree.processSpec();
        undoRedo.clear();
    }

    public void saveToFile(URI fileUri, FileType fileType) throws IOException {
        File file = new File(fileUri);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        // TODO xbupTree.toStreamUB(fileOutputStream);
        // TODO xbupTree.setModified(false);
        undoRedo.setSyncPosition();
    }

    @Nonnull
    @Override
    public XbupTree getXbupTree() {
        return xbupTree;
    }

    /**
     * Returns caption for given block type.
     *
     * Use cache if available.
     *
     * @param blockDecl block declaration
     * @return caption
     */
    @Nullable
    public String getBlockCaption(XBBlockDecl blockDecl) {
        return xbupTree.getBlockCaption(blockDecl);
    }

    /**
     * Returns icon for given block type.
     *
     * Use cache if available.
     *
     * @param blockDecl block declaration
     * @return icon
     */
    @Nullable
    public ImageIcon getBlockIcon(XBBlockDecl blockDecl) {
        return xbupTree.getBlockIcon(blockDecl);
    }
}
