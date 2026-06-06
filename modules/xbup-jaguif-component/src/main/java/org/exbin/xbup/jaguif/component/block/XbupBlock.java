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

import org.exbin.xbup.jaguif.component.*;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTDocument;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * XBUP block tree document.
 */
@ParametersAreNonnullByDefault
public class XbupBlock {

    protected XbupTree xbupTree;
    @Nullable
    protected XBTBlock block;

    public XbupBlock(XBTDocument document) {
        this.xbupTree = new XbupTree(document);
    }

    @Nonnull
    public XbupTree getXbupTree() {
        return xbupTree;
    }

    public void setXbupTree(XbupTree xbupTree) {
        this.xbupTree = xbupTree;
    }

    @Nonnull
    public Optional<XBTBlock> getBlock() {
        return Optional.ofNullable(block);
    }

    public void setBlock(@Nullable XBTBlock block) {
        this.block = block;
    }

    @Nonnull
    public XBACatalog getCatalog() {
        return xbupTree.getCatalog();
    }

    public void setCatalog(XBACatalog catalog) {
        xbupTree.setCatalog(catalog);
    }

    @Nonnull
    public XBPluginRepository getPluginRepository() {
        return xbupTree.getPluginRepository();
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        xbupTree.setPluginRepository(pluginRepository);
    }
}
