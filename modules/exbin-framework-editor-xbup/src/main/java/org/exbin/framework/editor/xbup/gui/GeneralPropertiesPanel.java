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
package org.exbin.framework.editor.xbup.gui;

import java.awt.BorderLayout;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemInfoPanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * General properties panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class GeneralPropertiesPanel extends javax.swing.JPanel {

    private XBTBlock block;
    private XBACatalog catalog;
    private final GeneralBlockPropertiesPanel generalPanel = new GeneralBlockPropertiesPanel();
    private final CatalogItemInfoPanel catalogItemPanel = new CatalogItemInfoPanel();
    private boolean devMode = false;

    public GeneralPropertiesPanel() {
        initComponents();
        init();
    }

    private void init() {
        add(generalPanel, BorderLayout.NORTH);
        add(catalogItemPanel, BorderLayout.CENTER);
    }

    public void setBlock(XBTBlock block) {
        this.block = block;
        generalPanel.setBlock(block);
        XBBlockDecl decl = block instanceof XBTTreeNode ? ((XBTTreeNode) block).getBlockDecl() : null;
        if (decl instanceof XBCBlockDecl) {
            XBCBlockSpec spec = ((XBCBlockDecl) decl).getBlockSpecRev().getParent();
            catalogItemPanel.setItem(spec);
        } else {
            catalogItemPanel.setItem(null);
        }
    }

    @Nullable
    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
        generalPanel.setCatalog(catalog);
        catalogItemPanel.setCatalog(catalog);
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
        generalPanel.setDevMode(devMode);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new GeneralPropertiesPanel());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
