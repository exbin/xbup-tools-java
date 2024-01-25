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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.xbup.catalog.item.file.CatalogFilesEditor;
import org.exbin.framework.xbup.catalog.item.plugin.CatalogPluginsEditor;
import org.exbin.framework.xbup.catalog.item.property.CatalogPropertiesEditor;
import org.exbin.framework.xbup.catalog.item.revision.CatalogRevisionsEditor;
import org.exbin.framework.xbup.catalog.item.spec.CatalogDefinitionEditor;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Catalog item edit properties panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogEditItemPanel extends javax.swing.JPanel {

    private XBACatalog catalog;

    private CatalogPropertiesEditor propertiesEditor;
    private CatalogRevisionsEditor revisionsEditor;
    private CatalogDefinitionEditor definitionEditor;
    private CatalogFilesEditor catalogFilesEditor;
    private CatalogPluginsEditor catalogPluginsEditor;
    private MenuManagement menuManagement;
    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CatalogEditItemPanel.class);

    public CatalogEditItemPanel() {
        initComponents();
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setCatalogItem(XBCItem item) {
        mainTabbedPane.removeAll();

        propertiesEditor = new CatalogPropertiesEditor();
        propertiesEditor.setCatalog(catalog);
        propertiesEditor.setItem(item);
        mainTabbedPane.add(propertiesEditor.getCatalogEditorPanel(), "Basic");

        if (item instanceof XBCSpec) {
            revisionsEditor = new CatalogRevisionsEditor();
            revisionsEditor.setCatalog(catalog);
            revisionsEditor.setCatalogItem(item);
            mainTabbedPane.add(revisionsEditor.getCatalogEditorPanel(), "Revisions");

            definitionEditor = new CatalogDefinitionEditor();
            CatalogDefsTableModel defsTableModel = new CatalogDefsTableModel();
            defsTableModel.setCatalog(catalog);
            defsTableModel.setCatalogItem(item);
            definitionEditor.setCatalog(catalog);
            definitionEditor.setCatalogItem(item);
            definitionEditor.setDefsModel(defsTableModel);
            revisionsEditor.setDefsModel(defsTableModel);
            mainTabbedPane.add(definitionEditor.getCatalogEditorPanel(), "Definition");
        } else if (item instanceof XBCNode) {
            catalogFilesEditor = new CatalogFilesEditor();
            catalogFilesEditor.setCatalog(catalog);
            catalogFilesEditor.setNode((XBCNode) item);
            if (menuManagement != null) {
                catalogFilesEditor.setMenuManagement(menuManagement);
            }

            mainTabbedPane.add(catalogFilesEditor.getCatalogEditorPanel(), "Files");

            catalogPluginsEditor = new CatalogPluginsEditor();
            catalogPluginsEditor.setCatalog(catalog);
            catalogPluginsEditor.setNode((XBCNode) item);
            if (menuManagement != null) {
                catalogPluginsEditor.setMenuManagement(menuManagement);
            }

            mainTabbedPane.add(catalogPluginsEditor.getCatalogEditorPanel(), "Plugins");
        }
    }

    public void persist() {
        propertiesEditor.persist();
        if (definitionEditor != null) {
            definitionEditor.persist();
        }
        if (revisionsEditor != null) {
            revisionsEditor.persist();
        }
        if (catalogFilesEditor != null) {
            catalogFilesEditor.persist();
        }
    }

    public void setMenuManagement(MenuManagement menuManagement) {
        this.menuManagement = menuManagement;

        if (catalogFilesEditor != null) {
            catalogFilesEditor.setMenuManagement(menuManagement);
        }
    }

    public XBCItem getCatalogItem() {
        return propertiesEditor.getItem();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        if (propertiesEditor != null) {
            propertiesEditor.setCatalog(catalog);
        }
        if (revisionsEditor != null) {
            revisionsEditor.setCatalog(catalog);
        }
        if (definitionEditor != null) {
            definitionEditor.setCatalog(catalog);
        }
        if (catalogFilesEditor != null) {
            catalogFilesEditor.setCatalog(catalog);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();

        setLayout(new java.awt.BorderLayout());
        add(mainTabbedPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new CatalogEditItemPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane mainTabbedPane;
    // End of variables declaration//GEN-END:variables

}
