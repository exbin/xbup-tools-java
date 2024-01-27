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
package org.exbin.framework.xbup.catalog.item.plugin.ation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.DefaultControlPanel;
import org.exbin.framework.window.api.handler.DefaultControlHandler;
import org.exbin.framework.xbup.catalog.item.plugin.gui.CatalogEditNodePluginPanel;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.catalog.entity.XBEXPlugUi;
import org.exbin.xbup.catalog.entity.XBEXPlugUiType;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;

/**
 * Edit catalog item plugin action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditItemPluginAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogItemPluginAction";
    
    private XBACatalog catalog;

    private Component parentComponent;
    private XBCXPlugin currentPlugin;
    private String resultName;
    private byte[] resultData;

    public EditItemPluginAction() {
    }

    @Nullable
    public XBCXPlugin getCurrentPlugin() {
        return currentPlugin;
    }

    public void setCurrentPlugin(XBCXPlugin currentPlugin) {
        this.currentPlugin = currentPlugin;
    }

    @Nullable
    public String getResultName() {
        return resultName;
    }

    @Nullable
    public byte[] getResultData() {
        return resultData;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultName = null;
        resultData = null;

        XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);

        long rowEditorsCount = uiService.getPlugUisCount(currentPlugin, XBPlugUiType.ROW_EDITOR);
        long panelViewersCount = uiService.getPlugUisCount(currentPlugin, XBPlugUiType.PANEL_VIEWER);
        long panelEditorsCount = uiService.getPlugUisCount(currentPlugin, XBPlugUiType.PANEL_EDITOR);

        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogEditNodePluginPanel editPanel = new CatalogEditNodePluginPanel();
//        editPanel.setMenuManagement(menuManagement);
        editPanel.setCatalog(catalog);
        editPanel.setPlugin(currentPlugin);
        editPanel.setRowEditorsCount(rowEditorsCount);
        editPanel.setPanelViewersCount(panelViewersCount);
        editPanel.setPanelEditorsCount(panelEditorsCount);

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(editPanel, controlPanel);
//        windowModule.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        windowModule.setWindowTitle(dialog, editPanel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                XBEXPlugin plugin = (XBEXPlugin) editPanel.getPlugin();
                XBCXFile file = editPanel.getFile();
                long updatedRowEditorsCount = editPanel.getRowEditorsCount();
                long updatedPanelViewersCount = editPanel.getPanelViewersCount();
                long updatedPanelEditorsCount = editPanel.getPanelEditorsCount();
                plugin.setPluginFile((XBEXFile) file);

                EntityManager em = ((XBECatalog) catalog).getEntityManager();
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.persist(plugin);

                if (updatedRowEditorsCount > rowEditorsCount) {
                    XBEXPlugUiType rowEditorType = (XBEXPlugUiType) uiService.findTypeById(XBPlugUiType.ROW_EDITOR.getDbIndex());
                    for (long i = rowEditorsCount; i < updatedRowEditorsCount; i++) {
                        XBEXPlugUi plugUi = new XBEXPlugUi();
                        plugUi.setPlugin(plugin);
                        plugUi.setUiType(rowEditorType);
                        plugUi.setMethodIndex(i);
                        em.persist(plugUi);
                    }
                } else {
                    for (long i = rowEditorsCount - 1; i >= updatedRowEditorsCount; i--) {
                        em.remove(uiService.getPlugUi(plugin, XBPlugUiType.ROW_EDITOR, i));
                    }
                }

                if (updatedPanelViewersCount > panelViewersCount) {
                    XBEXPlugUiType uiType = (XBEXPlugUiType) uiService.findTypeById(XBPlugUiType.PANEL_VIEWER.getDbIndex());
                    for (long i = panelViewersCount; i < updatedPanelViewersCount; i++) {
                        XBEXPlugUi plugUi = new XBEXPlugUi();
                        plugUi.setPlugin(plugin);
                        plugUi.setUiType(uiType);
                        plugUi.setMethodIndex(i);
                        em.persist(plugUi);
                    }
                } else {
                    for (long i = panelViewersCount - 1; i >= updatedPanelViewersCount; i--) {
                        em.remove(uiService.getPlugUi(plugin, XBPlugUiType.PANEL_VIEWER, i));
                    }
                }

                if (updatedPanelEditorsCount > panelEditorsCount) {
                    XBEXPlugUiType uiType = (XBEXPlugUiType) uiService.findTypeById(XBPlugUiType.PANEL_EDITOR.getDbIndex());
                    for (long i = panelEditorsCount; i < updatedPanelEditorsCount; i++) {
                        XBEXPlugUi plugUi = new XBEXPlugUi();
                        plugUi.setPlugin(plugin);
                        plugUi.setUiType(uiType);
                        plugUi.setMethodIndex(i);
                        em.persist(plugUi);
                    }
                } else {
                    for (long i = panelEditorsCount - 1; i >= updatedPanelEditorsCount; i--) {
                        em.remove(uiService.getPlugUi(plugin, XBPlugUiType.PANEL_EDITOR, i));
                    }
                }

                em.flush();
                transaction.commit();

                // TODO pluginsModel.updateItem(selectedRow, plugin, updatedRowEditorsCount, updatedPanelViewersCount, updatedPanelEditorsCount);
            }
            dialog.close();
        });
        dialog.showCentered(parentComponent);
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
