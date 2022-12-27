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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.xbup.catalog.XBFileType;
import org.exbin.framework.xbup.catalog.item.gui.CatalogNodesTreeModel.CatalogNodesTreeItem;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.convert.XBCatalogXb;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemPanel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogNodesTreeModel;
import org.exbin.framework.xbup.catalog.item.gui.CatalogSpecsTableModel;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalog editor panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogEditorPanel extends javax.swing.JPanel implements CatalogManagementAware {

    private XBApplication application;
    private XBCItem currentItem;

    private final ToolBarSidePanel catalogTreePanel = new ToolBarSidePanel();
    private final ToolBarSidePanel catalogItemPanel = new ToolBarSidePanel();

    private Controller controller;
    private XBACatalog catalog;
//    private MainFrameManagement mainFrameManagement;
    private CatalogNodesTreeModel nodesModel;
    private CatalogSpecsTableModel specsModel;
    private final CatalogItemPanel itemPanel;

    // Cached values
    private XBCNodeService nodeService;
    private XBCSpecService specService;
    private XBCRevService revService;
    private XBCXNameService nameService;
    private XBCXDescService descService;
    private XBCXStriService striService;

    private final Map<String, ActionListener> actionListenerMap = new HashMap<>();
    private MenuManagement menuManagement;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CatalogEditorPanel.class);
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

        catalogTreePanel.add(catalogTreeScrollPane, BorderLayout.CENTER);
        catalogItemPanel.add(catalogItemSplitPane, BorderLayout.CENTER);

        panelSplitPane.setLeftComponent(catalogTreePanel);
        panelSplitPane.setRightComponent(catalogItemPanel);

        updateItem();

        actionListenerMap.put(DefaultEditorKit.cutAction, (ActionListener) (ActionEvent e) -> {
            performCut();
        });
        actionListenerMap.put(DefaultEditorKit.copyAction, (ActionListener) (ActionEvent e) -> {
            performCopy();
        });
        actionListenerMap.put(DefaultEditorKit.pasteAction, (ActionListener) (ActionEvent e) -> {
            performPaste();
        });
        actionListenerMap.put(DefaultEditorKit.deleteNextCharAction, (ActionListener) (ActionEvent e) -> {
            performDelete();
        });
        actionListenerMap.put("delete", (ActionListener) (ActionEvent e) -> {
            performDelete();
        });
    }

    public void setApplication(XBApplication application) {
        this.application = application;
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setController(Controller controller) {
        this.controller = controller;
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
        catalogSpecListTable.getSelectionModel().addListSelectionListener((e) -> {
            updateListener.stateChanged();
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        catalogTreePopupMenu = new javax.swing.JPopupMenu();
        popupAddMenuItem = new javax.swing.JMenuItem();
        popupEditMenuItem = new javax.swing.JMenuItem();
        popupRefreshMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        popupImportItemMenuItem = new javax.swing.JMenuItem();
        popupExportItemMenuItem = new javax.swing.JMenuItem();
        popupImportXbMenuItem = new javax.swing.JMenuItem();
        popupExportXbMenuItem = new javax.swing.JMenuItem();
        catalogTreeScrollPane = new javax.swing.JScrollPane();
        catalogTree = new javax.swing.JTree();
        catalogItemSplitPane = new javax.swing.JSplitPane();
        catalogItemListScrollPane = new javax.swing.JScrollPane();
        catalogSpecListTable = new javax.swing.JTable();
        panelSplitPane = new javax.swing.JSplitPane();

        catalogTreePopupMenu.setName("catalogTreePopupMenu"); // NOI18N

        popupAddMenuItem.setText(resourceBundle.getString("addMenuItem.text")); // NOI18N
        popupAddMenuItem.setToolTipText(resourceBundle.getString("addMenuItem.toolTipText")); // NOI18N
        popupAddMenuItem.setName("popupAddMenuItem"); // NOI18N
        popupAddMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupAddMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupAddMenuItem);

        popupEditMenuItem.setText(resourceBundle.getString("editMenuItem.text")); // NOI18N
        popupEditMenuItem.setToolTipText(resourceBundle.getString("editMenuItem.toolTipText")); // NOI18N
        popupEditMenuItem.setName("popupEditMenuItem"); // NOI18N
        popupEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupEditMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupEditMenuItem);

        popupRefreshMenuItem.setText(resourceBundle.getString("refreshMenuItem.text")); // NOI18N
        popupRefreshMenuItem.setToolTipText(resourceBundle.getString("refreshMenuItem.toolTipText")); // NOI18N
        popupRefreshMenuItem.setName("popupRefreshMenuItem"); // NOI18N
        popupRefreshMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupRefreshMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupRefreshMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        catalogTreePopupMenu.add(jSeparator1);

        jSeparator2.setName("jSeparator2"); // NOI18N
        catalogTreePopupMenu.add(jSeparator2);

        popupImportItemMenuItem.setText(resourceBundle.getString("importItemMenuItem.text")); // NOI18N
        popupImportItemMenuItem.setToolTipText(resourceBundle.getString("importItemMenuItem.toolTipText")); // NOI18N
        popupImportItemMenuItem.setName("popupImportItemMenuItem"); // NOI18N
        popupImportItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupImportItemMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupImportItemMenuItem);

        popupExportItemMenuItem.setText(resourceBundle.getString("exportItemMenuItem.text")); // NOI18N
        popupExportItemMenuItem.setToolTipText(resourceBundle.getString("exportItemMenuItem.toolTipText")); // NOI18N
        popupExportItemMenuItem.setName("popupExportItemMenuItem"); // NOI18N
        popupExportItemMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupExportItemMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupExportItemMenuItem);

        popupImportXbMenuItem.setText("Test Import XB");
        popupImportXbMenuItem.setName("popupImportXbMenuItem"); // NOI18N
        popupImportXbMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupImportXbMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupImportXbMenuItem);

        popupExportXbMenuItem.setText("Test Export XB");
        popupExportXbMenuItem.setName("popupExportXbMenuItem"); // NOI18N
        popupExportXbMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupExportXbMenuItemActionPerformed(evt);
            }
        });
        catalogTreePopupMenu.add(popupExportXbMenuItem);

        catalogTreeScrollPane.setComponentPopupMenu(catalogTreePopupMenu);
        catalogTreeScrollPane.setName("catalogTreeScrollPane"); // NOI18N

        catalogTree.setModel(nodesModel);
        catalogTree.setComponentPopupMenu(catalogTreePopupMenu);
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

        catalogItemListScrollPane.setComponentPopupMenu(catalogTreePopupMenu);
        catalogItemListScrollPane.setName("catalogItemListScrollPane"); // NOI18N

        catalogSpecListTable.setModel(specsModel);
        catalogSpecListTable.setComponentPopupMenu(catalogTreePopupMenu);
        catalogSpecListTable.setName("catalogSpecListTable"); // NOI18N
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

    private void popupEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupEditMenuItemActionPerformed
        controller.editItem(catalogItemListScrollPane, currentItem);
    }//GEN-LAST:event_popupEditMenuItemActionPerformed

    private void popupExportItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupExportItemMenuItemActionPerformed
        controller.exportItem(catalogItemListScrollPane, currentItem);
    }//GEN-LAST:event_popupExportItemMenuItemActionPerformed

    private void popupImportItemMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupImportItemMenuItemActionPerformed
        controller.importItem(catalogItemListScrollPane, currentItem);
    }//GEN-LAST:event_popupImportItemMenuItemActionPerformed

    private void popupAddMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupAddMenuItemActionPerformed
        controller.addItem(catalogItemListScrollPane, currentItem);
    }//GEN-LAST:event_popupAddMenuItemActionPerformed

    private void popupRefreshMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupRefreshMenuItemActionPerformed
        Component invoker = catalogTreePopupMenu.getInvoker();
        if (invoker == catalogTree) {
            reloadNodesTree();
        } else {
            setNode((XBCNode) (currentItem == null || currentItem instanceof XBCNode ? currentItem : currentItem.getParentItem().orElse(null)));
        }
    }//GEN-LAST:event_popupRefreshMenuItemActionPerformed

    private void popupExportXbMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupExportXbMenuItemActionPerformed
        JFileChooser exportFileChooser = new JFileChooser();
        XBFileType xbFileType = new XBFileType();
        exportFileChooser.addChoosableFileFilter(xbFileType);
        exportFileChooser.setAcceptAllFileFilterUsed(true);
        if (exportFileChooser.showSaveDialog(WindowUtils.getFrame(this)) == JFileChooser.APPROVE_OPTION) {
            XBCatalogXb catalogXb = new XBCatalogXb();
            catalogXb.setCatalog(catalog);
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(exportFileChooser.getSelectedFile());
                catalogXb.exportToXbFile(fileOutputStream);
                fileOutputStream.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_popupExportXbMenuItemActionPerformed

    private void popupImportXbMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupImportXbMenuItemActionPerformed
        JFileChooser importFileChooser = new JFileChooser();
        XBFileType xbFileType = new XBFileType();
        importFileChooser.addChoosableFileFilter(xbFileType);
        importFileChooser.setAcceptAllFileFilterUsed(true);
        if (importFileChooser.showOpenDialog(WindowUtils.getFrame(this)) == JFileChooser.APPROVE_OPTION) {
            XBCatalogXb catalogXb = new XBCatalogXb();
            catalogXb.setCatalog(catalog);
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(importFileChooser.getSelectedFile());
                catalogXb.importFromXbFile(fileInputStream);
                fileInputStream.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CatalogEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_popupImportXbMenuItemActionPerformed

    @Nullable
    public XBCNode getSelectedTreeItem() {
        TreePath selectionPath = catalogTree.getSelectionPath();
        return selectionPath == null ? null : ((CatalogNodesTreeItem) selectionPath.getLastPathComponent()).getNode();
    }

    public void setNode(XBCNode node) {
        setItem(node);
        specsModel.setNode(node);
        if (node != null) {
            catalogSpecListTable.setRowSelectionInterval(0, 0);
        }
        catalogSpecListTable.revalidate();
    }

    public void setItem(XBCItem item) {
        currentItem = item;
        itemPanel.setItem(item);

//        if (mainFrameManagement != null) {
//            updateActionStatus(mainFrameManagement.getLastFocusOwner());
//        }
        updateItem();
    }

//    @Override
//    public boolean updateActionStatus(Component component) {
//        if (component == catalogTree) {
//            // clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//
//            if (mainFrameManagement != null) {
//                mainFrameManagement.getCutAction().setEnabled(currentItem != null);
//                mainFrameManagement.getCopyAction().setEnabled(currentItem != null);
//                mainFrameManagement.getPasteAction().setEnabled(false); // TODO clipboard.isDataFlavorAvailable(xbFlavor));
//                mainFrameManagement.getDeleteAction().setEnabled(currentItem != null);
//                mainFrameManagement.getSelectAllAction().setEnabled(false);
//            }
//
//            // frameManagement.getUndoAction().setEnabled(treeUndo.canUndo());
//            // frameManagement.getRedoAction().setEnabled(treeUndo.canRedo());
//            return true;
//        }
//
//        return false;
//    }
//    @Override
//    public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
//        this.mainFrameManagement = mainFrameManagement;
//        itemPanel.setMainFrameManagement(mainFrameManagement);
//    }
    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new CatalogEditorPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane catalogItemListScrollPane;
    private javax.swing.JSplitPane catalogItemSplitPane;
    private javax.swing.JTable catalogSpecListTable;
    private javax.swing.JTree catalogTree;
    private javax.swing.JPopupMenu catalogTreePopupMenu;
    private javax.swing.JScrollPane catalogTreeScrollPane;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane panelSplitPane;
    private javax.swing.JMenuItem popupAddMenuItem;
    private javax.swing.JMenuItem popupEditMenuItem;
    private javax.swing.JMenuItem popupExportItemMenuItem;
    private javax.swing.JMenuItem popupExportXbMenuItem;
    private javax.swing.JMenuItem popupImportItemMenuItem;
    private javax.swing.JMenuItem popupImportXbMenuItem;
    private javax.swing.JMenuItem popupRefreshMenuItem;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        nodeService = catalog == null ? null : catalog.getCatalogService(XBCNodeService.class);
        specService = catalog == null ? null : catalog.getCatalogService(XBCSpecService.class);
        revService = catalog == null ? null : catalog.getCatalogService(XBCRevService.class);
        nameService = catalog == null ? null : catalog.getCatalogService(XBCXNameService.class);
        descService = catalog == null ? null : catalog.getCatalogService(XBCXDescService.class);
        striService = catalog == null ? null : catalog.getCatalogService(XBCXStriService.class);

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

    public void performCut() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void performCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void performPaste() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void performDelete() {
        Object[] options = {
            "Delete",
            "Cancel"
        };

        String itemIdentification = nameService.getDefaultText(currentItem);
        if (currentItem instanceof XBCNode) {
            itemIdentification = "node " + itemIdentification;
        } else {
            itemIdentification = "item " + itemIdentification;
        }
        int result = JOptionPane.showOptionDialog(this,
                "Are you sure you want to delete " + itemIdentification + "?",
                "Delete Item",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (result == JOptionPane.OK_OPTION) {
            // TODO: Use different transaction management later
            EntityManager em = ((XBECatalog) catalog).getEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            List<XBCXName> names = nameService.getItemNames(currentItem);
            for (XBCXName name : names) {
                nameService.removeItem(name);
            }

            List<XBCXDesc> descs = descService.getItemDescs(currentItem);
            for (XBCXDesc desc : descs) {
                descService.removeItem(desc);
            }

            XBCXStri stri = striService.getItemStringId(currentItem);
            if (stri != null) {
                striService.removeItem(stri);
            }

            if (currentItem instanceof XBCNode) {
                nodeService.removeItem((XBCNode) currentItem);
            } else {
                specService.removeItem((XBCSpec) currentItem);
            }
            em.flush();
            transaction.commit();

            reloadNodesTree();
            repaint();
        }
    }

//    @Override
//    public boolean performAction(String eventName, ActionEvent event) {
//        if (mainFrameManagement != null && mainFrameManagement.getLastFocusOwner() != null) {
//            ActionListener actionListener = actionListenerMap.get(eventName);
//            if (actionListener != null) {
//                actionListener.actionPerformed(event);
//                return true;
//            }
//        }
//
//        return false;
//    }
    @Override
    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;
        menuManagement.insertMainPopupMenu(catalogTreePopupMenu, 4);
    }

    public void reloadNodesTree() {
        nodesModel = new CatalogNodesTreeModel(catalogRoot.getNode());
        nodesModel.setCatalog(catalog);
        catalogTree.setModel(nodesModel);
    }

    private void updateItem() {
        popupEditMenuItem.setEnabled(currentItem != null);
        popupExportItemMenuItem.setEnabled(currentItem != null);
        popupAddMenuItem.setEnabled(currentItem instanceof XBCNode);
        popupImportItemMenuItem.setEnabled(currentItem instanceof XBCNode);
    }

    @ParametersAreNonnullByDefault
    public interface Controller {

        void exportItem(Component parentComponent, XBCItem currentItem);

        void importItem(Component parentComponent, XBCItem currentItem);

        void addItem(Component parentComponent, XBCItem currentItem);

        void editItem(Component parentComponent, XBCItem currentItem);
    }
}
