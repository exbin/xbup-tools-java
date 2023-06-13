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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.viewer.gui.XBDocumentPanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeDocument;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * XBUP document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupDocumentViewer {

    private XBTTreeDocument treeDocument;

    private final XBDocumentPanel documentPanel = new XBDocumentPanel();

    private final List<DocumentTab> tabs = new ArrayList<>();
    private ViewerDocumentTab viewerDocumentTab;
    private PropertiesDocumentTab propertiesDocumentTab;
    private StructureDocumentTab structureDocumentTab;

    private XBApplication application;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;

    public XbupDocumentViewer() {
        init();
    }

    private void init() {
        viewerDocumentTab = new ViewerDocumentTab();
        tabs.add(viewerDocumentTab);
        structureDocumentTab = new StructureDocumentTab();
        tabs.add(structureDocumentTab);
        propertiesDocumentTab = new PropertiesDocumentTab();
        tabs.add(propertiesDocumentTab);
        tabs.add(new TextDocumentTab());
        tabs.add(new BinaryDocumentTab());

        for (DocumentTab documentTab : tabs) {
            documentPanel.addTabComponent(documentTab);
        }
    }

    @Nonnull
    public JComponent getComponent() {
        return documentPanel;
    }

    public void postWindowOpened() {
        structureDocumentTab.postWindowOpened();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        tabs.forEach(tab -> {
            tab.setCatalog(catalog);
        });
    }

    public void setApplication(XBApplication application) {
        this.application = application;

        tabs.forEach(tab -> {
            tab.setApplication(application);
        });
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
        documentPanel.setPluginRepository(pluginRepository);
        tabs.forEach(tab -> {
            tab.setPluginRepository(pluginRepository);
        });
    }

    @Nonnull
    public DocumentTab getCurrentTab() {
        return documentPanel.getActiveTab();
    }

    @Nonnull
    public XBTTreeDocument getTreeDocument() {
        return Objects.requireNonNull(treeDocument);
    }

    public void setTreeDocument(XBTTreeDocument treeDocument) {
        this.treeDocument = treeDocument;
        structureDocumentTab.setMainDoc(treeDocument);
    }
    
    public void setAddressText(String addressText) {
        documentPanel.setAddressText(addressText);
    }

    public boolean isEditable() {
        return true;
    }

    public void setDevMode(boolean devMode) {
        propertiesDocumentTab.setDevMode(devMode);
    }

    public void notifyFileChanged() {
        XBTBlock block = treeDocument.getRootBlock().orElse(null);
        structureDocumentTab.reportStructureChange(block);
        documentPanel.setBlock((XBTTreeNode) block);
    }
}
