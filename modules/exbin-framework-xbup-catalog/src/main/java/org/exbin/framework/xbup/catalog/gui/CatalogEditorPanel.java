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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.exbin.framework.App;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogNodesTreeModel.CatalogNodesTreeItem;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemPanel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogNodesTreeModel;
import org.exbin.framework.xbup.catalog.item.spec.gui.CatalogSpecsTableModel;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalog editor panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogEditorPanel extends javax.swing.JPanel {

    private XBCItem currentItem;

    private final ToolBarSidePanel catalogTreePanel = new ToolBarSidePanel();
    private final ToolBarSidePanel catalogItemPanel = new ToolBarSidePanel();

    private XBACatalog catalog;
    private CatalogNodesTreeModel nodesModel;
    private CatalogSpecsTableModel specsModel;
    private final CatalogItemPanel itemPanel;
    private EditItemActionsUpdateListener itemSelectionListener;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogEditorPanel.class);
    private XBCRoot catalogRoot;

    public CatalogEditorPanel() {
        nodesModel = new CatalogNodesTreeModel();
        specsModel = new CatalogSpecsTableModel();
        itemPanel = new CatalogItemPanel();

        initComponents();
        init();
    }

    private void init() {
        catalogTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                DefaultTreeCellRenderer retValue = (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (value instanceof CatalogNodesTreeItem) {
                    XBCNode node = ((CatalogNodesTreeItem) value).getNode();
                    String nodeName = ((CatalogNodesTreeItem) value).getName();
                    if (nodeName == null) {
                        retValue.setText("node [" + String.valueOf(node.getId()) + "]");
                    } else {
                        retValue.setText(nodeName);
                    }
                }

                return retValue;
            }
        });
        catalogTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        catalogSpecListTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                if (catalogSpecListTable.getSelectedRow() >= 0) {
                    setItem(specsModel.getItem(catalogSpecListTable.getSelectedRow()));
                } else {
                    setItem(null);
                }
            }
        });

        catalogItemSplitPane.setRightComponent(itemPanel);
        itemPanel.setJumpActionListener((XBCRev rev) -> {
            XBCSpec spec = rev.getParent();
            TreePath nodePath = nodesModel.findPathForSpec(spec);
            if (nodePath != null) {
                catalogTree.scrollPathToVisible(nodePath);
                catalogTree.setSelectionPath(nodePath);
                // TODO doesn't properly select tree node

                selectSpecTableRow(spec);
            }
        });

        catalogItemPanel.setToolBarPosition(ToolBarSidePanel.ToolBarPosition.RIGHT);
        catalogTreePanel.add(catalogTreeScrollPane, BorderLayout.CENTER);
        catalogItemPanel.add(catalogItemSplitPane, BorderLayout.CENTER);

        panelSplitPane.setLeftComponent(catalogTreePanel);
        panelSplitPane.setRightComponent(catalogItemPanel);
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void addTreeActions(ActionsProvider actionsProvider) {
        catalogTreePanel.addActions(actionsProvider);
    }

    public void addItemActions(ActionsProvider actionsProvider) {
        catalogItemPanel.addActions(actionsProvider);
    }

    public void addTreeSelectionListener(EditItemActionsUpdateListener updateListener) {
        catalogTree.getSelectionModel().addTreeSelectionListener((e) -> {
            updateListener.stateChanged();
        });
    }

    public void addItemSelectionListener(EditItemActionsUpdateListener updateListener) {
        itemSelectionListener = updateListener;
        catalogSpecListTable.getSelectionModel().addListSelectionListener((e) -> {
            updateListener.stateChanged();
        });
    }

    public void setTreePanelPopup(JPopupMenu popupMenu) {
        catalogTree.setComponentPopupMenu(popupMenu);
    }

    public void setItemPanelPopup(JPopupMenu popupMenu) {
        catalogSpecListTable.setComponentPopupMenu(popupMenu);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        catalogTreeScrollPane = new javax.swing.JScrollPane();
        catalogTree = new javax.swing.JTree();
        catalogItemSplitPane = new javax.swing.JSplitPane();
        catalogItemListScrollPane = new javax.swing.JScrollPane();
        catalogSpecListTable = new javax.swing.JTable();
        panelSplitPane = new javax.swing.JSplitPane();

        catalogTreeScrollPane.setName("catalogTreeScrollPane"); // NOI18N

        catalogTree.setModel(nodesModel);
        catalogTree.setName("catalogTree"); // NOI18N
        catalogTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                catalogTreeValueChanged(evt);
            }
        });
        catalogTreeScrollPane.setViewportView(catalogTree);

        catalogItemSplitPane.setDividerLocation(180);
        catalogItemSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        catalogItemSplitPane.setName("catalogItemSplitPane"); // NOI18N

        catalogItemListScrollPane.setName("catalogItemListScrollPane"); // NOI18N

        catalogSpecListTable.setModel(specsModel);
        catalogSpecListTable.setName("catalogSpecListTable"); // NOI18N
        catalogSpecListTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        catalogItemListScrollPane.setViewportView(catalogSpecListTable);

        catalogItemSplitPane.setLeftComponent(catalogItemListScrollPane);

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        panelSplitPane.setDividerLocation(100);
        panelSplitPane.setName("panelSplitPane"); // NOI18N
        add(panelSplitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void catalogTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_catalogTreeValueChanged
        if (catalogTree.getLastSelectedPathComponent() != null) {
            if (nodesModel.isLeaf(catalogTree.getLastSelectedPathComponent())) {
                setNode(((CatalogNodesTreeItem) catalogTree.getLastSelectedPathComponent()).getNode());
            } else {
                setNode(((CatalogNodesTreeItem) catalogTree.getLastSelectedPathComponent()).getNode());
            }
        } else {
            setNode(null);
        }
    }//GEN-LAST:event_catalogTreeValueChanged

    @Nullable
    public XBCNode getSelectedTreeItem() {
        TreePath selectionPath = catalogTree.getSelectionPath();
        return selectionPath == null ? null : ((CatalogNodesTreeItem) selectionPath.getLastPathComponent()).getNode();
    }

    @Nullable
    public XBCItem getCurrentItem() {
        return currentItem;
    }

    public void setNode(@Nullable XBCNode node) {
        setItem(node);
        specsModel.setNode(node);
        if (node != null) {
            catalogSpecListTable.setRowSelectionInterval(0, 0);
        }
        catalogSpecListTable.revalidate();
    }

    public void setItem(@Nullable XBCItem item) {
        currentItem = item;
        itemPanel.setItem(item);
        if (itemSelectionListener != null) {
            itemSelectionListener.stateChanged();
        }
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new CatalogEditorPanel());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane catalogItemListScrollPane;
    private javax.swing.JSplitPane catalogItemSplitPane;
    private javax.swing.JTable catalogSpecListTable;
    private javax.swing.JTree catalogTree;
    private javax.swing.JScrollPane catalogTreeScrollPane;
    private javax.swing.JSplitPane panelSplitPane;
    // End of variables declaration//GEN-END:variables

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        specsModel.setCatalog(catalog);
        itemPanel.setCatalog(catalog);
    }

    public void setCatalogRoot(XBCRoot catalogRoot) {
        this.catalogRoot = catalogRoot;
        reloadNodesTree();
    }

    public void selectSpecTableRow(XBCItem item) {
        int specRow = specsModel.getIndexOfItem(specsModel.new CatalogSpecTableItem(item));
        if (specRow >= 0) {
            catalogSpecListTable.setRowSelectionInterval(specRow, specRow);
            catalogSpecListTable.scrollRectToVisible(new Rectangle(catalogSpecListTable.getCellRect(specRow, 0, true)));
        }
    }

    @Nullable
    public XBCNode getSpecsNode() {
        return specsModel.getNode();
    }

    public void setSpecsNode(@Nullable XBCNode node) {
        specsModel.setNode(node);
    }

    public void reloadNodesTree() {
        nodesModel = new CatalogNodesTreeModel(catalogRoot.getNode());
        nodesModel.setCatalog(catalog);
        catalogTree.setModel(nodesModel);
    }
}
