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
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import org.exbin.framework.App;
import org.exbin.framework.editor.xbup.EditorXbupModule;
import org.exbin.framework.editor.xbup.gui.XBDocTreePanel;
import org.exbin.framework.editor.xbup.viewer.XbupTreeDocument;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.framework.editor.xbup.viewer.BlockViewer;
import org.exbin.framework.utils.TestApplication;

/**
 * Panel for document structure visualization.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBStructurePanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XBStructurePanel.class);

    private boolean showPreview = false;
    private Mode mode = Mode.BOTH;

    private final XBDocTreePanel treePanel;
    private final XBBlockListPanel blockListPanel;
    private List<BlockViewer> previewBlockViewers = new ArrayList<>();
    private int activeViewerIndex = -1;
    private BlockViewer activeViewer = null;
    private List<DocumentItemSelectionListener> itemSelectionListeners = new ArrayList<>();

    public XBStructurePanel() {
        initComponents();

        treePanel = new XBDocTreePanel();
        blockListPanel = new XBBlockListPanel();

        init();
    }

    private void init() {
        setShowPreviewPanel(true);
        toolBar.setFloatable(false);

        treeSplitPane.setLeftComponent(treePanel);
        treeSplitPane.setRightComponent(blockListPanel);
        structureToolBar.setFloatable(false);
        structurePanel.add(structureToolBar, BorderLayout.SOUTH);
        structurePanel.add(treeSplitPane, BorderLayout.CENTER);
        previewSplitPane.setLeftComponent(structurePanel);
        previewSplitPane.setRightComponent(previewPanel);
        add(previewSplitPane, BorderLayout.CENTER);

        addItemSelectionListener((item) -> {
            Optional<BlockViewer> previewActiveViewer = getPreviewActiveViewer();
            if (previewActiveViewer.isPresent()) {
                previewActiveViewer.get().setBlock(item);
            }
        });
        treePanel.addItemSelectionListener((item) -> {
            if (mode == Mode.TREE) {
                notifyItemSelectionChanged(item);
            } else if (mode == Mode.BOTH) {
                blockListPanel.setBlock(item);
            }
        });
        blockListPanel.addItemSelectionListener((item) -> {
            if (mode != Mode.TREE) {
                notifyItemSelectionChanged(item);
            }
        });

        EditorXbupModule xbupModule = App.getModule(EditorXbupModule.class);
        setPopupMenu(xbupModule.createItemPopupMenu());
    }

    public void setCatalog(XBACatalog catalog) {
        treePanel.setCatalog(catalog);
    }

    public void setUndoHandler(XBUndoHandler undoHandler) {
        treePanel.setUndoHandler(undoHandler);
    }

    public void postWindowOpened() {
        treeSplitPane.setDividerLocation(200);
        previewSplitPane.setDividerLocation(400);
    }

    public void addPreviewViewer(BlockViewer blockViewer) {
        int blockViewerIndex = previewBlockViewers.size();
        previewBlockViewers.add(blockViewer);

        ImageIcon icon = blockViewer.getIcon().orElse(null);
        JToggleButton toggleButton = new JToggleButton(blockViewer.getName(), icon);
        viewerButtonGroup.add(toggleButton);
        toggleButton.addActionListener((event) -> {
            viewerChanged(blockViewerIndex);
        });
        bottomPanel.add(toggleButton);
        if (blockViewerIndex == 0) {
            toggleButton.setSelected(true);
            viewerChanged(0);
        }
    }

    private void viewerChanged(int blockViewerIndex) {
        if (blockViewerIndex >= 0) {
            BlockViewer blockViewer = previewBlockViewers.get(blockViewerIndex);
            if (blockViewer == activeViewer) {
                return;
            }

            XBTBlock block = getSelectedItem().orElse(null);
            blockViewer.setBlock(block);

            if (activeViewer != null) {
                previewPanel.remove(activeViewer.getComponent());
            }

            activeViewer = blockViewer;
            previewPanel.add(activeViewer.getComponent(), BorderLayout.CENTER);
            previewPanel.revalidate();
            previewPanel.repaint();
            activeViewerIndex = blockViewerIndex;
        }
    }

    @Nonnull
    public Optional<BlockViewer> getPreviewActiveViewer() {
        return Optional.ofNullable(activeViewer);
    }

    public void setAddressText(String addressText) {
        addressTextField.setText(addressText);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        structurePanel = new javax.swing.JPanel();
        treeSplitPane = new javax.swing.JSplitPane();
        previewSplitPane = new javax.swing.JSplitPane();
        previewPanel = new javax.swing.JPanel();
        bottomPanel = new javax.swing.JPanel();
        structureToolBar = new javax.swing.JToolBar();
        treeModeToggleButton = new javax.swing.JToggleButton();
        bothModeToggleButton = new javax.swing.JToggleButton();
        listModeToggleButton = new javax.swing.JToggleButton();
        structureModeButtonGroup = new javax.swing.ButtonGroup();
        viewerButtonGroup = new javax.swing.ButtonGroup();
        headerPanel = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        addressTextField = new javax.swing.JTextField();

        structurePanel.setLayout(new java.awt.BorderLayout());

        previewPanel.setLayout(new java.awt.BorderLayout());

        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        previewPanel.add(bottomPanel, java.awt.BorderLayout.NORTH);

        structureModeButtonGroup.add(treeModeToggleButton);
        treeModeToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/view-list-tree-4.png"))); // NOI18N
        treeModeToggleButton.setEnabled(false);
        treeModeToggleButton.setFocusable(false);
        treeModeToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        treeModeToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        treeModeToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                treeModeToggleButtonActionPerformed(evt);
            }
        });
        structureToolBar.add(treeModeToggleButton);

        structureModeButtonGroup.add(bothModeToggleButton);
        bothModeToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/view-sidetree-3_.png"))); // NOI18N
        bothModeToggleButton.setSelected(true);
        bothModeToggleButton.setEnabled(false);
        bothModeToggleButton.setFocusable(false);
        bothModeToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bothModeToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bothModeToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bothModeToggleButtonActionPerformed(evt);
            }
        });
        structureToolBar.add(bothModeToggleButton);

        structureModeButtonGroup.add(listModeToggleButton);
        listModeToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/view-list-icon-4.png"))); // NOI18N
        listModeToggleButton.setEnabled(false);
        listModeToggleButton.setFocusable(false);
        listModeToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        listModeToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        listModeToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listModeToggleButtonActionPerformed(evt);
            }
        });
        structureToolBar.add(listModeToggleButton);

        setLayout(new java.awt.BorderLayout());

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

        add(headerPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void treeModeToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_treeModeToggleButtonActionPerformed
        setMode(Mode.TREE);
    }//GEN-LAST:event_treeModeToggleButtonActionPerformed

    private void bothModeToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bothModeToggleButtonActionPerformed
        setMode(Mode.BOTH);
    }//GEN-LAST:event_bothModeToggleButtonActionPerformed

    private void listModeToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listModeToggleButtonActionPerformed
        setMode(Mode.LIST);
    }//GEN-LAST:event_listModeToggleButtonActionPerformed

    @Nonnull
    public Optional<XBTBlock> getSelectedItem() {
        if (mode == Mode.TREE) {
            return Optional.ofNullable(treePanel.getSelectedItem());
        }

        return blockListPanel.getSelectedItem();
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
        TestApplication.run(() -> WindowUtils.invokeWindow(new XBStructurePanel()));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressTextField;
    private javax.swing.JToggleButton bothModeToggleButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JToggleButton listModeToggleButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JSplitPane previewSplitPane;
    private javax.swing.JButton previousButton;
    private javax.swing.ButtonGroup structureModeButtonGroup;
    private javax.swing.JPanel structurePanel;
    private javax.swing.JToolBar structureToolBar;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JToggleButton treeModeToggleButton;
    private javax.swing.JSplitPane treeSplitPane;
    private javax.swing.JButton upButton;
    private javax.swing.ButtonGroup viewerButtonGroup;
    // End of variables declaration//GEN-END:variables

    public void updateUndoAvailable() {
        firePropertyChange("undoAvailable", false, true);
        firePropertyChange("redoAvailable", false, true);
    }

    public boolean isShowPreview() {
        return showPreview;
    }

    public void setShowPreviewPanel(boolean showPreview) {
        if (this.showPreview != showPreview) {
            if (this.showPreview) {
                remove(previewSplitPane);
                add(structurePanel, BorderLayout.CENTER);
            } else {
                remove(structurePanel);
                previewSplitPane.setLeftComponent(structurePanel);
                add(previewSplitPane, BorderLayout.CENTER);
            }

            invalidate();
            repaint();
            this.showPreview = showPreview;
        }
    }

    @Nonnull
    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        if (this.mode == mode) {
            return;
        }

        switch (this.mode) {
            case LIST: {
                structurePanel.remove(blockListPanel);
                break;
            }
            case TREE: {
                structurePanel.remove(treePanel);
                break;
            }

            case BOTH: {
                structurePanel.remove(treeSplitPane);
                break;
            }
        }

        switch (mode) {
            case LIST: {
                structurePanel.add(blockListPanel, BorderLayout.CENTER);
                break;
            }
            case TREE: {
                structurePanel.add(treePanel, BorderLayout.CENTER);
                break;
            }

            case BOTH: {
                treeSplitPane.setLeftComponent(treePanel);
                treeSplitPane.setRightComponent(blockListPanel);
                structurePanel.add(treeSplitPane, BorderLayout.CENTER);
                blockListPanel.setBlock(treePanel.getSelectedItem());
                break;
            }
        }
        structurePanel.revalidate();
        structurePanel.repaint();

        this.mode = mode;
    }

    @Nonnull
    public Optional<BlockViewer> getActivePreviewViewer() {
        return Optional.ofNullable(activeViewer);
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
        blockListPanel.setPopupMenu(popupMenu);
    }

    public void setTreeDocument(XbupTreeDocument treeDocument) {
        treePanel.setTreeDocument(treeDocument);
        blockListPanel.setTreeDocument(treeDocument);
    }

    public void addItemSelectionListener(DocumentItemSelectionListener listener) {
        itemSelectionListeners.add(listener);
    }

    public void removeItemSelectionListener(DocumentItemSelectionListener listener) {
        itemSelectionListeners.remove(listener);
    }
    
    private void notifyItemSelectionChanged(@Nullable XBTBlock item) {
        for (DocumentItemSelectionListener listener : itemSelectionListeners) {
            listener.itemSelected(item);
        }
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

    public enum Mode {
        TREE,
        LIST,
        BOTH
    }
}
