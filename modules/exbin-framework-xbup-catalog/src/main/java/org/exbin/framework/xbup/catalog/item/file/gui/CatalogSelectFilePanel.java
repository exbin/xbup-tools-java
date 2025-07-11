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
package org.exbin.framework.xbup.catalog.item.file.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.DefaultListModel;
import org.exbin.framework.App;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemType;
import org.exbin.framework.xbup.catalog.item.spec.gui.CatalogSelectSpecPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.framework.window.api.controller.DefaultControlController;

/**
 * Catalog file selection panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogSelectFilePanel extends javax.swing.JPanel {

    private XBACatalog catalog;
    private List<XBCXFile> files = new ArrayList<>();

    public CatalogSelectFilePanel() {
        initComponents();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setNode(XBCNode node) {
        XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
        // TODO string path later
        nodeTextField.setText(String.valueOf(node.getId()));
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
        files = fileService.findFilesForNode(node);
        DefaultListModel<String> model = (DefaultListModel<String>) filesList.getModel();
        model.removeAllElements();
        if (files != null) {
            for (XBCXFile file : files) {
                model.addElement(file.getFilename());
            }
        }
    }

    @Nonnull
    public Optional<XBCXFile> getFile() {
        int selectedIndex = filesList.getSelectedIndex();
        return selectedIndex < 0 ? Optional.empty() : Optional.of(files.get(selectedIndex));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nodeLabel = new javax.swing.JLabel();
        nodeTextField = new javax.swing.JTextField();
        nodeSelectButton = new javax.swing.JButton();
        filesLabel = new javax.swing.JLabel();
        filesScrollPane = new javax.swing.JScrollPane();
        filesList = new javax.swing.JList<>();

        nodeLabel.setText("Node");

        nodeSelectButton.setText("Select...");
        nodeSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeSelectButtonActionPerformed(evt);
            }
        });

        filesLabel.setText("Files");

        filesList.setModel(new DefaultListModel<String>());
        filesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        filesScrollPane.setViewportView(filesList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filesScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nodeSelectButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nodeLabel)
                            .addComponent(filesLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nodeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nodeSelectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nodeSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeSelectButtonActionPerformed
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogSelectSpecPanel selectPanel = new CatalogSelectSpecPanel(CatalogItemType.NODE);
        // selectPanel.setApplication(application);
        //        editPanel.setMenuManagement(menuManagement);
        selectPanel.setCatalog(catalog);
        // selectPanel.setNode(node);

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(selectPanel, controlPanel);
        //        windowModule.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                XBCNode node = (XBCNode) selectPanel.getSpec();
                setNode(node);
                //                EntityManager em = ((XBECatalog) catalog).getEntityManager();
                //                EntityTransaction transaction = em.getTransaction();
                //                transaction.begin();
                //                editPanel.persist();
                //                setItem(currentItem);
                //                em.flush();
                //                transaction.commit();
                //                specsModel.setNode(specsModel.getNode());
                //                selectSpecTableRow(currentItem);
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }//GEN-LAST:event_nodeSelectButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new CatalogSelectFilePanel());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel filesLabel;
    private javax.swing.JList<String> filesList;
    private javax.swing.JScrollPane filesScrollPane;
    private javax.swing.JLabel nodeLabel;
    private javax.swing.JButton nodeSelectButton;
    private javax.swing.JTextField nodeTextField;
    // End of variables declaration//GEN-END:variables

}
