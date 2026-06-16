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
package org.exbin.xbup.jaguif.viewer.page;

import java.awt.BorderLayout;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;
import org.exbin.xbup.jaguif.component.page.XbupPagesPanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.block.XbupBlock;
import org.exbin.xbup.jaguif.viewer.gui.GeneralPropertiesPanel;
import org.exbin.xbup.jaguif.viewer.gui.SimpleMessagePanel;

/**
 * Properties viewer of document.
 */
@ParametersAreNonnullByDefault
public class PropertiesBlockPage extends AbstractTabPagesComponent implements XbupViewerBlockPage {

    private final JPanel panel = new JPanel();
    private final XbupPagesPanel viewerPanel = new XbupPagesPanel();
    private final GeneralPropertiesPanel generalPanel = new GeneralPropertiesPanel();
    private XbupBlock xbupBlockTree;

    public PropertiesBlockPage() {
        putValue(KEY_NAME, "Properties");
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/viewer/resources/icons/16px/tooloptions.png")));
        panel.setLayout(new BorderLayout());
        panel.add(viewerPanel, BorderLayout.CENTER);

        SimpleMessagePanel messagePanel = new SimpleMessagePanel();
        viewerPanel.setMainComponent(messagePanel);
    }

    @Override
    public void setXbupBlock(XbupBlock xbupBlock) {
        this.xbupBlockTree = xbupBlock;
        generalPanel.setCatalog(xbupBlock.getCatalog());

        XBTBlock block = xbupBlock == null ? null : xbupBlock.getBlock();
        viewerPanel.removeAllPages();
        if (block != null) {
            viewerPanel.addPage("General", generalPanel);
            viewerPanel.finishPages();
            generalPanel.setBlock(block);
        }

        viewerPanel.revalidate();
        viewerPanel.repaint();
    }

    public void setDevMode(boolean devMode) {
        generalPanel.setDevMode(devMode);
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return panel;
    }
}
