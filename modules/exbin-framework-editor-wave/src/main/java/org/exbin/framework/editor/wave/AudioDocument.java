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
package org.exbin.framework.editor.wave;

import java.io.File;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.editor.wave.gui.AudioPanel;
import org.exbin.framework.file.api.FileType;
import org.exbin.xbup.audio.wave.XBWave;
import org.exbin.framework.document.api.ComponentDocument;
import org.exbin.framework.document.api.Document;
import org.exbin.framework.document.api.DocumentSource;
import org.exbin.framework.document.api.EditableDocument;
import org.exbin.framework.file.api.FileDocument;
import org.exbin.framework.file.api.FileDocumentSource;
import org.exbin.framework.operation.undo.api.UndoRedoController;
import org.exbin.xbup.operation.undo.XBTLinearUndo;
import org.exbin.xbup.operation.undo.UndoRedo;

/**
 * Audio document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AudioDocument implements Document, FileDocument, EditableDocument, ComponentDocument {

    protected AudioPanel audioPanel = new AudioPanel();

    protected URI fileUri = null;
    private FileType fileType = null;
    private String title;
    private javax.sound.sampled.AudioFileFormat.Type audioFormatType = null;
    private UndoRedoController undoRedoController = null;

    public AudioDocument() {
        init();
    }

    private void init() {
    }

    public void registerUndoHandler() {
        UndoRedo undoRedo = new XBTLinearUndo(null);
        undoRedoController = new UndoRedoController() {
            @Override
            public boolean canUndo() {
                return undoRedo.canUndo();
            }

            @Override
            public boolean canRedo() {
                return undoRedo.canRedo();
            }

            @Override
            public void performUndo() {
                try {
                    undoRedo.performUndo();
                    notifyUndoChanged();
                } catch (Exception ex) {
                    Logger.getLogger(AudioDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void performRedo() {
                try {
                    undoRedo.performRedo();
                    notifyUndoChanged();
                } catch (Exception ex) {
                    Logger.getLogger(AudioDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        audioPanel.setUndoRedo(undoRedo);
        /* undoHandler.setUndoUpdateListener(() -> {
            notifyUndoChanged();
        }); */
        notifyUndoChanged();
    }

    @Nonnull
    @Override
    public AudioPanel getComponent() {
        return audioPanel;
    }

    @Nonnull
    @Override
    public Optional<DocumentSource> getDocumentSource() {
        return Optional.empty();
    }

    @Override
    public void loadFrom(DocumentSource documentSource) {
        if (!(documentSource instanceof FileDocumentSource)) {
            throw new UnsupportedOperationException();
        }
        
        File file = ((FileDocumentSource) documentSource).getFile();
        XBWave wave = new XBWave();
        wave.loadFromFile(file);
        audioPanel.setWave(wave);
        notifyUndoChanged();
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
        if (getBuildInFileType() == null) {
            audioPanel.getWave().saveToFile(file);
        } else {
            audioPanel.getWave().saveToFile(file, getBuildInFileType());
        }
        audioPanel.notifyFileSaved();
        notifyUndoChanged();
    }

    @Override
    public void clearFile() {
        audioPanel.newWave();
        notifyUndoChanged();
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

    @Nullable
    public javax.sound.sampled.AudioFileFormat.Type getBuildInFileType() {
        return audioFormatType;
    }

    public void setFileType(javax.sound.sampled.AudioFileFormat.Type fileType) {
        this.audioFormatType = fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean isModified() {
        return audioPanel.isModified();
    }

    protected void notifyUndoChanged() {
        if (undoRedoController != null) {
            // TODO actionContextService.updated(UndoRedoState.class, undoRedoController);
        }
    }
}
