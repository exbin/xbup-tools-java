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
package org.exbin.xbup.jaguif.catalog.item.plugin;

import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.jaguif.catalog.item.plugin.ation.AddItemPluginAction;
import org.exbin.xbup.jaguif.catalog.item.plugin.ation.EditItemPluginAction;
import org.exbin.xbup.jaguif.catalog.item.plugin.gui.CatalogItemEditPluginsPanel;
import org.exbin.xbup.jaguif.catalog.item.plugin.gui.CatalogPluginsTableModel;
import org.jspecify.annotations.NullMarked;

/**
 * Catalog plugins editor controller.
 */
@NullMarked
public class CatalogPluginsEditorController implements ContextEditItem {

    protected final AddItemPluginAction addPluginAction = new AddItemPluginAction();
    protected final EditItemPluginAction editPluginAction = new EditItemPluginAction();

    protected final CatalogItemEditPluginsPanel catalogEditorPanel;

    public CatalogPluginsEditorController(CatalogItemEditPluginsPanel catalogEditorPanel) {
        this.catalogEditorPanel = catalogEditorPanel;
        addPluginAction.init();
        editPluginAction.init();
    }

    @Override
    public void performAddItem() {
        addPluginAction.actionPerformed(null);
        AddItemPluginAction.ResultData resultData = addPluginAction.getResultData();
        if (resultData != null) {
            CatalogPluginsTableModel pluginsModel = catalogEditorPanel.getPluginsModel();
            pluginsModel.addItem(resultData.plugin, resultData.file, resultData.rowEditorsCount, resultData.panelViewersCount, resultData.panelEditorsCount);
//                    catalogEditorPanel.reloadNodesTree();
        }
    }

    @Override
    public void performEditItem() {
        editPluginAction.setCurrentPlugin(catalogEditorPanel.getSelectedPlugin());
        editPluginAction.actionPerformed(null);
    }

    @Override
    public void performDeleteItem() {
//                deleteCatalogItemAction.setCurrentItem(catalogEditorPanel.getSelectedTreeItem());
//                deleteCatalogItemAction.actionPerformed(null);
    }

    @Override
    public boolean canAddItem() {
        return true;
    }

    @Override
    public boolean canEditItem() {
        XBCXPlugin plugin = catalogEditorPanel.getSelectedPlugin();
        return plugin != null;
    }

    @Override
    public boolean canDeleteItem() {
        return false;
//                XBCNode node = catalogEditorPanel.getSelectedTreeItem();
//                return node != null && node.getParent().isPresent();
    }

    public void registerMonitoring(ContextMonitoringRegistration monitoringRegistrar) {
        monitoringRegistrar.registerContextMonitoring(addPluginAction);
        monitoringRegistrar.registerContextMonitoring(editPluginAction);
    }
}
