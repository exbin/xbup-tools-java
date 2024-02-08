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
package org.exbin.framework.editor.xbup.def;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.bined.CodeAreaCaretPosition;
import org.exbin.bined.EditMode;
import org.exbin.bined.EditOperation;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.bined.BinaryStatusApi;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.bined.action.GoToPositionAction;
import org.exbin.framework.bined.gui.BinEdComponentPanel;
import org.exbin.framework.bined.gui.BinaryStatusPanel;
import org.exbin.framework.bined.handler.CodeAreaPopupMenuHandler;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.SideToolBar;
import org.exbin.framework.editor.text.EncodingsHandler;
import org.exbin.framework.editor.xbup.def.action.ExportDataAction;
import org.exbin.framework.editor.xbup.def.action.ImportDataAction;
import org.exbin.framework.editor.xbup.def.gui.BinaryDataPanel;
import org.exbin.framework.editor.xbup.gui.BinaryToolbarPanel;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Binary data editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataEditor {

    private BinaryDataPanel editorPanel = new BinaryDataPanel();
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private final ActionsProvider actions;
    private boolean extraBarsAdded = false;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BinaryDataEditor.class);

    private ImportDataAction importDataAction = new ImportDataAction();
    private ExportDataAction exportDataAction = new ExportDataAction();

    public BinaryDataEditor() {
        actions = (SideToolBar sideToolBar) -> {
            sideToolBar.addAction(importDataAction);
            sideToolBar.addAction(exportDataAction);
        };

        editorPanel.addActions(actions);

        importDataAction.setup();
        exportDataAction.setup();

        BinedModule binedModule = App.getModule(BinedModule.class);
        CodeAreaPopupMenuHandler codeAreaPopupMenuHandler = binedModule.createCodeAreaPopupMenuHandler(BinedModule.PopupMenuVariant.NORMAL);
        popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                int clickedX = x;
                int clickedY = y;
                if (invoker instanceof JViewport) {
                    clickedX += ((JViewport) invoker).getParent().getX();
                    clickedY += ((JViewport) invoker).getParent().getY();
                }
                ExtCodeArea codeArea = editorPanel.getComponentPanel().getCodeArea();
                JPopupMenu popupMenu = codeAreaPopupMenuHandler.createPopupMenu(codeArea, BinedModule.BINARY_POPUP_MENU_ID + ".BinaryDataEditor", clickedX, clickedY);
                popupMenu.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        codeAreaPopupMenuHandler.dropPopupMenu(BinedModule.BINARY_POPUP_MENU_ID + ".BinaryDataEditor");
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                });

                ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
                LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
                JMenuItem importDataMenuItem = actionModule.actionToMenuItem(importDataAction);
                importDataMenuItem.setText(languageModule.getActionWithDialogText((String) importDataAction.getValue(Action.NAME)));
                popupMenu.add(importDataMenuItem);
                JMenuItem exportDataMenuItem = actionModule.actionToMenuItem(exportDataAction);
                exportDataMenuItem.setText(languageModule.getActionWithDialogText((String) exportDataAction.getValue(Action.NAME)));
                popupMenu.add(exportDataMenuItem);

                binedModule.updateActionStatus(codeArea);
                popupMenu.show(invoker, x, y);
            }
        };

        editorPanel.setDataPopupMenu(popupMenu);

        editorPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                BinedModule binedModule = App.getModule(BinedModule.class);
                binedModule.updateActionStatus(editorPanel.getComponentPanel().getCodeArea());
            }
        });
    }

    @Nonnull
    public BinaryDataPanel getEditorPanel() {
        return editorPanel;
    }

    public void attachExtraBars() {
        if (extraBarsAdded) {
            return;
        }

        BinedModule binedModule = App.getModule(BinedModule.class);
        BinEdComponentPanel binaryPanel = editorPanel.getComponentPanel();
        ExtCodeArea codeArea = binaryPanel.getCodeArea();
        BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
        binaryToolbarPanel.setCodeArea(codeArea);
        binaryPanel.add(binaryToolbarPanel, BorderLayout.NORTH);
        BinaryStatusPanel binaryStatusPanel = new BinaryStatusPanel();
        binaryStatusPanel.setStatusControlHandler(new BinaryStatusPanel.StatusControlHandler() {
            @Override
            public void changeEditOperation(EditOperation operation) {
                binaryPanel.getCodeArea().setEditOperation(operation);
            }

            @Override
            public void changeCursorPosition() {
                GoToPositionAction goToPositionAction = binedModule.createGoToPositionAction();
                goToPositionAction.actionPerformed(null);
            }

            @Override
            public void cycleEncodings() {
                EncodingsHandler encodingsHandler = binedModule.getEncodingsHandler();
                encodingsHandler.cycleEncodings();
            }

            @Override
            public void encodingsPopupEncodingsMenu(MouseEvent mouseEvent) {
                EncodingsHandler encodingsHandler = binedModule.getEncodingsHandler();
                encodingsHandler.popupEncodingsMenu(mouseEvent);
            }

            @Override
            public void changeMemoryMode(BinaryStatusApi.MemoryMode memoryMode) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        codeArea.addSelectionChangedListener(() -> {
            binaryStatusPanel.setSelectionRange(codeArea.getSelection());
//            updateClipboardActionsStatus();
        });
        long dataSize = codeArea.getDataSize();
        binaryStatusPanel.setCurrentDocumentSize(dataSize, dataSize);

        codeArea.addCaretMovedListener((CodeAreaCaretPosition caretPosition) -> {
            binaryStatusPanel.setCursorPosition(caretPosition);
        });

        codeArea.addEditModeChangedListener((EditMode mode, EditOperation operation) -> {
            binaryStatusPanel.setEditMode(mode, operation);
        });

        binaryPanel.add(binaryStatusPanel, BorderLayout.SOUTH);
        binaryPanel.revalidate();
        binaryPanel.repaint();
        extraBarsAdded = true;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setUndoHandler(XBUndoHandler undoHandler) {
        editorPanel.setUndoHandler(undoHandler);
    }

    public void setBlock(XBTTreeNode block) {
        editorPanel.setContentData(block.getBlockData());
    }

    public void setContentData(BinaryData binaryData) {
        editorPanel.setContentData(binaryData);
    }
}
