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
package org.exbin.framework.editor.xbup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.operation.BinaryDataCommand;
import org.exbin.bined.operation.undo.BinaryDataUndoRedo;
import org.exbin.bined.operation.undo.BinaryDataUndoRedoChangeListener;
import org.exbin.xbup.operation.Command;
import org.exbin.xbup.operation.undo.UndoRedo;

/**
 * Undo handler wrapper.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataWrapperUndoHandler implements BinaryDataUndoRedo {

    private final UndoRedo undoRedo;
    private final List<BinaryDataUndoRedoChangeListener> listeners = new ArrayList<>();

    public BinaryDataWrapperUndoHandler(UndoRedo undoRedo) {
        this.undoRedo = undoRedo;
        undoRedo.addChangeListener(() -> {
            for (BinaryDataUndoRedoChangeListener listener : listeners) {
                listener.undoChanged();
            }
        });
    }

    @Override
    public boolean canRedo() {
        return undoRedo.canRedo();
    }

    @Override
    public boolean canUndo() {
        return undoRedo.canUndo();
    }

    @Override
    public void clear() {
        undoRedo.clear();
    }

    @Override
    public void performSync() {
        undoRedo.performSync();
    }

    @Override
    public void execute(BinaryDataCommand command) {
        undoRedo.execute(new BinaryDataCommandWrapper(command));
    }

    public void addCommand(BinaryDataCommand command) {
        // TODO undoRedo.addCommand(new BinaryDataCommandWrapper(command));
    }

    @Nonnull
    @Override
    public List<BinaryDataCommand> getCommandList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    @Override
    public Optional<BinaryDataCommand> getTopUndoCommand() {
        Optional<Command> topUndoCommand = undoRedo.getTopUndoCommand();
        if (topUndoCommand.isPresent()) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        return Optional.empty();
    }

    @Override
    public long getCommandsCount() {
        return undoRedo.getCommandsCount();
    }

    @Override
    public boolean isModified() {
        return undoRedo.isModified();
    }

    @Override
    public long getCommandPosition() {
        return undoRedo.getCommandPosition();
    }

    @Override
    public long getSyncPosition() {
        return undoRedo.getSyncPosition();
    }

    @Override
    public void performUndo() {
        undoRedo.performUndo();
    }

    @Override
    public void performUndo(int count) {
        undoRedo.performUndo(count);
    }

    @Override
    public void performRedo() {
        undoRedo.performRedo();
    }

    @Override
    public void performRedo(int count) {
        undoRedo.performRedo(count);
    }

    public void setCommandPosition(long targetPosition) {
        // TODO undoRedo.setCommandPosition(targetPosition);
    }

    @Override
    public void setSyncPosition(long commandPosition) {
        undoRedo.setSyncPosition(commandPosition);
    }

    @Override
    public void setSyncPosition() {
        undoRedo.setSyncPosition();
    }

    @Override
    public void addChangeListener(BinaryDataUndoRedoChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeChangeListener(BinaryDataUndoRedoChangeListener listener) {
        listeners.remove(listener);
    }

    @ParametersAreNonnullByDefault
    private static final class BinaryDataCommandWrapper implements Command {

        private final BinaryDataCommand binaryDataCommand;

        public BinaryDataCommandWrapper(BinaryDataCommand binaryDataCommand) {
            this.binaryDataCommand = binaryDataCommand;
        }

        @Nonnull
        @Override
        public String getName() {
            return binaryDataCommand.getName();
        }

        @Override
        public void execute() {
            binaryDataCommand.execute();
        }

        @Override
        public void dispose() {
            binaryDataCommand.dispose();
        }
    }
}
