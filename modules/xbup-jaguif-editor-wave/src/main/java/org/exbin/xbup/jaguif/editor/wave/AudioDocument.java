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
package org.exbin.xbup.jaguif.editor.wave;

import java.io.File;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.exbin.xbup.jaguif.editor.wave.gui.AudioPanel;
import org.exbin.jaguif.file.api.FileType;
import org.exbin.xbup.audio.wave.XBWave;
import org.exbin.jaguif.document.api.ComponentDocument;
import org.exbin.jaguif.document.api.DocumentSource;
import org.exbin.jaguif.document.api.EditableDocument;
import org.exbin.jaguif.document.api.NamedDocument;
import org.exbin.jaguif.file.api.FileDocument;
import org.exbin.jaguif.file.api.FileDocumentSource;
import org.exbin.jaguif.operation.undo.api.UndoRedoController;
import org.exbin.xbup.operation.undo.XBTLinearUndo;
import org.exbin.xbup.operation.undo.UndoRedo;

/**
 * Audio document.
 */
@NullMarked
public class AudioDocument implements NamedDocument, FileDocument, EditableDocument, ComponentDocument {

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

    @Override
    public AudioPanel getComponent() {
        return audioPanel;
    }

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

    @Override
    public Optional<URI> getFileUri() {
        return Optional.ofNullable(fileUri);
    }

    @Override
    public String getDocumentName() {
        return getTitle();
    }

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

    public Optional<FileType> getFileType() {
        return Optional.ofNullable(fileType);
    }

    public javax.sound.sampled.AudioFileFormat.@Nullable Type getBuildInFileType() {
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
