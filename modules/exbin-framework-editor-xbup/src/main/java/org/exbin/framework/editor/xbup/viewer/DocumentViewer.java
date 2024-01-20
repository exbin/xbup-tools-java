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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.exbin.auxiliary.binary_data.ByteArrayEditableData;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.def.BinaryDataEditor;
import org.exbin.framework.editor.xbup.def.gui.BlockPanel;
import org.exbin.framework.editor.xbup.gui.BlockComponentEditorPanel;
import org.exbin.framework.editor.xbup.gui.BlockComponentViewerPanel;
import org.exbin.framework.editor.xbup.gui.BlockDefinitionPanel;
import org.exbin.framework.editor.xbup.gui.BlockRowEditorPanel;
import org.exbin.framework.editor.xbup.viewer.gui.DocumentViewerPanel;
import org.exbin.framework.editor.xbup.gui.SimpleMessagePanel;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.util.StreamUtils;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.parser_tree.XBTTreeWriter;
import org.exbin.xbup.plugin.XBCatalogPlugin;
import org.exbin.xbup.plugin.XBPanelEditor;
import org.exbin.xbup.plugin.XBPanelEditorCatalogPlugin;
import org.exbin.xbup.plugin.XBPanelViewer;
import org.exbin.xbup.plugin.XBPanelViewerCatalogPlugin;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Custom viewer of document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class DocumentViewer implements BlockViewer {

    private XBPluginRepository pluginRepository;

    private DocumentViewerPanel viewerPanel = new DocumentViewerPanel();
    private final BlockDefinitionPanel definitionPanel = new BlockDefinitionPanel();
    private final BlockPanel blockPanel = new BlockPanel();
    private final BinaryDataEditor binaryDataEditor = new BinaryDataEditor();
    private final BlockRowEditorPanel rowEditorPanel = new BlockRowEditorPanel();
    private XBTBlock selectedItem = null;
    private XBACatalog catalog;

    public DocumentViewer() {
        SimpleMessagePanel messagePanel = new SimpleMessagePanel();
        viewerPanel.setBorderComponent(messagePanel);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Viewer";
    }

    @Nonnull
    @Override
    public Optional<ImageIcon> getIcon() {
        return Optional.of(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/zoom-4.png")));
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return viewerPanel;
    }

    @Override
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        definitionPanel.setCatalog(catalog);
        blockPanel.setCatalog(catalog);
    }

    @Override
    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
        definitionPanel.setPluginRepository(pluginRepository);
        blockPanel.setPluginRepository(pluginRepository);
    }

    @Override
    public void setApplication(XBApplication application) {
        definitionPanel.setApplication(application);
        blockPanel.setApplication(application);
        binaryDataEditor.setApplication(application);
    }

    @Override
    public void setBlock(@Nullable XBTBlock block) {
        viewerPanel.removeAllViews();
        if (block != null) {
            XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
            XBBlockDecl decl = block instanceof XBTTreeNode ? ((XBTTreeNode) block).getBlockDecl() : null;
            if (decl instanceof XBCBlockDecl) {
                XBCBlockRev blockSpecRev = ((XBCBlockDecl) decl).getBlockSpecRev();

                XBCXBlockUi panelViewerUi = uiService.findUiByPR(blockSpecRev, XBPlugUiType.PANEL_VIEWER, 0);
                if (panelViewerUi != null) {
                    XBCXPlugUi plugUi = panelViewerUi.getUi();
                    Long methodIndex = plugUi.getMethodIndex();
                    //pane.getPlugin().getPluginFile();

                    try {
                        XBCatalogPlugin pluginHandler = pluginRepository.getPluginHandler(plugUi.getPlugin());
                        if (pluginHandler != null) {
                            XBPanelViewer panelViewer = ((XBPanelViewerCatalogPlugin) pluginHandler).getPanelViewer(methodIndex);
                            reloadCustomViewer(panelViewer, block);
                            viewerPanel.addView("Viewer", panelViewer.getViewer());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                XBCXBlockUi panelEditorUi = uiService.findUiByPR(blockSpecRev, XBPlugUiType.PANEL_EDITOR, 0);
                if (panelEditorUi != null) {
                    XBCXPlugUi plugUi = panelEditorUi.getUi();
                    Long methodIndex = plugUi.getMethodIndex();
                    //pane.getPlugin().getPluginFile();

                    try {
                        XBCatalogPlugin pluginHandler = pluginRepository.getPluginHandler(plugUi.getPlugin());
                        if (pluginHandler != null) {
                            XBPanelEditor panelEditor = ((XBPanelEditorCatalogPlugin) pluginHandler).getPanelEditor(methodIndex);
                            reloadCustomEditor(panelEditor, block);
                            viewerPanel.addView("Editor", panelEditor.getEditor());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                XBCXBlockUi componentViewerUi = uiService.findUiByPR(blockSpecRev, XBPlugUiType.COMPONENT_VIEWER, 0);
                if (componentViewerUi != null) {
                    XBCXPlugUi plugUi = componentViewerUi.getUi();

                    try {
                        XBCatalogPlugin pluginHandler = pluginRepository.getPluginHandler(plugUi.getPlugin());
                        if (pluginHandler != null) {
                            BlockComponentViewerPanel componentViewerPanel = new BlockComponentViewerPanel();
                            componentViewerPanel.setBlock(block, plugUi, pluginHandler);
                            viewerPanel.addView("Component Viewer", componentViewerPanel);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                XBCXBlockUi componentEditorUi = uiService.findUiByPR(blockSpecRev, XBPlugUiType.COMPONENT_EDITOR, 0);
                if (componentEditorUi != null) {
                    XBCXPlugUi plugUi = componentEditorUi.getUi();

                    try {
                        XBCatalogPlugin pluginHandler = pluginRepository.getPluginHandler(plugUi.getPlugin());
                        if (pluginHandler != null) {
                            BlockComponentEditorPanel componentEditorPanel = new BlockComponentEditorPanel();
                            componentEditorPanel.setBlock(block, plugUi, pluginHandler);
                            viewerPanel.addView("Component Editor", componentEditorPanel);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                XBCXBlockUi rowEditorUi = uiService.findUiByPR(blockSpecRev, XBPlugUiType.ROW_EDITOR, 0);
                if (rowEditorUi != null) {
                    XBCXPlugUi plugUi = rowEditorUi.getUi();

                    try {
                        XBCatalogPlugin pluginHandler = pluginRepository.getPluginHandler(plugUi.getPlugin());
                        if (pluginHandler != null) {
                            rowEditorPanel.setBlock(block, plugUi, pluginHandler);
                            viewerPanel.addView("Row Viewer", rowEditorPanel);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            blockPanel.setBlock((XBTTreeNode) block);
            if (block.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                ByteArrayEditableData byteArrayData = new ByteArrayEditableData();
                try (OutputStream dataOutputStream = byteArrayData.getDataOutputStream()) {
                    StreamUtils.copyInputStreamToOutputStream(block.getData(), dataOutputStream);
                } catch (IOException ex) {
                    Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
                binaryDataEditor.setContentData(byteArrayData);
                binaryDataEditor.attachExtraBars();
                viewerPanel.addView("Data", binaryDataEditor.getEditorPanel());
            } else {
                definitionPanel.setBlock(block);
                viewerPanel.addView("Definition", definitionPanel);
            }
            viewerPanel.addView("Block", blockPanel);
        }

        viewerPanel.viewsAdded();
        selectedItem = block;
        viewerPanel.revalidate();
        viewerPanel.repaint();
    }

    private void reloadCustomViewer(XBPanelViewer panelViewer, XBTBlock block) {
        XBPSerialReader serialReader = new XBPSerialReader(new XBTProviderToPullProvider(new XBTTreeWriter(block)));
        try {
            serialReader.read((XBSerializable) panelViewer);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadCustomEditor(XBPanelEditor panelEditor, XBTBlock block) {
        XBPSerialReader serialReader = new XBPSerialReader(new XBTProviderToPullProvider(new XBTTreeWriter(block)));
        try {
            serialReader.read((XBSerializable) panelEditor);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(DocumentViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
