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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.editor.xbup.viewer.DocumentTab;
import org.exbin.xbup.core.block.XBTBlock;

/**
 * Block definition panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlockDefinitionPanel extends javax.swing.JPanel {

    private XBApplication application;
    private XBACatalog catalog;
    private XBPropertyTablePanel propertiesPanel;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(BlockDefinitionPanel.class);

    public BlockDefinitionPanel() {
        initComponents();
        init();
    }

    private void init() {
        propertiesPanel = new XBPropertyTablePanel();
        panel.add(propertiesPanel, java.awt.BorderLayout.CENTER);
        panel.invalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        propertyPopupMenu = new javax.swing.JPopupMenu();
        popupItemOpenMenuItem = new javax.swing.JMenuItem();
        popupItemAddMenuItem = new javax.swing.JMenuItem();
        popupItemModifyMenuItem = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        popupUndoMenuItem = new javax.swing.JMenuItem();
        popupRedoMenuItem = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        popupCutMenuItem = new javax.swing.JMenuItem();
        popupCopyMenuItem = new javax.swing.JMenuItem();
        popupPasteMenuItem = new javax.swing.JMenuItem();
        popupDeleteMenuItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        popupSelectAllMenuItem = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        popupItemPropertiesMenuItem = new javax.swing.JMenuItem();
        panel = new javax.swing.JPanel();
        itemInfoHeaderPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        descLabel = new javax.swing.JLabel();
        descTextField = new javax.swing.JTextField();
        nameTextField = new javax.swing.JTextField();

        propertyPopupMenu.setName("propertyPopupMenu"); // NOI18N

        popupItemOpenMenuItem.setText(resourceBundle.getString("actionItemOpen.text")); // NOI18N
        popupItemOpenMenuItem.setName("popupItemOpenMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemOpenMenuItem);

        popupItemAddMenuItem.setText(resourceBundle.getString("popupItemAddMenuItem.text")); // NOI18N
        popupItemAddMenuItem.setToolTipText(resourceBundle.getString("popupItemAddMenuItem.toolTipText")); // NOI18N
        popupItemAddMenuItem.setName("popupItemAddMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemAddMenuItem);

        popupItemModifyMenuItem.setText(resourceBundle.getString("popupItemModifyMenuItem.text")); // NOI18N
        popupItemModifyMenuItem.setToolTipText(resourceBundle.getString("popupItemModifyMenuItem.toolTipText")); // NOI18N
        popupItemModifyMenuItem.setName("popupItemModifyMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemModifyMenuItem);

        jSeparator14.setName("jSeparator14"); // NOI18N
        propertyPopupMenu.add(jSeparator14);

        popupUndoMenuItem.setText(resourceBundle.getString("popupUndoMenuItem.text")); // NOI18N
        popupUndoMenuItem.setToolTipText(resourceBundle.getString("popupUndoMenuItem.toolTipText")); // NOI18N
        popupUndoMenuItem.setName("popupUndoMenuItem"); // NOI18N
        propertyPopupMenu.add(popupUndoMenuItem);

        popupRedoMenuItem.setText(resourceBundle.getString("popupRedoMenuItem.text")); // NOI18N
        popupRedoMenuItem.setToolTipText(resourceBundle.getString("popupRedoMenuItem.toolTipText")); // NOI18N
        popupRedoMenuItem.setName("popupRedoMenuItem"); // NOI18N
        propertyPopupMenu.add(popupRedoMenuItem);

        jSeparator10.setName("jSeparator10"); // NOI18N
        propertyPopupMenu.add(jSeparator10);

        popupCutMenuItem.setText("Cut"); // NOI18N
        popupCutMenuItem.setName("popupCutMenuItem"); // NOI18N
        propertyPopupMenu.add(popupCutMenuItem);

        popupCopyMenuItem.setText("Copy"); // NOI18N
        popupCopyMenuItem.setName("popupCopyMenuItem"); // NOI18N
        propertyPopupMenu.add(popupCopyMenuItem);

        popupPasteMenuItem.setText("Paste"); // NOI18N
        popupPasteMenuItem.setName("popupPasteMenuItem"); // NOI18N
        propertyPopupMenu.add(popupPasteMenuItem);

        popupDeleteMenuItem.setText("Delete"); // NOI18N
        popupDeleteMenuItem.setName("popupDeleteMenuItem"); // NOI18N
        propertyPopupMenu.add(popupDeleteMenuItem);

        jSeparator7.setName("jSeparator7"); // NOI18N
        propertyPopupMenu.add(jSeparator7);

        popupSelectAllMenuItem.setText("Select All"); // NOI18N
        popupSelectAllMenuItem.setName("popupSelectAllMenuItem"); // NOI18N
        propertyPopupMenu.add(popupSelectAllMenuItem);

        jSeparator16.setName("jSeparator16"); // NOI18N
        propertyPopupMenu.add(jSeparator16);

        popupItemPropertiesMenuItem.setText(resourceBundle.getString("popupItemPropertiesMenuItem.text")); // NOI18N
        popupItemPropertiesMenuItem.setName("popupItemPropertiesMenuItem"); // NOI18N
        propertyPopupMenu.add(popupItemPropertiesMenuItem);

        setName("Form"); // NOI18N

        panel.setName("panel"); // NOI18N
        panel.setLayout(new java.awt.BorderLayout());

        itemInfoHeaderPanel.setName("itemInfoHeaderPanel"); // NOI18N

        nameLabel.setText(resourceBundle.getString("nameLabel.text")); // NOI18N

        descLabel.setText(resourceBundle.getString("descLabel.text")); // NOI18N
        descLabel.setName("descLabel"); // NOI18N

        descTextField.setEditable(false);
        descTextField.setName("descTextField"); // NOI18N

        nameTextField.setEditable(false);
        nameTextField.setName("nameTextField"); // NOI18N

        javax.swing.GroupLayout itemInfoHeaderPanelLayout = new javax.swing.GroupLayout(itemInfoHeaderPanel);
        itemInfoHeaderPanel.setLayout(itemInfoHeaderPanelLayout);
        itemInfoHeaderPanelLayout.setHorizontalGroup(
            itemInfoHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemInfoHeaderPanelLayout.createSequentialGroup()
                .addGroup(itemInfoHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descLabel)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(itemInfoHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(nameTextField))
                .addContainerGap())
        );
        itemInfoHeaderPanelLayout.setVerticalGroup(
            itemInfoHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemInfoHeaderPanelLayout.createSequentialGroup()
                .addGroup(itemInfoHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemInfoHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descLabel)
                    .addComponent(descTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel.add(itemInfoHeaderPanel, java.awt.BorderLayout.NORTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setApplication(XBApplication application) {
        this.application = application;
        propertiesPanel.setApplication(application);
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        propertiesPanel.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        propertiesPanel.setPluginRepository(pluginRepository);
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new BlockDefinitionPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descLabel;
    private javax.swing.JTextField descTextField;
    private javax.swing.JPanel itemInfoHeaderPanel;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel panel;
    private javax.swing.JMenuItem popupCopyMenuItem;
    private javax.swing.JMenuItem popupCutMenuItem;
    private javax.swing.JMenuItem popupDeleteMenuItem;
    private javax.swing.JMenuItem popupItemAddMenuItem;
    private javax.swing.JMenuItem popupItemModifyMenuItem;
    private javax.swing.JMenuItem popupItemOpenMenuItem;
    private javax.swing.JMenuItem popupItemPropertiesMenuItem;
    private javax.swing.JMenuItem popupPasteMenuItem;
    private javax.swing.JMenuItem popupRedoMenuItem;
    private javax.swing.JMenuItem popupSelectAllMenuItem;
    private javax.swing.JMenuItem popupUndoMenuItem;
    private javax.swing.JPopupMenu propertyPopupMenu;
    // End of variables declaration//GEN-END:variables

    public void setBlock(XBTBlock block) {
        propertiesPanel.setBlock(block);
        nameTextField.setText(getCaption((XBTTreeNode) block));
        descTextField.setText(getDescription((XBTTreeNode) block));
    }

    public void setActiveViewer(DocumentTab viewer) {
        propertiesPanel.setActiveViewer(viewer);
    }

    private String getCaption(XBTTreeNode node) {
        if (node != null) {
            if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                return "Data Block";
            }
            XBBlockType blockType = node.getBlockType();
            XBCBlockDecl blockDecl = (XBCBlockDecl) node.getBlockDecl();
            if (blockDecl instanceof XBCBlockDecl) {
                XBCBlockSpec blockSpec = ((XBCBlockDecl) blockDecl).getBlockSpecRev().getParent();
                if (catalog != null) {
                    XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
                    return nameService.getDefaultText(blockSpec);
                }
            }

            if (blockType == null) {
                return "Unknown";
            }
            return "Unknown" + " (" + Integer.toString(((XBFBlockType) blockType).getGroupID().getInt()) + ", " + Integer.toString(((XBFBlockType) blockType).getBlockID().getInt()) + ")";
        }

        return "";
    }

    @Nonnull
    private String getDescription(XBTTreeNode node) {
        if (node != null) {
            XBBlockDecl decl = node.getBlockDecl();
            if (catalog != null && decl instanceof XBCBlockDecl) {
                XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpecRev().getParent();
                XBCXDescService descService = catalog.getCatalogService(XBCXDescService.class);
                XBCXDesc desc = descService.getDefaultItemDesc(spec);
                return desc == null ? "" : desc.getText();
            }
        }

        return "";
    }
}
