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
package org.exbin.framework.editor.xbup.def.gui;

import java.awt.BorderLayout;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.framework.App;
import org.exbin.framework.bined.BinEdFileHandler;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.bined.gui.BinEdComponentPanel;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.editor.xbup.BinaryDataWrapperUndoHandler;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.operation.undo.UndoRedo;

/**
 * Binary data panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BinaryDataPanel.class);
    private final ToolBarSidePanel toolBarPanel = new ToolBarSidePanel();

    private BinEdComponentPanel componentPanel = null;
    private JPopupMenu dataPopupMenu;
    private UndoRedo undoRedo;

    public BinaryDataPanel() {
        initComponents();
        toolBarPanel.setToolBarPosition(ToolBarSidePanel.ToolBarPosition.RIGHT);
    }

    public void setUndoRedo(UndoRedo undoRedo) {
        this.undoRedo = undoRedo;
        if (componentPanel != null) {
            componentPanel.setUndoRedo(new BinaryDataWrapperUndoHandler(undoRedo));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new BinaryDataPanel());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    //
    public void addActions(ActionsProvider actionsProvider) {
        toolBarPanel.addActions(actionsProvider);
    }

    public void setDataPopupMenu(JPopupMenu popupMenu) {
        this.dataPopupMenu = popupMenu;
        if (componentPanel != null) {
            componentPanel.setPopupMenu(popupMenu);
        }
    }

    @Nonnull
    public BinEdComponentPanel getComponentPanel() {
        return componentPanel;
    }

    public void setFileHandler(BinEdFileHandler binaryDataFile) {
        if (componentPanel != null) {
            remove(componentPanel);
        }
        componentPanel = binaryDataFile.getComponent();
        toolBarPanel.add(componentPanel, BorderLayout.CENTER);
        toolBarPanel.revalidate();
        toolBarPanel.repaint();
        add(toolBarPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setContentData(BinaryData binaryData) {
        if (componentPanel instanceof BinEdComponentPanel) {
            ((BinEdComponentPanel) componentPanel).setContentData(binaryData);
            return;
        }

        if (componentPanel != null) {
            remove(componentPanel);
        }

        BinedModule binedModule = App.getModule(BinedModule.class);
        componentPanel = new BinEdComponentPanel();
        componentPanel.getCodeArea().setBorder(BorderFactory.createLoweredBevelBorder());
        binedModule.getFileManager().initComponentPanel(componentPanel);

        if (undoRedo != null) {
            componentPanel.setUndoRedo(new BinaryDataWrapperUndoHandler(undoRedo));
        }
        if (dataPopupMenu != null) {
            componentPanel.setPopupMenu(dataPopupMenu);
        }
        componentPanel.setContentData(binaryData);
        toolBarPanel.add(componentPanel, BorderLayout.CENTER);
        toolBarPanel.revalidate();
        toolBarPanel.repaint();
        add(toolBarPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
