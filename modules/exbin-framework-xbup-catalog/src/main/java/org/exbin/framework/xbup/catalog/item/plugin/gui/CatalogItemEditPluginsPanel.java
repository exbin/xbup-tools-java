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

import java.awt.BorderLayout;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionListener;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Catalog item plugin panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogItemEditPluginsPanel extends javax.swing.JPanel {

    private final CatalogPluginsTableModel pluginsModel;
    private XBCNode node;
    private XBACatalog catalog;
    private final ToolBarSidePanel toolBarPanel = new ToolBarSidePanel();

    public CatalogItemEditPluginsPanel() {
        pluginsModel = new CatalogPluginsTableModel();

        initComponents();

        toolBarPanel.setToolBarPosition(ToolBarSidePanel.ToolBarPosition.RIGHT);
        toolBarPanel.add(scrollPane, BorderLayout.CENTER);
        add(toolBarPanel, BorderLayout.CENTER);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        pluginsModel.setCatalog(catalog);
    }

    public void setNode(XBCNode node) {
        this.node = node;
        pluginsModel.setNode(node);
    }

    public void addFileActions(ActionsProvider actionsProvider) {
        toolBarPanel.addActions(actionsProvider);
    }
    
    public JToolBar getToolBar() {
        return toolBarPanel.getToolBar();
    }

    @Nullable
    public XBCXPlugin getSelectedPlugin() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            return pluginsModel.getItem(selectedRow);
        }

        return null;
    }

    @Nonnull
    public CatalogPluginsTableModel getPluginsModel() {
        return pluginsModel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        table.setModel(pluginsModel);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication.run(() -> WindowUtils.invokeWindow(new CatalogItemEditPluginsPanel()));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    public void setPanelPopup(JPopupMenu popupMenu) {
        table.setComponentPopupMenu(popupMenu);
    }

    public void addSelectionListener(ListSelectionListener listSelectionListener) {
        table.getSelectionModel().addListSelectionListener(listSelectionListener);
    }
}
