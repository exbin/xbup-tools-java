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
package org.exbin.framework.editor.xbup.def.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.def.AttributesEditor;
import org.exbin.framework.editor.xbup.def.ParametersEditor;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Block node content panel - level 0.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class NodeBlockPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(NodeBlockPanel.class);
    private XBApplication application;
    private final AttributesEditor attributesEditor = new AttributesEditor();
    private final ParametersEditor parametersEditor = new ParametersEditor();

    public NodeBlockPanel() {
        initComponents();
        tabbedPane.add("Attributes", attributesEditor.getEditorPanel());
        tabbedPane.add("Child Blocks", parametersEditor.getEditorPanel());
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        attributesEditor.setApplication(application);
        parametersEditor.setApplication(application);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        parametersEditor.setPluginRepository(pluginRepository);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();

        setLayout(new java.awt.GridLayout());
        add(tabbedPane);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new NodeBlockPanel());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    public void setCatalog(XBACatalog catalog) {
        attributesEditor.setCatalog(catalog);
        parametersEditor.setCatalog(catalog);
    }

    public void setBlock(XBTTreeNode block) {
        attributesEditor.setBlock(block);
        parametersEditor.setBlock(block);
    }
}
