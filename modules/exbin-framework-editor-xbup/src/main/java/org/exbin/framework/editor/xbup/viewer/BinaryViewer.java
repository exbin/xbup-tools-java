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
package org.exbin.framework.editor.xbup.viewer;

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
import org.exbin.auxiliary.paged_data.ByteArrayEditableData;
import org.exbin.bined.CodeAreaCaretPosition;
import org.exbin.bined.EditMode;
import org.exbin.bined.EditOperation;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.bined.BinaryStatusApi;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.bined.action.ClipboardCodeActions;
import org.exbin.framework.bined.action.GoToPositionAction;
import org.exbin.framework.bined.gui.BinEdComponentPanel;
import org.exbin.framework.bined.gui.BinaryStatusPanel;
import org.exbin.framework.bined.handler.CodeAreaPopupMenuHandler;
import org.exbin.framework.editor.text.EncodingsHandler;
import org.exbin.framework.editor.xbup.gui.BinaryToolbarPanel;
import org.exbin.framework.editor.xbup.gui.SimpleMessagePanel;
import org.exbin.framework.utils.ClipboardActionsHandler;
import org.exbin.framework.utils.ClipboardActionsUpdateListener;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Binary viewer of document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryViewer implements BlockViewer, ClipboardActionsHandler {

    private final JPanel wrapperPanel = new JPanel(new BorderLayout());
    private final SimpleMessagePanel messagePanel = new SimpleMessagePanel();
    private final BinEdComponentPanel binaryPanel = new BinEdComponentPanel();
    private final BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
    private final BinaryStatusPanel binaryStatusPanel = new BinaryStatusPanel();
    private XBTBlock block = null;

    private GoToPositionAction goToPositionAction;
    private EncodingsHandler encodingsHandler;
    private ClipboardCodeActions clipboardCodeActions;

    public BinaryViewer() {
        init();
    }

    private void init() {
        wrapperPanel.add(messagePanel, BorderLayout.CENTER);
        binaryToolbarPanel.setCodeArea(binaryPanel.getCodeArea());
        binaryPanel.add(binaryToolbarPanel, BorderLayout.NORTH);
        binaryStatusPanel.setStatusControlHandler(new BinaryStatusPanel.StatusControlHandler() {
            @Override
            public void changeEditOperation(EditOperation operation) {
                binaryPanel.getCodeArea().setEditOperation(operation);
            }

            @Override
            public void changeCursorPosition() {
                if (goToPositionAction != null) {
                    goToPositionAction.actionPerformed(null);
                }
            }

            @Override
            public void cycleEncodings() {
                if (encodingsHandler != null) {
                    encodingsHandler.cycleEncodings();
                }
            }

            @Override
            public void encodingsPopupEncodingsMenu(MouseEvent mouseEvent) {
                if (encodingsHandler != null) {
                    encodingsHandler.popupEncodingsMenu(mouseEvent);
                }
            }

            @Override
            public void changeMemoryMode(BinaryStatusApi.MemoryMode memoryMode) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        // TODO
        ExtCodeArea codeArea = binaryPanel.getCodeArea();
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

        binaryPanel.add(binaryStatusPanel, BorderLayout.SOUTH);
        binaryPanel.revalidate();
        binaryPanel.repaint();
        // binaryPanel.setNoBorder();
    }

    @Override
    public void setApplication(XBApplication application) {
        BinedModule binedModule = application.getModuleRepository().getModuleByInterface(BinedModule.class);
        CodeAreaPopupMenuHandler codeAreaPopupMenuHandler = binedModule.createCodeAreaPopupMenuHandler(BinedModule.PopupMenuVariant.NORMAL);
        JPopupMenu popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                int clickedX = x;
                int clickedY = y;
                if (invoker instanceof JViewport) {
                    clickedX += ((JViewport) invoker).getParent().getX();
                    clickedY += ((JViewport) invoker).getParent().getY();
                }
                ExtCodeArea codeArea = binaryPanel.getCodeArea();
                JPopupMenu popupMenu = codeAreaPopupMenuHandler.createPopupMenu(codeArea, BinedModule.BINARY_POPUP_MENU_ID, clickedX, clickedY);
                popupMenu.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        codeAreaPopupMenuHandler.dropPopupMenu(BinedModule.BINARY_POPUP_MENU_ID);
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                });
                binedModule.updateActionStatus(codeArea);
                clipboardCodeActions.updateForActiveCodeArea(codeArea);
                popupMenu.show(invoker, x, y);
            }
        };
        binaryPanel.setPopupMenu(popupMenu);
        goToPositionAction = binedModule.getGoToPositionAction();
        goToPositionAction.updateForActiveCodeArea(binaryPanel.getCodeArea());
        clipboardCodeActions = binedModule.getClipboardCodeActions();
        binaryToolbarPanel.setGoToPositionAction(goToPositionAction);
        encodingsHandler = binedModule.getEncodingsHandler();
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
            if (block != null) {
                ByteArrayEditableData byteArrayData = new ByteArrayEditableData();
                try (OutputStream dataOutputStream = byteArrayData.getDataOutputStream()) {
                    ((XBTTreeNode) block).toStreamUB(dataOutputStream);
                } catch (IOException ex) {
                    Logger.getLogger(BinaryViewer.class.getName()).log(Level.SEVERE, null, ex);
                }

                binaryPanel.setContentData(byteArrayData);
            }

            if (block == null && this.block != null) {
                wrapperPanel.remove(binaryPanel);
                wrapperPanel.add(messagePanel, BorderLayout.CENTER);

                wrapperPanel.revalidate();
                wrapperPanel.repaint();
            } else if (block != null && this.block == null) {
                wrapperPanel.remove(messagePanel);
                wrapperPanel.add(binaryPanel, BorderLayout.CENTER);

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
        return Optional.of(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/binary-16x16.png")));
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
    public boolean isSelection() {
        return binaryPanel.getCodeArea().hasSelection();
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
    public boolean canPaste() {
        return binaryPanel.getCodeArea().canPaste();
    }

    @Override
    public boolean canDelete() {
        return isEditable();
    }

    @Override
    public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
        // binaryPanel.setUpdateListener(updateListener);
    }
}
