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
package org.exbin.xbup.jaguif.editor.def;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.bined.swing.section.SectCodeArea;
import org.exbin.jaguif.App;
import org.exbin.bined.jaguif.component.BinedComponentModule;
import org.exbin.bined.jaguif.component.gui.BinEdComponentPanel;
import org.exbin.bined.jaguif.viewer.BinedViewerModule;
import org.exbin.xbup.jaguif.editor.def.action.ExportDataAction;
import org.exbin.xbup.jaguif.editor.def.action.ImportDataAction;
import org.exbin.xbup.jaguif.editor.def.gui.BinaryDataPanel;
import org.exbin.xbup.jaguif.editor.gui.BinaryToolbarPanel;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Binary data editor.
 */
@ParametersAreNonnullByDefault
public class BinaryDataViewer {

    private BinaryDataPanel editorPanel = new BinaryDataPanel();
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private boolean extraBarsAdded = false;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BinaryDataViewer.class);

    private ImportDataAction importDataAction = new ImportDataAction();
    private ExportDataAction exportDataAction = new ExportDataAction();

    public BinaryDataViewer() {
        JToolBar sideToolBar = editorPanel.getSideToolBar();
        // TODO sideToolBar.addAction(importDataAction);
        // TODO sideToolBar.addAction(exportDataAction);

        importDataAction.init();
        exportDataAction.init();

        popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                int clickedX = x;
                int clickedY = y;
                if (invoker instanceof JViewport) {
                    clickedX += ((JViewport) invoker).getParent().getX();
                    clickedY += ((JViewport) invoker).getParent().getY();
                }
                SectCodeArea codeArea = editorPanel.getComponentPanel().getCodeArea();
                BinedComponentModule binedModule = App.getModule(BinedComponentModule.class);
                JPopupMenu popupMenu = binedModule.createCodeAreaPopupMenu(codeArea, clickedX, clickedY);

                MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
                LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
                JMenuItem importDataMenuItem = menuModule.actionToMenuItem(importDataAction);
                importDataMenuItem.setText(languageModule.getActionWithDialogText((String) importDataAction.getValue(Action.NAME)));
                popupMenu.add(importDataMenuItem);
                JMenuItem exportDataMenuItem = menuModule.actionToMenuItem(exportDataAction);
                exportDataMenuItem.setText(languageModule.getActionWithDialogText((String) exportDataAction.getValue(Action.NAME)));
                popupMenu.add(exportDataMenuItem);

                // TODO binedModule.updateActionStatus(codeArea);
                popupMenu.show(invoker, x, y);
            }
        };

        editorPanel.setDataPopupMenu(popupMenu);

        editorPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // TODO BinedComponentModule binedModule = App.getModule(BinedComponentModule.class);
                // binedModule.updateActionStatus(editorPanel.getComponentPanel().getCodeArea());
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

        BinedComponentModule binedModule = App.getModule(BinedComponentModule.class);
        BinedViewerModule binedViewerModule = App.getModule(BinedViewerModule.class);
        BinEdComponentPanel binaryPanel = editorPanel.getComponentPanel();
        SectCodeArea codeArea = binaryPanel.getCodeArea();
        BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
        binaryToolbarPanel.setCodeArea(codeArea);
        binaryPanel.add(binaryToolbarPanel, BorderLayout.NORTH);
        /*BinaryStatusPanel binaryStatusPanel = new BinaryStatusPanel();
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

        binaryPanel.add(binaryStatusPanel, BorderLayout.SOUTH); */
        binaryPanel.revalidate();
        binaryPanel.repaint();
        extraBarsAdded = true;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setUndoRedo(UndoRedo undoRedo) {
        editorPanel.setUndoRedo(undoRedo);
    }

    public void setBlock(XBTTreeNode block) {
        editorPanel.setContentData(block.getBlockData());
    }

    public void setContentData(BinaryData binaryData) {
        editorPanel.setContentData(binaryData);
    }
}
