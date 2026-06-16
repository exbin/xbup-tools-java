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
package org.exbin.xbup.jaguif.viewer.page;

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
import org.exbin.bined.jaguif.document.BinedDocumentModule;
import org.exbin.bined.jaguif.component.action.ClipboardCodeActions;
import org.exbin.bined.jaguif.component.action.GoToPositionAction;
import org.exbin.bined.jaguif.component.gui.BinEdComponentPanel;
import org.exbin.jaguif.text.encoding.EncodingsManager;
import org.exbin.bined.jaguif.component.BinEdDataComponent;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.block.XbupBlock;
import org.exbin.xbup.jaguif.viewer.gui.BinaryToolbarPanel;
import org.exbin.xbup.jaguif.viewer.gui.SimpleMessagePanel;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.jaguif.action.api.clipboard.TextClipboardOperationController;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;

/**
 * Binary viewer of document.
 */
@ParametersAreNonnullByDefault
public class BinaryBlockPage extends AbstractTabPagesComponent implements XbupViewerBlockPage, TextClipboardOperationController {

    protected final JPanel wrapperPanel = new JPanel(new BorderLayout());
    protected final SimpleMessagePanel messagePanel = new SimpleMessagePanel();
    protected final BinEdDataComponent binaryComponent = new BinEdDataComponent(new BinEdComponentPanel());
    protected final BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
    protected XbupBlock xbupBlock = null;

    protected GoToPositionAction goToPositionAction;
    protected EncodingsManager encodingsManager;
    protected ClipboardCodeActions clipboardCodeActions;

    public BinaryBlockPage() {
        init();
    }

    private void init() {
        putValue(KEY_NAME, "Binary");
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/viewer/resources/icons/16px/binary.png")));
        wrapperPanel.add(messagePanel, BorderLayout.CENTER);
        SectCodeArea codeArea = (SectCodeArea) binaryComponent.getCodeArea();
        binaryToolbarPanel.setCodeArea(codeArea);
        codeArea.setEditMode(EditMode.READ_ONLY);
        BinEdComponentPanel binaryPanel = (BinEdComponentPanel) binaryComponent.getComponent();
        binaryPanel.add(binaryToolbarPanel, BorderLayout.NORTH);
        // TODO
        /*binaryStatusPanel.setController(new BinaryStatusController());

        // TODO
        codeArea.addSelectionChangedListener(() -> {
            binaryStatusPanel.setSelectionRange(codeArea.getSelection());
//            updateClipboardActionsStatus();
        });

        codeArea.addCaretMovedListener((CodeAreaCaretPosition caretPosition) -> {
            binaryStatusPanel.setCursorPosition(caretPosition);
        });

        codeArea.addEditModeChangedListener((EditMode mode, EditOperation operation) -> {
            binaryStatusPanel.setEditMode(mode, operation);
        });

        binaryPanel.add(binaryStatusPanel, BorderLayout.SOUTH); */
        binaryPanel.revalidate();
        binaryPanel.repaint();
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
                SectCodeArea codeArea = binaryPanel.getCodeArea();
                BinedComponentModule binedModule = App.getModule(BinedComponentModule.class);
                JPopupMenu popupMenu = binedModule.createCodeAreaPopupMenu(codeArea, clickedX, clickedY);
                // TODO binedModule.updateActionStatus(codeArea);
                // TODO App.getModule(ActionModuleApi.class).updateActionsForComponent(CodeAreaCore.class, codeArea);
                popupMenu.show(invoker, x, y);
            }
        };
        binaryPanel.setPopupMenu(popupMenu);
        binedDocumentModule.getFileManager().initDataComponent(binaryComponent);
        clipboardCodeActions = binedComponentModule.getClipboardCodeActions();
        binaryToolbarPanel.setGoToPositionAction(goToPositionAction);
        encodingsManager = binedDocumentModule.getEncodingsManager();
    }

    @Override
    public void setXbupBlock(XbupBlock xbupBlock) {
        if (xbupBlock == this.xbupBlock) {
            return;
        }

        BinEdComponentPanel binaryPanel = (BinEdComponentPanel) binaryComponent.getComponent();
        XBTBlock prevBlock = this.xbupBlock == null ? null : this.xbupBlock.getBlock();
        XBTBlock block = xbupBlock == null ? null : xbupBlock.getBlock();
        if (block != null) {
            ByteArrayEditableData byteArrayData = new ByteArrayEditableData();
            try (OutputStream dataOutputStream = byteArrayData.getDataOutputStream()) {
                ((XBTTreeNode) block).toStreamUB(dataOutputStream);
            } catch (IOException ex) {
                Logger.getLogger(BinaryBlockPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            binaryPanel.setContentData(byteArrayData);
        }

        if (block == null && prevBlock != null) {
            wrapperPanel.remove(binaryPanel);
            wrapperPanel.add(messagePanel, BorderLayout.CENTER);

            wrapperPanel.revalidate();
            wrapperPanel.repaint();
        } else if (block != null && prevBlock == null) {
            wrapperPanel.remove(messagePanel);
            wrapperPanel.add(binaryPanel, BorderLayout.CENTER);

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
        binaryComponent.getCodeArea().cut();
    }

    @Override
    public void performCopy() {
        binaryComponent.getCodeArea().copy();
    }

    @Override
    public void performPaste() {
        binaryComponent.getCodeArea().paste();
    }

    @Override
    public void performDelete() {
        binaryComponent.getCodeArea().delete();
    }

    @Override
    public void performSelectAll() {
        binaryComponent.getCodeArea().selectAll();
    }

    @Override
    public boolean hasSelection() {
        return binaryComponent.getCodeArea().hasSelection();
    }

    @Override
    public boolean hasDataToCopy() {
        return hasSelection();
    }

    @Override
    public boolean isEditable() {
        return binaryComponent.getCodeArea().isEditable();
    }

    @Override
    public boolean canSelectAll() {
        return true;
    }

    @Override
    public boolean isValidForPaste() {
        return binaryComponent.getCodeArea().canPaste();
    }

    @Override
    public boolean canDelete() {
        return isEditable();
    }
}
