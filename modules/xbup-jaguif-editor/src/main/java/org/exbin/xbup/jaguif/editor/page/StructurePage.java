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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.jaguif.App;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;
import org.exbin.xbup.jaguif.editor.page.gui.XBStructurePanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.operation.undo.UndoRedo;

/**
 * Structure viewer/editor of document.
 */
@ParametersAreNonnullByDefault
public class StructurePage extends AbstractTabPagesComponent implements XbupEditorPage {

    public static final String PAGE_ID = "structure";

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(StructurePage.class);
    protected final XBStructurePanel structurePanel = new XBStructurePanel();
    protected XbupTree xbupTree;
    protected PluginUiBlockPage pluginPage;

    protected final List<XbupEditorBlockPage> blockViewers = new ArrayList<>();

    public StructurePage() {
        init();
    }

    private void init() {
        putValue(KEY_ID, PAGE_ID);
        putValue(KEY_NAME, resourceBundle.getString("page.name"));
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource(resourceBundle.getString("page.icon"))));
        pluginPage = new PluginUiBlockPage();
        blockViewers.add(pluginPage);
        blockViewers.add(new PropertiesBlockPage());
        blockViewers.add(new TextualBlockPage());
        blockViewers.add(new BinaryBlockPage());

        structurePanel.addBlockSelectionListener((item) -> {
            XBTBlock block = structurePanel.getSelectedItem().orElse(null);
            String itemPath;
            if (block != null) {
                if (!block.getParentBlock().isPresent()) {
                    itemPath = "/";
                } else {
                    StringBuilder builder = new StringBuilder();

                    Optional<XBTBlock> parentItem;
                    XBTBlock pathItem = block;
                    do {
                        parentItem = pathItem.getParentBlock();
                        if (parentItem.isPresent()) {
                            builder.insert(0, "/" + System.identityHashCode(pathItem));
                            pathItem = parentItem.get();
                        }
                    } while (parentItem.isPresent());
                    itemPath = builder.toString();
                }
            } else {
                itemPath = "";
            }
            structurePanel.setAddressText(itemPath);
        });

        for (XbupEditorBlockPage blockPage : blockViewers) {
            structurePanel.addBlockPage(blockPage);
        }
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return structurePanel;
    }

    @Override
    public void setXbupTree(XbupTree xbupTree) {
        this.xbupTree = xbupTree;
        structurePanel.setCatalog(xbupTree.getCatalog());
        structurePanel.setXbupTree(xbupTree);
    }

    public void setUndoHandler(UndoRedo undoRedo) {
        pluginPage.setUndoHandler(undoRedo);
    }

    public void postWindowOpened() {
        structurePanel.postWindowOpened();
    }

    public void reportStructureChange(XBTBlock block) {
        structurePanel.reportStructureChange(block);
    }

    @Nonnull
    public Optional<XBTBlock> getSelectedItem() {
        return structurePanel.getSelectedItem();
    }
}
