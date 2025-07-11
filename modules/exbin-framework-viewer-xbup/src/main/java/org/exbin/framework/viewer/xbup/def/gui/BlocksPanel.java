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
package org.exbin.framework.viewer.xbup.def.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import org.exbin.framework.App;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.viewer.xbup.def.model.BlocksTableModel;
import org.exbin.framework.viewer.xbup.gui.BlocksTableItem;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Blocks table panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlocksPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BlocksPanel.class);
    private BlocksTableModel blocksTableModel = new BlocksTableModel();
    private final ToolBarSidePanel toolBarPanel = new ToolBarSidePanel();

    public BlocksPanel() {
        initComponents();

        toolBarPanel.setToolBarPosition(ToolBarSidePanel.ToolBarPosition.RIGHT);
        toolBarPanel.add(blocksScrollPane, BorderLayout.CENTER);
        add(toolBarPanel, BorderLayout.CENTER);
    }

    public void setBlocksTableModel(BlocksTableModel blocksTableModel) {
        this.blocksTableModel = blocksTableModel;
        blocksTable.setModel(blocksTableModel);
    }

    @Nonnull
    public JTable getBlocksTable() {
        return blocksTable;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blocksScrollPane = new javax.swing.JScrollPane();
        blocksTable = new JTable(blocksTableModel) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                boolean result = super.editCellAt(row, column, e);
                final Component editor = getEditorComponent();
                if (editor == null || !(editor instanceof JTextComponent)) {
                    return result;
                }
                if (e instanceof MouseEvent) {
                    EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            ((JTextComponent) editor).selectAll();
                        }

                    });
                } else {
                    ((JTextComponent) editor).selectAll();
                }
                return result;
            }
        };

        blocksTable.setModel(blocksTableModel);
        blocksTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                blocksTablePropertyChange(evt);
            }
        });
        blocksScrollPane.setViewportView(blocksTable);

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    private void blocksTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_blocksTablePropertyChange
        blocksTable.repaint();
    }//GEN-LAST:event_blocksTablePropertyChange

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new BlocksPanel());
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane blocksScrollPane;
    private javax.swing.JTable blocksTable;
    // End of variables declaration//GEN-END:variables

    public void setCatalog(XBACatalog catalog) {
    }

    public void addActions(ActionsProvider actionsProvider) {
        toolBarPanel.addActions(actionsProvider);
    }

    public void setPanelPopup(JPopupMenu popupMenu) {
        blocksTable.setComponentPopupMenu(popupMenu);
    }

    public void addSelectionListener(ListSelectionListener listSelectionListener) {
        blocksTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }
    
    @Nonnull
    public JToolBar getToolBar() {
        return toolBarPanel.getToolBar();
    }

    @Nullable
    public BlocksTableItem getSelectedRow() {
        int selectedRow = blocksTable.getSelectedRow();
        if (selectedRow >= 0) {
            return blocksTableModel.getRow(selectedRow);
        }
        return null;
    }
}
