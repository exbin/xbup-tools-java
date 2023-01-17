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
package org.exbin.framework.xbup.catalog.item.gui;

import org.exbin.framework.xbup.catalog.item.file.gui.CatalogFilesTableModel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import org.exbin.framework.data.model.CatalogDefsTableItem;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.data.model.CatalogRevsTableModel;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;

/**
 * Panel for basic XBItem viewing/edit.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogItemPanel extends javax.swing.JPanel {

    private XBCItem item;

    private final CatalogDefsTableModel defsModel;
    private final CatalogRevsTableModel revsModel;
    private final CatalogFilesTableModel filesModel;
    private XBCNodeService nodeService;
    private XBCXNameService nameService;
    private XBCXDescService descService;
    private XBCXStriService striService;
    private XBCXHDocService hDocService;
    private XBCXFileService fileService;
    private XBCXIconService iconService;
    private XBCXName itemName;
    private XBCXDesc itemDesc;
    private XBCXHDoc itemHDoc;
    private XBCXIcon itemIcon;
    private JumpActionListener jumpActionListener = null;
    private XBACatalog catalog;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CatalogItemPanel.class);

    public CatalogItemPanel() {
        defsModel = new CatalogDefsTableModel();
        revsModel = new CatalogRevsTableModel();
        filesModel = new CatalogFilesTableModel();
        initComponents();
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        definitionPopupMenu = new javax.swing.JPopupMenu();
        jumpToMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        filesPopupMenu = new javax.swing.JPopupMenu();
        exportFileMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mainTabbedPane = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        basicItemScrollPane = new javax.swing.JScrollPane();
        basicItemDataPanel = new javax.swing.JPanel();
        iconPanel = new javax.swing.JPanel();
        itemIconLabel = new javax.swing.JLabel();
        itemTitleLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        itemNameLabel = new javax.swing.JLabel();
        itemNameTextField = new javax.swing.JTextField();
        itemPathLabel = new javax.swing.JLabel();
        itemPathTextField = new javax.swing.JTextField();
        itemTypeLabel = new javax.swing.JLabel();
        itemTypeTextField = new javax.swing.JTextField();
        itemDescriptionLabel = new javax.swing.JLabel();
        itemDescriptionTextField = new javax.swing.JTextField();
        fullPathLabel = new javax.swing.JLabel();
        fullPathTextField = new javax.swing.JTextField();
        itemCreatedLabel = new javax.swing.JLabel();
        itemCreatedTextField = new javax.swing.JTextField();
        documentationPanel = new javax.swing.JPanel();
        itemHDocScrollPane = new javax.swing.JScrollPane();
        itemHDocEditorPane = new javax.swing.JEditorPane();
        revisionsPanel = new javax.swing.JPanel();
        itemRevisionsScrollPane = new javax.swing.JScrollPane();
        itemRevisionsTable = new javax.swing.JTable();
        definitionPanel = new javax.swing.JPanel();
        itemDefinitionScrollPane = new javax.swing.JScrollPane();
        itemDefinitionTable = new javax.swing.JTable();
        filesPanel = new javax.swing.JPanel();
        itemFilesScrollPane = new javax.swing.JScrollPane();
        itemFilesTable = new javax.swing.JTable();

        definitionPopupMenu.setName("definitionPopupMenu"); // NOI18N

        jumpToMenuItem.setText("Jump To Type");
        jumpToMenuItem.setToolTipText("Navigate to type of selected definition row");
        jumpToMenuItem.setEnabled(false);
        jumpToMenuItem.setName("jumpToMenuItem"); // NOI18N
        jumpToMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumpToMenuItemActionPerformed(evt);
            }
        });
        definitionPopupMenu.add(jumpToMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        definitionPopupMenu.add(jSeparator2);

        filesPopupMenu.setName("filesPopupMenu"); // NOI18N

        exportFileMenuItem.setText("Export...");
        exportFileMenuItem.setToolTipText("Export content of the remote file to local file");
        exportFileMenuItem.setEnabled(false);
        exportFileMenuItem.setName("exportFileMenuItem"); // NOI18N
        exportFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportFileMenuItemActionPerformed(evt);
            }
        });
        filesPopupMenu.add(exportFileMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        filesPopupMenu.add(jSeparator3);

        setLayout(new java.awt.BorderLayout());

        mainTabbedPane.setName("mainTabbedPane"); // NOI18N

        generalPanel.setName("generalPanel"); // NOI18N
        generalPanel.setLayout(new java.awt.BorderLayout());

        basicItemScrollPane.setName("basicItemScrollPane"); // NOI18N

        basicItemDataPanel.setName("basicItemDataPanel"); // NOI18N

        iconPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        iconPanel.setName("iconPanel"); // NOI18N
        iconPanel.setLayout(new java.awt.BorderLayout());

        itemIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/xbup/catalog/item/resources/images/empty.png"))); // NOI18N
        itemIconLabel.setName("itemIconLabel"); // NOI18N
        iconPanel.add(itemIconLabel, java.awt.BorderLayout.CENTER);

        itemTitleLabel.setText("Unknown item");
        itemTitleLabel.setName("itemTitleLabel"); // NOI18N

        jSeparator1.setName("jSeparator1"); // NOI18N

        itemNameLabel.setText(resourceBundle.getString("itemNameLabel.text")); // NOI18N
        itemNameLabel.setName("itemNameLabel"); // NOI18N

        itemNameTextField.setEditable(false);
        itemNameTextField.setName("itemNameTextField"); // NOI18N

        itemPathLabel.setText("StringId");
        itemPathLabel.setName("itemPathLabel"); // NOI18N

        itemPathTextField.setEditable(false);
        itemPathTextField.setName("itemPathTextField"); // NOI18N

        itemTypeLabel.setText("Type");
        itemTypeLabel.setName("itemTypeLabel"); // NOI18N

        itemTypeTextField.setEditable(false);
        itemTypeTextField.setName("itemTypeTextField"); // NOI18N

        itemDescriptionLabel.setText(resourceBundle.getString("itemDescriptionLabel.text")); // NOI18N
        itemDescriptionLabel.setName("itemDescriptionLabel"); // NOI18N

        itemDescriptionTextField.setEditable(false);
        itemDescriptionTextField.setName("itemDescriptionTextField"); // NOI18N

        fullPathLabel.setText(resourceBundle.getString("fullPathLabel.text")); // NOI18N
        fullPathLabel.setName("fullPathLabel"); // NOI18N

        fullPathTextField.setEditable(false);
        fullPathTextField.setName("fullPathTextField"); // NOI18N
        fullPathTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fullPathTextFieldFocusGained(evt);
            }
        });

        itemCreatedLabel.setText(resourceBundle.getString("itemCreatedLabel.text")); // NOI18N
        itemCreatedLabel.setName("itemCreatedLabel"); // NOI18N

        itemCreatedTextField.setEditable(false);
        itemCreatedTextField.setName("itemCreatedTextField"); // NOI18N

        javax.swing.GroupLayout basicItemDataPanelLayout = new javax.swing.GroupLayout(basicItemDataPanel);
        basicItemDataPanel.setLayout(basicItemDataPanelLayout);
        basicItemDataPanelLayout.setHorizontalGroup(
            basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicItemDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(basicItemDataPanelLayout.createSequentialGroup()
                        .addComponent(iconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
                    .addComponent(jSeparator1)
                    .addGroup(basicItemDataPanelLayout.createSequentialGroup()
                        .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(itemDescriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fullPathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemCreatedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemPathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fullPathTextField)
                            .addComponent(itemCreatedTextField)
                            .addComponent(itemPathTextField)
                            .addComponent(itemTypeTextField)
                            .addComponent(itemDescriptionTextField)
                            .addComponent(itemNameTextField))))
                .addContainerGap())
        );
        basicItemDataPanelLayout.setVerticalGroup(
            basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicItemDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemPathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemDescriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fullPathTextField)
                    .addComponent(fullPathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicItemDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemCreatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCreatedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        basicItemScrollPane.setViewportView(basicItemDataPanel);

        generalPanel.add(basicItemScrollPane, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Basic", generalPanel);

        documentationPanel.setName("documentationPanel"); // NOI18N

        itemHDocScrollPane.setName("itemHDocScrollPane"); // NOI18N

        itemHDocEditorPane.setEditable(false);
        itemHDocEditorPane.setContentType("text/html"); // NOI18N
        itemHDocEditorPane.setName("itemHDocEditorPane"); // NOI18N
        itemHDocScrollPane.setViewportView(itemHDocEditorPane);

        javax.swing.GroupLayout documentationPanelLayout = new javax.swing.GroupLayout(documentationPanel);
        documentationPanel.setLayout(documentationPanelLayout);
        documentationPanelLayout.setHorizontalGroup(
            documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemHDocScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
        );
        documentationPanelLayout.setVerticalGroup(
            documentationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemHDocScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );

        mainTabbedPane.addTab("Documentation", documentationPanel);

        revisionsPanel.setName("revisionsPanel"); // NOI18N
        revisionsPanel.setLayout(new java.awt.BorderLayout());

        itemRevisionsScrollPane.setName("itemRevisionsScrollPane"); // NOI18N

        itemRevisionsTable.setModel(revsModel);
        itemRevisionsTable.setName("itemRevisionsTable"); // NOI18N
        itemRevisionsScrollPane.setViewportView(itemRevisionsTable);

        revisionsPanel.add(itemRevisionsScrollPane, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Revisions", revisionsPanel);

        definitionPanel.setName("definitionPanel"); // NOI18N
        definitionPanel.setLayout(new java.awt.BorderLayout());

        itemDefinitionScrollPane.setComponentPopupMenu(definitionPopupMenu);
        itemDefinitionScrollPane.setName("itemDefinitionScrollPane"); // NOI18N

        itemDefinitionTable.setModel(defsModel);
        itemDefinitionTable.setComponentPopupMenu(definitionPopupMenu);
        itemDefinitionTable.setName("itemDefinitionTable"); // NOI18N
        itemDefinitionTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemDefinitionScrollPane.setViewportView(itemDefinitionTable);

        definitionPanel.add(itemDefinitionScrollPane, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Definition", definitionPanel);

        filesPanel.setName("filesPanel"); // NOI18N
        filesPanel.setLayout(new java.awt.BorderLayout());

        itemFilesScrollPane.setComponentPopupMenu(filesPopupMenu);
        itemFilesScrollPane.setName("itemFilesScrollPane"); // NOI18N

        itemFilesTable.setModel(filesModel);
        itemFilesTable.setComponentPopupMenu(filesPopupMenu);
        itemFilesTable.setName("itemFilesTable"); // NOI18N
        itemFilesScrollPane.setViewportView(itemFilesTable);

        filesPanel.add(itemFilesScrollPane, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Files", filesPanel);

        add(mainTabbedPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jumpToMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumpToMenuItemActionPerformed
        CatalogDefsTableModel model = (CatalogDefsTableModel) itemDefinitionTable.getModel();
        CatalogDefsTableItem rowItem = model.getRowItem(itemDefinitionTable.getSelectedRow());
        XBCRev target = rowItem.getTarget();
        if (jumpActionListener != null) {
            jumpActionListener.jumpToRev(target);
        }
    }//GEN-LAST:event_jumpToMenuItemActionPerformed

    private void exportFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportFileMenuItemActionPerformed
        XBCXFile file = filesModel.getItem(itemFilesTable.getSelectedRow());
        JFileChooser exportFileChooser = new JFileChooser(file.getFilename());
        exportFileChooser.setAcceptAllFileFilterUsed(true);
        if (exportFileChooser.showSaveDialog(WindowUtils.getFrame(this)) == JFileChooser.APPROVE_OPTION) {
            FileOutputStream fileStream;
            try {
                fileStream = new FileOutputStream(exportFileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileStream.write(file.getContent());
                } finally {
                    fileStream.close();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CatalogItemPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CatalogItemPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_exportFileMenuItemActionPerformed

    private void fullPathTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fullPathTextFieldFocusGained
        fullPathTextField.selectAll();
    }//GEN-LAST:event_fullPathTextFieldFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel basicItemDataPanel;
    private javax.swing.JScrollPane basicItemScrollPane;
    private javax.swing.JPanel definitionPanel;
    private javax.swing.JPopupMenu definitionPopupMenu;
    private javax.swing.JPanel documentationPanel;
    private javax.swing.JMenuItem exportFileMenuItem;
    private javax.swing.JPanel filesPanel;
    private javax.swing.JPopupMenu filesPopupMenu;
    private javax.swing.JLabel fullPathLabel;
    private javax.swing.JTextField fullPathTextField;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JPanel iconPanel;
    private javax.swing.JLabel itemCreatedLabel;
    private javax.swing.JTextField itemCreatedTextField;
    private javax.swing.JScrollPane itemDefinitionScrollPane;
    private javax.swing.JTable itemDefinitionTable;
    private javax.swing.JLabel itemDescriptionLabel;
    private javax.swing.JTextField itemDescriptionTextField;
    private javax.swing.JScrollPane itemFilesScrollPane;
    private javax.swing.JTable itemFilesTable;
    private javax.swing.JEditorPane itemHDocEditorPane;
    private javax.swing.JScrollPane itemHDocScrollPane;
    private javax.swing.JLabel itemIconLabel;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JTextField itemNameTextField;
    private javax.swing.JLabel itemPathLabel;
    private javax.swing.JTextField itemPathTextField;
    private javax.swing.JScrollPane itemRevisionsScrollPane;
    private javax.swing.JTable itemRevisionsTable;
    private javax.swing.JLabel itemTitleLabel;
    private javax.swing.JLabel itemTypeLabel;
    private javax.swing.JTextField itemTypeTextField;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem jumpToMenuItem;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JPanel revisionsPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new CatalogItemPanel());
    }

    public XBCItem getItem() {
        return item;
    }

    public void setItem(XBCItem item) {
        this.item = item;
        reloadItem();
    }

    private CatalogItemType getItemType() {
        CatalogItemType itemType = CatalogItemType.NODE;
        if (item instanceof XBCBlockSpec) {
            itemType = CatalogItemType.BLOCK;
        } else if (item instanceof XBCGroupSpec) {
            itemType = CatalogItemType.GROUP;
        } else if (item instanceof XBCFormatSpec) {
            itemType = CatalogItemType.FORMAT;
        }

        return itemType;
    }

    private void reloadItem() {
        if (item != null) {
            boolean isSpec = item instanceof XBCSpec;
            if (isSpec) {
                if (item instanceof XBCFormatSpec) {
                    itemTypeTextField.setText("Format Specification");
                } else if (item instanceof XBCGroupSpec) {
                    itemTypeTextField.setText("Group Specification");
                } else if (item instanceof XBCBlockSpec) {
                    itemTypeTextField.setText("Block Specification");
                } else {
                    itemTypeTextField.setText("Failed to recognize");
                }
                // TODO: Fix button "Jump" enablement
            } else {
                itemTypeTextField.setText("Node");
                // jumpToDefButton.setEnabled(itemDefinitionTable.getSelectedRow()>=0);
            }
            Long xbIndex = item.getXBIndex();
//            jLabel6.setText((xbIndex==null)?"":xbIndex.toString());
            Long itemId = ((XBCItem) item).getId();
//            jLabel7.setText((itemId==null)?"":itemId.toString());
            itemName = nameService.getDefaultItemName(item);
            itemNameTextField.setText((itemName == null) ? "" : itemName.getText());
            itemDesc = descService.getDefaultItemDesc(item);
            itemDescriptionTextField.setText((itemDesc == null) ? "" : itemDesc.getText());
            itemTitleLabel.setText(((itemName == null) ? "-" : itemName.getText()) + " (" + ((xbIndex == null) ? "" : xbIndex.toString()) + ")");
            String fullPath = "";
            if ((item instanceof XBCSpec) || (item instanceof XBCNode)) {
                Long[] specPath = item instanceof XBCNode
                        ? nodeService.getNodeXBPath((XBCNode) item)
                        : catalog.getSpecPath((XBCSpec) item);
                if (specPath != null) {
                    StringBuilder pathBuilder = new StringBuilder();
                    for (Long pathIndex : specPath) {
                        if (pathBuilder.length() > 0) {
                            pathBuilder.append(", ");
                        }
                        pathBuilder.append(pathIndex);
                    }

                    fullPath = "{" + pathBuilder.toString() + "}";
                }
            }

            fullPathTextField.setText(fullPath);

            XBCXStri stringId = striService.getItemStringId(item);
            itemPathTextField.setText(stringId == null ? "" : stringId.getText());

            itemHDocEditorPane.setText(null);
            itemHDoc = hDocService.getDocumentation(item);
            if (itemHDoc != null && itemHDoc.getDocFile() != null) {
                XBCXFile itemHDocFile = itemHDoc.getDocFile();
                InputStream fileStream = fileService.getFile(itemHDocFile);
                try {
                    itemHDocEditorPane.getEditorKit().createDefaultDocument();
                    itemHDocEditorPane.read(fileStream, itemHDocEditorPane.getDocument());

                } catch (RuntimeException | IOException ex) {
                    Logger.getLogger(CatalogItemPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            itemIcon = iconService.getDefaultIcon(item);
            if (itemIcon != null) {
                ImageIcon icon = iconService.getDefaultImageIcon(item);
                itemIconLabel.setIcon(icon);
            } else {
                itemIconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/xbup/catalog/item/resources/images/empty.png")));
            }
//            itemHD
        } else {
            itemTitleLabel.setText("unknown");
            itemTypeTextField.setText("unknown");
            itemNameTextField.setText("");
            itemDescriptionTextField.setText("");
            itemHDocEditorPane.setText("");
            fullPathTextField.setText("");
        }

        revsModel.setSpec(item instanceof XBCSpec ? (XBCSpec) item : null);
        defsModel.setCatalogItem(item);
        defsModel.setRevsModel(revsModel);
        definitionTableUpdated();
        filesModel.setNode(item instanceof XBCNode ? (XBCNode) item : null);
        itemFilesTable.revalidate();
        fileTableUpdated();
    }

    public JumpActionListener getJumpActionListener() {
        return jumpActionListener;
    }

    public void setJumpActionListener(JumpActionListener jumpActionListener) {
        this.jumpActionListener = jumpActionListener;
    }

//    public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
//        JPopupMenu defaultTextPopupMenu = mainFrameManagement.getDefaultTextPopupMenu();
//        for (Component menuComponent : defaultTextPopupMenu.getComponents()) {
//            definitionPopupMenu.add(mainFrameManagement.duplicateMenuComponent(menuComponent));
//            filesPopupMenu.add(mainFrameManagement.duplicateMenuComponent(menuComponent));
//        }
//
//        mainFrameManagement.initPopupMenu(definitionPopupMenu);
//        mainFrameManagement.initPopupMenu(filesPopupMenu);
//        itemDefinitionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    definitionTableUpdated();
//                }
//            }
//        });
//        itemFilesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    fileTableUpdated();
//                }
//            }
//        });
//    }

    private void definitionTableUpdated() {
        if (jumpActionListener != null) {
            int rowIndex = itemDefinitionTable.getSelectedRow();
            if (rowIndex >= 0) {
                if (defsModel.getRowItem(rowIndex).getTarget() != null) {
                    jumpToMenuItem.setEnabled(true);
                    return;
                }
            }
        }

        jumpToMenuItem.setEnabled(false);
    }

    private void fileTableUpdated() {
        int rowIndex = itemFilesTable.getSelectedRow();
        exportFileMenuItem.setEnabled(rowIndex >= 0);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        nodeService = catalog == null ? null : catalog.getCatalogService(XBCNodeService.class);
        striService = catalog == null ? null : catalog.getCatalogService(XBCXStriService.class);
        nameService = catalog == null ? null : catalog.getCatalogService(XBCXNameService.class);
        descService = catalog == null ? null : catalog.getCatalogService(XBCXDescService.class);
        hDocService = catalog == null ? null : catalog.getCatalogService(XBCXHDocService.class);
        fileService = catalog == null ? null : catalog.getCatalogService(XBCXFileService.class);
        iconService = catalog == null ? null : catalog.getCatalogService(XBCXIconService.class);

        defsModel.setCatalog(catalog);
        revsModel.setCatalog(catalog);
        filesModel.setCatalog(catalog);
        reloadItem();
    }

    public interface JumpActionListener {

        void jumpToRev(XBCRev rev);
    }
}
