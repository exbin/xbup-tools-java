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
package org.exbin.xbup.jaguif.editor.page;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import org.exbin.auxiliary.binary_data.array.ByteArrayEditableData;
import org.exbin.bined.EditMode;
import org.exbin.bined.swing.section.SectCodeArea;
import org.exbin.jaguif.App;
import org.exbin.bined.jaguif.component.BinedComponentModule;
import org.exbin.bined.jaguif.component.action.ClipboardCodeActions;
import org.exbin.bined.jaguif.component.action.GoToPositionAction;
import org.exbin.bined.jaguif.component.gui.BinEdComponentPanel;
import org.exbin.bined.jaguif.document.BinedDocumentModule;
import org.exbin.xbup.jaguif.editor.gui.BinaryToolbarPanel;
import org.exbin.xbup.jaguif.editor.gui.SimpleMessagePanel;
import org.exbin.jaguif.text.encoding.EncodingsManager;
import org.exbin.bined.jaguif.component.BinEdDataComponent;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.block.XbupBlock;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.jaguif.action.api.clipboard.TextClipboardOperationController;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;

/**
 * Binary viewer block page.
 */
@ParametersAreNonnullByDefault
public class BinaryBlockPage extends AbstractTabPagesComponent implements XbupEditorBlockPage, TextClipboardOperationController {

    protected final JPanel wrapperPanel = new JPanel(new BorderLayout());
    protected final SimpleMessagePanel messagePanel = new SimpleMessagePanel();
    protected final BinEdDataComponent binaryPanel = new BinEdDataComponent(new BinEdComponentPanel());
    protected final BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
    protected XbupBlock xbupBlock = null;

    protected GoToPositionAction goToPositionAction;
    protected EncodingsManager encodingsManager;
    protected ClipboardCodeActions clipboardCodeActions;

    public BinaryBlockPage() {
        init();
    }

    private void init() {
        putValue(KEY_ID, "binaryBlock");
        putValue(KEY_NAME, "Binary");
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/editor/resources/icons/16px/binary.png")));
        wrapperPanel.add(messagePanel, BorderLayout.CENTER);
        SectCodeArea codeArea = (SectCodeArea) binaryPanel.getCodeArea();
        binaryToolbarPanel.setCodeArea(codeArea);
        codeArea.setEditMode(EditMode.READ_ONLY);
        BinEdComponentPanel binaryComponentPanel = (BinEdComponentPanel) binaryPanel.getComponent();
        binaryComponentPanel.add(binaryToolbarPanel, BorderLayout.NORTH);
        /* binaryStatusBar.setController(new BinaryStatusController());

        // TODO
        codeArea.addSelectionChangedListener(() -> {
            binaryStatusBar.setSelectionRange(codeArea.getSelection());
//            updateClipboardActionsStatus();
        });

        codeArea.addCaretMovedListener((CodeAreaCaretPosition caretPosition) -> {
            binaryStatusBar.setCursorPosition(caretPosition);
        });

        codeArea.addEditModeChangedListener((EditMode mode, EditOperation operation) -> {
            binaryStatusBar.setEditMode(mode, operation);
        });

        binaryComponentPanel.add(binaryStatusBar, BorderLayout.SOUTH); */
        binaryComponentPanel.revalidate();
        binaryComponentPanel.repaint();
        // binaryPanel.setNoBorder();

        BinedComponentModule binedComponentModule = App.getModule(BinedComponentModule.class);
        BinedDocumentModule binedDocumentModule = App.getModule(BinedDocumentModule.class);
        JPopupMenu popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                int clickedX = x;
                int clickedY = y;
                if (invoker instanceof JViewport) {
                    clickedX += ((JViewport) invoker).getParent().getX();
                    clickedY += ((JViewport) invoker).getParent().getY();
                }
                BinedComponentModule binedModule = App.getModule(BinedComponentModule.class);
                JPopupMenu popupMenu = binedModule.createCodeAreaPopupMenu(codeArea, clickedX, clickedY);
                // TODO binedModule.updateActionStatus(codeArea);
                // TODO App.getModule(ActionModuleApi.class).updateActionsForComponent(CodeAreaCore.class, codeArea);
                popupMenu.show(invoker, x, y);
            }
        };
        binaryComponentPanel.setPopupMenu(popupMenu);
        binedDocumentModule.getFileManager().initDataComponent(binaryPanel);
        clipboardCodeActions = binedComponentModule.getClipboardCodeActions();
        binaryToolbarPanel.setGoToPositionAction(goToPositionAction);
        encodingsManager = binedDocumentModule.getEncodingsManager();
    }

    @Override
    public void setXbupBlock(XbupBlock xbupBlock) {
        if (xbupBlock == this.xbupBlock) {
            return;
        }

        BinEdComponentPanel binaryComponentPanel = (BinEdComponentPanel) binaryPanel.getComponent();
        XBTBlock prevBlock = this.xbupBlock == null ? null : this.xbupBlock.getBlock().orElse(null);
        XBTBlock block = xbupBlock.getBlock().orElse(null);
        if (block != null) {
            ByteArrayEditableData byteArrayData = new ByteArrayEditableData();
            try (OutputStream dataOutputStream = byteArrayData.getDataOutputStream()) {
                ((XBTTreeNode) block).toStreamUB(dataOutputStream);
            } catch (IOException ex) {
                Logger.getLogger(BinaryBlockPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            binaryComponentPanel.setContentData(byteArrayData);
        }

        if (block == null && prevBlock != null) {
            wrapperPanel.remove(binaryComponentPanel);
            wrapperPanel.add(messagePanel, BorderLayout.CENTER);

            wrapperPanel.revalidate();
            wrapperPanel.repaint();
        } else if (block != null && prevBlock == null) {
            wrapperPanel.remove(messagePanel);
            wrapperPanel.add(binaryComponentPanel, BorderLayout.CENTER);

            wrapperPanel.revalidate();
            wrapperPanel.repaint();
        }

        this.xbupBlock = xbupBlock;
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return wrapperPanel;
    }

    @Override
    public void performCut() {
        binaryPanel.getCodeArea().cut();
    }

    @Override
    public void performCopy() {
        binaryPanel.getCodeArea().copy();
    }

    @Override
    public void performPaste() {
        binaryPanel.getCodeArea().paste();
    }

    @Override
    public void performDelete() {
        binaryPanel.getCodeArea().delete();
    }

    @Override
    public void performSelectAll() {
        binaryPanel.getCodeArea().selectAll();
    }

    @Override
    public boolean hasSelection() {
        return binaryPanel.getCodeArea().hasSelection();
    }

    @Override
    public boolean hasDataToCopy() {
        return hasSelection();
    }

    @Override
    public boolean isEditable() {
        return binaryPanel.getCodeArea().isEditable();
    }

    @Override
    public boolean canSelectAll() {
        return true;
    }

    @Override
    public boolean isValidForPaste() {
        return binaryPanel.getCodeArea().canPaste();
    }

    @Override
    public boolean canDelete() {
        return isEditable();
    }
}
