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
package org.exbin.framework.xbup.catalog.item.plugin.gui;

import org.exbin.framework.xbup.catalog.item.plugin.gui.CatalogSelectPlugUiPanel;
import javax.annotation.Nullable;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.gui.DefaultControlPanel;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;

/**
 * Editor for line panel selection.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class CatalogSelectRowEditorPanel extends javax.swing.JPanel {

    private XBACatalog catalog;
    private XBCXPlugUi plugUi;
    private XBApplication application;

    public CatalogSelectRowEditorPanel() {
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

        editorButtonGroup = new javax.swing.ButtonGroup();
        noEditorRadioButton = new javax.swing.JRadioButton();
        editorRadioButton = new javax.swing.JRadioButton();
        editorTextField = new javax.swing.JTextField();
        selectEditorButton = new javax.swing.JButton();

        editorButtonGroup.add(noEditorRadioButton);
        noEditorRadioButton.setSelected(true);
        noEditorRadioButton.setText("No Editor");

        editorButtonGroup.add(editorRadioButton);
        editorRadioButton.setText("Editor");

        editorTextField.setEditable(false);

        selectEditorButton.setText("Select...");
        selectEditorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectEditorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(editorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectEditorButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noEditorRadioButton)
                            .addComponent(editorRadioButton))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noEditorRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectEditorButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setApplication(XBApplication application) {
        this.application = application;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    private void selectEditorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectEditorButtonActionPerformed
        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
        CatalogSelectPlugUiPanel selectPanel = new CatalogSelectPlugUiPanel(XBPlugUiType.ROW_EDITOR);
        selectPanel.setApplication(application);
        //        editPanel.setMenuManagement(menuManagement);
        selectPanel.setCatalog(catalog);
        // selectPanel.setNode(node);

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowUtils.DialogWrapper dialog = frameModule.createDialog(selectPanel, controlPanel);
        //        WindowUtils.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                plugUi = selectPanel.getPlugUi();
                editorRadioButton.setSelected(true);
                editorTextField.setText(String.valueOf(plugUi.getId()));
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }//GEN-LAST:event_selectEditorButtonActionPerformed

    public XBCXPlugUi getPlugUi() {
        return noEditorRadioButton.isSelected() ? null : plugUi;
    }

    public void setPlugUi(@Nullable XBCXPlugUi plugUi) {
        this.plugUi = plugUi;
        if (plugUi == null) {
            noEditorRadioButton.setSelected(true);
            editorTextField.setText("");
        } else {
            editorRadioButton.setSelected(true);
            editorTextField.setText(String.valueOf(plugUi.getId()));
        }
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new CatalogSelectRowEditorPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup editorButtonGroup;
    private javax.swing.JRadioButton editorRadioButton;
    private javax.swing.JTextField editorTextField;
    private javax.swing.JRadioButton noEditorRadioButton;
    private javax.swing.JButton selectEditorButton;
    // End of variables declaration//GEN-END:variables

}
