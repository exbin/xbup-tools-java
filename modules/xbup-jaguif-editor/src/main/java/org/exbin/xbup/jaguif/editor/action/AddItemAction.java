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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.document.api.ContextDocument;
import org.exbin.xbup.jaguif.viewer.document.XbupTreeDocument;
import org.exbin.xbup.jaguif.editor.gui.AddBlockPanel;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.gui.MultiStepControlPanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.operation.command.XBTDocCommand;
import org.exbin.xbup.operation.basic.command.XBTAddBlockCommand;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.jaguif.window.api.controller.MultiStepControlController;

/**
 * Add item action.
 */
@ParametersAreNonnullByDefault
public class AddItemAction extends AbstractAction {

    public static final String ACTION_ID = "addItem";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AddItemAction.class);

    private XbupTreeDocument xbupDocument;
    private DialogParentComponent dialogParentComponent;
    private AddBlockPanel addItemPanel = null;

    public AddItemAction() {
    }

    public void init() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                    xbupDocument = instance instanceof XbupTreeDocument ? (XbupTreeDocument) instance : null;
                    setEnabled(xbupDocument != null);
                });
                registrar.registerChangeListener(DialogParentComponent.class, (DialogParentComponent instance) -> {
                    dialogParentComponent = instance;
                });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBTBlock block = xbupDocument.getSelectedItem().orElse(null);

        XBACatalog catalog = xbupDocument.getCatalog();
//        UndoRedoState undoRedo = xbupFile.getUndoRedo();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
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
        controlPanel.setController((MultiStepControlController.ControlActionType actionType) -> {
            switch (actionType) {
                case FINISH: {
                    XBTTreeNode newNode = addItemPanel.getWorkNode();
                    try {
                        XBTTreeDocument mainDoc = xbupDocument.getDocument();
                        long parentPosition = node == null ? -1 : node.getBlockIndex();
                        int childIndex = node == null ? 0 : node.getChildCount();
                        XBTDocCommand step = new XBTAddBlockCommand(mainDoc, parentPosition, childIndex, newNode);
                        throw new UnsupportedOperationException("Not supported yet.");
                        // undoRedo.execute(step);
                    } catch (Exception ex) {
                        Logger.getLogger(AddItemAction.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    xbupDocument.notifyItemModified(newNode);

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
        dialog.showCentered(dialogParentComponent.getComponent());
    }
}
