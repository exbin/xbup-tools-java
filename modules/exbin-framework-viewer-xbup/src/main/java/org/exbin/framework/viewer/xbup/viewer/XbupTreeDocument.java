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
package org.exbin.framework.viewer.xbup.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import org.exbin.framework.file.api.FileType;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.operation.Operation;
import org.exbin.xbup.operation.XBTDocOperation;
import org.exbin.xbup.operation.undo.XBTLinearUndo;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * XBUP tree document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupTreeDocument implements XbupDocument {

    private final XBTTreeDocument treeDocument = new XBTTreeDocument();
    private UndoRedo undoRedo;

    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;

    private final Map<Long, String> captionCache = new HashMap<>();
    private final Map<Long, ImageIcon> iconCache = new HashMap<>();

    public XbupTreeDocument() {
        undoRedo = new XBTLinearUndo(treeDocument);
    }

    @Nonnull
    @Override
    public XBACatalog getCatalog() {
        return Objects.requireNonNull(catalog);
    }

    @Nonnull
    @Override
    public XBPluginRepository getPluginRepository() {
        return Objects.requireNonNull(pluginRepository);
    }

    @Nonnull
    public UndoRedo getUndoRedo() {
        return undoRedo;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        treeDocument.setCatalog(catalog);
        treeDocument.processSpec();
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }
/*
    @Override
    public void notifyChange(OperationEvent event) {
        Operation operation = event.getOperation();
        // TODO Consolidate
//        processSpec();
//        notifyFileChanged();
        // getDoc().setModified(true);
//            updateItem();
//            updateActionStatus(null);
//            if (clipboardActionsUpdateListener != null) {
//                clipboardActionsUpdateListener.stateChanged();
//            }

        if (operation instanceof XBTDocOperation) {
            // setSelectedTab(ViewerTab.VIEW);
        } else {
            // TODO
        }
    } */
    
//    @Override
//    public boolean canPaste() {
//        Clipboard clipboard = ClipboardUtils.getClipboard();
//        return clipboard.isDataFlavorAvailable(XBDocTreeTransferHandler.XB_DATA_FLAVOR);
//    }

    @Nullable
    public XBTTreeNode getRoot() {
        return treeDocument.getRoot();
    }

    @Nonnull
    public Optional<XBTBlock> getRootBlock() {
        return treeDocument.getRootBlock();
    }

    public void loadFromResourcePath(Class<?> classInstance, String resourcePath) throws IOException {
        treeDocument.fromStreamUB(classInstance.getResourceAsStream(resourcePath));
        treeDocument.processSpec();
        undoRedo.clear();
    }

    public void newFile() {
        treeDocument.clear();
        undoRedo.clear();
    }

    public void loadFromFile(URI fileUri, FileType fileType) throws FileNotFoundException, IOException {
        File file = new File(fileUri);
        FileInputStream fileStream = new FileInputStream(file);
        treeDocument.fromStreamUB(fileStream);
        treeDocument.processSpec();
        undoRedo.clear();
    }

    public void saveToFile(URI fileUri, FileType fileType) throws IOException {
        File file = new File(fileUri);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        treeDocument.toStreamUB(fileOutputStream);
        treeDocument.setModified(false);
        undoRedo.setSyncPosition();
    }

    public boolean wasModified() {
        return treeDocument.wasModified();
    }
    
    @Nonnull
    @Override
    public XBTTreeDocument getDocument() {
        return treeDocument;
    }

    public void notifyItemModified(XBTTreeNode block) {
        treeDocument.setModified(true);
        treeDocument.processSpec();
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
