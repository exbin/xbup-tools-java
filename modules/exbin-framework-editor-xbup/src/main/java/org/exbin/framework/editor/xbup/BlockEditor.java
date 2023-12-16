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
package org.exbin.framework.editor.xbup;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.def.AttributesEditor;
import org.exbin.framework.editor.xbup.def.BinaryDataEditor;
import org.exbin.framework.editor.xbup.def.ParametersEditor;
import org.exbin.framework.editor.xbup.def.gui.BasicNodePanel;
import org.exbin.framework.editor.xbup.def.gui.BlockEditorPanel;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.parser_tree.XBTTreeWriter;
import org.exbin.xbup.plugin.XBCatalogPlugin;
import org.exbin.xbup.plugin.XBPanelEditor;
import org.exbin.xbup.plugin.XBPanelEditorCatalogPlugin;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Block editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlockEditor {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(BlockEditor.class);
    private XBApplication application;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;

    private XBTTreeNode block;

    private final BlockEditorPanel blockEditorPanel = new BlockEditorPanel();
    private final BasicNodePanel basicNodePanel = new BasicNodePanel();
    private final AttributesEditor attributesEditor = new AttributesEditor();
    private final ParametersEditor parametersEditor = new ParametersEditor();
    private final BinaryDataEditor dataEditor = new BinaryDataEditor();

    private XBPanelEditor customEditor;

    public BlockEditor() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setApplication(XBApplication application) {
        this.application = application;

        blockEditorPanel.setApplication(application);
        basicNodePanel.setApplication(application);
        attributesEditor.setApplication(application);
        parametersEditor.setApplication(application);
        dataEditor.setApplication(application);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        blockEditorPanel.setCatalog(catalog);
        basicNodePanel.setCatalog(catalog);
        attributesEditor.setCatalog(catalog);
        parametersEditor.setCatalog(catalog);
        dataEditor.setCatalog(catalog);
    }

    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
        parametersEditor.setPluginRepository(pluginRepository);
    }

    @Nonnull
    public BlockEditorPanel getPanel() {
        return blockEditorPanel;
    }

    public XBTTreeNode getBlock() {
        return block;
    }

    public void setBlock(XBTTreeNode block) {
        this.block = block.cloneNode(true);

        basicNodePanel.setBlock(block);
        blockEditorPanel.addTab(resourceBundle.getString("basicPanel.title"), basicNodePanel);
        XBBlockDataMode dataMode = block.getDataMode();
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
            dataEditor.setBlock(block);
            blockEditorPanel.addTab(resourceBundle.getString("dataEditor.title"), dataEditor.getEditorPanel());
        } else {
            try {
                customEditor = getCustomPanel(block);
                if (customEditor != null) {
                    ((XBPanelEditor) customEditor).attachChangeListener(() -> {
                        // TODO dataChanged = true;
                    });

                    reloadCustomEditor();
                    blockEditorPanel.addTab(resourceBundle.getString("customEditor.title"), customEditor.getEditor());
                }
            } catch (Exception ex) {
                Logger.getLogger(BlockEditor.class.getName()).log(Level.SEVERE, null, ex);
            }

            parametersEditor.setBlock(block);
            blockEditorPanel.addTab(resourceBundle.getString("parametersEditor.title"), parametersEditor.getEditorPanel());
            attributesEditor.setBlock(block);
            blockEditorPanel.addTab(resourceBundle.getString("attributesEditor.title"), attributesEditor.getEditorPanel());
        }

//        if (block.getParent() == null) {
//            blockEditorPanel.addTab(extAreaEditorPanelTitle, tailDataPanel);
//            tailDataBinaryDataFile = null;
//        }
    }

    private void reloadCustomEditor() {
        XBPSerialReader serialReader = new XBPSerialReader(new XBTProviderToPullProvider(new XBTTreeWriter(block)));
        try {
            serialReader.read((XBSerializable) customEditor);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(BlockEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Nullable
    private XBPanelEditor getCustomPanel(XBTTreeNode srcNode) {
        if (catalog == null) {
            return null;
        }
        if (srcNode.getBlockType() == null) {
            return null;
        }
        if (srcNode.getBlockDecl() == null) {
            return null;
        }
        XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
        XBCBlockDecl decl = (XBCBlockDecl) srcNode.getBlockDecl();
        if (decl == null) {
            return null;
        }
        XBCBlockRev rev = decl.getBlockSpecRev();
        if (rev == null) {
            return null;
        }
        XBCXBlockUi blockUi = uiService.findUiByPR(rev, XBPlugUiType.PANEL_EDITOR, 0);
        if (blockUi == null) {
            return null;
        }
        XBCXPlugUi plugUi = blockUi.getUi();
        if (plugUi == null) {
            return null;
        }
        XBCXPlugin plugin = plugUi.getPlugin();
        XBCatalogPlugin pluginHandler;

        // This part is stub for Java Webstart, uncomment it if needed
        /*if ("XBPicturePlugin.jar".equals(plugin.getPluginFile().getFilename())) {
         pluginHandler = new XBPicturePlugin();
         } else */
        pluginHandler = pluginRepository.getPluginHandler(plugin);
        if (pluginHandler == null) {
            return null;
        }

        return ((XBPanelEditorCatalogPlugin) pluginHandler).getPanelEditor(plugUi.getMethodIndex());
    }
}
