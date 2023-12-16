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
package org.exbin.framework.editor.xbup.def;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.def.gui.BlocksPanel;
import org.exbin.framework.editor.xbup.def.model.BlocksTableModel;
import org.exbin.framework.editor.xbup.gui.BlocksTableCellEditor;
import org.exbin.framework.editor.xbup.gui.BlocksTableCellRenderer;
import org.exbin.framework.editor.xbup.gui.BlocksTableItem;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.definition.catalog.XBCBlockDef;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBPSerialWriter;
import org.exbin.xbup.parser_tree.XBATreeParamExtractor;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBCatalogPlugin;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.xbup.plugin.XBRowEditor;
import org.exbin.xbup.plugin.XBRowEditorCatalogPlugin;

/**
 * Blocks editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlocksEditor {

    private BlocksPanel editorPanel = new BlocksPanel();
    private final BlocksTableModel blocksTableModel = new BlocksTableModel();
    private XBApplication application;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(BlocksEditor.class);

    public BlocksEditor() {
        popupMenu = new JPopupMenu();

        editorPanel.setPanelPopup(popupMenu);
        editorPanel.setBlocksTableModel(blocksTableModel);
    }

    @Nonnull
    public BlocksPanel getEditorPanel() {
        return editorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        editorPanel.setApplication(application);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        editorPanel.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    public void setBlock(XBTTreeNode block) {
        TableColumnModel columnModel = editorPanel.getBlocksTable().getColumnModel();
        TableColumn column = columnModel.getColumn(3);
        BlocksTableCellEditor blocksTableCellEditor = new BlocksTableCellEditor(catalog, pluginRepository, block);
        blocksTableCellEditor.setApplication(application);
        column.setCellEditor(blocksTableCellEditor);
        BlocksTableCellRenderer blocksTableCellRenderer = new BlocksTableCellRenderer(catalog, pluginRepository, block);
        blocksTableCellRenderer.setApplication(application);
        column.setCellRenderer(blocksTableCellRenderer);

        blocksTableModel.clear();

        if (block == null) {
            blocksTableModel.fireTableDataChanged();
            return;
        }

        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
        XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
//        XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpecRev().getParent();
//        long bindCount = specService.getSpecDefsCount(spec);
//        XBATreeParamExtractor paramExtractor = new XBATreeParamExtractor(block, catalog);
        // TODO: if (desc != null) descTextField.setText(desc.getText());

        long childCount = block.getChildrenCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            XBTTreeNode childBlock = block.getChildAt(childIndex);
            XBBlockDecl decl = childBlock.getBlockDecl();
            XBCBlockSpec spec = null;
            XBCBlockRev blockRev = null;
            if (decl instanceof XBCBlockDecl) {
                blockRev = ((XBCBlockDecl) decl).getBlockSpecRev();
                spec = blockRev.getParent();
            }
            String specName = "";
            String specType = "";
            XBRowEditor lineEditor = null;

            if (spec != null) {
                XBCXName specNameItem = nameService.getDefaultItemName(spec);
                if (specNameItem != null) {
                    specName = specNameItem.getText();
                }

                try {
                    lineEditor = getCustomEditor(blockRev, uiService);
                    if (lineEditor != null) {
                        XBATreeParamExtractor paramExtractor = new XBATreeParamExtractor(childBlock, catalog);
                        paramExtractor.setParameterIndex(childIndex);
                        XBPSerialReader serialReader = new XBPSerialReader(paramExtractor);
                        serialReader.read(lineEditor);

                        lineEditor.attachChangeListener(new ComponentEditorChangeListener(lineEditor, paramExtractor, childIndex));
                    }
                } catch (IOException | XBProcessingException ex) {
                    Logger.getLogger(BlocksEditor.class.getName()).log(Level.SEVERE, null, ex);
                }

                XBCXName typeName = nameService.getDefaultItemName(spec);
                specType = typeName.getText();
            }

            BlocksTableItem itemRecord = new BlocksTableItem(specName, specType, lineEditor);
            itemRecord.setTypeName("");
            blocksTableModel.addRow(itemRecord);
        }

        blocksTableModel.fireTableDataChanged();
    }

    private XBRowEditor getCustomEditor(XBCBlockRev rev, XBCXUiService uiService) {
        if (rev == null || catalog == null) {
            return null;
        }

        XBCXBlockUi blockUi = uiService.findUiByPR(rev, XBPlugUiType.ROW_EDITOR, 0);
        if (blockUi == null) {
            return null;
        }
        XBCXPlugUi plugUi = blockUi.getUi();
        if (plugUi == null) {
            return null;
        }
        XBCXPlugin plugin = plugUi.getPlugin();
        XBCatalogPlugin pluginHandler;

        pluginHandler = pluginRepository.getPluginHandler(plugin);
        if (pluginHandler == null) {
            return null;
        }

        return ((XBRowEditorCatalogPlugin) pluginHandler).getRowEditor(plugUi.getMethodIndex());
    }

    @ParametersAreNonnullByDefault
    private class ComponentEditorChangeListener implements XBRowEditor.ChangeListener {

        private final XBATreeParamExtractor paramExtractor;
        private final int parameterIndex;
        private final XBRowEditor lineEditor;

        private ComponentEditorChangeListener(XBRowEditor lineEditor, XBATreeParamExtractor paramExtractor, int parameterIndex) {
            this.lineEditor = lineEditor;
            this.paramExtractor = paramExtractor;
            this.parameterIndex = parameterIndex;
        }

        @Override
        public void valueChanged() {
            paramExtractor.setParameterIndex(parameterIndex);
            XBPSerialWriter serialWriter = new XBPSerialWriter(paramExtractor);
            serialWriter.write(lineEditor);
            // TODO dataChanged = true;
        }
    }
}
