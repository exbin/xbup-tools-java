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
package org.exbin.framework.editor.xbup.def.gui;

import java.awt.BorderLayout;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Block content panel - level 0.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlockPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(BlockPanel.class);
    private XBApplication application;
    private JComponent activeComponent = null;
    private XBTTreeNode block;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;
    private boolean dataModeAdjusting = false;

    public BlockPanel() {
        initComponents();
    }

    public void setApplication(XBApplication application) {
        this.application = application;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blockTypeButtonGroup = new javax.swing.ButtonGroup();
        terminationModeCheckBox = new javax.swing.JCheckBox();
        blockTypeLabel = new javax.swing.JLabel();
        nodeBlockRadioButton = new javax.swing.JRadioButton();
        dataBlockRadioButton = new javax.swing.JRadioButton();
        contentPanel = new javax.swing.JPanel();

        terminationModeCheckBox.setText("Block size specified");

        blockTypeLabel.setText("Block Type");
        blockTypeLabel.setToolTipText("");

        blockTypeButtonGroup.add(nodeBlockRadioButton);
        nodeBlockRadioButton.setSelected(true);
        nodeBlockRadioButton.setText("Node Block");
        nodeBlockRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nodeBlockRadioButtonItemStateChanged(evt);
            }
        });

        blockTypeButtonGroup.add(dataBlockRadioButton);
        dataBlockRadioButton.setText("Data Block");
        dataBlockRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dataBlockRadioButtonItemStateChanged(evt);
            }
        });

        contentPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(terminationModeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(blockTypeLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(nodeBlockRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dataBlockRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(terminationModeCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blockTypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nodeBlockRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataBlockRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nodeBlockRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nodeBlockRadioButtonItemStateChanged
        if (!dataModeAdjusting) {
            changeDataMode(XBBlockDataMode.NODE_BLOCK);
        }
    }//GEN-LAST:event_nodeBlockRadioButtonItemStateChanged

    private void dataBlockRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dataBlockRadioButtonItemStateChanged
        if (!dataModeAdjusting) {
            changeDataMode(XBBlockDataMode.DATA_BLOCK);
        }
    }//GEN-LAST:event_dataBlockRadioButtonItemStateChanged

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new BlockPanel());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup blockTypeButtonGroup;
    private javax.swing.JLabel blockTypeLabel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JRadioButton dataBlockRadioButton;
    private javax.swing.JRadioButton nodeBlockRadioButton;
    private javax.swing.JCheckBox terminationModeCheckBox;
    // End of variables declaration//GEN-END:variables

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    public void setBlock(XBTTreeNode block) {
        this.block = block;
        terminationModeCheckBox.setSelected(block.getTerminationMode() == XBBlockTerminationMode.SIZE_SPECIFIED);
        XBBlockDataMode dataMode = block.getDataMode();
        dataModeAdjusting = true;
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
            dataBlockRadioButton.setSelected(true);
        } else {
            nodeBlockRadioButton.setSelected(true);
        }
        dataModeAdjusting = false;
        if (!switchContentComponent(dataMode)) {
            updateContentComponent(activeComponent);
        }
    }

    private boolean switchContentComponent(XBBlockDataMode blockDataMode) {
        switch (blockDataMode) {
            case DATA_BLOCK: {
                if (!(activeComponent instanceof BinaryDataPanel)) {
                    if (activeComponent != null) {
                        contentPanel.remove(activeComponent);
                    }
                    BinaryDataPanel binaryDataPanel = new BinaryDataPanel();
                    binaryDataPanel.setApplication(application);
                    updateContentComponent(binaryDataPanel);

                    contentPanel.add(binaryDataPanel, BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                    activeComponent = binaryDataPanel;
                    return true;
                }
                break;
            }
            case NODE_BLOCK: {
                if (!(activeComponent instanceof NodeBlockPanel)) {
                    if (activeComponent != null) {
                        contentPanel.remove(activeComponent);
                    }
                    NodeBlockPanel nodeBlockPanel = new NodeBlockPanel();
                    nodeBlockPanel.setApplication(application);
                    nodeBlockPanel.setPluginRepository(pluginRepository);
                    nodeBlockPanel.setCatalog(catalog);
                    updateContentComponent(nodeBlockPanel);

                    contentPanel.add(nodeBlockPanel, BorderLayout.CENTER);
                    contentPanel.revalidate();
                    contentPanel.repaint();
                    activeComponent = nodeBlockPanel;
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private void updateContentComponent(JComponent activeComponent) {
        if (activeComponent instanceof BinaryDataPanel) {
            BinaryDataPanel binaryDataPanel = (BinaryDataPanel) activeComponent;
            binaryDataPanel.setContentData(block.getBlockData());
        } else if (activeComponent instanceof NodeBlockPanel) {
            NodeBlockPanel nodeBlockPanel = (NodeBlockPanel) activeComponent;
            nodeBlockPanel.setBlock(block);
        }
    }

    private void changeDataMode(XBBlockDataMode blockDataMode) {
        block.setDataMode(blockDataMode);
        switchContentComponent(blockDataMode);
    }
}
