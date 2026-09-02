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
package org.exbin.xbup.jaguif.catalog.item.plugin.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.jaguif.catalog.item.plugin.gui.CatalogEditNodePluginPanel;
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
import org.exbin.jaguif.window.api.controller.DefaultControlController;

/**
 * Edit catalog item plugin action.
 */
@NullMarked
public class EditItemPluginAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogItemPlugin";
    
    protected final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(EditItemPluginAction.class);
    protected @Nullable XBACatalog catalog;

    protected @Nullable DialogParentComponent parentComponent;
    protected @Nullable XBCXPlugin currentPlugin;
    protected @Nullable String resultName;
    protected @Nullable byte[] resultData;

    public EditItemPluginAction() {
    }

    public void init() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(DialogParentComponent.class, (instance) -> {
                    parentComponent = instance;
                });
                registrar.registerChangeListener(XBACatalog.class, (instance) -> {
                    catalog = instance;
                });
            }
        });
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

    public void setParentComponent(DialogParentComponent parentComponent) {
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
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
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
        dialog.showCentered(parentComponent.getComponent());
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
