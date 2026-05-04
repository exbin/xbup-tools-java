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
package org.exbin.xbup.jaguif.viewer;

import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.jaguif.App;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.block.XbupBlockComponent;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Block viewer.
 */
@ParametersAreNonnullByDefault
public class XbupViewer {

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupViewer.class);

    protected final XbupBlockComponent documentComponent = new XbupBlockComponent();

    public XbupViewer() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setCatalog(XBACatalog catalog) {
        documentComponent.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        documentComponent.setPluginRepository(pluginRepository);
    }
    
    public void setTreeDocument(XbupTree xbupTree) {
        documentComponent.setTreeDocument(xbupTree);
    }

    @Nonnull
    public JComponent getComponent() {
        return documentComponent.getComponent();
    }

    @Nonnull
    public Optional<XBTBlock> getBlock() {
        return documentComponent.getBlock();
    }

    public void setBlock(@Nullable XBTBlock block) {
        documentComponent.setBlock(block);
    }
}
