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

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.exbin.framework.App;
import org.exbin.framework.editor.xbup.BlockEditor;
import org.exbin.framework.editor.xbup.viewer.XbupEditorProvider;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.operation.XBTDocCommand;
import org.exbin.xbup.operation.basic.XBTModifyBlockOperation;
import org.exbin.xbup.operation.basic.XBTTailDataOperation;
import org.exbin.xbup.operation.basic.command.XBTChangeBlockCommand;
import org.exbin.xbup.operation.basic.command.XBTModifyBlockCommand;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Edit item action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditItemAction extends AbstractAction {

    public static final String ACTION_ID = "editItemAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(EditItemAction.class);

    private XbupEditorProvider editorProvider;

    public EditItemAction() {
    }

    public void setup(XbupEditorProvider editorProvider) {
        this.editorProvider = editorProvider;

        ActionUtils.setupAction(this, resourceBundle, ACTION_ID);
        putValue(ActionUtils.ACTION_DIALOG_MODE, true);
        setEnabled(false);
        editorProvider.addItemSelectionListener((@Nullable XBTBlock item) -> {
            setEnabled(item != null);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBACatalog catalog = editorProvider.getCatalog();
        XbupFileHandler xbupFile = (XbupFileHandler) editorProvider.getActiveFile().get();
        XBUndoHandler undoHandler = xbupFile.getUndoHandler();
        XBTTreeDocument mainDoc = xbupFile.getDocument();
        XBPluginRepository pluginRepository = editorProvider.getPluginRepository();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        XBTBlock block = xbupFile.getSelectedItem().get();
        if (!(block instanceof XBTTreeNode)) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        XBTTreeNode node = (XBTTreeNode) block;

        BlockEditor blockEditor = new BlockEditor();
        blockEditor.setCatalog(catalog);
        blockEditor.setPluginRepository(pluginRepository);
        blockEditor.setBlock(node);
        JComponent component = blockEditor.getPanel();
        
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(component, controlPanel);
        windowModule.addHeaderPanel(dialog.getWindow(), BlockEditor.class, blockEditor.getResourceBundle());
        windowModule.setWindowTitle(dialog, blockEditor.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                XBTTreeNode newNode = blockEditor.getBlock();
                XBTDocCommand undoStep;
                if (node.getParent() == null) {
                    undoStep = new XBTChangeBlockCommand(mainDoc);
                    long position = node.getBlockIndex();
                    XBTModifyBlockOperation modifyOperation = new XBTModifyBlockOperation(mainDoc, position, newNode);
                    ((XBTChangeBlockCommand) undoStep).appendOperation(modifyOperation);
                    XBData tailData = new XBData();
                    // TODO panel.saveTailData(tailData.getDataOutputStream());
                    XBTTailDataOperation extOperation = new XBTTailDataOperation(mainDoc, tailData);
                    ((XBTChangeBlockCommand) undoStep).appendOperation(extOperation);
                } else {
                    undoStep = new XBTModifyBlockCommand(mainDoc, node, newNode);
                }
                // TODO: Optimized diff command later
                //                if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                //                    undoStep = new XBTModDataBlockCommand(node, newNode);
                //                } else if (newNode.getChildrenCount() > 0) {
                //                } else {
                //                    undoStep = new XBTModAttrBlockCommand(node, newNode);
                //                }
                try {
                    undoHandler.execute(undoStep);
                } catch (Exception ex) {
                    Logger.getLogger(EditItemAction.class.getName()).log(Level.SEVERE, null, ex);
                }

                xbupFile.notifyItemModified(node);
            }

            dialog.close();
            dialog.dispose();
        });
        dialog.showCentered(editorProvider.getEditorComponent());
    }
}
