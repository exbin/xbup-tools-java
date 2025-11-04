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
package org.exbin.framework.editor.xbup.action;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeRegistration;
import org.exbin.framework.editor.xbup.gui.XBDocTreeTransferHandler;
import org.exbin.framework.editor.xbup.document.XbupFileHandler;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.utils.ClipboardUtils;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.operation.command.XBTDocCommand;
import org.exbin.xbup.operation.basic.command.XBTAddBlockCommand;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Paste item from clipboard action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class PasteItemAction extends AbstractAction {

    public static final String ACTION_ID = "pasteItemAction";

    private FileHandler fileHandler;

    public PasteItemAction() {
    }

    public void setup() {
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ActionContextChangeRegistration registrar) {
                registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                    fileHandler = instance;
                    setEnabled(fileHandler instanceof XbupFileHandler);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Clipboard clipboard = ClipboardUtils.getClipboard();
        if (clipboard.isDataFlavorAvailable(XBDocTreeTransferHandler.XB_DATA_FLAVOR)) {
//            org.exbin.framework.operation.undo.api.UndoRedoState undoRedo = xbupFile.getUndoRedo();
            XBTTreeDocument mainDoc = ((XbupFileHandler) fileHandler).getDocument();
            try {
                ByteArrayOutputStream stream = (ByteArrayOutputStream) clipboard.getData(XBDocTreeTransferHandler.XB_DATA_FLAVOR);
                XBTBlock block = ((XbupFileHandler) fileHandler).getSelectedItem().get();
                if (!(block instanceof XBTTreeNode)) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                XBTTreeNode node = (XBTTreeNode) block;
                XBTTreeNode newNode = new XBTTreeNode(node);
                try {
                    newNode.fromStreamUB(new ByteArrayInputStream(stream.toByteArray()));
                    try {
                        long parentPosition = node == null ? -1 : node.getBlockIndex();
                        int childIndex = node == null ? 0 : node.getChildCount();
                        XBTDocCommand step = new XBTAddBlockCommand(mainDoc, parentPosition, childIndex, newNode);
                        throw new UnsupportedOperationException("Not supported yet.");
                        // undoRedo.execute(step);
                        // xbupFile.notifyFileChanged();
//                        updateItemStatus();
                    } catch (Exception ex) {
                        Logger.getLogger(PasteItemAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException | XBProcessingException ex) {
                    Logger.getLogger(PasteItemAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(PasteItemAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
