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
package org.exbin.xbup.jaguif.editor.block;

import java.util.Optional;
import java.util.ResourceBundle;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.swing.JComponent;
import org.exbin.jaguif.App;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.block.XbupBlock;
import org.exbin.xbup.jaguif.component.page.XbupPagesPanel;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Block editor.
 */
@NullMarked
public class XbupBlockEditor {

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupBlockEditor.class);
    protected final XbupPagesPanel editorComponent = new XbupPagesPanel();
    protected XbupTree xbupTree;
    @Nullable
    protected XbupBlock xbupBlock;

    public XbupBlockEditor() {
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setCatalog(XBACatalog catalog) {
        xbupTree.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        xbupTree.setPluginRepository(pluginRepository);
    }

    public void setXbupTree(XbupTree xbupTree) {
        this.xbupTree = xbupTree;
    }

    public JComponent getComponent() {
        return editorComponent;
    }

    public Optional<XBTBlock> getBlock() {
        return xbupBlock == null ? Optional.empty() : Optional.of(xbupBlock.getBlock());
    }

    public void setBlock(@Nullable XBTBlock block) {
        xbupBlock = block == null ? null : new XbupBlock(xbupTree, block);
    }
}
