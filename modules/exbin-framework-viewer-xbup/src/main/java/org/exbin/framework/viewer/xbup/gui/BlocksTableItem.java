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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.plugin.XBRowEditor;

/**
 * Blocks list table item record.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
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

    @Nonnull
    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Nonnull
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
