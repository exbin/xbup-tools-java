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

import java.awt.Component;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.exbin.framework.App;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemType;
import org.exbin.framework.xbup.catalog.item.gui.CatalogItemsSearchPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;

/**
 * Catalog specification's revision selection panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogSelectRevPanel extends javax.swing.JPanel {

    private XBCRevService revService;
    private final CatalogItemsSearchPanel selectSpecPanel;
    private final CatalogRevsComboBoxModel revsModel;
    private boolean selectEnabled;
    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogSelectRevPanel.class);

    public CatalogSelectRevPanel(XBACatalog catalog, CatalogItemType specType) {
        revsModel = new CatalogRevsComboBoxModel();
        selectSpecPanel = new CatalogItemsSearchPanel();
        selectSpecPanel.setCatalog(catalog);
        initComponents();

        if (catalog != null) {
            revService = catalog.getCatalogService(XBCRevService.class);
        }

        init();
        selectSpecPanel.switchToSpecTypeMode(specType);
    }

    private void init() {
        add(selectSpecPanel);
        selectSpecPanel.setSelectionListener((XBCItem spec) -> {
            selectEnabled = spec != null;
            if (spec != null) {
                if (spec instanceof XBCSpec) {
                    revsModel.setRevs(revService.getRevs((XBCSpec) spec));
                    targetRevisionComboBox.setSelectedIndex(revsModel.getSize() - 1);
                } else {
                    revsModel.getRevs().clear();
                }
            } else {
                targetRevisionComboBox.setSelectedIndex(-1);
                revsModel.getRevs().clear();
            }

            revsModel.fireDataChanged();
        });

        targetRevisionComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                DefaultListCellRenderer retValue = (DefaultListCellRenderer) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof XBCRev) {
                    retValue.setText("Revision " + ((XBCRev) value).getXBIndex());
                }

                return retValue;
            }
        });
    }

    @Nonnull
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

        revisionSelectionPanel = new javax.swing.JPanel();
        targetRevisionComboBox = new javax.swing.JComboBox<>();

        setLayout(new java.awt.BorderLayout());

        targetRevisionComboBox.setModel(revsModel);

        javax.swing.GroupLayout revisionSelectionPanelLayout = new javax.swing.GroupLayout(revisionSelectionPanel);
        revisionSelectionPanel.setLayout(revisionSelectionPanelLayout);
        revisionSelectionPanelLayout.setHorizontalGroup(
            revisionSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(revisionSelectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(targetRevisionComboBox, 0, 388, Short.MAX_VALUE)
                .addContainerGap())
        );
        revisionSelectionPanelLayout.setVerticalGroup(
            revisionSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, revisionSelectionPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(targetRevisionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(revisionSelectionPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication.run(() -> WindowUtils.invokeWindow(new CatalogSelectRevPanel(null, CatalogItemType.NODE)));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel revisionSelectionPanel;
    private javax.swing.JComboBox<XBCRev> targetRevisionComboBox;
    // End of variables declaration//GEN-END:variables

    public boolean isSelectEnabled() {
        return selectEnabled;
    }

    public XBCItem getSpec() {
        return selectSpecPanel.getItem();
    }

    public void setSpec(XBCItem spec) {
        selectSpecPanel.setItem(spec);
    }

    public XBCRev getTarget() {
        return (XBCRev) targetRevisionComboBox.getSelectedItem();
    }

    public void setTarget(XBCRev rev) {
        XBCSpec specification;
        if (rev != null) {
            specification = rev.getParent();
            revsModel.setRevs(revService.getRevs((XBCSpec) specification));
            targetRevisionComboBox.setSelectedIndex((int) rev.getXBIndex());
        } else {
            specification = null;
        }

        setSpec(specification);
    }
}
