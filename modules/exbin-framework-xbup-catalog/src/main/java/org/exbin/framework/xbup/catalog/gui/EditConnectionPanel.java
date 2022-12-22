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

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;

/**
 * Connection editor panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditConnectionPanel extends javax.swing.JPanel {

    private static final int DEFAULT_PORT = 22594;
    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(EditConnectionPanel.class);

    public EditConnectionPanel() {
        initComponents();
        connectionPortSpinner.setValue(DEFAULT_PORT);
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

        connectionHostLabel = new javax.swing.JLabel();
        connectionPortSpinner = new javax.swing.JSpinner();
        connectionPortLabel = new javax.swing.JLabel();
        connectionHostTextField = new javax.swing.JTextField();

        connectionHostLabel.setText(resourceBundle.getString("connectionHostLabel.text")); // NOI18N

        connectionPortLabel.setText(resourceBundle.getString("connectionPortLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connectionHostTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(connectionPortLabel)
                            .addComponent(connectionPortSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(connectionHostLabel))
                        .addGap(0, 217, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionHostLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectionHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectionPortLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectionPortSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new EditConnectionPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel connectionHostLabel;
    private javax.swing.JTextField connectionHostTextField;
    private javax.swing.JLabel connectionPortLabel;
    private javax.swing.JSpinner connectionPortSpinner;
    // End of variables declaration//GEN-END:variables

    @Nonnull
    public String getConnection() {
        return connectionHostTextField.getText() + ":" + String.valueOf((Integer) connectionPortSpinner.getValue());
    }

    public void setConnection(String string) {
        int colonPos = string.indexOf(":");
        connectionHostTextField.setText(string.substring(0, colonPos));
        connectionPortSpinner.setValue(Integer.valueOf(string.substring(colonPos + 1)));
    }
}
