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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionActiveComponent;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.ComponentActivationManager;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.editor.xbup.gui.AddBlockPanel;
import org.exbin.framework.editor.xbup.viewer.XbupEditorProvider;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.operation.undo.api.UndoRedoState;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.handler.MultiStepControlHandler;
import org.exbin.framework.window.api.gui.MultiStepControlPanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.operation.XBTDocCommand;
import org.exbin.xbup.operation.basic.command.XBTAddBlockCommand;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Add item action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AddItemAction extends AbstractAction implements ActionActiveComponent {

    public static final String ACTION_ID = "addItemAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AddItemAction.class);

    private EditorProvider editorProvider;
    private AddBlockPanel addItemPanel = null;

    public AddItemAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_ACTIVE_COMPONENT, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(editorProvider instanceof XbupEditorProvider)) {
            return;
        }

        XBACatalog catalog = ((XbupEditorProvider) editorProvider).getCatalog();
        XbupFileHandler xbupFile = (XbupFileHandler) editorProvider.getActiveFile().get();
//        UndoRedoState undoRedo = xbupFile.getUndoRedo();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        XBTBlock block = xbupFile.getSelectedItem().orElse(null);
        if (!(block instanceof XBTTreeNode) && block != null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        XBTTreeNode node = (XBTTreeNode) block;

        addItemPanel = new AddBlockPanel();
        addItemPanel.setCatalog(catalog);
        addItemPanel.setParentNode(node);
        MultiStepControlPanel controlPanel = new MultiStepControlPanel();
        final WindowHandler dialog = windowModule.createDialog(addItemPanel, controlPanel);
        windowModule.addHeaderPanel(dialog.getWindow(), AddBlockPanel.class, addItemPanel.getResourceBundle());
        controlPanel.setHandler((MultiStepControlHandler.ControlActionType actionType) -> {
            switch (actionType) {
                case FINISH: {
                    XBTTreeNode newNode = addItemPanel.getWorkNode();
                    try {
                        XBTTreeDocument mainDoc = xbupFile.getDocument();
                        long parentPosition = node == null ? -1 : node.getBlockIndex();
                        int childIndex = node == null ? 0 : node.getChildCount();
                        XBTDocCommand step = new XBTAddBlockCommand(mainDoc, parentPosition, childIndex, newNode);
                        throw new UnsupportedOperationException("Not supported yet.");
                        // undoRedo.execute(step);
                    } catch (Exception ex) {
                        Logger.getLogger(AddItemAction.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    xbupFile.notifyItemModified(newNode);

                    dialog.close();
                    dialog.dispose();
                    break;
                }
                case CANCEL: {
                    dialog.close();
                    dialog.dispose();
                    break;
                }
                case NEXT: {
                    break;
                }
                case PREVIOUS: {
                    break;
                }
            }
        });
        dialog.showCentered(editorProvider.getEditorComponent());
    }

    @Override
    public void register(ComponentActivationManager manager) {
        manager.registerUpdateListener(EditorProvider.class, (instance) -> {
            editorProvider = instance;
            setEnabled(editorProvider instanceof XbupEditorProvider);
        });
    }
}
