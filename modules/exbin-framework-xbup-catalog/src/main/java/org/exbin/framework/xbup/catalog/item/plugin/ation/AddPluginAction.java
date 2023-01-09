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
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.gui.DefaultControlPanel;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.framework.xbup.catalog.item.plugin.gui.CatalogEditNodePluginPanel;
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

/**
 * Add new plugin action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AddPluginAction extends AbstractAction {

    public static final String ACTION_ID = "addCatalogItemPluginAction";
    
    private XBApplication application;
    private XBACatalog catalog;

    private Component parentComponent;
    private XBCNode currentNode;
    private int currentCount;
    private String resultName;
    private byte[] resultData;

    public AddPluginAction() {
    }

    public void setup(XBApplication application) {
        this.application = application;
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
        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
        CatalogEditNodePluginPanel editPanel = new CatalogEditNodePluginPanel();
        editPanel.setApplication(application);
//        editPanel.setMenuManagement(menuManagement);
        editPanel.setCatalog(catalog);
        editPanel.setNode(currentNode);

        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowUtils.DialogWrapper dialog = frameModule.createDialog(editPanel, controlPanel);
//        WindowUtils.addHeaderPanel(dialog.getWindow(), editPanel.getClass(), editPanel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
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
// TODO                pluginsModel.addItem(plugin, file, editPanel.getRowEditorsCount(), editPanel.getPanelViewersCount(), editPanel.getPanelEditorsCount());
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
