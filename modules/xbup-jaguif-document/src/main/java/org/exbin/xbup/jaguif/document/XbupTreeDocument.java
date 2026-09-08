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
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.swing.ImageIcon;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.context.api.ContextStateManagement;
import org.exbin.jaguif.context.api.ContextActivable;
import org.exbin.jaguif.context.api.ContextComponent;
import org.exbin.jaguif.document.api.ComponentDocument;
import org.exbin.jaguif.document.api.DocumentSource;
import org.exbin.jaguif.document.api.EditableDocument;
import org.exbin.jaguif.document.api.EmptyDocumentSource;
import org.exbin.jaguif.document.api.StreamDocumentSource;
import org.exbin.jaguif.file.api.FileDocument;
import org.exbin.jaguif.file.api.FileDocumentSource;
import org.exbin.jaguif.file.api.FileType;
import org.exbin.jaguif.operation.undo.api.ContextUndoRedo;
import org.exbin.jaguif.search.api.ContextSearch;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.jaguif.component.XbupEditableTree;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.editor.XbupEditorComponent;

/**
 * XBUP tree document.
 */
@NullMarked
public class XbupTreeDocument implements XbupDocument, ComponentDocument, FileDocument, EditableDocument, ContextActivable {

    protected XbupEditorComponent xbupComponent;
    protected @Nullable DocumentSource documentSource = null;
    protected @Nullable ContextStateManagement stateManagement;

    protected final Map<Long, String> captionCache = new HashMap<>();
    protected final Map<Long, ImageIcon> iconCache = new HashMap<>();

    public XbupTreeDocument(XbupTree xbupTree) {
        xbupComponent = new XbupEditorComponent();
        xbupComponent.setXbupTree(xbupTree);
    }

    @Override
    public Optional<URI> getFileUri() {
        if (!(documentSource instanceof FileDocumentSource)) {
            return Optional.empty();
        }
        return Optional.of(((FileDocumentSource) documentSource).getFile().toURI());
    }

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

    @Override
    public Component getComponent() {
        return xbupComponent.getComponent();
    }

    @Override
    public void notifyActivated(ContextStateManagement stateManagement) {
        this.stateManagement = stateManagement;
        stateManagement.changeActiveState(ContextComponent.class, xbupComponent);
        stateManagement.changeActiveState(ContextUndoRedo.class, xbupComponent);
        stateManagement.changeActiveState(ContextSearch.class, xbupComponent.getSearchController().orElse(null));
        stateManagement.changeActiveState(DialogParentComponent.class, (DialogParentComponent) xbupComponent::getComponent);
    }

    @Override
    public void notifyDeactivated(ContextStateManagement stateManagement) {
        this.stateManagement = null;
        stateManagement.changeActiveState(ContextComponent.class, null);
        stateManagement.changeActiveState(ContextUndoRedo.class, null);
        stateManagement.changeActiveState(ContextSearch.class, null);
        stateManagement.changeActiveState(DialogParentComponent.class, (DialogParentComponent) xbupComponent::getComponent);
    }

    public void notifyModified() {
        // TODO Replace with content update messaging
        // xbupComponent.setXbupTree(xbupTree);
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

    @Nullable
    public XBTBlock getRoot() {
        return xbupComponent.getXbupTree().getRootBlock().orElse(null);
    }

    public Optional<XBTBlock> getRootBlock() {
        return xbupComponent.getXbupTree().getRootBlock();
    }

    public void loadFromResourcePath(Class<?> classInstance, String resourcePath) throws IOException {
        ((XbupEditableTree) xbupComponent.getXbupTree()).fromStreamUB(classInstance.getResourceAsStream(resourcePath));
        // TODO xbupTree.processSpec();
        xbupComponent.getUndoRedo().clear();
        notifyModified();
    }

    public void newFile() {
        ((XbupEditableTree) xbupComponent.getXbupTree()).clear();
        xbupComponent.getUndoRedo().clear();
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
        ((XbupEditableTree) xbupComponent.getXbupTree()).fromStreamUB(fileStream);
        // TODO xbupTree.processSpec();
        xbupComponent.getUndoRedo().clear();
    }

    public void saveToFile(URI fileUri, FileType fileType) throws IOException {
        File file = new File(fileUri);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        // TODO xbupTree.toStreamUB(fileOutputStream);
        // TODO xbupTree.setModified(false);
        xbupComponent.getUndoRedo().setSyncPosition();
    }

    @Override
    public XbupTree getXbupTree() {
        return xbupComponent.getXbupTree();
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
        return xbupComponent.getXbupTree().getBlockCaption(blockDecl);
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
        return xbupComponent.getXbupTree().getBlockIcon(blockDecl);
    }
}
