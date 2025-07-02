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
package org.exbin.framework.viewer.xbup.def;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.exbin.framework.App;
import org.exbin.framework.viewer.xbup.def.gui.ParametersPanel;
import org.exbin.framework.viewer.xbup.def.model.ParametersTableModel;
import org.exbin.framework.viewer.xbup.gui.ParametersTableCellEditor;
import org.exbin.framework.viewer.xbup.gui.ParametersTableCellRenderer;
import org.exbin.framework.viewer.xbup.gui.ParametersTableItem;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
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
 * Parameters editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ParametersEditor {

    private ParametersPanel editorPanel = new ParametersPanel();
    private final ParametersTableModel parametersTableModel = new ParametersTableModel();
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ParametersEditor.class);

    public ParametersEditor() {
        popupMenu = new JPopupMenu();

        editorPanel.setPanelPopup(popupMenu);
        editorPanel.setParametersTableModel(parametersTableModel);
    }

    @Nonnull
    public ParametersPanel getEditorPanel() {
        return editorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        editorPanel.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    public void setBlock(XBTTreeNode block) {
        TableColumnModel columnModel = editorPanel.getParametersTable().getColumnModel();
        TableColumn column = columnModel.getColumn(3);
        ParametersTableCellEditor parametersTableCellEditor = new ParametersTableCellEditor(catalog, pluginRepository, block);
        column.setCellEditor(parametersTableCellEditor);
        ParametersTableCellRenderer parametersTableCellRenderer = new ParametersTableCellRenderer(catalog, pluginRepository, block);
        column.setCellRenderer(parametersTableCellRenderer);

        parametersTableModel.clear();

        if (block == null) {
            parametersTableModel.fireTableDataChanged();
            return;
        }

        XBBlockDecl decl = block.getBlockDecl();
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        if (decl instanceof XBCBlockDecl) {
            XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
            XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
            XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpecRev().getParent();
            long bindCount = specService.getSpecDefsCount(spec);
            XBATreeParamExtractor paramExtractor = new XBATreeParamExtractor(block, catalog);
            // TODO: if (desc != null) descTextField.setText(desc.getText());
            for (int paramIndex = 0; paramIndex < bindCount; paramIndex++) {
                // TODO: Exclusive lock
                XBCSpecDef specDef = specService.getSpecDefByOrder(spec, paramIndex);
                String specName = "";
                String specType = "";
                XBRowEditor lineEditor = null;

                if (specDef != null) {
                    XBCXName specDefName = nameService.getDefaultItemName(specDef);
                    if (specDefName != null) {
                        specName = specDefName.getText();
                    }

                    Optional<XBCRev> rowRev = specDef.getTargetRev();
                    if (rowRev.isPresent()) {
                        XBCSpec rowSpec = rowRev.get().getParent();
                        try {
                            lineEditor = getCustomEditor((XBCBlockRev) rowRev.get(), uiService);
                            if (lineEditor != null) {
                                paramExtractor.setParameterIndex(paramIndex);
                                XBPSerialReader serialReader = new XBPSerialReader(paramExtractor);
                                serialReader.read(lineEditor);

                                lineEditor.attachChangeListener(new ComponentEditorChangeListener(lineEditor, paramExtractor, paramIndex));
                            }
                        } catch (IOException | XBProcessingException ex) {
                            Logger.getLogger(ParametersEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        XBCXName typeName = nameService.getDefaultItemName(rowSpec);
                        specType = typeName.getText();
                    }
                }

                ParametersTableItem itemRecord = new ParametersTableItem(specDef, specName, specType, lineEditor);
                itemRecord.setTypeName(itemRecord.getDefTypeName());
                parametersTableModel.addRow(itemRecord);
            }
        }

        parametersTableModel.fireTableDataChanged();
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
