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
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.editor.xbup.def.model.AttributesTableModel;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Attributes table panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AttributesPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(AttributesPanel.class);
    private AttributesTableModel attributesTableModel = new AttributesTableModel();
    private final ToolBarSidePanel toolBarPanel = new ToolBarSidePanel();

    public AttributesPanel() {
        initComponents();

        toolBarPanel.setToolBarPosition(ToolBarSidePanel.ToolBarPosition.RIGHT);
        add(toolBarPanel, BorderLayout.EAST);
    }

    public void setAttributesTableModel(AttributesTableModel attributesTableModel) {
        this.attributesTableModel = attributesTableModel;
        attributesTable.setModel(attributesTableModel);
    }

    @Nonnull
    public JTable getAttributesTable() {
        return attributesTable;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        attributesScrollPane = new javax.swing.JScrollPane();
        attributesTable = new JTable(attributesTableModel) {
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

        setLayout(new java.awt.BorderLayout());

        attributesTable.setModel(attributesTableModel);
        attributesTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                attributesTablePropertyChange(evt);
            }
        });
        attributesScrollPane.setViewportView(attributesTable);

        add(attributesScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void attributesTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_attributesTablePropertyChange
        attributesTable.repaint();
    }//GEN-LAST:event_attributesTablePropertyChange

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new AttributesPanel());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane attributesScrollPane;
    private javax.swing.JTable attributesTable;
    // End of variables declaration//GEN-END:variables

    public void setCatalog(XBACatalog catalog) {
    }

    public void addActions(ActionsProvider actionsProvider) {
        toolBarPanel.addActions(actionsProvider);
    }

    public void setPanelPopup(JPopupMenu popupMenu) {
        attributesTable.setComponentPopupMenu(popupMenu);
    }

    public void addSelectionListener(EditItemActionsUpdateListener updateListener) {
        attributesTable.getSelectionModel().addListSelectionListener((e) -> {
            updateListener.stateChanged();
        });
    }

    @Nullable
    public XBAttribute getSelectedRow() {
        int selectedRow = attributesTable.getSelectedRow();
        if (selectedRow >= 0) {
            return attributesTableModel.getAttribs().get(selectedRow);
        }
        return null;
    }
}
