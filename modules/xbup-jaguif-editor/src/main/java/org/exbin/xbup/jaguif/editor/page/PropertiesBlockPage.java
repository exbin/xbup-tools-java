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
package org.exbin.xbup.jaguif.editor.page;

import java.awt.BorderLayout;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.exbin.jaguif.App;
import org.exbin.jaguif.context.api.ContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;
import org.exbin.xbup.jaguif.component.page.XbupPagesPanel;
import org.exbin.xbup.jaguif.editor.gui.SimpleMessagePanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.block.XbupBlockState;
import org.exbin.xbup.jaguif.viewer.gui.GeneralPropertiesPanel;

/**
 * Properties viewer of document.
 */
@ParametersAreNonnullByDefault
public class PropertiesBlockPage extends AbstractTabPagesComponent implements XbupEditorBlockPage {

    public static final String PAGE_ID = "propertiesBlock";

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(PropertiesBlockPage.class);
    protected final JPanel panel = new JPanel();
    protected final XbupPagesPanel viewerPanel = new XbupPagesPanel();
    protected final GeneralPropertiesPanel generalPanel = new GeneralPropertiesPanel();
    protected XbupBlockState xbupBlock;

    public PropertiesBlockPage() {
        putValue(KEY_ID, PAGE_ID);
        putValue(KEY_NAME, resourceBundle.getString("page.name"));
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource(resourceBundle.getString("page.icon"))));
        putValue(KEY_CONTEXT_CHANGE, new ContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(XbupBlockState.class, (instance) -> {
                    setXbupBlock(instance);
                });
            }
        });
        panel.setLayout(new BorderLayout());
        panel.add(viewerPanel, BorderLayout.CENTER);

        SimpleMessagePanel messagePanel = new SimpleMessagePanel();
        viewerPanel.setMainComponent(messagePanel);
    }

    @Override
    public void setXbupBlock(@Nullable XbupBlockState xbupBlock) {
        this.xbupBlock = xbupBlock;

        XBTBlock block = xbupBlock == null ? null : xbupBlock.getBlock();
        viewerPanel.removeAllPages();
        if (block != null) {
            XbupTree xbupTree = xbupBlock.getXbupTree();
            generalPanel.setCatalog(xbupTree.getCatalog());
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
