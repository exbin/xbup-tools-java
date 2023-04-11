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
package org.exbin.framework.editor.xbup.def.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.table.AbstractTableModel;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Attributes list table model for item editing.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AttributesTableModel extends AbstractTableModel {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(AttributesTableModel.class);
    private List<XBAttribute> attributes;
    private ChangeListener changeListener = null;

    private final String[] columnNames;
    private Class[] columnTypes = new Class[]{
        java.lang.Integer.class, java.lang.Integer.class
    };
    private final boolean[] columnsEditable = new boolean[]{false, true};

    public AttributesTableModel() {
        columnNames = new String[]{
            resourceBundle.getString("itemOrder"),
            resourceBundle.getString("itemValue")
        };
        attributes = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return attributes.size();
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
        return columnsEditable[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            return getAttribs().get(rowIndex).convertToNatural().getInt();
        } else {
            return rowIndex;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < getRowCount()) {
            if (columnIndex == 1) {
                attributes.get(rowIndex).convertToNatural().setValue((Integer) aValue);
                fireDataChanged();
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Nonnull
    public List<XBAttribute> getAttribs() {
        return attributes;
    }

    public void setAttribs(List<XBAttribute> attributes) {
        this.attributes = attributes;
        fireTableDataChanged();
    }

    @Nonnull
    public Class[] getTypes() {
        return columnTypes;
    }

    public void setTypes(Class[] types) {
        this.columnTypes = types;
    }

    public int getAttribute(int index) {
        if (index >= attributes.size()) {
            return 0;
        }

        XBAttribute attribute = attributes.get(index);
        return attribute != null ? attribute.getNaturalInt() : 0;
    }

    public void fireDataChanged() {
        if (changeListener != null) {
            changeListener.valueChanged();
        }
    }

    public void attachChangeListener(ChangeListener listener) {
        changeListener = listener;
    }

    public interface ChangeListener {

        void valueChanged();
    }
}
