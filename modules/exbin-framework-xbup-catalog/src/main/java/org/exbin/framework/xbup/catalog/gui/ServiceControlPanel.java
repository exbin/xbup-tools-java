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
public class ServiceControlPanel extends javax.swing.JPanel {

    public ServiceControlPanel() {
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

        serviceStatusPanel = new javax.swing.JPanel();
        restartServiceButton = new javax.swing.JButton();
        stopServiceButton = new javax.swing.JButton();
        startServiceButton = new javax.swing.JButton();
        logMessagesPanel = new javax.swing.JPanel();
        logMessagesScrollPane = new javax.swing.JScrollPane();
        logMessagesTextArea = new javax.swing.JTextArea();

        serviceStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Service Status"));

        restartServiceButton.setText("Restart");
        restartServiceButton.setEnabled(false);

        stopServiceButton.setText("Stop");
        stopServiceButton.setEnabled(false);
        stopServiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopServiceButtonActionPerformed(evt);
            }
        });

        startServiceButton.setText("Start");
        startServiceButton.setEnabled(false);

        javax.swing.GroupLayout serviceStatusPanelLayout = new javax.swing.GroupLayout(serviceStatusPanel);
        serviceStatusPanel.setLayout(serviceStatusPanelLayout);
        serviceStatusPanelLayout.setHorizontalGroup(
            serviceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceStatusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startServiceButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopServiceButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(restartServiceButton)
                .addContainerGap())
        );
        serviceStatusPanelLayout.setVerticalGroup(
            serviceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceStatusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(serviceStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(restartServiceButton)
                    .addComponent(stopServiceButton)
                    .addComponent(startServiceButton))
                .addContainerGap())
        );

        logMessagesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Log Messages"));

        logMessagesTextArea.setEditable(false);
        logMessagesTextArea.setColumns(20);
        logMessagesTextArea.setRows(5);
        logMessagesScrollPane.setViewportView(logMessagesTextArea);

        javax.swing.GroupLayout logMessagesPanelLayout = new javax.swing.GroupLayout(logMessagesPanel);
        logMessagesPanel.setLayout(logMessagesPanelLayout);
        logMessagesPanelLayout.setHorizontalGroup(
            logMessagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logMessagesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logMessagesScrollPane)
                .addContainerGap())
        );
        logMessagesPanelLayout.setVerticalGroup(
            logMessagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logMessagesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logMessagesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logMessagesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(serviceStatusPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logMessagesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void stopServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopServiceButtonActionPerformed
        //getService().stop();
    }//GEN-LAST:event_stopServiceButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new ServiceControlPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel logMessagesPanel;
    private javax.swing.JScrollPane logMessagesScrollPane;
    private javax.swing.JTextArea logMessagesTextArea;
    private javax.swing.JButton restartServiceButton;
    private javax.swing.JPanel serviceStatusPanel;
    private javax.swing.JButton startServiceButton;
    private javax.swing.JButton stopServiceButton;
    // End of variables declaration//GEN-END:variables

    void setStopButtonEnabled(boolean b) {
        stopServiceButton.setEnabled(true);
    }
}
