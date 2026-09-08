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
package org.exbin.xbup.jaguif.editor;

import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.exbin.jaguif.action.api.clipboard.TextClipboardOperationController;
import org.exbin.jaguif.operation.undo.api.UndoRedoController;
import org.exbin.jaguif.search.api.ContextSearch;
import org.exbin.jaguif.utils.ClipboardUtils;
import org.exbin.xbup.jaguif.component.XbupComponent;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.gui.XBDocTreeTransferHandler;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.operation.undo.XBTLinearUndo;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.jspecify.annotations.Nullable;

/**
 * Block editor component.
 */
@NullMarked
public class XbupEditorComponent extends XbupComponent implements TextClipboardOperationController, UndoRedoController{
    
    protected XbupEditor xbupEditor;
    protected UndoRedo undoRedo;
    protected @Nullable ContextSearch searchController;

    public XbupEditorComponent() {
    }

    @Override
    public Component getComponent() {
        return xbupEditor.getComponent();
    }

    @Override
    public void setXbupTree(XbupTree xbupTree) {
        super.setXbupTree(xbupTree);
        undoRedo = new XBTLinearUndo((XBTTreeDocument) xbupTree.getDocument());
    }

    public Optional<ContextSearch> getSearchController() {
        return Optional.ofNullable(searchController);
    }

    public UndoRedo getUndoRedo() {
        return undoRedo;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public void performCut() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void performCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void performPaste() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasDataToCopy() {
        // TODO
        return false;
    }

    @Override
    public boolean isValidForPaste() {
        Clipboard clipboard = ClipboardUtils.getClipboard();
        return clipboard.isDataFlavorAvailable(XBDocTreeTransferHandler.XB_DATA_FLAVOR);
    }

    @Override
    public void performSelectAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasSelection() {
        // TODO
        return false;
    }

    @Override
    public boolean canSelectAll() {
        return true;
    }

    @Override
    public void performDelete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canDelete() {
        // TODO
        return false;
    }

    @Override
    public void performRedo() {
        undoRedo.performUndo();
    }

    @Override
    public void performUndo() {
        undoRedo.performRedo();
    }

    @Override
    public boolean canUndo() {
        return undoRedo.canUndo();
    }

    @Override
    public boolean canRedo() {
        return undoRedo.canRedo();
    }
}
