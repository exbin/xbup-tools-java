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
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.viewer.page.BinaryBlockPage;
import org.exbin.xbup.jaguif.viewer.page.PluginUiBlockPage;
import org.exbin.xbup.jaguif.viewer.page.PropertiesBlockPage;
import org.exbin.xbup.jaguif.viewer.page.StructurePage;
import org.exbin.xbup.jaguif.viewer.page.TextualBlockPage;
import org.exbin.xbup.jaguif.viewer.page.XbupViewerBlockPage;
import org.exbin.xbup.operation.undo.UndoRedo;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * XBUP document viewer.
 */
@ParametersAreNonnullByDefault
public class XbupDataComponent implements ContextComponent, ComponentProvider {

    protected XbupTree xbupTree;

    protected final XBDocumentPanel documentPanel = new XBDocumentPanel();

    protected final List<XbupViewerBlockPage> blockViewers = new ArrayList<>();
    protected final PluginUiBlockPage documentViewer = new PluginUiBlockPage();
//    protected final StructurePage structureViewer = new StructurePage();
    protected final PropertiesBlockPage propertiesViewer = new PropertiesBlockPage();
    protected final TextualBlockPage textualViewer = new TextualBlockPage();
    protected final BinaryBlockPage binaryViewer = new BinaryBlockPage();

    public XbupDataComponent() {
        init();
    }

    private void init() {
        blockViewers.add(documentViewer);
//        blockViewers.add(structureViewer);
        blockViewers.add(propertiesViewer);
        blockViewers.add(textualViewer);
        blockViewers.add(binaryViewer);

        for (XbupViewerBlockPage blockViewer : blockViewers) {
            documentPanel.addBlockViewer(blockViewer);
        }

        documentPanel.addTabChangedListener(() -> {
            XbupViewerBlockPage activeViewer = documentPanel.getActiveViewer().orElse(null);
            if (activeViewer instanceof StructurePage) {
                ((StructurePage) activeViewer).postWindowOpened();
            }
        });
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return documentPanel;
    }

//    public void postWindowOpened() {
//        structureViewer.postWindowOpened();
//    }

    @Nonnull
    public Optional<XbupViewerBlockPage> getCurrentViewer() {
        return documentPanel.getActiveViewer();
    }

    @Nonnull
    public XbupTree getXbupTree() {
        return Objects.requireNonNull(xbupTree);
    }

    public void setXbupTree(XbupTree xbupTree) {
        this.xbupTree = xbupTree;
        // TODO
//        structureViewer.setTreeDocument(treeDocument);
//        blockViewers.forEach(blockViewer -> {
//            blockViewer.setTreeDocument(treeDocument);
//        });
    }
    
    public void setUndoHandler(UndoRedo undoRedo) {
        documentViewer.setUndoHandler(undoRedo);
        // structureViewer.setUndoHandler(undoRedo);
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
        XBTBlock block = xbupTree.getRootBlock().orElse(null);
        // structureViewer.reportStructureChange(block);
        documentPanel.setBlock((XBTTreeNode) block);
    }

//    @Nonnull
//    public Optional<XBTBlock> getSelectedItem() {
//        return structureViewer.getSelectedItem();
//    }
}
