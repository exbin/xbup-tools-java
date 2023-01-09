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

import java.awt.CardLayout;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.DefaultComboBoxModel;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.xbup.catalog.item.revision.gui.CatalogSelectRevPanel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemType;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.WindowUtils.DialogWrapper;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.framework.utils.gui.DefaultControlPanel;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBDBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.XBDeclaration;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCGroupDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.parser.token.event.convert.XBTListenerToEventListener;
import org.exbin.xbup.core.serial.XBPSerialWriter;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.parser_tree.XBTTreeReader;

/**
 * Panel for adding new item into given document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AddBlockPanel extends javax.swing.JPanel {

    private XBApplication application;

    @Nullable
    private XBTTreeNode parentNode;
    private XBTTreeNode workNode = null;
    private XBACatalog catalog;
    private XBBlockType contextBlockType = null;
    private XBBlockType catalogBlockType = null;
    private ActionStateListener actionStateListener = null;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(AddBlockPanel.class);

    public AddBlockPanel() {
        initComponents();
        init();
    }

    private void init() {
        reloadBasicTypes();
        ((CardLayout) getLayout()).show(this, "type");
    }

    public void setApplication(XBApplication application) {
        this.application = application;
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setActionStateListener(ActionStateListener listener) {
        this.actionStateListener = listener;
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
        typePanel = new javax.swing.JPanel();
        dataRadioButton = new javax.swing.JRadioButton();
        basicTypeRadioButton = new javax.swing.JRadioButton();
        basicTypeComboBox = new javax.swing.JComboBox<>();
        contextTypeRadioButton = new javax.swing.JRadioButton();
        contextTypeSelectButton = new javax.swing.JButton();
        contextTypeTextField = new javax.swing.JTextField();
        catalogTypeRadioButton = new javax.swing.JRadioButton();
        catalogTypeSelectButton = new javax.swing.JButton();
        catalogTypeTextField = new javax.swing.JTextField();
        conditionsPanel = new javax.swing.JPanel();
        generateDeclarationCheckBox = new javax.swing.JCheckBox();
        prefillCheckBox = new javax.swing.JCheckBox();

        setLayout(new java.awt.CardLayout());

        typePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceBundle.getString("typePanel.border.title"))); // NOI18N

        blockTypeButtonGroup.add(dataRadioButton);
        dataRadioButton.setText(resourceBundle.getString("dataRadioButton.text")); // NOI18N
        dataRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        blockTypeButtonGroup.add(basicTypeRadioButton);
        basicTypeRadioButton.setSelected(true);
        basicTypeRadioButton.setText(resourceBundle.getString("basicTypeRadioButton.text")); // NOI18N
        basicTypeRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        basicTypeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                basicTypeRadioButtonStateChanged(evt);
            }
        });

        blockTypeButtonGroup.add(contextTypeRadioButton);
        contextTypeRadioButton.setText(resourceBundle.getString("contextTypeRadioButton.text")); // NOI18N
        contextTypeRadioButton.setEnabled(false);
        contextTypeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contextTypeRadioButtonStateChanged(evt);
            }
        });

        contextTypeSelectButton.setText(resourceBundle.getString("contextTypeSelectButton.text")); // NOI18N
        contextTypeSelectButton.setEnabled(false);
        contextTypeSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contextTypeSelectButtonActionPerformed(evt);
            }
        });

        contextTypeTextField.setEditable(false);

        blockTypeButtonGroup.add(catalogTypeRadioButton);
        catalogTypeRadioButton.setText(resourceBundle.getString("catalogTypeRadioButton.text")); // NOI18N
        catalogTypeRadioButton.setEnabled(false);
        catalogTypeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                catalogTypeRadioButtonStateChanged(evt);
            }
        });

        catalogTypeSelectButton.setText(resourceBundle.getString("catalogTypeSelectButton.text")); // NOI18N
        catalogTypeSelectButton.setEnabled(false);
        catalogTypeSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catalogTypeSelectButtonActionPerformed(evt);
            }
        });

        catalogTypeTextField.setEditable(false);
        catalogTypeTextField.setEnabled(false);

        javax.swing.GroupLayout typePanelLayout = new javax.swing.GroupLayout(typePanel);
        typePanel.setLayout(typePanelLayout);
        typePanelLayout.setHorizontalGroup(
            typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, typePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(contextTypeRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(catalogTypeRadioButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                    .addComponent(dataRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(basicTypeRadioButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(typePanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(catalogTypeTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(catalogTypeSelectButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, typePanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(basicTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, typePanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(contextTypeTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contextTypeSelectButton)))
                .addContainerGap())
        );
        typePanelLayout.setVerticalGroup(
            typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dataRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(basicTypeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(basicTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contextTypeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contextTypeSelectButton)
                    .addComponent(contextTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catalogTypeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(catalogTypeSelectButton)
                    .addComponent(catalogTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(typePanel, "type");

        generateDeclarationCheckBox.setSelected(true);
        generateDeclarationCheckBox.setText(resourceBundle.getString("generateDeclarationCheckBox.text")); // NOI18N

        prefillCheckBox.setText(resourceBundle.getString("prefillCheckBox.text")); // NOI18N
        prefillCheckBox.setEnabled(false);

        javax.swing.GroupLayout conditionsPanelLayout = new javax.swing.GroupLayout(conditionsPanel);
        conditionsPanel.setLayout(conditionsPanelLayout);
        conditionsPanelLayout.setHorizontalGroup(
            conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conditionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generateDeclarationCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(conditionsPanelLayout.createSequentialGroup()
                        .addComponent(prefillCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        conditionsPanelLayout.setVerticalGroup(
            conditionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(conditionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(generateDeclarationCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(prefillCheckBox)
                .addContainerGap(234, Short.MAX_VALUE))
        );

        add(conditionsPanel, "cond");
    }// </editor-fold>//GEN-END:initComponents

    private void basicTypeRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_basicTypeRadioButtonStateChanged
        basicTypeComboBox.setEnabled(basicTypeRadioButton.isSelected());
    }//GEN-LAST:event_basicTypeRadioButtonStateChanged

    private void contextTypeRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_contextTypeRadioButtonStateChanged
        contextTypeSelectButton.setEnabled(contextTypeRadioButton.isSelected());
        updateActionState();
    }//GEN-LAST:event_contextTypeRadioButtonStateChanged

    private void contextTypeSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contextTypeSelectButtonActionPerformed
        if (catalog != null) {
            FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
            DefaultControlPanel controlPanel = new DefaultControlPanel();
            final ContextTypeChoicePanel panel = new ContextTypeChoicePanel(catalog, parentNode);
            panel.setCanProceedListener(controlPanel.createEnablementListener());

            final DialogWrapper dialog = frameModule.createDialog(panel, controlPanel);
            controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
                switch (actionType) {
                    case OK: {
                        contextBlockType = panel.getBlockType();
                        XBCBlockDecl blockDecl = (XBCBlockDecl) ((XBDeclBlockType) contextBlockType).getBlockDecl();
                        XBCBlockSpec blockSpec = blockDecl.getBlockSpecRev().getParent();
                        //new XBDeclBlockType(new XBCBlockDecl();
                        XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                        String targetCaption = nameService.getItemNamePath(blockSpec);
                        if (targetCaption == null) {
                            targetCaption = "";
                        } else {
                            targetCaption += " ";
                        }
                        targetCaption += "(" + Long.toString(blockSpec.getId()) + ")";
                        contextTypeTextField.setText(targetCaption);

                        dialog.close();
                        dialog.dispose();
                        break;
                    }

                    case CANCEL: {
                        dialog.close();
                        dialog.dispose();
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected action type " + actionType.name());
                }
            });
            dialog.showCentered(this);
        }

        updateActionState();
    }//GEN-LAST:event_contextTypeSelectButtonActionPerformed

    private void catalogTypeRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_catalogTypeRadioButtonStateChanged
        catalogTypeTextField.setEnabled(catalogTypeRadioButton.isSelected());
        catalogTypeSelectButton.setEnabled(catalogTypeRadioButton.isSelected());
        if (actionStateListener != null) {
            actionStateListener.nextEnabled(catalogTypeRadioButton.isSelected());
        }
        updateActionState();
    }//GEN-LAST:event_catalogTypeRadioButtonStateChanged

    private void catalogTypeSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catalogTypeSelectButtonActionPerformed
        if (catalog != null) {
            FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
            final CatalogSelectRevPanel panel = new CatalogSelectRevPanel(catalog, CatalogItemType.BLOCK);
            panel.setApplication(application);

            DefaultControlPanel controlPanel = new DefaultControlPanel();
            final DialogWrapper dialog = frameModule.createDialog(panel, controlPanel);
            controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
                switch (actionType) {
                    case OK: {
                        XBCRev blockRev = panel.getTarget();
                        catalogBlockType = new XBDeclBlockType(new XBCBlockDecl((XBCBlockRev) blockRev, catalog));
                        XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                        String targetCaption = nameService.getItemNamePath(blockRev.getParent());
                        if (targetCaption == null) {
                            targetCaption = "";
                        } else {
                            targetCaption += " ";
                        }
                        targetCaption += "(" + Long.toString(blockRev.getId()) + ")";
                        catalogTypeTextField.setText(targetCaption);

                        dialog.close();
                        dialog.dispose();
                        break;
                    }

                    case CANCEL: {
                        dialog.close();
                        dialog.dispose();
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected action type " + actionType.name());
                }
            });
            dialog.showCentered(this);
        }

        updateActionState();
    }//GEN-LAST:event_catalogTypeSelectButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new AddBlockPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> basicTypeComboBox;
    private javax.swing.JRadioButton basicTypeRadioButton;
    private javax.swing.ButtonGroup blockTypeButtonGroup;
    private javax.swing.JRadioButton catalogTypeRadioButton;
    private javax.swing.JButton catalogTypeSelectButton;
    private javax.swing.JTextField catalogTypeTextField;
    private javax.swing.JPanel conditionsPanel;
    private javax.swing.JRadioButton contextTypeRadioButton;
    private javax.swing.JButton contextTypeSelectButton;
    private javax.swing.JTextField contextTypeTextField;
    private javax.swing.JRadioButton dataRadioButton;
    private javax.swing.JCheckBox generateDeclarationCheckBox;
    private javax.swing.JCheckBox prefillCheckBox;
    private javax.swing.JPanel typePanel;
    // End of variables declaration//GEN-END:variables

    @Nonnull
    public XBTTreeNode getWorkNode() {
        workNode = new XBTTreeNode();
        if (parentNode != null) {
            workNode.setContext(parentNode.getContext());
        }

        if (dataRadioButton.isSelected()) {
            workNode.setDataMode(XBBlockDataMode.DATA_BLOCK);
        } else if (basicTypeRadioButton.isSelected()) {
            workNode.setBlockType(new XBFixedBlockType(XBBasicBlockType.valueOf(basicTypeComboBox.getSelectedIndex())));
        } else if (contextTypeRadioButton.isSelected()) {
            workNode.setBlockType(contextBlockType);
        } else if (catalogTypeRadioButton.isSelected()) {
            if (generateDeclarationCheckBox.isSelected()) {
                XBPSerialWriter writer = new XBPSerialWriter(new XBTListenerToEventListener(new XBTTreeReader(workNode)));
                XBDeclaration newDeclaration = new XBDeclaration(((XBDBlockType) catalogBlockType).getBlockDecl());
                writer.write(newDeclaration);
                XBTTreeNode newNode = new XBTTreeNode();
                newNode.setBlockType(catalogBlockType);
                workNode.setChildAt(newNode, workNode.getChildrenCount());
            } else {
                workNode.setBlockType(catalogBlockType);
            }
        }

        return workNode;
    }

    public void setWorkNode(XBTTreeNode workNode) {
        this.workNode = workNode;
    }

    private void updateActionState() {
        if (actionStateListener != null) {
            actionStateListener.finishEnabled(
                    !(contextTypeRadioButton.isSelected() || catalogTypeRadioButton.isSelected())
                    || (contextBlockType != null && contextTypeRadioButton.isSelected())
                    || (catalogBlockType != null && catalogTypeRadioButton.isSelected()));
        }
    }

    public void setParentNode(@Nullable XBTTreeNode parentNode) {
        this.parentNode = parentNode;
        contextTypeRadioButton.setEnabled(parentNode != null && parentNode.getContext().getGroupsCount() > 1);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        reloadBasicTypes();
        fireCatalogUpdate();
    }

    private void fireCatalogUpdate() {
        catalogTypeRadioButton.setEnabled(catalog != null);
    }

    private void reloadBasicTypes() {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) basicTypeComboBox.getModel();
        model.removeAllElements();
        if (catalog != null) {
            Long[] basicGroupPath = {0L, 0L};
            List<XBBlockDecl> list = catalog.getBlocks(((XBCGroupDecl) catalog.findGroupTypeByPath(basicGroupPath, 0)).getGroupSpecRev().getParent());

            XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
            for (XBBlockDecl decl : list) {
                model.addElement(nameService.getDefaultText(((XBCBlockDecl) decl).getBlockSpecRev().getParent()));
            }
        }
    }

    public interface ActionStateListener {

        void finishEnabled(boolean enablement);

        void nextEnabled(boolean enablement);
    }
}
