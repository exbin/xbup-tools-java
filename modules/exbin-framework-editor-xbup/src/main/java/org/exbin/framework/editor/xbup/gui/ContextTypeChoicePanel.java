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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.DefaultListModel;
import org.exbin.framework.App;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.XBGroup;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Context type choice panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ContextTypeChoicePanel extends javax.swing.JPanel {

    private XBTTreeNode parentNode;
    private final XBACatalog catalog;
    private int selectedGroup;
    private final XBCXNameService nameService;
    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ContextTypeChoicePanel.class);
    private DefaultControlHandler.DefaultControlEnablementListener enablementListener = null;

    public ContextTypeChoicePanel(XBACatalog catalog, XBTTreeNode parentNode) {
        this.catalog = catalog;
        this.parentNode = parentNode;

        nameService = catalog.getCatalogService(XBCXNameService.class);
        initComponents();
        init();
    }

    private void init() {
        if (catalog != null && parentNode != null) {
            XBContext context = parentNode.getContext();
            int groupId = 1;
            for (XBGroup typeGroup : context.getGroups()) {
                groupComboBox.addItem(Integer.toString(groupId)); // TODO group description if available
                groupId++;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupLabel = new javax.swing.JLabel();
        groupComboBox = new javax.swing.JComboBox<>();
        blockLabel = new javax.swing.JLabel();
        blockTypeScrollPane = new javax.swing.JScrollPane();
        blockTypeList = new javax.swing.JList<>();

        groupLabel.setText("Type Group");

        groupComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupComboBoxActionPerformed(evt);
            }
        });

        blockLabel.setText("Block Type");

        blockTypeList.setModel(new DefaultListModel<String>());
        blockTypeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        blockTypeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                blockTypeListValueChanged(evt);
            }
        });
        blockTypeScrollPane.setViewportView(blockTypeList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(groupLabel)
                            .addComponent(blockLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(blockTypeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .addComponent(groupComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blockLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blockTypeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void groupComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupComboBoxActionPerformed
        ((DefaultListModel<String>) blockTypeList.getModel()).removeAllElements();
        if (parentNode != null) {
            XBContext context = parentNode.getContext();
            selectedGroup = groupComboBox.getSelectedIndex() + 1;
            if (selectedGroup >= 0) {
                XBGroup groupForId = context.getGroupForId(selectedGroup);
                if (groupForId != null) {
                    int blockId = 0;
                    for (XBBlockDecl blockDecl : groupForId.getBlocks()) {
                        String blockCaption = Integer.toString(blockId);
                        if (blockDecl != null) {
                            if (blockDecl instanceof XBCBlockDecl) {
                                blockCaption += ": " + nameService.getDefaultText(((XBCBlockDecl) blockDecl).getBlockSpecRev().getParent());
                            }
                        }

                        ((DefaultListModel<String>) blockTypeList.getModel()).addElement(blockCaption);
                        blockId++;
                    }
                }
            }
        }
    }//GEN-LAST:event_groupComboBoxActionPerformed

    private void blockTypeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_blockTypeListValueChanged
        if (!evt.getValueIsAdjusting()) {
            if (enablementListener != null) {
                enablementListener.actionEnabled(DefaultControlHandler.ControlActionType.OK, blockTypeList.getSelectedIndex() >= 0);
            }
        }
    }//GEN-LAST:event_blockTypeListValueChanged

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication.run(() -> WindowUtils.invokeWindow(new ContextTypeChoicePanel(null, null)));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel blockLabel;
    private javax.swing.JList<String> blockTypeList;
    private javax.swing.JScrollPane blockTypeScrollPane;
    private javax.swing.JComboBox<String> groupComboBox;
    private javax.swing.JLabel groupLabel;
    // End of variables declaration//GEN-END:variables

    public void setParentNode(XBTTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    @Nullable
    public XBBlockType getBlockType() {
        if (selectedGroup >= 0) {
            XBContext context = parentNode.getContext();
            XBGroup groupForId = context.getGroupForId(selectedGroup);
            XBBlockDecl blockDecl = groupForId.getBlockForId(blockTypeList.getSelectedIndex());
            return blockDecl != null ? new XBDeclBlockType(blockDecl) : null;
        }

        return null;
    }

    public void setCanProceedListener(DefaultControlHandler.DefaultControlEnablementListener enablementListener) {
        this.enablementListener = enablementListener;
        enablementListener.actionEnabled(DefaultControlHandler.ControlActionType.OK, false);
    }
}
