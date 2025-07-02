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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.exbin.framework.viewer.xbup.viewer.gui.XBStructurePanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Structure viewer/editor of document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class StructureViewer implements BlockViewer {

    private final XBStructurePanel structurePanel = new XBStructurePanel();
    private XBTBlock selectedItem = null;
    private DocumentViewer documentViewer;

    private final List<BlockViewer> blockViewers = new ArrayList<>();

    public StructureViewer() {
        init();
    }

    private void init() {
        documentViewer = new DocumentViewer();
        blockViewers.add(documentViewer);
        blockViewers.add(new PropertiesViewer());
        blockViewers.add(new TextualViewer());
        blockViewers.add(new BinaryViewer());

        structurePanel.addItemSelectionListener((item) -> {
            this.selectedItem = item;
            String itemPath;
            if (selectedItem != null) {
                if (!selectedItem.getParentBlock().isPresent()) {
                    itemPath = "/";
                } else {
                    StringBuilder builder = new StringBuilder();

                    Optional<XBTBlock> parentItem;
                    XBTBlock pathItem = selectedItem;
                    do {
                        parentItem = pathItem.getParentBlock();
                        if (parentItem.isPresent()) {
                            builder.insert(0, "/" + System.identityHashCode(pathItem));
                            pathItem = parentItem.get();
                        }
                    } while (parentItem.isPresent());
                    itemPath = builder.toString();
                }
            } else {
                itemPath = "";
            }
            structurePanel.setAddressText(itemPath);
        });

        for (BlockViewer blockViewer : blockViewers) {
            structurePanel.addPreviewViewer(blockViewer);
        }
    }

    @Override
    public void setCatalog(XBACatalog catalog) {
        structurePanel.setCatalog(catalog);

        blockViewers.forEach(blockViewer -> {
            blockViewer.setCatalog(catalog);
        });
    }

    @Override
    public void setPluginRepository(XBPluginRepository pluginRepository) {
        blockViewers.forEach(blockViewer -> {
            blockViewer.setPluginRepository(pluginRepository);
        });
    }

    public void setUndoHandler(UndoRedo undoRedo) {
        documentViewer.setUndoHandler(undoRedo);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Structure";
    }

    @Nonnull
    @Override
    public Optional<ImageIcon> getIcon() {
        return Optional.of(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/viewer/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/list.png")));
    }

    @Override
    public void setBlock(@Nullable XBTBlock block) {
        
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return structurePanel;
    }

    public void setTreeDocument(XbupTreeDocument treeDocument) {
        structurePanel.setTreeDocument(treeDocument);
    }

    public void postWindowOpened() {
        structurePanel.postWindowOpened();
    }

    public void reportStructureChange(XBTBlock block) {
        structurePanel.reportStructureChange(block);
    }

    public void addItemSelectionListener(DocumentItemSelectionListener listener) {
        structurePanel.addItemSelectionListener(listener);
    }

    public void removeItemSelectionListener(DocumentItemSelectionListener listener) {
        structurePanel.removeItemSelectionListener(listener);
    }

    @Nonnull
    public Optional<XBTBlock> getSelectedItem() {
        return structurePanel.getSelectedItem();
    }
}
