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
package org.exbin.framework.xbup.catalog.gui;

import java.awt.CardLayout;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Simple panel with catalog availability message.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogAvailabilityPanel extends javax.swing.JPanel {

    public CatalogAvailabilityPanel() {
        initComponents();
        setNoCatalog();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        catalogNotAvailablePanel = new javax.swing.JPanel();
        notAvailableLabel = new javax.swing.JLabel();
        catalogLoadingPanel = new javax.swing.JPanel();
        progressBarPanel = new javax.swing.JPanel();
        loadingProgressBar = new javax.swing.JProgressBar();
        loadingCatalogLabel = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        catalogNotAvailablePanel.setLayout(new java.awt.GridBagLayout());

        notAvailableLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        notAvailableLabel.setText("Catalog is not available");
        catalogNotAvailablePanel.add(notAvailableLabel, new java.awt.GridBagConstraints());

        add(catalogNotAvailablePanel, "notAvailable");

        catalogLoadingPanel.setLayout(new java.awt.GridBagLayout());

        loadingProgressBar.setMaximum(0);
        loadingProgressBar.setIndeterminate(true);
        loadingProgressBar.setString("Catalog is loading...");

        loadingCatalogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadingCatalogLabel.setText("Catalog is loading...");

        javax.swing.GroupLayout progressBarPanelLayout = new javax.swing.GroupLayout(progressBarPanel);
        progressBarPanel.setLayout(progressBarPanelLayout);
        progressBarPanelLayout.setHorizontalGroup(
            progressBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(progressBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loadingCatalogLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(progressBarPanelLayout.createSequentialGroup()
                        .addComponent(loadingProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        progressBarPanelLayout.setVerticalGroup(
            progressBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadingCatalogLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadingProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        catalogLoadingPanel.add(progressBarPanel, new java.awt.GridBagConstraints());

        add(catalogLoadingPanel, "catalogLoading");

        getAccessibleContext().setAccessibleName("catalog");
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new CatalogAvailabilityPanel());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel catalogLoadingPanel;
    private javax.swing.JPanel catalogNotAvailablePanel;
    private javax.swing.JLabel loadingCatalogLabel;
    private javax.swing.JProgressBar loadingProgressBar;
    private javax.swing.JLabel notAvailableLabel;
    private javax.swing.JPanel progressBarPanel;
    // End of variables declaration//GEN-END:variables

    private void setNoCatalog() {
        ((CardLayout) getLayout()).show(this, "notAvailable");
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        if (catalog == null) {
            setNoCatalog();
        } else {
            ((CardLayout) getLayout()).show(this, "catalogLoading");
        }
    }
}
