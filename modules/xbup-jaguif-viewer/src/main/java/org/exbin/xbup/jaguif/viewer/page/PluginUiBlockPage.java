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
package org.exbin.xbup.jaguif.viewer.page;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.swing.JComponent;
import org.exbin.jaguif.App;
import org.exbin.jaguif.context.api.ContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;
import org.exbin.xbup.jaguif.component.page.XbupPagesPanel;
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
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.block.XbupBlockState;
import org.exbin.xbup.jaguif.component.def.BinaryDataViewer;
import org.exbin.xbup.jaguif.component.def.gui.BlockPanel;
import org.exbin.xbup.jaguif.viewer.gui.BlockComponentEditorPanel;
import org.exbin.xbup.jaguif.viewer.gui.BlockComponentViewerPanel;
import org.exbin.xbup.jaguif.viewer.gui.BlockDefinitionPanel;
import org.exbin.xbup.jaguif.viewer.gui.BlockRowEditorPanel;
import org.exbin.xbup.jaguif.viewer.gui.SimpleMessagePanel;
import org.exbin.xbup.operation.undo.UndoRedo;
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
 */
@NullMarked
public class PluginUiBlockPage extends AbstractTabPagesComponent implements XbupViewerBlockPage {

    public static final String PAGE_ID = "pluginBlock";

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(PluginUiBlockPage.class);
    protected XbupPagesPanel viewerPanel = new XbupPagesPanel();
    protected final BlockDefinitionPanel definitionPanel = new BlockDefinitionPanel();
    protected final BlockPanel blockPanel = new BlockPanel();
    protected final BinaryDataViewer binaryDataViewer = new BinaryDataViewer();
    protected final BlockRowEditorPanel rowEditorPanel = new BlockRowEditorPanel();
    protected XbupBlockState xbupBlock;

    public PluginUiBlockPage() {
        putValue(KEY_ID, PAGE_ID);
        putValue(KEY_NAME, resourceBundle.getString("page.name"));
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource(resourceBundle.getString("page.icon"))));
        putValue(KEY_CONTEXT_CHANGE, new ContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(XbupBlockState.class, (instance) -> {
                    setXbupBlock(instance);
                });
            }
        });
        SimpleMessagePanel messagePanel = new SimpleMessagePanel();
        viewerPanel.setMainComponent(messagePanel);
    }

    @Override
    public JComponent getComponent() {
        return viewerPanel;
    }

    @Override
    public void setXbupBlock(@Nullable XbupBlockState xbupBlock) {
        this.xbupBlock = xbupBlock;

        viewerPanel.removeAllPages();
        XBTBlock block = xbupBlock == null ? null : xbupBlock.getBlock();
        if (block != null) {
            XbupTree xbupTree = xbupBlock.getXbupTree();
            XBACatalog catalog = xbupTree.getCatalog();
            definitionPanel.setCatalog(catalog);
            blockPanel.setCatalog(catalog);
            XBPluginRepository pluginRepository = xbupTree.getPluginRepository();
            definitionPanel.setPluginRepository(pluginRepository);
            blockPanel.setPluginRepository(pluginRepository);

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
                            viewerPanel.addPage("Viewer", panelViewer.getViewer());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(PluginUiBlockPage.class.getName()).log(Level.SEVERE, null, ex);
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
                            viewerPanel.addPage("Editor", panelEditor.getEditor());
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(PluginUiBlockPage.class.getName()).log(Level.SEVERE, null, ex);
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
                            viewerPanel.addPage("Component Viewer", componentViewerPanel);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(PluginUiBlockPage.class.getName()).log(Level.SEVERE, null, ex);
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
                            viewerPanel.addPage("Component Editor", componentEditorPanel);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(PluginUiBlockPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                XBCXBlockUi rowEditorUi = uiService.findUiByPR(blockSpecRev, XBPlugUiType.ROW_EDITOR, 0);
                if (rowEditorUi != null) {
                    XBCXPlugUi plugUi = rowEditorUi.getUi();

                    try {
                        XBCatalogPlugin pluginHandler = pluginRepository.getPluginHandler(plugUi.getPlugin());
                        if (pluginHandler != null) {
                            rowEditorPanel.setBlock(block, plugUi, pluginHandler);
                            viewerPanel.addPage("Row Viewer", rowEditorPanel);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(PluginUiBlockPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            blockPanel.setBlock((XBTTreeNode) block);
            if (block.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                binaryDataViewer.setContentData(block.getBlockData());
                binaryDataViewer.attachExtraBars();
                viewerPanel.addPage("Data", binaryDataViewer.getBinaryDataPanel());
            } else {
                definitionPanel.setBlock(block);
                viewerPanel.addPage("Definition", definitionPanel);
            }
            viewerPanel.addPage("Block", blockPanel);
        }

        viewerPanel.finishPages();
        viewerPanel.revalidate();
        viewerPanel.repaint();
    }

    public void setUndoHandler(UndoRedo undoRedo) {
        blockPanel.setUndoRedo(undoRedo);
        binaryDataViewer.setUndoRedo(undoRedo);
    }

    private void reloadCustomViewer(XBPanelViewer panelViewer, XBTBlock block) {
        XBPSerialReader serialReader = new XBPSerialReader(new XBTProviderToPullProvider(new XBTTreeWriter(block)));
        try {
            serialReader.read((XBSerializable) panelViewer);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(PluginUiBlockPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reloadCustomEditor(XBPanelEditor panelEditor, XBTBlock block) {
        XBPSerialReader serialReader = new XBPSerialReader(new XBTProviderToPullProvider(new XBTTreeWriter(block)));
        try {
            serialReader.read((XBSerializable) panelEditor);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(PluginUiBlockPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
