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
package org.exbin.framework.xbup.catalog.item.gui;

import java.util.ResourceBundle;
import javax.annotation.Nullable;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;

/**
 * XBManager catalog specification selection panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class CatalogEditDocumentationPanel extends javax.swing.JPanel {

    private String documentation;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CatalogEditDocumentationPanel.class);

    public CatalogEditDocumentationPanel() {
        initComponents();
        init();
    }

    private void init() {
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        editorPane.setContentType("text/html"); // NOI18N
        scrollPane.setViewportView(editorPane);

        add(scrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new CatalogEditDocumentationPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane editorPane;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(@Nullable String documentation) {
        this.documentation = documentation;
        editorPane.setText(documentation == null ? "<html><body></body></html>" : documentation);
    }
}
