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
package org.exbin.framework.viewer.xbup.def.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.table.AbstractTableModel;
import org.exbin.framework.App;
import org.exbin.framework.viewer.xbup.gui.BlocksTableItem;
import org.exbin.framework.language.api.LanguageModuleApi;

/**
 * Blocks list table model for item editing.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlocksTableModel extends AbstractTableModel {

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BlocksTableModel.class);
    private List<BlocksTableItem> blocks;

    private final String[] columnNames;
    private Class[] columnTypes = new Class[]{
        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
    };
    private final boolean[] columnsEditable = new boolean[]{false, false, false, true};

    public BlocksTableModel() {
        columnNames = new String[]{
            resourceBundle.getString("itemOrder"),
            resourceBundle.getString("itemName"),
            resourceBundle.getString("itemType"),
            resourceBundle.getString("itemValue")
        };
        blocks = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return blocks.size();
    }

    public BlocksTableItem getRow(int index) {
        return blocks.get(index);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getTypes()[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnsEditable[columnIndex];
    }

    @Nonnull
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return rowIndex;
            case 1:
                return getParameter(rowIndex).getValueName();
            case 2:
                return getParameter(rowIndex).getTypeName();
            case 3:
                return "";
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < getRowCount()) {
            if (columnIndex == 3) {
                // ((UBNat32) parameters.get(rowIndex)).setValue((Integer) aValue);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Nonnull
    public List<BlocksTableItem> getParameters() {
        return blocks;
    }

    public void setParameters(List<BlocksTableItem> attributes) {
        this.blocks = attributes;
    }

    @Nonnull
    public Class[] getTypes() {
        return columnTypes;
    }

    public void setTypes(Class[] types) {
        this.columnTypes = types;
    }

    @Nullable
    public BlocksTableItem getParameter(int index) {
        if (index >= blocks.size()) {
            return null;
        }

        return blocks.get(index);
    }

    public void clear() {
        blocks.clear();
    }

    public void addRow(BlocksTableItem item) {
        blocks.add(item);
    }

    public boolean isEmpty() {
        return blocks == null || blocks.isEmpty();
    }
}
