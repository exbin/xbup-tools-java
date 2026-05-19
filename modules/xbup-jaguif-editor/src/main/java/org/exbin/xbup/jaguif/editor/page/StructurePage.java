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
package org.exbin.xbup.jaguif.editor.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.exbin.xbup.jaguif.editor.page.gui.XBStructurePanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.block.XbupBlockTree;
import org.exbin.xbup.operation.undo.UndoRedo;

/**
 * Structure viewer/editor of document.
 */
@ParametersAreNonnullByDefault
public class StructurePage implements XbupEditorBlockPage {

    private final XBStructurePanel structurePanel = new XBStructurePanel();
    private XbupBlockTree xbupBlockTree;
    private PluginUiPage documentViewer;

    private final List<XbupEditorBlockPage> blockViewers = new ArrayList<>();

    public StructurePage() {
        init();
    }

    private void init() {
        documentViewer = new PluginUiPage();
        blockViewers.add(documentViewer);
        blockViewers.add(new PropertiesPage());
        blockViewers.add(new TextualPage());
        blockViewers.add(new BinaryPage());

        // TODO
        /* structurePanel.addItemSelectionListener((item) -> {
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
        }); */

        for (XbupEditorBlockPage blockViewer : blockViewers) {
            structurePanel.addPreviewViewer(blockViewer);
        }
    }

    @Override
    public void setDocumentTree(XbupBlockTree xbupBlockTree) {
        this.xbupBlockTree = xbupBlockTree;
        structurePanel.setCatalog(xbupBlockTree.getCatalog());
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
        return Optional.of(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/editor/resources/icons/16px/list.png")));
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return structurePanel;
    }

    public void setTreeDocument(XbupTree treeDocument) {
        structurePanel.setTreeDocument(treeDocument);
    }

    public void postWindowOpened() {
        structurePanel.postWindowOpened();
    }

    public void reportStructureChange(XBTBlock block) {
        structurePanel.reportStructureChange(block);
    }

    @Nonnull
    public Optional<XBTBlock> getSelectedItem() {
        return structurePanel.getSelectedItem();
    }
}
