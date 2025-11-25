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
package org.exbin.framework.editor.picture;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import javax.swing.undo.UndoManager;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.file.api.FileType;
import org.exbin.framework.action.api.DialogParentComponent;
import org.exbin.framework.document.api.ComponentDocument;
import org.exbin.framework.document.api.Document;
import org.exbin.framework.document.api.EditableDocument;
import org.exbin.framework.file.api.FileDocument;
import org.exbin.framework.operation.undo.api.UndoRedoController;

/**
 * Image document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ImageDocument implements Document, FileDocument, EditableDocument, ComponentDocument {

    private static final String DEFAULT_PICTURE_FILE_EXT = "PNG";

    protected ImagePanel imagePanel = new ImagePanel();

    protected URI fileUri = null;
    private String ext = null;
    private String title;
    private FileType fileType = null;
    private UndoRedoController undoRedoController = null;
    private DialogParentComponent dialogParentComponent;

    public ImageDocument() {
        init();
    }

    private void init() {

    }

    public void registerUndoHandler() {
        UndoManager undoHandler = imagePanel.getUndo();
        undoRedoController = new UndoRedoController() {
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
                undoHandler.undo();
                notifyUndoChanged();
            }

            @Override
            public void performRedo() {
                undoHandler.redo();
                notifyUndoChanged();
            }
        };
        /* undoHandler.setUndoUpdateListener(() -> {
            notifyUndoChanged();
        }); */
        notifyUndoChanged();
    }

    @Nonnull
    @Override
    public ImagePanel getComponent() {
        return imagePanel;
    }

    public void loadFromFile(URI fileUri, @Nullable FileType fileType) {
        try {
            imagePanel.setImage(ImagePanel.toBufferedImage(Toolkit.getDefaultToolkit().getImage(fileUri.toURL())));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean canSave() {
        return true;
    }

    public void saveFile() {
        saveToFile(fileUri, fileType);
    }

    public void saveToFile(URI fileUri, @Nullable FileType fileType) {
        File file = new File(fileUri);
        try {
            if (fileType instanceof PictureFileType) {
                ext = ((PictureFileType) fileType).getExt();
            }

            ImageIO.write((RenderedImage) imagePanel.getImage(), ext == null ? DEFAULT_PICTURE_FILE_EXT : ext, file);
        } catch (IOException ex) {
            Logger.getLogger(ImageDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Nonnull
    @Override
    public Optional<URI> getFileUri() {
        return Optional.ofNullable(fileUri);
    }

    @Nonnull
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
    public Optional<FileType> getFileType() {
        return Optional.ofNullable(fileType);
    }

    public void clearFile() {
        imagePanel.newImage();
        imagePanel.setModified(false);
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean isModified() {
        return imagePanel.isModified();
    }

    private void notifyUndoChanged() {
        if (undoRedoController != null) {
            // TODO actionContextService.updated(UndoRedoState.class, undoRedoController);
        }
    }

    public void setDialogParentComponent(DialogParentComponent dialogParentComponent) {
        this.dialogParentComponent = dialogParentComponent;
    }
}
