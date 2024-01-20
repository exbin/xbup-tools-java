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

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.parser_tree.XBTTreeWriter;
import org.exbin.xbup.plugin.XBCatalogPlugin;
import org.exbin.framework.editor.xbup.viewer.BlockViewer;
import org.exbin.xbup.plugin.XBComponentEditor;
import org.exbin.xbup.plugin.XBComponentEditorCatalogPlugin;

/**
 * Single component editor panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlockComponentEditorPanel extends javax.swing.JPanel {

    private XBApplication application;
    private XBACatalog catalog;
    private JComponent component = null;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(BlockComponentEditorPanel.class);

    public BlockComponentEditorPanel() {
        initComponents();
        init();
    }

    private void init() {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        valueLabel = new javax.swing.JLabel();
        valuePanel = new javax.swing.JPanel();

        setName("Form"); // NOI18N

        valueLabel.setText(resourceBundle.getString("valueLabel.text")); // NOI18N
        valueLabel.setName("valueLabel"); // NOI18N

        valuePanel.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.background"));
        valuePanel.setName("valuePanel"); // NOI18N
        valuePanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(valueLabel)
                        .addGap(0, 698, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(valueLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valuePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(305, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setApplication(XBApplication application) {
        this.application = application;
//        propertiesPanel.setApplication(application);
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
//        propertiesPanel.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
//        propertiesPanel.setPluginRepository(pluginRepository);
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new BlockComponentEditorPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel valueLabel;
    private javax.swing.JPanel valuePanel;
    // End of variables declaration//GEN-END:variables

    public void setBlock(XBTBlock block, XBCXPlugUi plugUi, XBCatalogPlugin pluginHandler) {
        long methodIndex = plugUi.getMethodIndex();
        XBComponentEditor componentEditor = ((XBComponentEditorCatalogPlugin) pluginHandler).getComponentEditor(methodIndex);
        if (component != null) {
            valuePanel.remove(component);
        }
        XBPSerialReader serialReader = new XBPSerialReader(new XBTProviderToPullProvider(new XBTTreeWriter(block)));
        try {
            serialReader.read((XBSerializable) componentEditor);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(BlockComponentEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        component = componentEditor.getEditor();
        valuePanel.add(component, BorderLayout.CENTER);
        valuePanel.revalidate();
    }

    public void setActiveViewer(BlockViewer viewer) {
//        propertiesPanel.setActiveViewer(viewer);
    }
}
