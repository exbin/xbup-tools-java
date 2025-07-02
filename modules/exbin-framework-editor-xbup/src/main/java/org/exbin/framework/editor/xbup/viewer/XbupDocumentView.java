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
package org.exbin.framework.editor.xbup.viewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.editor.xbup.viewer.gui.XBDocumentPanel;
import org.exbin.framework.ui.api.Document;
import org.exbin.framework.ui.api.DocumentView;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * XBUP document viewer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupDocumentView implements DocumentView {

    private XbupTreeDocument treeDocument;

    private final XBDocumentPanel documentPanel = new XBDocumentPanel();

    private final List<BlockViewer> blockViewers = new ArrayList<>();
    private final DocumentViewer documentViewer = new DocumentViewer();
    private final StructureViewer structureViewer = new StructureViewer();
    private final PropertiesViewer propertiesViewer = new PropertiesViewer();
    private final TextualViewer textualViewer = new TextualViewer();
    private final BinaryViewer binaryViewer = new BinaryViewer();

    public XbupDocumentView() {
        init();
    }

    private void init() {
        blockViewers.add(documentViewer);
        blockViewers.add(structureViewer);
        blockViewers.add(propertiesViewer);
        blockViewers.add(textualViewer);
        blockViewers.add(binaryViewer);

        for (BlockViewer blockViewer : blockViewers) {
            documentPanel.addBlockViewer(blockViewer);
        }

        documentPanel.addTabChangedListener(() -> {
            BlockViewer activeViewer = documentPanel.getActiveViewer().orElse(null);
            if (activeViewer instanceof StructureViewer) {
                ((StructureViewer) activeViewer).postWindowOpened();
            }
        });
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return documentPanel;
    }

    @Nonnull
    @Override
    public Document getDocument() {
        return treeDocument;
    }

    @Override
    public void setDocument(Document document) {
        treeDocument = (XbupTreeDocument) document;
    }

    public void postWindowOpened() {
        structureViewer.postWindowOpened();
    }

    public void setCatalog(XBACatalog catalog) {
        blockViewers.forEach(blockViewer -> {
            blockViewer.setCatalog(catalog);
        });
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        documentPanel.setPluginRepository(pluginRepository);
        blockViewers.forEach(blockViewer -> {
            blockViewer.setPluginRepository(pluginRepository);
        });
    }

    @Nonnull
    public Optional<BlockViewer> getCurrentViewer() {
        return documentPanel.getActiveViewer();
    }

    @Nonnull
    public XbupTreeDocument getTreeDocument() {
        return Objects.requireNonNull(treeDocument);
    }

    public void setTreeDocument(XbupTreeDocument treeDocument) {
        this.treeDocument = treeDocument;
        structureViewer.setTreeDocument(treeDocument);
    }
    
    public void setBlock(XBTBlock block) {
        blockViewers.forEach(blockViewer -> {
            blockViewer.setBlock(block);
        });
        documentPanel.setBlock((XBTTreeNode) block);
    }
    
    public void setUndoHandler(UndoRedo undoRedo) {
        documentViewer.setUndoHandler(undoRedo);
        structureViewer.setUndoHandler(undoRedo);
    }

    public void setAddressText(String addressText) {
        documentPanel.setAddressText(addressText);
    }

    public boolean isEditable() {
        return true;
    }

    public void setDevMode(boolean devMode) {
        propertiesViewer.setDevMode(devMode);
    }

    public void notifyFileChanged() {
        XBTBlock block = treeDocument.getRootBlock().orElse(null);
        structureViewer.reportStructureChange(block);
        documentPanel.setBlock((XBTTreeNode) block);
    }

    public void addItemSelectionListener(DocumentItemSelectionListener listener) {
        structureViewer.addItemSelectionListener(listener);
    }

    public void removeItemSelectionListener(DocumentItemSelectionListener listener) {
        structureViewer.removeItemSelectionListener(listener);
    }

    @Nonnull
    public Optional<XBTBlock> getSelectedItem() {
        return structureViewer.getSelectedItem();
    }
}
