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

import org.exbin.framework.utils.WindowUtils;

/**
 * Panel for service information.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class ServiceStartupPanel extends javax.swing.JPanel {

    public ServiceStartupPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startupControlPanel = new javax.swing.JPanel();
        runOnSystemStartCheckBox = new javax.swing.JCheckBox();
        runOnSystemLoginCheckBox2 = new javax.swing.JCheckBox();
        runAsLabel = new javax.swing.JLabel();
        runAsComboBox = new javax.swing.JComboBox();
        updatingModePanel = new javax.swing.JPanel();
        checkForNewVersionheckBox = new javax.swing.JCheckBox();
        updatingSourceLabel = new javax.swing.JLabel();
        updatingSourceComboBox = new javax.swing.JComboBox();

        startupControlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Startup"));

        runOnSystemStartCheckBox.setText("Run on system startup");
        runOnSystemStartCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        runOnSystemStartCheckBox.setEnabled(false);
        runOnSystemStartCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        runOnSystemLoginCheckBox2.setText("Run on system login");
        runOnSystemLoginCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        runOnSystemLoginCheckBox2.setEnabled(false);
        runOnSystemLoginCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));

        runAsLabel.setText("Run as");

        runAsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        runAsComboBox.setEnabled(false);

        javax.swing.GroupLayout startupControlPanelLayout = new javax.swing.GroupLayout(startupControlPanel);
        startupControlPanel.setLayout(startupControlPanelLayout);
        startupControlPanelLayout.setHorizontalGroup(
            startupControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startupControlPanelLayout.createSequentialGroup()
                .addGroup(startupControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(runOnSystemStartCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(runOnSystemLoginCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(runAsLabel))
                    .addGroup(startupControlPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(runAsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        startupControlPanelLayout.setVerticalGroup(
            startupControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startupControlPanelLayout.createSequentialGroup()
                .addComponent(runOnSystemStartCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runOnSystemLoginCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runAsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runAsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        updatingModePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Updating"));

        checkForNewVersionheckBox.setText("Check for newer version on startup");
        checkForNewVersionheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkForNewVersionheckBox.setEnabled(false);
        checkForNewVersionheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        updatingSourceLabel.setText("Updating source");

        updatingSourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        updatingSourceComboBox.setEnabled(false);

        javax.swing.GroupLayout updatingModePanelLayout = new javax.swing.GroupLayout(updatingModePanel);
        updatingModePanel.setLayout(updatingModePanelLayout);
        updatingModePanelLayout.setHorizontalGroup(
            updatingModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatingModePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updatingModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkForNewVersionheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatingSourceLabel)
                    .addComponent(updatingSourceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        updatingModePanelLayout.setVerticalGroup(
            updatingModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatingModePanelLayout.createSequentialGroup()
                .addComponent(checkForNewVersionheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatingSourceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatingSourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updatingModePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startupControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startupControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatingModePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new ServiceStartupPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkForNewVersionheckBox;
    private javax.swing.JComboBox runAsComboBox;
    private javax.swing.JLabel runAsLabel;
    private javax.swing.JCheckBox runOnSystemLoginCheckBox2;
    private javax.swing.JCheckBox runOnSystemStartCheckBox;
    private javax.swing.JPanel startupControlPanel;
    private javax.swing.JPanel updatingModePanel;
    private javax.swing.JComboBox updatingSourceComboBox;
    private javax.swing.JLabel updatingSourceLabel;
    // End of variables declaration//GEN-END:variables
}
