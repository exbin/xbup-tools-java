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
package org.exbin.xbup.jaguif.component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTDocument;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * XBUP tree document.
 */
@ParametersAreNonnullByDefault
public class XbupTree implements XBTDocument {

    protected final XBTDocument document;
    protected XBACatalog catalog;
    protected XBPluginRepository pluginRepository;

    protected final Map<Long, String> captionCache = new HashMap<>();
    protected final Map<Long, ImageIcon> iconCache = new HashMap<>();

    public XbupTree(XBTDocument document) {
        this.document = document;
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> getRootBlock() {
        return document.getRootBlock();
    }

    @Override
    public long getDocumentSize() {
        return document.getDocumentSize();
    }

    @Nonnull
    @Override
    public Optional<InputStream> getTailData() {
        return document.getTailData();
    }

    @Override
    public long getTailDataSize() {
        return document.getTailDataSize();
    }

    @Nonnull
    public XBACatalog getCatalog() {
        return Objects.requireNonNull(catalog);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    @Nonnull
    public XBPluginRepository getPluginRepository() {
        return Objects.requireNonNull(pluginRepository);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    /**
     * Gets caption for given block type.
     *
     * Use cache if available.
     *
     * @param blockDecl block declaration
     * @return caption
     */
    @Nullable
    public String getBlockCaption(XBBlockDecl blockDecl) {
        if (blockDecl instanceof XBCBlockDecl) {
            XBCBlockSpec blockSpec = (XBCBlockSpec) ((XBCBlockDecl) blockDecl).getBlockSpecRev().getParent();
            if (captionCache.containsKey(blockSpec.getId())) {
                return captionCache.get(blockSpec.getId());
            }

            XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
            String caption = nameService.getDefaultText(blockSpec);
            captionCache.put(blockSpec.getId(), caption);
            return caption;
        } else if (blockDecl instanceof XBLBlockDecl) {
            // TOOD
            /* XBCBlockDecl blockDecl = (XBCBlockDecl) ((XBLBlockDecl) blockDecl).getBlockDecl();
             if (blockDecl != null) {
             XBCBlockSpec blockSpec = blockDecl.getBlockSpecRev().getParent();
             if (captionCache.containsKey(blockSpec.getId())) {
             return captionCache.get(blockSpec.getId());
             }

             XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
             String caption = nameService.getDefaultText(blockSpec);
             captionCache.put(blockSpec.getId(), caption);
             return caption;
             } */
        }

        return null;
    }

    /**
     * Gets icon for given block type.
     *
     * Use cache if available.
     *
     * @param blockDecl block declaration
     * @return icon
     */
    @Nullable
    public ImageIcon getBlockIcon(XBBlockDecl blockDecl) {
        if (blockDecl instanceof XBCBlockDecl) {
            XBCBlockSpec blockSpec = (XBCBlockSpec) ((XBCBlockDecl) blockDecl).getBlockSpecRev().getParent();
            if (iconCache.containsKey(blockSpec.getId())) {
                return iconCache.get(blockSpec.getId());
            }
            XBCXIconService iconService = catalog.getCatalogService(XBCXIconService.class);
            ImageIcon icon = iconService.getDefaultImageIcon(blockSpec);
            if (icon == null) {
                iconCache.put(blockSpec.getId(), icon);
                return null;
            }
            if (icon.getImage() == null) {
                return null;
            }
            icon = new ImageIcon(icon.getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
            iconCache.put(blockSpec.getId(), icon);
            return icon;
        } else if (blockDecl instanceof XBDeclBlockType) {
            // TODO
            /* XBCBlockDecl blockDecl = (XBCBlockDecl) ((XBDBlockType) blockDecl).getBlockDecl();
             if (blockDecl != null) {
             XBCBlockSpec blockSpec = blockDecl.getBlockSpecRev().getParent();
             if (iconCache.containsKey(blockSpec.getId())) {
             return iconCache.get(blockSpec.getId());
             }
             XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
             ImageIcon icon = iconService.getDefaultImageIcon(blockSpec);
             if (icon == null) {
             iconCache.put(blockSpec.getId(), icon);
             return null;
             }
             icon = new ImageIcon(icon.getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
             iconCache.put(blockSpec.getId(), icon);
             return icon;
             } */
        }

        return null;
    }
}
