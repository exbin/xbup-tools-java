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
package org.exbin.xbup.jaguif.editor.action;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.context.api.ContextComponent;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.block.XbupBlockComponent;
import org.exbin.xbup.operation.command.XBTDocCommand;
import org.exbin.xbup.operation.basic.command.XBTDeleteBlockCommand;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Delete item action.
 */
@ParametersAreNonnullByDefault
public class DeleteItemAction extends AbstractAction {

    public static final String ACTION_ID = "deleteItem";

    private XbupBlockComponent xbupDocument;

    public DeleteItemAction() {
    }

    public void init() {
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(ContextComponent.class, (instance) -> {
                    xbupDocument = instance instanceof XbupBlockComponent ? (XbupBlockComponent) instance : null;
                    setEnabled(xbupDocument != null);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBTBlock block = xbupDocument.getBlock().orElse(null);
        if (!(block instanceof XBTTreeNode)) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        XBTTreeNode node = (XBTTreeNode) block;
        XbupTree mainDoc = xbupDocument.getTreeDocument();
//        UndoRedoState undoRedo = xbupFile.getUndoRedo();

        XBTTreeNode parent = (XBTTreeNode) node.getParent();
        try {
            XBTDocCommand command = new XBTDeleteBlockCommand((XBTEditableDocument) mainDoc, node);
            throw new UnsupportedOperationException("Not supported yet.");
            // undoRedo.execute(command);
        } catch (Exception ex) {
            Logger.getLogger(DeleteItemAction.class.getName()).log(Level.SEVERE, null, ex);
        }

//        if (parent == null) {
//            mainDocModel.fireTreeChanged();
//        } else {
//            mainDocModel.fireTreeStructureChanged(parent);
//        }
        // TODO mainDoc.setModified(true);
    }
}
