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
import org.exbin.jaguif.action.api.clipboard.TextClipboardOperationController;
import org.exbin.jaguif.context.api.ContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;
import org.exbin.xbup.core.block.XBTDocument;
import org.exbin.xbup.jaguif.component.XbupComponent;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.gui.BinaryToolbarPanel;
import org.exbin.xbup.parser_tree.XBTTreeDocument;

/**
 * Binary viewer of document.
 */
@ParametersAreNonnullByDefault
public class BinaryPage extends AbstractTabPagesComponent implements XbupViewerPage, TextClipboardOperationController {

    public static final String PAGE_ID = "binary";

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BinaryPage.class);
    protected final JPanel wrapperPanel = new JPanel(new BorderLayout());
    protected final BinEdDataComponent binaryComponent = new BinEdDataComponent(new BinEdComponentPanel());
    protected final BinaryToolbarPanel binaryToolbarPanel = new BinaryToolbarPanel();
    protected XbupTree xbupTree = null;

    protected GoToPositionAction goToPositionAction;
    protected EncodingsManager encodingsManager;
    protected ClipboardCodeActions clipboardCodeActions;

    public BinaryPage() {
        init();
    }

    private void init() {
        putValue(KEY_ID, PAGE_ID);
        putValue(KEY_NAME, resourceBundle.getString("page.name"));
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource(resourceBundle.getString("page.icon"))));
        putValue(KEY_CONTEXT_CHANGE, new ContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(XbupComponent.class, (instance) -> {
                    setXbupTree(instance.getXbupTree());
                });
            }
        });
        wrapperPanel.add(binaryComponent.getComponent(), BorderLayout.CENTER);
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
    public void setXbupTree(XbupTree xbupTree) {
        if (xbupTree == this.xbupTree) {
            return;
        }

        XBTDocument document = xbupTree.getDocument();
        if (document instanceof XBTTreeDocument) {
            ByteArrayEditableData byteArrayData = new ByteArrayEditableData();
            try (OutputStream dataOutputStream = byteArrayData.getDataOutputStream()) {
                ((XBTTreeDocument) document).toStreamUB(dataOutputStream);
            } catch (IOException ex) {
                Logger.getLogger(BinaryPage.class.getName()).log(Level.SEVERE, null, ex);
            }

            BinEdComponentPanel binaryComponentPanel = (BinEdComponentPanel) binaryComponent.getComponent();
            binaryComponentPanel.setContentData(byteArrayData);
        }

        this.xbupTree = xbupTree;
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
