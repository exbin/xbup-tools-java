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
package org.exbin.xbup.jaguif.component.block;

import java.awt.Component;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.jaguif.context.api.ContextComponent;
import org.exbin.jaguif.utils.ComponentProvider;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * XBUP document viewer.
 */
@ParametersAreNonnullByDefault
public class XbupBlockComponent implements ContextComponent, ComponentProvider {

    protected XbupTree treeDocument;
    protected XBTBlock block;

    public XbupBlockComponent() {
    }

    @Override
    public Component getComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    public XbupTree getTreeDocument() {
        return treeDocument;
    }

    public void setTreeDocument(XbupTree treeDocument) {
        this.treeDocument = treeDocument;
    }

    @Nonnull
    public XBTBlock getBlock() {
        return block;
    }

    public void setBlock(XBTBlock block) {
        this.block = block;
    }

    @Nonnull
    public XBACatalog getCatalog() {
        return treeDocument.getCatalog();
    }

    public void setCatalog(XBACatalog catalog) {
        treeDocument.setCatalog(catalog);
    }

    @Nonnull
    public XBPluginRepository getPluginRepository() {
        return treeDocument.getPluginRepository();
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        treeDocument.setPluginRepository(pluginRepository);
    }
}
