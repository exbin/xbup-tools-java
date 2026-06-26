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
package org.exbin.xbup.jaguif.component.action;

import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import org.jspecify.annotations.NullMarked;
import javax.swing.AbstractAction;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.context.api.ContextComponent;
import org.exbin.jaguif.utils.ClipboardUtils;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.block.XbupBlockComponent;
import org.exbin.xbup.jaguif.component.gui.XBDocTreeTransferHandler;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Copy item to clipboard action.
 */
@NullMarked
public class CopyItemAction extends AbstractAction {

    public static final String ACTION_ID = "copyItem";

    private XbupBlockComponent xbupComponent;

    public CopyItemAction() {
    }

    public void init() {
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(ContextComponent.class, (instance) -> {
                    xbupComponent = instance instanceof XbupBlockComponent ? (XbupBlockComponent) instance : null;
                    setEnabled(xbupComponent != null);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBTBlock block = xbupComponent.getBlock().orElse(null);
        if (!(block instanceof XBTTreeNode)) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        XBTTreeNode node = (XBTTreeNode) block;
        Clipboard clipboard = ClipboardUtils.getClipboard();
        XBDocTreeTransferHandler.XBTSelection selection = new XBDocTreeTransferHandler.XBTSelection(node);
        clipboard.setContents(selection, selection);
    }
}
