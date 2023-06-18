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
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.viewer.gui.XBStructurePanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Structure viewer/editor of document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class StructureDocumentTab implements DocumentTab {

    private final XBStructurePanel structurePanel = new XBStructurePanel();
    private DocumentItemSelectionListener itemSelectionListener;
    private XBACatalog catalog;
    private XBTBlock selectedItem = null;

    private final List<DocumentTab> previewTabs = new ArrayList<>();

    public StructureDocumentTab() {
        init();
    }

    private void init() {
        previewTabs.add(new ViewerDocumentTab());
        previewTabs.add(new PropertiesDocumentTab());
        previewTabs.add(new TextDocumentTab());
        previewTabs.add(new BinaryDocumentTab());

        for (DocumentTab documentTab : previewTabs) {
            structurePanel.addPreviewTabComponent(documentTab);
        }
    }

    @Override
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        structurePanel.setCatalog(catalog);

        previewTabs.forEach(tab -> {
            tab.setCatalog(catalog);
        });
    }

    @Override
    public void setApplication(XBApplication application) {
        structurePanel.setApplication(application);

        previewTabs.forEach(tab -> {
            tab.setApplication(application);
        });
    }

    @Override
    public void setPluginRepository(XBPluginRepository pluginRepository) {
        structurePanel.setPluginRepository(pluginRepository);

        previewTabs.forEach(tab -> {
            tab.setPluginRepository(pluginRepository);
        });
    }

    @Nonnull
    @Override
    public String getTabName() {
        return "Structure";
    }

    @Nonnull
    @Override
    public Optional<ImageIcon> getTabIcon() {
        return Optional.of(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/list.png")));
    }

    @Override
    public void setBlock(@Nullable XBTBlock block) {
    }

    public void setTreeDocument(XbupTreeDocument treeDocument) {
        structurePanel.setTreeDocument(treeDocument);

        structurePanel.addItemSelectionListener((item) -> {
            this.selectedItem = item;
            String itemPath;
            if (selectedItem != null) {
                StringBuilder builder = new StringBuilder();

                Optional<XBTBlock> parentItem;
                XBTBlock pathItem = selectedItem;
                do {
                    parentItem = pathItem.getParentBlock();
                    if (parentItem.isPresent()) {
                        builder.insert(0, System.identityHashCode(pathItem) + "/");
                        pathItem = parentItem.get();
                    }
                } while (parentItem.isPresent());
                itemPath = builder.toString();
            } else {
                itemPath = "";
            }
            structurePanel.setAddressText(itemPath);
            notifySelectedItem();
            notifyItemSelectionChanged();
        });

    }

    public void postWindowOpened() {
        structurePanel.postWindowOpened();
    }

    public void reportStructureChange(XBTBlock block) {
        structurePanel.reportStructureChange(block);
    }

    private void notifySelectedItem() {
//        DocumentTab currentTab = getCurrentTab();
//        try {
//            currentTab.setBlock(selectedItem);
//        } catch (Exception ex) {
//            Logger.getLogger(XbupSingleEditorProvider.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void notifyItemSelectionChanged() {
        if (itemSelectionListener != null) {
            itemSelectionListener.itemSelected(selectedItem);
        }
    }

    public void setItemSelectionListener(DocumentItemSelectionListener itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
        notifyItemSelectionChanged();
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return structurePanel;
    }
}
