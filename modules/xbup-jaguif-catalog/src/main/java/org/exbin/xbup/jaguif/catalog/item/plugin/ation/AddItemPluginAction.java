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
package org.exbin.xbup.jaguif.catalog.item.plugin.ation;

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
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.catalog.entity.XBEXPlugUi;
import org.exbin.xbup.catalog.entity.XBEXPlugUiType;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.exbin.jaguif.window.api.controller.DefaultControlController;

/**
 * Add new plugin action.
 */
@NullMarked
public class AddItemPluginAction extends AbstractAction {

    public static final String ACTION_ID = "addCatalogItemPlugin";
    
    protected final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AddItemPluginAction.class);
    protected @Nullable XBACatalog catalog;

    protected @Nullable DialogParentComponent parentComponent;
    protected @Nullable XBCNode currentNode;
    protected int currentCount;
    protected @Nullable ResultData resultData;

    public AddItemPluginAction() {
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
    public XBCNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(XBCNode currentNode) {
        this.currentNode = currentNode;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    @Nullable
    public ResultData getResultData() {
        return resultData;
    }

    public void setParentComponent(DialogParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultData = null;
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        CatalogEditNodePluginPanel editPanel = new CatalogEditNodePluginPanel();
//        editPanel.setMenuManagement(menuManagement);
        editPanel.setCatalog(catalog);
        editPanel.setNode(currentNode);

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowHandler dialog = windowModule.createDialog(editPanel, controlPanel);
//        windowModule.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        windowModule.setWindowTitle(dialog, editPanel.getResourceBundle());
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            if (actionType == DefaultControlController.ControlActionType.OK) {
                XBCXUiService uiService = catalog.getCatalogService(XBCXUiService.class);
                XBCXFile file = editPanel.getFile();
                long rowEditorsCount = editPanel.getRowEditorsCount();
                long panelViewersCount = editPanel.getPanelViewersCount();
                long panelEditorsCount = editPanel.getPanelEditorsCount();
                XBEXPlugin plugin = new XBEXPlugin();
                plugin.setOwner((XBENode) currentNode);
                plugin.setPluginFile((XBEXFile) file);
                plugin.setPluginIndex(Long.valueOf(currentCount));

                EntityManager em = ((XBECatalog) catalog).getEntityManager();
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.persist(plugin);

                XBEXPlugUiType rowEditorType = (XBEXPlugUiType) uiService.findTypeById(XBPlugUiType.ROW_EDITOR.getDbIndex());
                for (long i = 0; i < rowEditorsCount; i++) {
                    XBEXPlugUi plugUi = new XBEXPlugUi();
                    plugUi.setPlugin(plugin);
                    plugUi.setUiType(rowEditorType);
                    plugUi.setMethodIndex(i);
                    em.persist(plugUi);
                }
                XBEXPlugUiType panelViewerType = (XBEXPlugUiType) uiService.findTypeById(XBPlugUiType.PANEL_VIEWER.getDbIndex());
                for (long i = 0; i < panelViewersCount; i++) {
                    XBEXPlugUi plugUi = new XBEXPlugUi();
                    plugUi.setPlugin(plugin);
                    plugUi.setUiType(panelViewerType);
                    plugUi.setMethodIndex(i);
                    em.persist(plugUi);
                }

                XBEXPlugUiType panelEditorType = (XBEXPlugUiType) uiService.findTypeById(XBPlugUiType.PANEL_EDITOR.getDbIndex());
                for (long i = 0; i < panelEditorsCount; i++) {
                    XBEXPlugUi plugUi = new XBEXPlugUi();
                    plugUi.setPlugin(plugin);
                    plugUi.setUiType(panelEditorType);
                    plugUi.setMethodIndex(i);
                    em.persist(plugUi);
                }

                em.flush();
                transaction.commit();
                resultData = new ResultData();
                resultData.plugin = plugin;
                resultData.file = file;
                resultData.rowEditorsCount = rowEditorsCount;
                resultData.panelViewersCount = panelViewersCount;
                resultData.panelEditorsCount = panelEditorsCount;
            }
            dialog.close();
        });
        dialog.showCentered(parentComponent.getComponent());
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
    
    // TODO make a record
    public static final class ResultData {
        public XBEXPlugin plugin;
        public XBCXFile file;
        public long rowEditorsCount;
        public long panelViewersCount;
        public long panelEditorsCount;
    }
}
