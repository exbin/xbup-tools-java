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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import org.exbin.framework.App;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.editor.xbup.viewer.BlockViewer;

/**
 * Panel for document viewer/editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDocumentPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XBDocumentPanel.class);

    private XBPluginRepository pluginRepository;

    private XBTBlock block;
    private List<BlockViewer> blockViewers = new ArrayList<>();
    private List<ViewerChangedListener> viewerChangedListeners = new ArrayList<>();
    private int activeViewerIndex = -1;
    private BlockViewer activeViewer = null;

    public XBDocumentPanel() {
        initComponents();
    }

    public void addBlockViewer(BlockViewer blockViewer) {
        int blockViewerIndex = blockViewers.size();
        blockViewers.add(blockViewer);

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
            BlockViewer blockViewer = blockViewers.get(blockViewerIndex);
            if (blockViewer == activeViewer) {
                return;
            }

            blockViewer.setBlock(block);

            if (activeViewer != null) {
                remove(activeViewer.getComponent());
            }

            activeViewer = blockViewer;
            add(activeViewer.getComponent(), BorderLayout.CENTER);
            revalidate();
            repaint();
            activeViewerIndex = blockViewerIndex;
        }

        for (ViewerChangedListener listener : viewerChangedListeners) {
            listener.viewerChanged();
        }
    }

    @Nonnull
    public Optional<BlockViewer> getActiveViewer() {
        return Optional.ofNullable(activeViewer);
    }

    public void setBlock(@Nullable XBTBlock block) {
        this.block = block;
        if (activeViewer != null) {
            activeViewer.setBlock(block);
        }
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

        viewerButtonGroup = new javax.swing.ButtonGroup();
        headerPanel = new javax.swing.JPanel();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        addressTextField = new javax.swing.JTextField();
        bottomPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/arrow-left.png"))); // NOI18N
        previousButton.setEnabled(false);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/xbup/resources/icons/open_icon_library-standard/icons/png/16x16/actions/arrow-right.png"))); // NOI18N
        nextButton.setEnabled(false);

        addressTextField.setEditable(false);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(previousButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(previousButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );

        add(headerPanel, java.awt.BorderLayout.NORTH);

        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        add(bottomPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeWindow(new XBDocumentPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressTextField;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.ButtonGroup viewerButtonGroup;
    // End of variables declaration//GEN-END:variables

    @Nonnull
    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    public void addTabChangedListener(ViewerChangedListener listener) {
        viewerChangedListeners.add(listener);
    }

    public void removeTabChangedListener(ViewerChangedListener listener) {
        viewerChangedListeners.remove(listener);
    }

    public interface ViewerChangedListener {

        void viewerChanged();
    }
}
