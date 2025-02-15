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
package org.exbin.framework.xbup.service.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.exbin.framework.App;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.xbup.service.XBDbServiceClient;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.preferences.api.OptionsStorage;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.client.XBCatalogNetServiceClient;
import org.exbin.xbup.client.XBCatalogServiceClient;

/**
 * Service connection panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ConnectionPanel extends javax.swing.JPanel {

    private XBCatalogServiceClient service;

    private static final String PREFERENCES_PREFIX = "catalogConnection";
    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ConnectionPanel.class);

    public ConnectionPanel() {
        initComponents();

        service = null;
    }

    public void setConnectionStatus(Color color, String status, String statusLabel) {
        statusTextLabel.setText(status);
        statusIndicatorPanel.setBackground(color);
        if (statusLabel == null) {
            ((CardLayout) connectionStatusPanel.getLayout()).show(connectionStatusPanel, "default");
        } else {
            busyProgressBar.setString(statusLabel);
            ((CardLayout) connectionStatusPanel.getLayout()).show(connectionStatusPanel, "busy");
        }
    }

    public XBCatalogServiceClient getService() {
        return service;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        accessTypeButtonGroup = new javax.swing.ButtonGroup();
        connectionHeaderPanel = new javax.swing.JPanel();
        prereleaseWarningLabel = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        serviceLabel = new javax.swing.JLabel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        logoImageLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        connectionLoginPanel = new javax.swing.JPanel();
        loginPanel = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        connectionPointLabel = new javax.swing.JLabel();
        connectionComboBox = new javax.swing.JComboBox<>();
        connectionManageButton = new javax.swing.JButton();
        anonymousRadioButton = new javax.swing.JRadioButton();
        loginRadioButton = new javax.swing.JRadioButton();
        devDbLocalButton = new javax.swing.JButton();
        devDbCatalogButton = new javax.swing.JButton();
        devDbCatalogDevButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        connectionStatusPanel = new javax.swing.JPanel();
        defaultPanel = new javax.swing.JPanel();
        statusModeLabel = new javax.swing.JLabel();
        busyStatusPanel = new javax.swing.JPanel();
        busyProgressBar = new javax.swing.JProgressBar();
        statusIndicatorPanel = new javax.swing.JPanel();
        statusTextLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        connectionHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));

        prereleaseWarningLabel.setText(resourceBundle.getString("prereleaseWarningLabel.text")); // NOI18N

        jLayeredPane1.setBackground(new java.awt.Color(204, 255, 204));
        jLayeredPane1.setOpaque(true);

        serviceLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        serviceLabel.setText(resourceBundle.getString("serviceLabel.text")); // NOI18N
        jLayeredPane1.add(serviceLabel);
        serviceLabel.setBounds(100, 0, 480, 43);

        logoImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/xbup/service/resources/images/xbup_maskot4_small.png"))); // NOI18N
        jLayeredPane2.add(logoImageLabel);
        logoImageLabel.setBounds(20, 0, 50, 0);

        javax.swing.GroupLayout connectionHeaderPanelLayout = new javax.swing.GroupLayout(connectionHeaderPanel);
        connectionHeaderPanel.setLayout(connectionHeaderPanelLayout);
        connectionHeaderPanelLayout.setHorizontalGroup(
            connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connectionHeaderPanelLayout.createSequentialGroup()
                .addGap(333, 333, 333)
                .addComponent(prereleaseWarningLabel)
                .addContainerGap(82, Short.MAX_VALUE))
            .addGroup(connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
            .addGroup(connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
            .addGroup(connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
        );
        connectionHeaderPanelLayout.setVerticalGroup(
            connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionHeaderPanelLayout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(prereleaseWarningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLayeredPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
            .addGroup(connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(connectionHeaderPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(28, Short.MAX_VALUE)))
            .addGroup(connectionHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connectionHeaderPanelLayout.createSequentialGroup()
                    .addGap(0, 82, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        add(connectionHeaderPanel, java.awt.BorderLayout.NORTH);

        loginPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceBundle.getString("loginPanel.border.title"))); // NOI18N
        loginPanel.setEnabled(false);

        usernameLabel.setText(resourceBundle.getString("usernameLabel.text")); // NOI18N
        usernameLabel.setEnabled(false);

        usernameTextField.setText("admin");
        usernameTextField.setEnabled(false);

        passwordLabel.setText(resourceBundle.getString("passwordLabel.text")); // NOI18N
        passwordLabel.setEnabled(false);

        passwordField.setEnabled(false);

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameLabel)
                    .addComponent(usernameTextField)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField))
                .addContainerGap())
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                .addComponent(usernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        okButton.setText(resourceBundle.getString("okButton.text")); // NOI18N
        okButton.setToolTipText("");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceBundle.getString("cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        connectionPointLabel.setText(resourceBundle.getString("connectionPointLabel.text")); // NOI18N

        connectionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "localhost:22594" }));

        connectionManageButton.setText(resourceBundle.getString("connectionManageButton.text")); // NOI18N
        connectionManageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionManageButtonActionPerformed(evt);
            }
        });

        anonymousRadioButton.setSelected(true);
        anonymousRadioButton.setText(resourceBundle.getString("anonymousRadioButton.text")); // NOI18N
        anonymousRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anonymousRadioButtonActionPerformed(evt);
            }
        });

        loginRadioButton.setText(resourceBundle.getString("loginRadioButton.text")); // NOI18N
        loginRadioButton.setToolTipText("");
        loginRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginRadioButtonActionPerformed(evt);
            }
        });

        devDbLocalButton.setText("DB: Local");
        devDbLocalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devDbLocalButtonActionPerformed(evt);
            }
        });

        devDbCatalogButton.setText("DB: xbcatalog");
        devDbCatalogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devDbCatalogButtonActionPerformed(evt);
            }
        });

        devDbCatalogDevButton.setText("DB: xbcatalog-dev");
        devDbCatalogDevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devDbCatalogDevButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connectionLoginPanelLayout = new javax.swing.GroupLayout(connectionLoginPanel);
        connectionLoginPanel.setLayout(connectionLoginPanelLayout);
        connectionLoginPanelLayout.setHorizontalGroup(
            connectionLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectionLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(anonymousRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(connectionPointLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connectionLoginPanelLayout.createSequentialGroup()
                        .addComponent(connectionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(connectionManageButton))
                    .addComponent(loginRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, connectionLoginPanelLayout.createSequentialGroup()
                        .addComponent(devDbLocalButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(devDbCatalogButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(devDbCatalogDevButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton))
                    .addComponent(loginPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        connectionLoginPanelLayout.setVerticalGroup(
            connectionLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionPointLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectionLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectionManageButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(anonymousRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(connectionLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(devDbLocalButton)
                    .addComponent(devDbCatalogButton)
                    .addComponent(devDbCatalogDevButton))
                .addContainerGap())
        );

        add(connectionLoginPanel, java.awt.BorderLayout.CENTER);

        statusPanel.setBackground(javax.swing.UIManager.getDefaults().getColor("PropSheet.setBackground"));
        statusPanel.setLayout(new java.awt.BorderLayout());

        connectionStatusPanel.setLayout(new java.awt.CardLayout());

        defaultPanel.setLayout(new java.awt.BorderLayout());
        defaultPanel.add(statusModeLabel, java.awt.BorderLayout.CENTER);

        connectionStatusPanel.add(defaultPanel, "default");

        busyProgressBar.setIndeterminate(true);
        busyProgressBar.setString("");
        busyProgressBar.setStringPainted(true);

        javax.swing.GroupLayout busyStatusPanelLayout = new javax.swing.GroupLayout(busyStatusPanel);
        busyStatusPanel.setLayout(busyStatusPanelLayout);
        busyStatusPanelLayout.setHorizontalGroup(
            busyStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(busyProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
        );
        busyStatusPanelLayout.setVerticalGroup(
            busyStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(busyProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        connectionStatusPanel.add(busyStatusPanel, "busy");

        statusPanel.add(connectionStatusPanel, java.awt.BorderLayout.CENTER);

        statusIndicatorPanel.setBackground(new java.awt.Color(153, 153, 153));

        statusTextLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        statusTextLabel.setForeground(javax.swing.UIManager.getDefaults().getColor("TabRenderer.selectedActivatedForeground"));
        statusTextLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusTextLabel.setText("Disconnected");

        javax.swing.GroupLayout statusIndicatorPanelLayout = new javax.swing.GroupLayout(statusIndicatorPanel);
        statusIndicatorPanel.setLayout(statusIndicatorPanelLayout);
        statusIndicatorPanelLayout.setHorizontalGroup(
            statusIndicatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusTextLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
        );
        statusIndicatorPanelLayout.setVerticalGroup(
            statusIndicatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        statusPanel.add(statusIndicatorPanel, java.awt.BorderLayout.EAST);

        add(statusPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        String connectionString = (String) connectionComboBox.getSelectedItem();
        String connectionHost;
        int connectionPort = 22594; // is 0x5842 (XB)
        int pos = connectionString.indexOf(":");
        if (pos >= 0) {
            connectionHost = connectionString.substring(0, pos);
            connectionPort = Integer.valueOf(connectionString.substring(pos + 1));
        } else {
            connectionHost = connectionString;
        }

        okButton.setEnabled(false);
        service = new XBCatalogNetServiceClient(connectionHost, connectionPort); // 22594 is 0x5842 (XB)
        final String connectionLabel = "Connecting to server " + connectionHost + ":" + connectionPort;
        new Thread(() -> {
            setConnectionStatus(Color.ORANGE, "Connecting", connectionLabel);
            if (service != null) {
                setConnectionStatus(Color.ORANGE, "Logging in", "Logging in...");
                try {
                    int loginResult = service.login(usernameTextField.getText(), passwordField.getPassword());
                    if (loginResult == 0) {
                        setConnectionStatus(Color.GREEN, "Connected", null);
                        closeDialog();
                    } else {
                        statusModeLabel.setText("Unable to login: error " + loginResult);
                        setConnectionStatus(Color.RED, "Failed", null);
                    }
                } catch (ConnectException ex) {
                    statusModeLabel.setText("Unable to connect: " + ex.getMessage());
                    setConnectionStatus(Color.RED, "Failed", null);
                } catch (UnsupportedOperationException | IOException ex) {
                    Logger.getLogger(ConnectionPanel.class.getName()).log(Level.SEVERE, null, ex);
                    setConnectionStatus(Color.RED, "Failed", null);
                }
            } else {
                setConnectionStatus(Color.RED, "Disconnected", null);
            }

            okButton.setEnabled(true);
        }).start();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        closeDialog();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void connectionManageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionManageButtonActionPerformed
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        final ConnectionsManagerPanel panel = new ConnectionsManagerPanel();

        {
            List<String> connectionList = new ArrayList<>();
            int itemsCount = connectionComboBox.getItemCount();
            for (int i = 0; i < itemsCount; i++) {
                connectionList.add((String) connectionComboBox.getItemAt(i));
            }
            panel.setConnectionList(connectionList);
        }

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            switch (actionType) {
                case OK: {
                    List<String> connectionList = panel.getConnectionList();
                    DefaultComboBoxModel<String> comboBoxModel = (DefaultComboBoxModel<String>) connectionComboBox.getModel();
                    comboBoxModel.removeAllElements();
                    connectionList.forEach((connection) -> {
                        comboBoxModel.addElement(connection);
                    });
                    break;
                }
                case CANCEL: {
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected action type " + actionType.name());
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }//GEN-LAST:event_connectionManageButtonActionPerformed

    private void anonymousRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anonymousRadioButtonActionPerformed
        setLoginAccountEnabled(false);
    }//GEN-LAST:event_anonymousRadioButtonActionPerformed

    private void loginRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginRadioButtonActionPerformed
        setLoginAccountEnabled(true);
    }//GEN-LAST:event_loginRadioButtonActionPerformed

    private void devDbLocalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devDbLocalButtonActionPerformed
        service = new XBDbServiceClient(Persistence.createEntityManagerFactory("XBManagerLocalPU"));
        try {
            service.login(usernameTextField.getText(), passwordField.getPassword());
        } catch (IOException ex) {
            Logger.getLogger(ConnectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        setConnectionStatus(Color.GREEN, "Connected", null);
        closeDialog();
    }//GEN-LAST:event_devDbLocalButtonActionPerformed

    private void devDbCatalogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devDbCatalogButtonActionPerformed
        service = new XBDbServiceClient(Persistence.createEntityManagerFactory("XBManagerMySQLPU"));
        try {
            service.login(usernameTextField.getText(), passwordField.getPassword());
        } catch (IOException ex) {
            Logger.getLogger(ConnectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        setConnectionStatus(Color.GREEN, "Connected", null);
        closeDialog();
    }//GEN-LAST:event_devDbCatalogButtonActionPerformed

    private void devDbCatalogDevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devDbCatalogDevButtonActionPerformed
        service = new XBDbServiceClient(Persistence.createEntityManagerFactory("XBManagerMySQLDevPU"));
        try {
            service.login(usernameTextField.getText(), passwordField.getPassword());
        } catch (IOException ex) {
            Logger.getLogger(ConnectionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        setConnectionStatus(Color.GREEN, "Connected", null);
        closeDialog();
    }//GEN-LAST:event_devDbCatalogDevButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new ConnectionPanel());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup accessTypeButtonGroup;
    private javax.swing.JRadioButton anonymousRadioButton;
    private javax.swing.JProgressBar busyProgressBar;
    private javax.swing.JPanel busyStatusPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> connectionComboBox;
    private javax.swing.JPanel connectionHeaderPanel;
    private javax.swing.JPanel connectionLoginPanel;
    private javax.swing.JButton connectionManageButton;
    private javax.swing.JLabel connectionPointLabel;
    private javax.swing.JPanel connectionStatusPanel;
    private javax.swing.JPanel defaultPanel;
    private javax.swing.JButton devDbCatalogButton;
    private javax.swing.JButton devDbCatalogDevButton;
    private javax.swing.JButton devDbLocalButton;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JRadioButton loginRadioButton;
    private javax.swing.JLabel logoImageLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel prereleaseWarningLabel;
    private javax.swing.JLabel serviceLabel;
    private javax.swing.JPanel statusIndicatorPanel;
    private javax.swing.JLabel statusModeLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel statusTextLabel;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables

    private void setLoginAccountEnabled(boolean enabled) {
        loginPanel.setEnabled(enabled);
        usernameLabel.setEnabled(enabled);
        usernameTextField.setEnabled(enabled);
        passwordLabel.setEnabled(enabled);
        passwordField.setEnabled(enabled);
    }

    public List<String> getConnectionList() {
        ArrayList<String> connectionList = new ArrayList<>();
        int pos = 0;
        while (pos < connectionComboBox.getItemCount()) {
            connectionList.add((String) connectionComboBox.getItemAt(pos));
            pos++;
        }
        return connectionList;
    }

    public void setConnectionList(List<String> connectionList) {
        connectionComboBox.removeAllItems();
        connectionList.forEach((connection) -> {
            connectionComboBox.addItem(connection);
        });
    }

    public void loadConnectionList(OptionsStorage preferences) {
        ArrayList<String> connectionList = new ArrayList<>();
        long pos = 1;
        while (preferences.exists(PREFERENCES_PREFIX + String.valueOf(pos))) {
            connectionList.add(preferences.get(PREFERENCES_PREFIX + String.valueOf(pos), ""));
            pos++;
        }

        if (!connectionList.isEmpty()) {
            setConnectionList(connectionList);
        }
    }

    public void saveConnectionList(OptionsStorage preferences) {
        List<String> connectionList = getConnectionList();
        int pos = 0;
        while (pos < connectionList.size()) {
            preferences.put(PREFERENCES_PREFIX + String.valueOf(pos + 1), connectionList.get(pos));
            pos++;
        }
    }

    private void closeDialog() {
        WindowUtils.closeWindow(SwingUtilities.getWindowAncestor(this));
    }

    public JButton getCloseButton() {
        return cancelButton;
    }
}
