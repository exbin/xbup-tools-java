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
package org.exbin.xbup.jaguif.editor.page.gui;

import java.awt.Component;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Block table name cell renderer.
 */
@ParametersAreNonnullByDefault
public class XBBlockNameTableCellRenderer extends DefaultTableCellRenderer {

    private final ImageIcon dataBlockIcon = new ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/editor/resources/icons/16px/data-block.png"));
    private final Icon directoryIcon = UIManager.getIcon("FileView.directoryIcon");

    private XbupTree treeDocument;

    public XBBlockNameTableCellRenderer() {
    }

    @Nonnull
    @Override
    public Component getTableCellRendererComponent(@Nullable JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel component = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table == null) {
            return component;
        }

        XBBlockTableModel tableModel = (XBBlockTableModel) table.getModel();
        XBTBlock block = tableModel.getRowAt(row);

        Icon icon = null;
        if (block != null && block.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            icon = dataBlockIcon;
        } else if (block instanceof XBTTreeNode) {
            icon = treeDocument.getBlockIcon(((XBTTreeNode) block).getBlockDecl());
        }

        component.setIcon(icon == null ? directoryIcon : icon);

        return component;
    }

    public void setTreeDocument(XbupTree treeDocument) {
        this.treeDocument = treeDocument;
    }
}
