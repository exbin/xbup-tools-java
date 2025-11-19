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
package org.exbin.framework.editor.xbup.gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.exbin.framework.App;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBPSerialWriter;
import org.exbin.xbup.parser_tree.XBATreeParamExtractor;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBCatalogPlugin;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.plugin.XBRowEditor;
import org.exbin.xbup.plugin.XBRowEditorCatalogPlugin;
import org.exbin.xbup.core.block.XBTBlock;

/**
 * Panel for properties of the actual panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPropertyTablePanel extends javax.swing.JPanel {

    private XBACatalog catalog;
    private final XBPropertyTableModel tableModel;
    private final XBPropertyTableCellRenderer valueCellRenderer;
    private final TableCellRenderer nameCellRenderer;
    private final XBPropertyTableCellEditor valueCellEditor;
    private XBCXUiService uiService = null;
    private XBPluginRepository pluginRepository = null;

    private Thread propertyThread;
    private final Semaphore valueFillingSemaphore;
    private XBTBlock block;
    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XBPropertyTablePanel.class);

    public XBPropertyTablePanel() {
        tableModel = new XBPropertyTableModel();

        initComponents();

        TableColumnModel columns = propertiesTable.getColumnModel();
        columns.getColumn(0).setPreferredWidth(190);
        columns.getColumn(1).setPreferredWidth(190);
        columns.getColumn(0).setWidth(190);
        columns.getColumn(1).setWidth(190);
        nameCellRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JComponent component = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                XBPropertyTableItem tableItem = ((XBPropertyTableModel) table.getModel()).getRow(row);
                component.setToolTipText("(" + tableItem.getDefTypeName() + ") " + tableItem.getValueName());
                return component;
            }

        };
        columns.getColumn(0).setCellRenderer(nameCellRenderer);
        valueCellRenderer = new XBPropertyTableCellRenderer(null, null);
        columns.getColumn(1).setCellRenderer(valueCellRenderer);
        valueCellEditor = new XBPropertyTableCellEditor(null, null);
        columns.getColumn(1).setCellEditor(valueCellEditor);

        propertiesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (component instanceof JComponent) {
                    ((JComponent) component).setBorder(noFocusBorder);
                }

                return component;
            }
        });

        propertyThread = null;
        valueFillingSemaphore = new Semaphore(1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainScrollPane = new javax.swing.JScrollPane();
        propertiesTable = new javax.swing.JTable();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        mainScrollPane.setName("mainScrollPane"); // NOI18N

        propertiesTable.setModel(tableModel);
        propertiesTable.setName("propertiesTable"); // NOI18N
        propertiesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        mainScrollPane.setViewportView(propertiesTable);

        add(mainScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JTable propertiesTable;
    // End of variables declaration//GEN-END:variables

    public void setBlock(XBTBlock block) {
        this.block = block;
        valueCellRenderer.setBlock(block);
        valueCellEditor.setBlock(block);
        new PropertyThread(this, (XBTTreeNode) block).start();
    }

    @Nullable
    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        uiService = catalog == null ? null : catalog.getCatalogService(XBCXUiService.class);

        valueCellRenderer.setCatalog(catalog);
        valueCellEditor.setCatalog(catalog);
    }

    private Thread getPropertyThread() {
        return propertyThread;
    }

    private void setPropertyThread(Thread propertyThread) {
        this.propertyThread = propertyThread;
    }

    private Semaphore getValueFillingSemaphore() {
        return valueFillingSemaphore;
    }

    // TODO: Prepare values and then fill it to property panel
    private class PropertyThread extends Thread {

        private final XBPropertyTablePanel propertyPanel;
        private final XBTTreeNode node;

        public PropertyThread(XBPropertyTablePanel propertyPanel, XBTTreeNode node) {
            super();
            this.propertyPanel = propertyPanel;
            this.node = node;
        }

        @Override
        public void start() {
            super.start();
            try {
                getValueFillingSemaphore().acquire();
                for (int rowIndex = tableModel.getRowCount() - 1; rowIndex >= 0; rowIndex--) {
                    tableModel.removeRow(rowIndex);
                }
                propertyPanel.setPropertyThread(this);
                getValueFillingSemaphore().release();
                fillPanel();
            } catch (InterruptedException ex) {
                Logger.getLogger(XBPropertyTablePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void fillPanel() {
            if (node == null) {
                return;
            }
            XBBlockType type = node.getBlockType();
            XBBlockDecl decl = node.getBlockDecl();
            if (propertyThread != this && catalog == null) {
                return;
            }

            XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
            if (decl instanceof XBCBlockDecl) {
                XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpecRev().getParent();
                if (propertyThread != this) {
                    return;
                }
                long bindCount = specService.getSpecDefsCount(spec);
                try {
                    getValueFillingSemaphore().acquire();
                    XBATreeParamExtractor paramExtractor = new XBATreeParamExtractor(node, catalog);

                    if (propertyPanel.getPropertyThread() == this) {
                        for (int parameterIndex = 0; parameterIndex < bindCount; parameterIndex++) {
                            String rowNameText = "";
                            String typeNameText = "";
                            XBPropertyTableItem row;
                            XBCSpecDef def = specService.getSpecDefByOrder(spec, parameterIndex);
                            XBCBlockSpec rowSpec;
                            XBRowEditor lineEditor = null;
                            if (def != null) {
                                try {
                                    rowNameText = nameService.getDefaultText(def);
                                    Optional<XBCRev> rowRev = def.getTargetRev();
                                    if (rowRev.isPresent()) {
                                        rowSpec = (XBCBlockSpec) rowRev.get().getParent();
                                        typeNameText = nameService.getDefaultText(rowSpec);
                                        if (rowNameText.isEmpty()) {
                                            rowNameText = typeNameText;
                                        }

                                        lineEditor = getCustomEditor((XBCBlockRev) rowRev.get());
                                        if (lineEditor != null) {
                                            paramExtractor.setParameterIndex(parameterIndex);
                                            XBPSerialReader serialReader = new XBPSerialReader(paramExtractor);
                                            serialReader.read(lineEditor);

                                            lineEditor.attachChangeListener(new RowEditorChangeListener(lineEditor, paramExtractor, parameterIndex));
                                        }
                                    }
                                } catch (XBProcessingException | IOException ex) {
                                    Logger.getLogger(XBPropertyTablePanel.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(propertyPanel, ex.getMessage(), "Exception in property panel", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            row = new XBPropertyTableItem(def, rowNameText, typeNameText, lineEditor);
                            tableModel.addRow(row);
                        }
                    }
                    getValueFillingSemaphore().release();
                } catch (InterruptedException | HeadlessException ex) {
                    Logger.getLogger(XBPropertyTablePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Nullable
    private XBRowEditor getCustomEditor(XBCBlockRev rev) {
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

    public void actionEditSelectAll() {
        throw new UnsupportedOperationException("Not supported yet.");
        // activeViewer.performSelectAll();
    }

    public void actionItemAdd() {
        throw new UnsupportedOperationException("Not supported yet.");
        // activeViewer.performAdd();
    }

    public void actionItemModify() {
        throw new UnsupportedOperationException("Not supported yet.");
        // activeViewer.performModify();
    }

    public void actionItemProperties() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionItemOpen() {
    }

    public boolean isStub1Enabled() {
        throw new UnsupportedOperationException("Not supported yet.");
        // return activeViewer.getMode() == XBDocumentPanel.PanelMode.TEXT;
    }

    private boolean addEnabled = false;

    public boolean isAddEnabled() {
        return addEnabled;
    }

    public void setAddEnabled(boolean enabled) {
        boolean old = isAddEnabled();
        this.addEnabled = enabled;
        firePropertyChange("addEnabled", old, isAddEnabled());
    }

    private boolean undoAvailable = false;

    public boolean isUndoAvailable() {
        return undoAvailable;
    }

    public void setUndoAvailable(boolean enabled) {
        boolean old = isUndoAvailable();
        this.undoAvailable = enabled;
        firePropertyChange("undoAvailable", old, isUndoAvailable());
    }

    public boolean isRedoAvailable() {
        return false; //activePanel.canRedo();
    }

    public boolean isEditEnabled() {
        return false;
    }

    public boolean isPasteEnabled() {
        return false;
    }

    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
        valueCellRenderer.setPluginRepository(pluginRepository);
        valueCellEditor.setPluginRepository(pluginRepository);
    }

    @ParametersAreNonnullByDefault
    private class RowEditorChangeListener implements XBRowEditor.ChangeListener {

        private final XBATreeParamExtractor paramExtractor;
        private final int parameterIndex;
        private final XBRowEditor rowEditor;

        private RowEditorChangeListener(XBRowEditor rowEditor, XBATreeParamExtractor paramExtractor, int parameterIndex) {
            this.rowEditor = rowEditor;
            this.paramExtractor = paramExtractor;
            this.parameterIndex = parameterIndex;
        }

        @Override
        public void valueChanged() {
            paramExtractor.setParameterIndex(parameterIndex);
            XBPSerialWriter serialWriter = new XBPSerialWriter(paramExtractor);
            serialWriter.write(rowEditor);
        }
    }
}
