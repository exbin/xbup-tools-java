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
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import org.exbin.framework.client.api.ConnectionStatus;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;

/**
 * Status panel for XB document editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDocStatusPanel extends javax.swing.JPanel {

    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(XBDocStatusPanel.class);

    public XBDocStatusPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        connectionStatusPanel = new javax.swing.JPanel();
        connectionStatusLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        defaultPanel = new javax.swing.JPanel();
        operationPanel = new javax.swing.JPanel();
        operationProgressBar = new javax.swing.JProgressBar();
        operationStopButton = new javax.swing.JButton();
        activityPanel = new javax.swing.JPanel();
        activityProgressBar = new javax.swing.JProgressBar();

        setLayout(new java.awt.BorderLayout());

        connectionStatusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/images/status/network-offline.png"))); // NOI18N

        javax.swing.GroupLayout connectionStatusPanelLayout = new javax.swing.GroupLayout(connectionStatusPanel);
        connectionStatusPanel.setLayout(connectionStatusPanelLayout);
        connectionStatusPanelLayout.setHorizontalGroup(
            connectionStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(connectionStatusLabel)
        );
        connectionStatusPanelLayout.setVerticalGroup(
            connectionStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(connectionStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        add(connectionStatusPanel, java.awt.BorderLayout.EAST);

        mainPanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout defaultPanelLayout = new javax.swing.GroupLayout(defaultPanel);
        defaultPanel.setLayout(defaultPanelLayout);
        defaultPanelLayout.setHorizontalGroup(
            defaultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 401, Short.MAX_VALUE)
        );
        defaultPanelLayout.setVerticalGroup(
            defaultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 27, Short.MAX_VALUE)
        );

        mainPanel.add(defaultPanel, "default");

        operationProgressBar.setIndeterminate(true);
        operationProgressBar.setMinimumSize(new java.awt.Dimension(10, 10));
        operationProgressBar.setRequestFocusEnabled(false);
        operationProgressBar.setStringPainted(true);

        operationStopButton.setText("Stop"); // NOI18N
        operationStopButton.setEnabled(false);
        operationStopButton.setMinimumSize(new java.awt.Dimension(67, 15));
        operationStopButton.setPreferredSize(new java.awt.Dimension(75, 20));

        javax.swing.GroupLayout operationPanelLayout = new javax.swing.GroupLayout(operationPanel);
        operationPanel.setLayout(operationPanelLayout);
        operationPanelLayout.setHorizontalGroup(
            operationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, operationPanelLayout.createSequentialGroup()
                .addComponent(operationProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(operationStopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        operationPanelLayout.setVerticalGroup(
            operationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(operationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(operationStopButton, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(operationProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
        );

        mainPanel.add(operationPanel, "operation");

        activityProgressBar.setIndeterminate(true);
        activityProgressBar.setName(""); // NOI18N
        activityProgressBar.setRequestFocusEnabled(false);
        activityProgressBar.setStringPainted(true);

        javax.swing.GroupLayout activityPanelLayout = new javax.swing.GroupLayout(activityPanel);
        activityPanel.setLayout(activityPanelLayout);
        activityPanelLayout.setHorizontalGroup(
            activityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(activityProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        );
        activityPanelLayout.setVerticalGroup(
            activityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(activityProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        mainPanel.add(activityPanel, "activity");

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new XBDocStatusPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activityPanel;
    private javax.swing.JProgressBar activityProgressBar;
    private javax.swing.JLabel connectionStatusLabel;
    private javax.swing.JPanel connectionStatusPanel;
    private javax.swing.JPanel defaultPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel operationPanel;
    private javax.swing.JProgressBar operationProgressBar;
    private javax.swing.JButton operationStopButton;
    // End of variables declaration//GEN-END:variables

    public void setConnectionStatus(ConnectionStatus status) {
        connectionStatusLabel.setIcon(new ImageIcon(getClass().getResource(resourceBundle.getString("connectionStatus" + status.name() + ".icon"))));
        connectionStatusLabel.setToolTipText(resourceBundle.getString("connectionStatus" + status.name() + ".toolTipText"));

        switch (status) {
            case CONNECTING: {
                activityProgressBar.setString("Connection in progress...");
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "activity");
                break;
            }
            case INITIALIZING: {
                activityProgressBar.setString("Initializing catalog...");
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "activity");
                break;
            }
            case UPDATING: {
                activityProgressBar.setString("Updating catalog...");
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "activity");
                break;
            }
            default: {
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "default");
                break;
            }
        }
    }
}
