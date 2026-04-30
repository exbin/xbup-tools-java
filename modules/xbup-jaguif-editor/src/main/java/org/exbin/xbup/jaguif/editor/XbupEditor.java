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
package org.exbin.xbup.jaguif.editor;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.jaguif.App;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.jaguif.viewer.XbupDataComponent;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Block editor.
 */
@ParametersAreNonnullByDefault
public class XbupEditor {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupEditor.class);

    private final XbupDataComponent component = new XbupDataComponent();
    private XBTTreeNode block;

    public XbupEditor() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setCatalog(XBACatalog catalog) {
        component.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        component.setPluginRepository(pluginRepository);
    }

    @Nonnull
    public JComponent getPanel() {
        return component.getComponent();
    }

    public XBTTreeNode getBlock() {
        return block;
    }

    public void setBlock(XBTTreeNode block) {
        this.block = block.cloneNode(true);

        component.setBlock(block);
    }
}
