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
package org.exbin.framework.viewer.xbup.action;

import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeManager;
import org.exbin.framework.viewer.xbup.gui.XBDocTreeTransferHandler;
import org.exbin.framework.viewer.xbup.viewer.XbupFileHandler;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.utils.ClipboardUtils;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Copy item to clipboard action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CopyItemAction extends AbstractAction {

    public static final String ACTION_ID = "copyItemAction";

    private FileHandler fileHandler;

    public CopyItemAction() {
    }

    public void setup() {
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ActionContextChangeManager manager) {
                manager.registerUpdateListener(FileHandler.class, (instance) -> {
                    fileHandler = instance;
                    setEnabled(fileHandler instanceof XbupFileHandler);
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBTBlock block = ((XbupFileHandler) fileHandler).getSelectedItem().get();
        if (!(block instanceof XBTTreeNode)) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        XBTTreeNode node = (XBTTreeNode) block;
        Clipboard clipboard = ClipboardUtils.getClipboard();
        XBDocTreeTransferHandler.XBTSelection selection = new XBDocTreeTransferHandler.XBTSelection(node);
        clipboard.setContents(selection, selection);
    }
}
