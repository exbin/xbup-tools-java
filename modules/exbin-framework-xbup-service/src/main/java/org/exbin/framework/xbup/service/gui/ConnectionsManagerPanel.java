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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import org.exbin.framework.App;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;

/**
 * Connection management panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class ConnectionsManagerPanel extends javax.swing.JPanel {


    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ConnectionsManagerPanel.class);

    public ConnectionsManagerPanel() {
        initComponents();
        connectionsList.getModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                contentsChanged(e);
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                selectAllButton.setEnabled(connectionsList.getModel().getSize() > 0);
            }
        });
        connectionsList.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                boolean emptySelection = connectionsList.isSelectionEmpty();
                removeButton.setEnabled(!emptySelection);
                modifyButton.setEnabled(!emptySelection && connectionsList.getSelectionModel().getMinSelectionIndex() == connectionsList.getSelectionModel().getMaxSelectionIndex());
                selectAllButton.setEnabled(connectionsList.getModel().getSize() > 0);
                if (!emptySelection) {
                    int[] indices = connectionsList.getSelectedIndices();
                    upButton.setEnabled(connectionsList.getMaxSelectionIndex() >= indices.length);
                    downButton.setEnabled(connectionsList.getMinSelectionIndex() + indices.length < connectionsList.getModel().getSize());
                } else {
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                }
            }
        });

        ((ConnectionsListModel) connectionsList.getModel()).setConnections(new ArrayList<>());
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

        connectionsScrollPane = new javax.swing.JScrollPane();
        connectionsList = new javax.swing.JList();
        connectionsControlPanel = new javax.swing.JPanel();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        modifyButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        connectionsScrollPane.setName("connectionsScrollPane"); // NOI18N

        connectionsList.setModel(new ConnectionsListModel());
        connectionsList.setName("connectionsList"); // NOI18N
        connectionsScrollPane.setViewportView(connectionsList);

        connectionsControlPanel.setName("connectionsControlPanel"); // NOI18N

        upButton.setText(resourceBundle.getString("upButton.text")); // NOI18N
        upButton.setEnabled(false);
        upButton.setName("upButton"); // NOI18N
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText(resourceBundle.getString("downButton.text")); // NOI18N
        downButton.setEnabled(false);
        downButton.setName("downButton"); // NOI18N
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        addButton.setText(resourceBundle.getString("addButton.text") + "...");
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText(resourceBundle.getString("removeButton.text")); // NOI18N
        removeButton.setEnabled(false);
        removeButton.setName("removeButton"); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        selectAllButton.setText(resourceBundle.getString("selectAllButton.text")); // NOI18N
        selectAllButton.setEnabled(false);
        selectAllButton.setName("selectAllButton"); // NOI18N
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        modifyButton.setText(resourceBundle.getString("modifyButton.text") + "...");
        modifyButton.setEnabled(false);
        modifyButton.setName("modifyButton"); // NOI18N
        modifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connectionsControlPanelLayout = new javax.swing.GroupLayout(connectionsControlPanel);
        connectionsControlPanel.setLayout(connectionsControlPanelLayout);
        connectionsControlPanelLayout.setHorizontalGroup(
            connectionsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectionsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(modifyButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectAllButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(downButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(upButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        connectionsControlPanelLayout.setVerticalGroup(
            connectionsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(upButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectAllButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modifyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connectionsControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(connectionsControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(connectionsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        final EditConnectionPanel panel = new EditConnectionPanel();

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            switch (actionType) {
                case OK: {
                    ((ConnectionsListModel) connectionsList.getModel()).add(connectionsList.isSelectionEmpty() ? -1 : connectionsList.getSelectedIndex(), panel.getConnection());
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
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        ((ConnectionsListModel) connectionsList.getModel()).removeIndices(connectionsList.getSelectedIndices());
        connectionsList.clearSelection();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        int[] indices = connectionsList.getSelectedIndices();
        int last = 0;
        for (int i = 0; i < indices.length; i++) {
            int next = indices[i];
            if (last != next) {
                ConnectionsListModel model = (ConnectionsListModel) connectionsList.getModel();
                String item = (String) model.getElementAt(next);
                model.add(next - 1, item);
                connectionsList.getSelectionModel().addSelectionInterval(next - 1, next - 1);
                model.remove(next + 1);
            } else {
                last++;
            }
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        int[] indices = connectionsList.getSelectedIndices();
        int last = connectionsList.getModel().getSize() - 1;
        for (int i = indices.length; i > 0; i--) {
            int next = indices[i - 1];
            if (last != next) {
                ConnectionsListModel model = (ConnectionsListModel) connectionsList.getModel();
                String item = (String) model.getElementAt(next);
                model.add(next + 2, item);
                connectionsList.getSelectionModel().addSelectionInterval(next + 2, next + 2);
                model.remove(next);
            } else {
                last--;
            }
        }
    }//GEN-LAST:event_downButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        if (connectionsList.getSelectedIndices().length < connectionsList.getModel().getSize()) {
            connectionsList.setSelectionInterval(0, connectionsList.getModel().getSize() - 1);
        } else {
            connectionsList.clearSelection();
        }
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void modifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyButtonActionPerformed
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        final EditConnectionPanel panel = new EditConnectionPanel();
        panel.setConnection((String) connectionsList.getSelectedValue());

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            switch (actionType) {
                case OK: {
                    int modifiedRowIndex = connectionsList.getSelectedIndex();
                    ((ConnectionsListModel) connectionsList.getModel()).remove(modifiedRowIndex);
                    ((ConnectionsListModel) connectionsList.getModel()).add(modifiedRowIndex, panel.getConnection());
                    connectionsList.setSelectedIndex(modifiedRowIndex);
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
    }//GEN-LAST:event_modifyButtonActionPerformed

    public void setConnectionList(List<String> list) {
        ((ConnectionsListModel) connectionsList.getModel()).setConnections(list);
    }

    public List<String> getConnectionList() {
        return ((ConnectionsListModel) connectionsList.getModel()).getConnections();
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeWindow(new ConnectionsManagerPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel connectionsControlPanel;
    private javax.swing.JList connectionsList;
    private javax.swing.JScrollPane connectionsScrollPane;
    private javax.swing.JButton downButton;
    private javax.swing.JButton modifyButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    private class ConnectionsListModel extends AbstractListModel {

        private List<String> connections = null;

        @Override
        public int getSize() {
            if (connections == null) {
                return 0;
            }
            return connections.size();
        }

        @Override
        public Object getElementAt(int index) {
            return connections.get(index);
        }

        public List<String> getConnections() {
            return connections;
        }

        public void setConnections(List<String> charsets) {
            this.connections = charsets;
            fireContentsChanged(this, 0, charsets.size());
        }

        public void addAll(List<String> list, int pos) {
            if (pos >= 0) {
                connections.addAll(pos, list);
                fireIntervalAdded(this, pos, list.size() + pos);
            } else {
                connections.addAll(list);
                fireIntervalAdded(this, connections.size() - list.size(), connections.size());
            }
        }

        public void removeIndices(int[] indices) {
            if (indices.length == 0) {
                return;
            }
            Arrays.sort(indices);
            for (int i = indices.length - 1; i >= 0; i--) {
                connections.remove(indices[i]);
                fireIntervalRemoved(this, indices[i], indices[i]);
            }
        }

        public void remove(int index) {
            connections.remove(index);
            fireIntervalRemoved(this, index, index);
        }

        public void add(int index, String item) {
            if (index >= 0) {
                connections.add(index, item);
                fireIntervalAdded(this, index, index);
            } else {
                connections.add(item);
                fireIntervalAdded(this, connections.size() - 1, connections.size());
            }
        }
    }
}
