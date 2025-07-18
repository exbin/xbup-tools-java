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

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.DefaultListModel;
import org.exbin.framework.App;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemType;
import org.exbin.framework.xbup.catalog.item.spec.gui.CatalogSelectSpecPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;
import org.exbin.framework.window.api.controller.DefaultControlController;

/**
 * Panel for plugin row editor selection.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogSelectPlugUiPanel extends javax.swing.JPanel {

    private XBACatalog catalog;
    private XBCNode node;
    private List<XBCXPlugin> plugins;
    private List<XBCXPlugUi> plugUis;
    private final XBPlugUiType plugUiType;

    public CatalogSelectPlugUiPanel(XBPlugUiType plugUiType) {
        this.plugUiType = plugUiType;
        initComponents();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setNode(XBCNode node) {
        this.node = node;

        XBCXPlugService plugService = catalog.getCatalogService(XBCXPlugService.class);
        DefaultListModel<String> model = (DefaultListModel<String>) pluginsList.getModel();
        model.removeAllElements();
        plugins = plugService.findPluginsForNode(node);
        if (plugins != null) {
            for (XBCXPlugin plugin : plugins) {
                model.addElement(String.valueOf(plugin.getId()) + ": " + plugin.getPluginFile().getFilename());
            }
        }
    }

    @Nullable
    public XBCXPlugUi getPlugUi() {
        int selectedIndex = hooksList.getSelectedIndex();
        return selectedIndex < 0 ? null : plugUis.get(selectedIndex);
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
        pluginsScrollPane = new javax.swing.JScrollPane();
        pluginsList = new javax.swing.JList<>();
        pluginsLabel = new javax.swing.JLabel();
        hooksLabel = new javax.swing.JLabel();
        hooksScrollPane = new javax.swing.JScrollPane();
        hooksList = new javax.swing.JList<>();

        nodeLabel.setText("Node");

        nodeTextField.setEditable(false);

        nodeSelectButton.setText("Select...");
        nodeSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeSelectButtonActionPerformed(evt);
            }
        });

        pluginsList.setModel(new DefaultListModel<String>());
        pluginsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        pluginsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                pluginsListValueChanged(evt);
            }
        });
        pluginsScrollPane.setViewportView(pluginsList);

        pluginsLabel.setText("Plugins");

        hooksLabel.setText("Plugin Hooks");

        hooksList.setModel(new DefaultListModel<String>());
        hooksList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        hooksScrollPane.setViewportView(hooksList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nodeTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nodeSelectButton))
                    .addComponent(hooksScrollPane)
                    .addComponent(pluginsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nodeLabel)
                            .addComponent(hooksLabel)
                            .addComponent(pluginsLabel))
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
                .addComponent(pluginsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pluginsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hooksLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hooksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nodeSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeSelectButtonActionPerformed
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogSelectSpecPanel selectPanel = new CatalogSelectSpecPanel(CatalogItemType.NODE);
        //        editPanel.setMenuManagement(menuManagement);
        selectPanel.setCatalog(catalog);
        // selectPanel.setNode(node);

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(selectPanel, controlPanel);
        //        windowModule.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                XBCNode selectedNode = (XBCNode) selectPanel.getSpec();
                setNode(selectedNode);

                XBCXNameService nodeService = catalog.getCatalogService(XBCXNameService.class);
                nodeTextField.setText(nodeService.getItemNamePath(selectedNode));
            }
            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }//GEN-LAST:event_nodeSelectButtonActionPerformed

    private void pluginsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_pluginsListValueChanged
        XBCXPlugin plugin = plugins.get(pluginsList.getSelectedIndex());
        XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
        plugUis = uiService.getPlugUis(plugin, plugUiType);
        DefaultListModel<String> model = (DefaultListModel<String>) hooksList.getModel();
        model.removeAllElements();
        if (plugUis != null) {
            for (XBCXPlugUi plugUi : plugUis) {
                model.addElement(String.valueOf(plugUi.getId()) + ": " + plugUi.getMethodIndex());
            }
        }
    }//GEN-LAST:event_pluginsListValueChanged

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication.run(() -> WindowUtils.invokeWindow(new CatalogSelectPlugUiPanel(XBPlugUiType.ROW_EDITOR)));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel hooksLabel;
    private javax.swing.JList<String> hooksList;
    private javax.swing.JScrollPane hooksScrollPane;
    private javax.swing.JLabel nodeLabel;
    private javax.swing.JButton nodeSelectButton;
    private javax.swing.JTextField nodeTextField;
    private javax.swing.JLabel pluginsLabel;
    private javax.swing.JList<String> pluginsList;
    private javax.swing.JScrollPane pluginsScrollPane;
    // End of variables declaration//GEN-END:variables

}
