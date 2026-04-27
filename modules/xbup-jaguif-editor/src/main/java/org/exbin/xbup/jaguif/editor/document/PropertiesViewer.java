/*
 * Copyright (C) ExBin Project, https://exbin.org
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
package org.exbin.xbup.jaguif.editor.document;

import java.awt.BorderLayout;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.exbin.xbup.jaguif.viewer.document.gui.DocumentViewerPanel;
import org.exbin.xbup.jaguif.editor.gui.GeneralPropertiesPanel;
import org.exbin.xbup.jaguif.editor.gui.SimpleMessagePanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Properties viewer of document.
 */
@ParametersAreNonnullByDefault
public class PropertiesViewer implements BlockViewer {

    private final JPanel panel = new JPanel();
    private final DocumentViewerPanel viewerPanel = new DocumentViewerPanel();
    private final GeneralPropertiesPanel generalPanel = new GeneralPropertiesPanel();
    private XBACatalog catalog;

    public PropertiesViewer() {
        panel.setLayout(new BorderLayout());
        panel.add(viewerPanel, BorderLayout.CENTER);

        SimpleMessagePanel messagePanel = new SimpleMessagePanel();
        viewerPanel.setMainComponent(messagePanel);
    }

    @Override
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        generalPanel.setCatalog(catalog);
    }

    @Override
    public void setPluginRepository(XBPluginRepository pluginRepository) {
    }

    public void setDevMode(boolean devMode) {
        generalPanel.setDevMode(devMode);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Properties";
    }

    @Nonnull
    @Override
    public Optional<ImageIcon> getIcon() {
        return Optional.of(new javax.swing.ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/editor/resources/icons/16px/tooloptions.png")));
    }

    @Override
    public void setBlock(@Nullable XBTBlock block) {
        viewerPanel.removeAllViews();
        if (block != null) {
            viewerPanel.addView("General", generalPanel);
            viewerPanel.viewsAdded();
            generalPanel.setBlock(block);
        }

        viewerPanel.revalidate();
        viewerPanel.repaint();
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return panel;
    }
}
