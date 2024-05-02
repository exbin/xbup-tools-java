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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.operation.BinaryDataCommand;
import org.exbin.bined.operation.BinaryDataOperationException;
import org.exbin.bined.operation.undo.BinaryDataUndoHandler;
import org.exbin.bined.operation.undo.BinaryDataUndoUpdateListener;
import org.exbin.xbup.operation.Command;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.operation.undo.XBUndoUpdateListener;

/**
 * Undo handler wrapper.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataWrapperUndoHandler implements BinaryDataUndoHandler {

    private final XBUndoHandler undoHandler;
    private final List<BinaryDataUndoUpdateListener> listeners = new ArrayList<>();

    public BinaryDataWrapperUndoHandler(XBUndoHandler undoHandler) {
        this.undoHandler = undoHandler;
        undoHandler.addUndoUpdateListener(new XBUndoUpdateListener() {
            @Override
            public void undoCommandPositionChanged() {
                for (BinaryDataUndoUpdateListener listener : listeners) {
                    listener.undoCommandPositionChanged();
                }
            }

            @Override
            public void undoCommandAdded(Command command) {
                if (command instanceof BinaryDataCommandWrapper) {
                    for (BinaryDataUndoUpdateListener listener : listeners) {
                        listener.undoCommandAdded(((BinaryDataCommandWrapper) command).binaryDataCommand);
                    }
                }
            }
        });
    }

    @Override
    public boolean canRedo() {
        return undoHandler.canRedo();
    }

    @Override
    public boolean canUndo() {
        return undoHandler.canUndo();
    }

    @Override
    public void clear() {
        undoHandler.clear();
    }

    @Override
    public void doSync() throws BinaryDataOperationException {
        try {
            undoHandler.doSync();
        } catch (Exception ex) {
            throw new BinaryDataOperationException(ex);
        }
    }

    @Override
    public void execute(BinaryDataCommand command) throws BinaryDataOperationException {
        try {
            undoHandler.execute(new BinaryDataCommandWrapper(command));
        } catch (Exception ex) {
            throw new BinaryDataOperationException(ex);
        }
    }

    @Override
    public void addCommand(BinaryDataCommand command) {
        undoHandler.addCommand(new BinaryDataCommandWrapper(command));
    }

    @Nonnull
    @Override
    public List<BinaryDataCommand> getCommandList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getCommandPosition() {
        return undoHandler.getCommandPosition();
    }

    @Override
    public long getMaximumUndo() {
        return undoHandler.getMaximumUndo();
    }

    @Override
    public long getSyncPosition() {
        return undoHandler.getSyncPosition();
    }

    @Override
    public long getUndoMaximumSize() {
        return undoHandler.getUndoMaximumSize();
    }

    @Override
    public long getUsedSize() {
        return undoHandler.getUsedSize();
    }

    @Override
    public void performUndo() {
        undoHandler.performUndo();
    }

    @Override
    public void performUndo(int count) {
        undoHandler.performUndo(count);
    }

    @Override
    public void performRedo() {
        undoHandler.performRedo();
    }

    @Override
    public void performRedo(int count) {
        undoHandler.performRedo(count);
    }

    @Override
    public void setCommandPosition(long targetPosition) {
        undoHandler.setCommandPosition(targetPosition);
    }

    @Override
    public void setSyncPosition(long commandPosition) {
        undoHandler.setSyncPosition(commandPosition);
    }

    @Override
    public void setSyncPosition() {
        undoHandler.setSyncPosition();
    }

    @Override
    public void addUndoUpdateListener(BinaryDataUndoUpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeUndoUpdateListener(BinaryDataUndoUpdateListener listener) {
        listeners.remove(listener);
    }
    
    @ParametersAreNonnullByDefault
    private static final class BinaryDataCommandWrapper implements Command {
        
        private final BinaryDataCommand binaryDataCommand;

        public BinaryDataCommandWrapper(BinaryDataCommand binaryDataCommand) {
            this.binaryDataCommand = binaryDataCommand;
        }

        @Override
        public String getCaption() {
            return binaryDataCommand.getCaption();
        }

        @Override
        public void execute() throws Exception {
            binaryDataCommand.execute();
        }

        @Override
        public void redo() throws Exception {
            binaryDataCommand.redo();
        }

        @Override
        public void undo() throws Exception {
            binaryDataCommand.undo();
        }

        @Override
        public boolean canUndo() {
            return binaryDataCommand.canUndo();
        }

        @Override
        public void dispose() throws Exception {
            binaryDataCommand.dispose();
        }
    }
}
