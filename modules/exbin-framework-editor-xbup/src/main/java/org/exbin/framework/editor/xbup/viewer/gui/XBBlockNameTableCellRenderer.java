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
package org.exbin.framework.editor.xbup.viewer.gui;

import java.awt.Component;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.exbin.framework.editor.xbup.viewer.XbupTreeDocument;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Block table name cell renderer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBBlockNameTableCellRenderer extends DefaultTableCellRenderer {

    private XbupTreeDocument treeDocument;
    private XBTTreeNode block;

    public XBBlockNameTableCellRenderer() {
    }

    @Nonnull
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        XBPropertyTableItem tableItem = ((XBPropertyTableModel) table.getModel()).getRow(row);
//        JComponent component = tableItem.getRowEditor() == null ? null : tableItem.getRowEditor().getViewer();
//        XBPropertyTableCellPanel cellPanel = component == null ? new XBPropertyTableCellPanel(catalog, pluginRepository, block, doc, row) : new XBPropertyTableCellPanel(component, catalog, pluginRepository, block, doc, row);
//        cellPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
//        cellPanel.getCellComponent().setBorder(null);
//        return cellPanel;
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    public void setTreeDocument(XbupTreeDocument treeDocument) {
        this.treeDocument = treeDocument;
    }

    public void setBlock(XBTBlock block) {
        this.block = (XBTTreeNode) block;
    }
}
