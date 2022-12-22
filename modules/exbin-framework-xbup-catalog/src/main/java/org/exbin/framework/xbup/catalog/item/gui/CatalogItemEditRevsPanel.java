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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.data.model.CatalogRevsTableModel;
import org.exbin.framework.data.model.CatalogRevsTableItem;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.WindowUtils.DialogWrapper;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.framework.utils.gui.DefaultControlPanel;
import org.exbin.xbup.catalog.entity.XBERev;
import org.exbin.xbup.catalog.entity.service.XBEXDescService;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;

/**
 * XBManager catalog item edit revisions panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class CatalogItemEditRevsPanel extends javax.swing.JPanel {

    private XBApplication application;
    private XBACatalog catalog;
    private XBCItem catalogItem;
    private XBCRevService revService;
    private final CatalogRevsTableModel revsModel;
    private CatalogDefsTableModel defsModel;
    private List<CatalogRevsTableItem> removeList;
    private List<CatalogRevsTableItem> updateList;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CatalogItemEditRevsPanel.class);

    public CatalogItemEditRevsPanel() {
        revsModel = new CatalogRevsTableModel();
        initComponents();

        itemRevisionsTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                updateItemStatus();
            }
        });

        updateItemStatus();
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

        itemRevisionsScrollPane = new javax.swing.JScrollPane();
        itemRevisionsTable = new javax.swing.JTable();
        revisionsControlPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        revisionsControlSidePanel = new javax.swing.JPanel();
        modifyButton = new javax.swing.JButton();
        removeDefButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        itemRevisionsTable.setModel(revsModel);
        itemRevisionsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemRevisionsScrollPane.setViewportView(itemRevisionsTable);

        add(itemRevisionsScrollPane, java.awt.BorderLayout.CENTER);

        addButton.setText(resourceBundle.getString("addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout revisionsControlPanelLayout = new javax.swing.GroupLayout(revisionsControlPanel);
        revisionsControlPanel.setLayout(revisionsControlPanelLayout);
        revisionsControlPanelLayout.setHorizontalGroup(
            revisionsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(revisionsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addButton)
                .addContainerGap(312, Short.MAX_VALUE))
        );
        revisionsControlPanelLayout.setVerticalGroup(
            revisionsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, revisionsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(revisionsControlPanel, java.awt.BorderLayout.SOUTH);

        modifyButton.setText(resourceBundle.getString("modifyButton.text")); // NOI18N
        modifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyButtonActionPerformed(evt);
            }
        });

        removeDefButton.setText(resourceBundle.getString("removeDefButton.text")); // NOI18N
        removeDefButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeDefButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout revisionsControlSidePanelLayout = new javax.swing.GroupLayout(revisionsControlSidePanel);
        revisionsControlSidePanel.setLayout(revisionsControlSidePanelLayout);
        revisionsControlSidePanelLayout.setHorizontalGroup(
            revisionsControlSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(revisionsControlSidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(revisionsControlSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modifyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeDefButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        revisionsControlSidePanelLayout.setVerticalGroup(
            revisionsControlSidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(revisionsControlSidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modifyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeDefButton)
                .addContainerGap(183, Short.MAX_VALUE))
        );

        add(revisionsControlSidePanel, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
        CatalogSpecRevEditorPanel panel = new CatalogSpecRevEditorPanel();
        panel.setRevItem(new CatalogRevsTableItem());
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final DialogWrapper dialog = frameModule.createDialog(panel, controlPanel);
        frameModule.setDialogTitle(dialog, panel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                long maxXbIndex = 0;
                if (revsModel.getRowCount() > 0) {
                    CatalogRevsTableItem rewItem = revsModel.getRowItem(revsModel.getRowCount() - 1);
                    if (rewItem.getXbIndex() >= maxXbIndex) {
                        maxXbIndex = rewItem.getXbIndex() + 1;
                    }
                }

                CatalogRevsTableItem revItem = panel.getRevItem();
                revItem.setXbIndex(maxXbIndex);
                if (!updateList.contains(revItem)) {
                    updateList.add(revItem);
                }

                revsModel.getRevs().add(revItem);
                revsModel.fireTableDataChanged();
                defsModel.updateDefRevisions();
                updateItemStatus();
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }//GEN-LAST:event_addButtonActionPerformed

    private void modifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyButtonActionPerformed
        int selectedRow = itemRevisionsTable.getSelectedRow();
        CatalogRevsTableItem row = revsModel.getRowItem(selectedRow);

        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
        CatalogSpecRevEditorPanel panel = new CatalogSpecRevEditorPanel();
        panel.setRevItem(row);
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final DialogWrapper dialog = frameModule.createDialog(panel, controlPanel);
        frameModule.setDialogTitle(dialog, panel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                CatalogRevsTableItem revItem = panel.getRevItem();
                if (!updateList.contains(revItem)) {
                    updateList.add(revItem);
                }

                defsModel.updateDefRevisions();
                updateItemStatus();
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }//GEN-LAST:event_modifyButtonActionPerformed

    private void removeDefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDefButtonActionPerformed
        int selectedRow = itemRevisionsTable.getSelectedRow();
        CatalogRevsTableItem revItem = revsModel.getRowItem(selectedRow);

        if (updateList.contains(revItem)) {
            updateList.remove(revItem);
        }

        removeList.add(revItem);
        revsModel.getRevs().remove(revItem);
        revsModel.fireTableDataChanged();
        defsModel.updateDefRevisions();
        updateItemStatus();
    }//GEN-LAST:event_removeDefButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new CatalogItemEditRevsPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JScrollPane itemRevisionsScrollPane;
    private javax.swing.JTable itemRevisionsTable;
    private javax.swing.JButton modifyButton;
    private javax.swing.JButton removeDefButton;
    private javax.swing.JPanel revisionsControlPanel;
    private javax.swing.JPanel revisionsControlSidePanel;
    // End of variables declaration//GEN-END:variables

    public void persist() {
        updateList.forEach((revItem) -> {
            XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
            XBCXDescService descService = catalog.getCatalogService(XBCXDescService.class);

            XBERev rev = (XBERev) revItem.getRev();
            if (rev == null) {
                rev = (XBERev) revService.createRev((XBCSpec) catalogItem);
                rev.setXBIndex(revItem.getXbIndex());
            }

            rev.setXBLimit(revItem.getLimit());

            revService.persistItem(rev);

            ((XBEXNameService) nameService).setDefaultText(rev, revItem.getName());
            ((XBEXDescService) descService).setDefaultText(rev, revItem.getDescription());
        });

        removeList.stream().filter((revItem) -> (revItem.getRev() != null)).forEachOrdered((revItem) -> {
            revService.removeItemDepth(revItem.getRev());
        });
    }

    private void updateItemStatus() {
        int selectedRow = itemRevisionsTable.getSelectedRow();
        int rowsCount = revsModel.getRowCount();
        if ((selectedRow >= 0) && (selectedRow < rowsCount)) {
            modifyButton.setEnabled(true);
            removeDefButton.setEnabled(true);
        } else {
            modifyButton.setEnabled(false);
            removeDefButton.setEnabled(false);
        }

        itemRevisionsTable.repaint();
    }

    public void setCatalogItem(XBCItem catalogItem) {
        this.catalogItem = catalogItem;
        addButton.setEnabled(!(catalogItem instanceof XBCNode));
        revsModel.setSpec((XBCSpec) catalogItem);
        updateList = new ArrayList<>();
        removeList = new ArrayList<>();
        updateItemStatus();
    }

    public XBCItem getCatalogItem() {
        return catalogItem;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        revService = catalog.getCatalogService(XBCRevService.class);
        revsModel.setCatalog(catalog);
    }

    public void setDefsModel(CatalogDefsTableModel defsModel) {
        this.defsModel = defsModel;
        defsModel.setRevsModel(revsModel);
    }
}
