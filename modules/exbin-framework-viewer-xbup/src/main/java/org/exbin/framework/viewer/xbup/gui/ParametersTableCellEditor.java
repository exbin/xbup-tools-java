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
package org.exbin.framework.viewer.xbup.gui;

import org.exbin.framework.viewer.xbup.def.model.ParametersTableModel;
import java.awt.Component;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.xbup.plugin.XBRowEditor;

/**
 * Parameters table cell renderer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ParametersTableCellEditor extends DefaultCellEditor {

    private XBACatalog catalog;
    private final XBPluginRepository pluginRepository;

    private XBTTreeNode node;
    private XBRowEditor lineEditor = null;
    private JComponent lineEditorComponent = null;

    public ParametersTableCellEditor(XBACatalog catalog, XBPluginRepository pluginRepository, XBTTreeNode node) {
        super(new JTextField());
        super.setClickCountToStart(0);
        this.catalog = catalog;
        this.pluginRepository = pluginRepository;
        this.node = node;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ParametersTableItem tableItem = ((ParametersTableModel) table.getModel()).getRow(row);
        lineEditor = tableItem.getRowEditor();
        lineEditorComponent = lineEditor == null ? null : lineEditor.getEditor();
        XBPropertyTableCellPanel cellPanel;
        if (lineEditorComponent == null) {
            JComponent defaultComponent = (JComponent) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            defaultComponent.setEnabled(false);
            cellPanel = new XBPropertyTableCellPanel(defaultComponent, catalog, pluginRepository, node, row);
        } else {
            cellPanel = new XBPropertyTableCellPanel(lineEditorComponent, catalog, pluginRepository, node, row);
        }
        cellPanel.setBackground(table.getSelectionBackground());
        cellPanel.getCellComponent().setBorder(null);
        return cellPanel;
    }

    @Override
    public boolean stopCellEditing() {
        if (lineEditor != null) {
            try {
                lineEditor.finishEditor();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editorComponent, ex.toString(), "Cell Input", JOptionPane.ERROR_MESSAGE);
            }
        }

        return super.stopCellEditing();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setNode(XBTTreeNode node) {
        this.node = node;
    }
}
