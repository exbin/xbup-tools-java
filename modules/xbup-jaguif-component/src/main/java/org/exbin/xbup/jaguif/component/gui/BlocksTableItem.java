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
package org.exbin.xbup.jaguif.component.gui;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.exbin.xbup.plugin.XBRowEditor;

/**
 * Blocks list table item record.
 */
@NullMarked
public class BlocksTableItem {

    private String valueName;
    private String typeName;
    private XBRowEditor rowEditor;

    public BlocksTableItem(String valueName, String typeName, @Nullable XBRowEditor rowEditor) {
        this.valueName = valueName;
        this.typeName = typeName;
        this.rowEditor = rowEditor;
    }

    public BlocksTableItem(String name, String type) {
        this(name, type, null);
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Nullable
    public XBRowEditor getRowEditor() {
        return rowEditor;
    }

    public void setRowEditor(@Nullable XBRowEditor rowEditor) {
        this.rowEditor = rowEditor;
    }
}
