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
package org.exbin.framework.xbup.catalog.item.revision.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.component.gui.ToolBarSidePanel;
import org.exbin.framework.data.model.CatalogDefsTableModel;
import org.exbin.framework.data.model.CatalogRevsTableModel;
import org.exbin.framework.data.model.CatalogRevsTableItem;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.catalog.entity.XBERev;
import org.exbin.xbup.catalog.entity.service.XBEXDescService;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;

/**
 * Catalog item revisions panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogItemEditRevsPanel extends javax.swing.JPanel {

    private XBApplication application;
    private XBACatalog catalog;
    private XBCItem catalogItem;
    private XBCRevService revService;
    private final CatalogRevsTableModel revsModel;
    private CatalogDefsTableModel defsModel;
    private List<CatalogRevsTableItem> removeList;
    private List<CatalogRevsTableItem> updateList;
    private final ToolBarSidePanel toolBarPanel = new ToolBarSidePanel();

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CatalogItemEditRevsPanel.class);

    public CatalogItemEditRevsPanel() {
        revsModel = new CatalogRevsTableModel();
        initComponents();

        toolBarPanel.setToolBarPosition(ToolBarSidePanel.ToolBarPosition.RIGHT);
        toolBarPanel.add(itemRevisionsScrollPane, BorderLayout.CENTER);
        add(toolBarPanel, BorderLayout.CENTER);
    }

    public void setApplication(XBApplication application) {
        this.application = application;
    }

    @Nullable
    public CatalogRevsTableItem getSelectedRevision() {
        int selectedRow = itemRevisionsTable.getSelectedRow();
        if (selectedRow >= 0) {
            return revsModel.getRowItem(selectedRow);
        }
        
        return null;
    }

    public void addFileActions(ActionsProvider actionsProvider) {
        toolBarPanel.addActions(actionsProvider);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itemRevisionsScrollPane = new javax.swing.JScrollPane();
        itemRevisionsTable = new javax.swing.JTable();

        itemRevisionsTable.setModel(revsModel);
        itemRevisionsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemRevisionsScrollPane.setViewportView(itemRevisionsTable);

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowUtils.invokeDialog(new CatalogItemEditRevsPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane itemRevisionsScrollPane;
    private javax.swing.JTable itemRevisionsTable;
    // End of variables declaration//GEN-END:variables

    public void persist() {
        updateList.forEach((revItem) -> {
            XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
            XBCXDescService descService = catalog.getCatalogService(XBCXDescService.class);

            XBERev rev = (XBERev) revItem.getRev();
            if (rev == null) {
                rev = (XBERev) revService.createRev((XBCSpec) catalogItem);
                rev.setXBIndex(revItem.getXbIndex());
            }

            rev.setXBLimit(revItem.getLimit());

            revService.persistItem(rev);

            ((XBEXNameService) nameService).setDefaultText(rev, revItem.getName());
            ((XBEXDescService) descService).setDefaultText(rev, revItem.getDescription());
        });

        removeList.stream().filter((revItem) -> (revItem.getRev() != null)).forEachOrdered((revItem) -> {
            revService.removeItemDepth(revItem.getRev());
        });
    }

    public void setCatalogItem(XBCItem catalogItem) {
        this.catalogItem = catalogItem;
        revsModel.setSpec((XBCSpec) catalogItem);
        updateList = new ArrayList<>();
        removeList = new ArrayList<>();
    }

    public XBCItem getCatalogItem() {
        return catalogItem;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        revService = catalog.getCatalogService(XBCRevService.class);
        revsModel.setCatalog(catalog);
    }

    public void setDefsModel(CatalogDefsTableModel defsModel) {
        this.defsModel = defsModel;
        defsModel.setRevsModel(revsModel);
    }

    public void revisionAdded(CatalogRevsTableItem resultRevision) {
        long maxXbIndex = 0;
        if (revsModel.getRowCount() > 0) {
            CatalogRevsTableItem rewItem = revsModel.getRowItem(revsModel.getRowCount() - 1);
            if (rewItem.getXbIndex() >= maxXbIndex) {
                maxXbIndex = rewItem.getXbIndex() + 1;
            }
        }

        resultRevision.setXbIndex(maxXbIndex);
        if (!updateList.contains(resultRevision)) {
            updateList.add(resultRevision);
        }

        revsModel.getRevs().add(resultRevision);
        revsModel.fireTableDataChanged();
        defsModel.updateDefRevisions();
    }

    public void revisionEdited(CatalogRevsTableItem resultRevision) {
        if (!updateList.contains(resultRevision)) {
            updateList.add(resultRevision);
        }

        defsModel.updateDefRevisions();
    }

    public void revisionRemoved(CatalogRevsTableItem resultRevision) {
        if (updateList.contains(resultRevision)) {
            updateList.remove(resultRevision);
        }

        removeList.add(resultRevision);
        revsModel.getRevs().remove(resultRevision);
        revsModel.fireTableDataChanged();
        defsModel.updateDefRevisions();
    }

    public void setPanelPopup(JPopupMenu popupMenu) {
        itemRevisionsScrollPane.setComponentPopupMenu(popupMenu);
    }

    public void addSelectionListener(EditItemActionsUpdateListener updateListener) {
        itemRevisionsTable.getSelectionModel().addListSelectionListener((e) -> {
            updateListener.stateChanged();
        });
    }

}
