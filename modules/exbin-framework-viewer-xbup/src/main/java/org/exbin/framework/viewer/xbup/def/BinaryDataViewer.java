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
package org.exbin.framework.viewer.xbup.def;

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
import org.exbin.bined.swing.section.SectCodeArea;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.bined.action.GoToPositionAction;
import org.exbin.framework.bined.gui.BinEdComponentPanel;
import org.exbin.framework.bined.gui.BinaryStatusPanel;
import org.exbin.framework.bined.handler.CodeAreaPopupMenuHandler;
import org.exbin.framework.bined.viewer.BinedViewerModule;
import org.exbin.framework.component.api.action.ActionsProvider;
import org.exbin.framework.component.api.toolbar.SideToolBar;
import org.exbin.framework.viewer.xbup.def.action.ExportDataAction;
import org.exbin.framework.viewer.xbup.def.gui.BinaryDataPanel;
import org.exbin.framework.viewer.xbup.gui.BinaryToolbarPanel;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.text.encoding.EncodingsManager;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Binary data viewer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataViewer {

    private BinaryDataPanel binaryDataPanel = new BinaryDataPanel();
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private final ActionsProvider actions;
    private boolean extraBarsAdded = false;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BinaryDataViewer.class);

    private ExportDataAction exportDataAction = new ExportDataAction();

    public BinaryDataViewer() {
        actions = (SideToolBar sideToolBar) -> {
            sideToolBar.addAction(exportDataAction);
        };

        binaryDataPanel.addActions(actions);

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
                SectCodeArea codeArea = binaryDataPanel.getComponentPanel().getCodeArea();
                JPopupMenu popupMenu = codeAreaPopupMenuHandler.createPopupMenu(codeArea, BinedModule.BINARY_POPUP_MENU_ID + ".BinaryDataViewer", clickedX, clickedY);
                popupMenu.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        codeAreaPopupMenuHandler.dropPopupMenu(BinedModule.BINARY_POPUP_MENU_ID + ".BinaryDataViewer");
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                });

                ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
                LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
                JMenuItem exportDataMenuItem = actionModule.actionToMenuItem(exportDataAction);
                exportDataMenuItem.setText(languageModule.getActionWithDialogText((String) exportDataAction.getValue(Action.NAME)));
                popupMenu.add(exportDataMenuItem);

                // TODO binedModule.updateActionStatus(codeArea);
                popupMenu.show(invoker, x, y);
            }
        };

        binaryDataPanel.setDataPopupMenu(popupMenu);

        binaryDataPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // TODO BinedModule binedModule = App.getModule(BinedModule.class);
                // binedModule.updateActionStatus(binaryDataPanel.getComponentPanel().getCodeArea());
            }
        });
    }

    @Nonnull
    public BinaryDataPanel getBinaryDataPanel() {
        return binaryDataPanel;
    }

    public void attachExtraBars() {
        if (extraBarsAdded) {
            return;
        }

        BinedModule binedModule = App.getModule(BinedModule.class);
        BinedViewerModule binedViewerModule = App.getModule(BinedViewerModule.class);
        BinEdComponentPanel binaryPanel = binaryDataPanel.getComponentPanel();
        SectCodeArea codeArea = binaryPanel.getCodeArea();
        BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
        binaryToolbarPanel.setCodeArea(codeArea);
        binaryPanel.add(binaryToolbarPanel, BorderLayout.NORTH);
        BinaryStatusPanel binaryStatusPanel = new BinaryStatusPanel();
        binaryStatusPanel.setController(new BinaryStatusController(binaryPanel, binedModule, codeArea, binedViewerModule));
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

    public void setUndoRedo(UndoRedo undoRedo) {
        binaryDataPanel.setUndoRedo(undoRedo);
    }

    public void setBlock(XBTTreeNode block) {
        binaryDataPanel.setContentData(block.getBlockData());
    }

    public void setContentData(BinaryData binaryData) {
        binaryDataPanel.setContentData(binaryData);
    }

    @ParametersAreNonnullByDefault
    private static class BinaryStatusController implements BinaryStatusPanel.Controller, BinaryStatusPanel.EncodingsController {

        private final BinEdComponentPanel binaryPanel;
        private final BinedModule binedModule;
        private final SectCodeArea codeArea;
        private final BinedViewerModule binedViewerModule;

        public BinaryStatusController(BinEdComponentPanel binaryPanel, BinedModule binedModule, SectCodeArea codeArea, BinedViewerModule binedViewerModule) {
            this.binaryPanel = binaryPanel;
            this.binedModule = binedModule;
            this.codeArea = codeArea;
            this.binedViewerModule = binedViewerModule;
        }

        @Override
        public void changeEditOperation(EditOperation operation) {
            binaryPanel.getCodeArea().setEditOperation(operation);
        }

        @Override
        public void changeCursorPosition() {
            GoToPositionAction goToPositionAction = binedModule.createGoToPositionAction();
            goToPositionAction.setCodeArea(codeArea);
            goToPositionAction.actionPerformed(null);
        }

        @Override
        public void cycleNextEncoding() {
            EncodingsManager encodingsManager = binedViewerModule.getEncodingsManager();
            encodingsManager.cycleNextEncoding();
        }

        @Override
        public void cyclePreviousEncoding() {
            EncodingsManager encodingsManager = binedViewerModule.getEncodingsManager();
            encodingsManager.cyclePreviousEncoding();
        }

        @Override
        public void encodingsPopupEncodingsMenu(MouseEvent mouseEvent) {
            EncodingsManager encodingsManager = binedViewerModule.getEncodingsManager();
            encodingsManager.popupEncodingsMenu(mouseEvent);
        }
    }
}
