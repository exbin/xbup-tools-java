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
package org.exbin.framework.editor.xbup.viewer.gui;

import java.awt.BorderLayout;
import org.exbin.framework.editor.xbup.viewer.DocumentItemSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.EditorXbupModule;
import org.exbin.framework.editor.xbup.gui.XBDocTreePanel;
import org.exbin.framework.editor.xbup.viewer.DocumentTab;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeDocument;

/**
 * Panel for document visualization.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBStructurePanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(XBStructurePanel.class);

    private boolean showPreview = false;

    private final XBDocTreePanel treePanel;
    private final XBBlockTablePanel blockTablePanel;
    private List<DocumentTab> previewTabs = new ArrayList<>();

    private XBPluginRepository pluginRepository;

    public XBStructurePanel() {
        initComponents();

        treePanel = new XBDocTreePanel();
        blockTablePanel = new XBBlockTablePanel();

        init();
    }

    private void init() {
        setShowPreviewPanel(true);
        mainTabbedPane.addChangeListener((ChangeEvent e) -> {
            int selectedIndex = mainTabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                XBTTreeNode block = getSelectedItem();
                DocumentTab tab = previewTabs.get(selectedIndex);
                tab.setBlock(block);
            }
        });
        toolBar.setFloatable(false);

        treeSplitPane.setLeftComponent(treePanel);
        treeSplitPane.setRightComponent(blockTablePanel);
        previewSplitPane.setLeftComponent(treeSplitPane);
        previewSplitPane.setRightComponent(previewPanel);
        add(previewSplitPane, BorderLayout.CENTER);

        addItemSelectionListener((item) -> {
            DocumentTab previewActiveTab = getPreviewActiveTab();
            blockTablePanel.setBlock(item);
            previewActiveTab.setBlock(item);
        });
    }

    public void setApplication(XBApplication application) {
        treePanel.setApplication(application);

        EditorXbupModule xbupModule = application.getModuleRepository().getModuleByInterface(EditorXbupModule.class);
        setPopupMenu(xbupModule.getItemPopupMenu());
    }

    public void setCatalog(XBACatalog catalog) {
        treePanel.setCatalog(catalog);
    }

    public void setUndoHandler(XBUndoHandler undoHandler) {
        treePanel.setUndoHandler(undoHandler);
    }

    public void postWindowOpened() {
        treeSplitPane.setDividerLocation(getWidth() - 300 > 0 ? getWidth() - 300 : getWidth() / 3);
    }

    public void addPreviewTabComponent(DocumentTab tab) {
        previewTabs.add(tab);
        ImageIcon icon = tab.getTabIcon().orElse(null);
        mainTabbedPane.addTab(tab.getTabName(), icon, tab.getComponent());
    }

    @Nonnull
    public DocumentTab getPreviewActiveTab() {
        int selectedIndex = mainTabbedPane.getSelectedIndex();
        if (selectedIndex < 0) {
            throw new IllegalStateException("No active tab");
        }
        return previewTabs.get(selectedIndex);
    }

    public void setAddressText(String addressText) {
        addressTextField.setText(addressText);
    }

//    /**
//     * Updating selected item available operations status, like add, edit,
//     * delete.
//     */
//    public void updateItem() {
//        treePanel.updateItemStatus();
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        treeSplitPane = new javax.swing.JSplitPane();
        previewSplitPane = new javax.swing.JSplitPane();
        previewPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        addressTextField = new javax.swing.JTextField();
        mainTabbedPane = new javax.swing.JTabbedPane();

        treeSplitPane.setDividerLocation(200);

        previewSplitPane.setDividerLocation(200);

        previewPanel.setLayout(new java.awt.BorderLayout());

        toolBar.setRollover(true);

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/arrow-left.png"))); // NOI18N
        previousButton.setEnabled(false);
        toolBar.add(previousButton);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/arrow-right.png"))); // NOI18N
        nextButton.setEnabled(false);
        toolBar.add(nextButton);

        upButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/arrow-up.png"))); // NOI18N
        upButton.setEnabled(false);
        toolBar.add(upButton);

        addressTextField.setEditable(false);
        toolBar.add(addressTextField);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        previewPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        mainTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabbedPaneStateChanged(evt);
            }
        });
        previewPanel.add(mainTabbedPane, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    private void mainTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainTabbedPaneStateChanged
//        throw new UnsupportedOperationException("Not supported yet.");
        // updateActiveViewer();
//        JComponent viewerComponent = activeViewer.getComponent();
//        viewerComponent.requestFocus();
//        mainTabbedPane.setSelectedComponent(viewerComponent);

//        mainTabbedPane.addT  setTabComponentAt(mainTabbedPane.getSelectedIndex(), viewerComponent);
        // setMode(PanelMode.values()[mainTabbedPane.getSelectedIndex()]);
    }//GEN-LAST:event_mainTabbedPaneStateChanged

    public XBTTreeNode getSelectedItem() {
        return treePanel.getSelectedItem();
    }

    public void reportStructureChange(XBTBlock block) {
        treePanel.reportStructureChange(block);
    }

    public void addUpdateListener(ActionListener listener) {
        treePanel.addUpdateListener(listener);
    }

    public void removeUpdateListener(ActionListener listener) {
        treePanel.removeUpdateListener(listener);
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new XBStructurePanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressTextField;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JSplitPane previewSplitPane;
    private javax.swing.JButton previousButton;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JSplitPane treeSplitPane;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    public void updateUndoAvailable() {
        firePropertyChange("undoAvailable", false, true);
        firePropertyChange("redoAvailable", false, true);
    }

    public XBUndoHandler getUndoHandler() {
        return treePanel.getUndoHandler();
    }

    public void setShowPreviewPanel(boolean showPreview) {
        if (this.showPreview != showPreview) {
//            if (showPropertiesPanel) {
//                viewSplitPane.setLeftComponent(mainTabbedPane);
//                viewSplitPane.setRightComponent(propertyPanel);
//                mainSplitPane.setRightComponent(viewSplitPane);
//            } else {
            treeSplitPane.setRightComponent(previewPanel);
//            }

            this.showPreview = showPreview;
        }
    }

    @Nonnull
    public DocumentTab getActivePreviewTab() {
        int selectedIndex = mainTabbedPane.getSelectedIndex();
        if (selectedIndex < 0) {
            throw new IllegalStateException("No active tab");
        }
        return previewTabs.get(selectedIndex);
    }

    public boolean isShowPreview() {
        return showPreview;
    }

    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        // this.propertyChangeListener = propertyChangeListener;
        treePanel.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            if (propertyChangeListener != null) {
                propertyChangeListener.propertyChange(evt);
            }
        });

        super.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if (propertyChangeListener != null) {
                propertyChangeListener.propertyChange(evt);
            }
        });
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        treePanel.setPopupMenu(popupMenu);
    }

    public void setMainDoc(XBTTreeDocument mainDoc) {
        treePanel.setMainDoc(mainDoc);
    }

    public void addItemSelectionListener(DocumentItemSelectionListener listener) {
        treePanel.addItemSelectionListener(listener);
    }

    public void removeItemSelectionListener(DocumentItemSelectionListener listener) {
        treePanel.removeItemSelectionListener(listener);
    }

    public void performSelectAll() {
        treePanel.performSelectAll();
    }

    public boolean hasSelection() {
        return treePanel.hasSelection();
    }

    public void addTreeFocusListener(FocusListener focusListener) {
        treePanel.addTreeFocusListener(focusListener);
    }

    public void removeTreeFocusListener(FocusListener focusListener) {
        treePanel.removeTreeFocusListener(focusListener);
    }
}
