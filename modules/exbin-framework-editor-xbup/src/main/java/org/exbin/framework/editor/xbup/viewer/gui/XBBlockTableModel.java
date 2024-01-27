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

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.table.AbstractTableModel;
import org.exbin.framework.App;
import org.exbin.framework.editor.xbup.viewer.XbupTreeDocument;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Child blocks table model for item editing.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBBlockTableModel extends AbstractTableModel {

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XBBlockTableModel.class);
    private XbupTreeDocument treeDocument;
    private XBTBlock block;

    private final String[] columnNames;
    private final Class[] columnTypes = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class
    };

    public XBBlockTableModel() {
        columnNames = new String[]{"Name", "Block Type", "Size"};
    }

    public void setTreeDocument(XbupTreeDocument treeDocument) {
        this.treeDocument = treeDocument;
    }

    public void setBlock(@Nullable XBTBlock block) {
        this.block = block;
    }

    @Override
    public int getRowCount() {
        if (block == null) {
            return 0;
        }

        return block.getChildrenCount() + 1;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Nonnull
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Nonnull
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getTypes()[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Nullable
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0) {
            switch (columnIndex) {
                case 0:
                    return "..";
                case 1:
                case 2:
                    return "";
                default:
                    return "";
            }
        }

        if (treeDocument == null) {
            return "";
        }

        XBTBlock childBlock = block.getChildAt(rowIndex - 1);
        if (childBlock == null) {
            return "";
        }

        switch (columnIndex) {
            case 0: {
                if (childBlock.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                    return "Data Block";
                }

                if (childBlock instanceof XBTTreeNode) {
                    return treeDocument.getBlockCaption(((XBTTreeNode) childBlock).getBlockDecl());
                }

                return "Node (" + (rowIndex - 1) + ")";
            }
            case 1: {
                if (childBlock.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                    return "Data";
                }

                return "Node";
            }
            case 2: {
                if (childBlock instanceof XBTTreeNode) {
                    return ((XBTTreeNode) childBlock).getSizeUB();
                }

                return "-";
            }
            default:
                return "";
        }
    }

    @Nullable
    public XBTBlock getRowAt(int rowIndex) {
        if (rowIndex == 0) {
            return block;
        }

        return block.getChildAt(rowIndex - 1);
    }

    @Nonnull
    public Class[] getTypes() {
        return columnTypes;
    }
}
