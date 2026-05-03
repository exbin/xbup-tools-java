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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.jaguif.context.api.ContextComponent;
import org.exbin.xbup.jaguif.viewer.page.gui.XBDocumentPanel;
import org.exbin.jaguif.utils.ComponentProvider;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.viewer.page.BinaryPage;
import org.exbin.xbup.jaguif.viewer.page.PluginUiPage;
import org.exbin.xbup.jaguif.viewer.page.PropertiesPage;
import org.exbin.xbup.jaguif.viewer.page.StructurePage;
import org.exbin.xbup.jaguif.viewer.page.TextualPage;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.xbup.jaguif.viewer.page.XbupViewerPage;

/**
 * XBUP document viewer.
 */
@ParametersAreNonnullByDefault
public class XbupDataComponent implements ContextComponent, ComponentProvider {

    private XbupTree treeDocument;

    private final XBDocumentPanel documentPanel = new XBDocumentPanel();

    private final List<XbupViewerPage> blockViewers = new ArrayList<>();
    private final PluginUiPage documentViewer = new PluginUiPage();
    private final StructurePage structureViewer = new StructurePage();
    private final PropertiesPage propertiesViewer = new PropertiesPage();
    private final TextualPage textualViewer = new TextualPage();
    private final BinaryPage binaryViewer = new BinaryPage();

    public XbupDataComponent() {
        init();
    }

    private void init() {
        blockViewers.add(documentViewer);
        blockViewers.add(structureViewer);
        blockViewers.add(propertiesViewer);
        blockViewers.add(textualViewer);
        blockViewers.add(binaryViewer);

        for (XbupViewerPage blockViewer : blockViewers) {
            documentPanel.addBlockViewer(blockViewer);
        }

        documentPanel.addTabChangedListener(() -> {
            XbupViewerPage activeViewer = documentPanel.getActiveViewer().orElse(null);
            if (activeViewer instanceof StructurePage) {
                ((StructurePage) activeViewer).postWindowOpened();
            }
        });
    }

    @Nonnull
    public JComponent getComponent() {
        return documentPanel;
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
    public Optional<XbupViewerPage> getCurrentViewer() {
        return documentPanel.getActiveViewer();
    }

    @Nonnull
    public XbupTree getTreeDocument() {
        return Objects.requireNonNull(treeDocument);
    }

    public void setTreeDocument(XbupTree treeDocument) {
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

    @Nonnull
    public Optional<XBTBlock> getSelectedItem() {
        return structureViewer.getSelectedItem();
    }
}
