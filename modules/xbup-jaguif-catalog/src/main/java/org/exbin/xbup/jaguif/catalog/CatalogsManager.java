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
package org.exbin.xbup.jaguif.catalog;

import org.jspecify.annotations.NullMarked;
import org.exbin.jaguif.App;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.xbup.jaguif.catalog.gui.CatalogsManagerPanel;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextRegistration;
import org.exbin.jaguif.context.api.ContextUpdateManagement;
import org.exbin.jaguif.toolbar.api.ToolBarDefinitionManagement;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Catalogs manager.
 */
@NullMarked
public class CatalogsManager {

    public static final String TOOLBAR_ID = "CatalogsManager.toolBar";

    protected final CatalogsManagerPanel catalogsManagerPanel;
    protected XBACatalog catalog;

    public CatalogsManager() {
        catalogsManagerPanel = new CatalogsManagerPanel();
        init();
    }

    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ActiveContextManagement contextManager = contextModule.createContextManager();
        CatalogsController catalogsController = new CatalogsController(catalogsManagerPanel);
        contextManager.changeActiveState(ContextEditItem.class, catalogsController);
        catalogsManagerPanel.addRowSelectionListener((arg0) -> {
            contextManager.updateActiveState(ContextEditItem.class, catalogsController, ContextEditItem.UpdateType.EDIT_STATE);
        });
        ContextUpdateManagement updateManagement = contextModule.createContextUpdateManagement(contextManager);
        ContextRegistration contextRegistrar = contextModule.createContextRegistrator("", updateManagement, contextManager);

        ToolBarDefinitionManagement toolBarDefinition = toolBarModule.createToolBarDefinition(toolBarManager, TOOLBAR_ID, XbupCatalogModule.MODULE_ID);
        DefaultEditItemActions editItemActions = new DefaultEditItemActions();
        editItemActions.registerToolBarContributions(toolBarDefinition);
        toolBarManager.buildIconToolBar(catalogsManagerPanel.getToolBar(), TOOLBAR_ID, contextRegistrar);
    }

    public CatalogsManagerPanel getCatalogsManagerPanel() {
        return catalogsManagerPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogsManagerPanel.setCatalog(catalog);
    }
}
