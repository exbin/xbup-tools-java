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
package org.exbin.xbup.jaguif.editor.document;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.exbin.auxiliary.binary_data.array.ByteArrayEditableData;
import org.exbin.bined.CodeAreaCaretPosition;
import org.exbin.bined.EditMode;
import org.exbin.bined.EditOperation;
import org.exbin.bined.capability.EditModeCapable;
import org.exbin.bined.swing.section.SectCodeArea;
import org.exbin.jaguif.App;
import org.exbin.bined.jaguif.component.BinedComponentModule;
import org.exbin.bined.jaguif.component.action.ClipboardCodeActions;
import org.exbin.bined.jaguif.component.action.GoToPositionAction;
import org.exbin.bined.jaguif.component.gui.BinEdComponentPanel;
import org.exbin.bined.jaguif.component.handler.CodeAreaPopupMenuHandler;
import org.exbin.bined.jaguif.viewer.BinedViewerModule;
import org.exbin.xbup.jaguif.editor.gui.BinaryToolbarPanel;
import org.exbin.xbup.jaguif.editor.gui.SimpleMessagePanel;
import org.exbin.jaguif.text.encoding.EncodingsManager;
import org.exbin.jaguif.action.api.clipboard.TextClipboardController;
import org.exbin.jaguif.action.api.clipboard.ClipboardStateListener;
import org.exbin.jaguif.statusbar.api.StatusBar;
import org.exbin.bined.jaguif.component.BinEdDataComponent;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Binary viewer of document.
 */
@ParametersAreNonnullByDefault
public class BinaryViewer implements BlockViewer, TextClipboardController {

    private final JPanel wrapperPanel = new JPanel(new BorderLayout());
    private final SimpleMessagePanel messagePanel = new SimpleMessagePanel();
    private final BinEdDataComponent binaryPanel = new BinEdDataComponent(new BinEdComponentPanel());
    private final BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
    private final StatusBar binaryStatusBar = null;
    private XBTBlock block = null;

    private GoToPositionAction goToPositionAction;
    private EncodingsManager encodingsManager;
    private ClipboardCodeActions clipboardCodeActions;

    public BinaryViewer() {
        init();
    }

    private void init() {
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

        BinedComponentModule binedModule = App.getModule(BinedComponentModule.class);
        BinedViewerModule binedViewerModule = App.getModule(BinedViewerModule.class);
        CodeAreaPopupMenuHandler codeAreaPopupMenuHandler = binedModule.createCodeAreaPopupMenuHandler(BinedComponentModule.PopupMenuVariant.NORMAL);
        JPopupMenu popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                int clickedX = x;
                int clickedY = y;
                if (invoker instanceof JViewport) {
                    clickedX += ((JViewport) invoker).getParent().getX();
                    clickedY += ((JViewport) invoker).getParent().getY();
                }
                JPopupMenu popupMenu = codeAreaPopupMenuHandler.createPopupMenu(codeArea, BinedComponentModule.BINARY_POPUP_MENU_ID + ".BinaryViewer", clickedX, clickedY);
                popupMenu.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        codeAreaPopupMenuHandler.dropPopupMenu(BinedComponentModule.BINARY_POPUP_MENU_ID + ".BinaryViewer");
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                });
                // TODO binedModule.updateActionStatus(codeArea);
                // TODO App.getModule(ActionModuleApi.class).updateActionsForComponent(CodeAreaCore.class, codeArea);
                popupMenu.show(invoker, x, y);
            }
        };
        binaryComponentPanel.setPopupMenu(popupMenu);
        binedModule.getFileManager().initDataComponent(binaryPanel);
        clipboardCodeActions = binedModule.getClipboardCodeActions();
        binaryToolbarPanel.setGoToPositionAction(goToPositionAction);
        encodingsManager = binedViewerModule.getEncodingsManager();
    }

    @Override
    public void setCatalog(XBACatalog catalog) {
    }

    @Override
    public void setPluginRepository(XBPluginRepository pluginRepository) {
    }

    @Override
    public void setBlock(@Nullable XBTBlock block) {
        if (this.block != block) {
            BinEdComponentPanel binaryComponentPanel = (BinEdComponentPanel) binaryPanel.getComponent();
            if (block != null) {
                ByteArrayEditableData byteArrayData = new ByteArrayEditableData();
                try (OutputStream dataOutputStream = byteArrayData.getDataOutputStream()) {
                    ((XBTTreeNode) block).toStreamUB(dataOutputStream);
                } catch (IOException ex) {
                    Logger.getLogger(BinaryViewer.class.getName()).log(Level.SEVERE, null, ex);
                }

                binaryComponentPanel.setContentData(byteArrayData);
            }

            if (block == null && this.block != null) {
                wrapperPanel.remove(binaryComponentPanel);
                wrapperPanel.add(messagePanel, BorderLayout.CENTER);

                wrapperPanel.revalidate();
                wrapperPanel.repaint();
            } else if (block != null && this.block == null) {
                wrapperPanel.remove(messagePanel);
                wrapperPanel.add(binaryComponentPanel, BorderLayout.CENTER);

                wrapperPanel.revalidate();
                wrapperPanel.repaint();
            }

            this.block = block;
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Binary";
    }

    @Nonnull
    @Override
    public Optional<ImageIcon> getIcon() {
        return Optional.of(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/editor/resources/icons/16px/binary.png")));
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
