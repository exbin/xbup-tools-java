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
package org.exbin.framework.xbup.catalog.gui;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.exbin.framework.App;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandlerEmpty;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.service.XBCRootService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;

/**
 * Panel for list of catalogs.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogsManagerPanel extends javax.swing.JPanel {

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogsManagerPanel.class);

    private final ToolBarSidePanel toolBar = new ToolBarSidePanel();
    private XBACatalog catalog;
    private List<XBCRoot> catalogRoots;

    public CatalogsManagerPanel() {
        initComponents();
        init();
    }

    private void init() {
        add(toolBar, BorderLayout.WEST);
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void addActions(ActionsProvider actionsProvider) {
        toolBar.addActions(actionsProvider);
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
        if (catalog != null) {
            reload();
        }
    }

    public void reload() {
        DefaultTableModel tableModel = ((DefaultTableModel) catalogsTable.getModel());
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        XBCRootService rootService = catalog.getCatalogService(XBCRootService.class);
        XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);

        catalogRoots = rootService.getAllItems();
        for (XBCRoot catalogRoot : catalogRoots) {
            Optional<Date> lastUpdate = catalogRoot.getLastUpdate();
            String lastUpdateText = lastUpdate.isPresent() ? lastUpdate.get().toString() : "";
            Object[] row;
            if (catalogRoot.getUrl().isPresent()) {
                String nodeName = nameService.getDefaultText(catalogRoot.getNode());
                row = new Object[]{"Catalog " + nodeName, catalogRoot.getUrl().orElse(""), lastUpdateText};
            } else {
                row = new Object[]{"Main", "(build-in)", lastUpdateText};
            }
            tableModel.addRow(row);
        }
    }

    public void addRowSelectionListener(ListSelectionListener listener) {
        catalogsTable.getSelectionModel().addListSelectionListener(listener);
    }

    @Nullable
    public XBCRoot getSelectedItem() {
        int selectedRow = catalogsTable.getSelectedRow();
        return selectedRow < 0 ? null : catalogRoots.get(selectedRow);
    }

    public boolean hasSelection() {
        return catalogsTable.getSelectedRow() >= 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        catalogsScrollPane = new javax.swing.JScrollPane();
        catalogsTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        catalogsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "URL", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        catalogsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        catalogsScrollPane.setViewportView(catalogsTable);

        add(catalogsScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CatalogsManagerPanel catalogsBrowserPanel = new CatalogsManagerPanel();
        DefaultEditItemActions defaultEditItemActions = new DefaultEditItemActions();
        defaultEditItemActions.setEditItemActionsHandler(new EditItemActionsHandlerEmpty());
        catalogsBrowserPanel.addActions(defaultEditItemActions);
        WindowUtils.invokeWindow(catalogsBrowserPanel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane catalogsScrollPane;
    private javax.swing.JTable catalogsTable;
    // End of variables declaration//GEN-END:variables

}
