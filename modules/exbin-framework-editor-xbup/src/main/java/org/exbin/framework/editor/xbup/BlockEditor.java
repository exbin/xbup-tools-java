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
package org.exbin.framework.editor.xbup;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.App;
import org.exbin.framework.editor.xbup.viewer.XbupDocumentView;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Block editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlockEditor {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(BlockEditor.class);

    private final XbupDocumentView documentViewer = new XbupDocumentView();
    private XBTTreeNode block;

    public BlockEditor() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setCatalog(XBACatalog catalog) {
        documentViewer.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        documentViewer.setPluginRepository(pluginRepository);
    }

    @Nonnull
    public JComponent getPanel() {
        return documentViewer.getComponent();
    }

    public XBTTreeNode getBlock() {
        return block;
    }

    public void setBlock(XBTTreeNode block) {
        this.block = block.cloneNode(true);

        documentViewer.setBlock(block);
    }
}
