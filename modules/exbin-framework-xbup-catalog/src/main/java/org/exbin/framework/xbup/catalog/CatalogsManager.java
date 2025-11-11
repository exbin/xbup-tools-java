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
package org.exbin.framework.xbup.catalog;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionContextRegistration;
import org.exbin.framework.action.api.ActionManagement;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.xbup.catalog.action.AddCatalogAction;
import org.exbin.framework.xbup.catalog.action.DeleteCatalogAction;
import org.exbin.framework.xbup.catalog.action.EditCatalogAction;
import org.exbin.framework.xbup.catalog.gui.CatalogsManagerPanel;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.ContextEditItem;
import org.exbin.framework.context.api.ActiveContextManagement;
import org.exbin.framework.context.api.ContextModuleApi;
import org.exbin.framework.toolbar.api.ToolBarManagement;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalogs manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogsManager {
    
    public static final String TOOLBAR_ID = "CatalogsManager.toolBar";

    private final CatalogsManagerPanel catalogsManagerPanel;
    private final DefaultEditItemActions actions;
    private XBACatalog catalog;

    public CatalogsManager() {
        catalogsManagerPanel = new CatalogsManagerPanel();
        actions = new DefaultEditItemActions();
        init();
    }

    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");
        
        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ActiveContextManagement contextManager = contextModule.createContextManager();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        ActionManagement actionManager = actionModule.createActionManager(contextManager);
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", actions.createAddItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", actions.createEditItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", actions.createDeleteItemAction());
        ContextEditItem contextEditItem = new ContextEditItem() {
            @Override
            public void performAddItem() {
                AddCatalogAction action = new AddCatalogAction();
                action.setup();
                action.setParentComponent(catalogsManagerPanel);
                action.actionPerformed(null);
                Optional<XBCRoot> resultRoot = action.getResultRoot();
                if (resultRoot.isPresent()) {
                    catalogsManagerPanel.reload();
                }
            }

            @Override
            public void performEditItem() {
                EditCatalogAction action = new EditCatalogAction();
                action.setup();
                action.setParentComponent(catalogsManagerPanel);
                action.setActiveItem(catalogsManagerPanel.getSelectedItem());
                action.actionPerformed(null);
                catalogsManagerPanel.reload();
            }

            @Override
            public void performDeleteItem() {
                DeleteCatalogAction action = new DeleteCatalogAction();
                action.setup();
                action.setParentComponent(catalogsManagerPanel);
                action.setActiveItem(catalogsManagerPanel.getSelectedItem());
                action.actionPerformed(null);
            }

            @Override
            public boolean canAddItem() {
                return true;
            }

            @Override
            public boolean canDeleteItem() {
                return catalogsManagerPanel.hasSelection();
            }

            @Override
            public boolean canEditItem() {
                return catalogsManagerPanel.hasSelection();
            }
        };
        contextManager.changeActiveState(ContextEditItem.class, contextEditItem);
        catalogsManagerPanel.addRowSelectionListener((arg0) -> {
            contextManager.changeActiveState(ContextEditItem.class, contextEditItem);
        });
        ActionContextRegistration actionContextRegistrar = actionModule.createActionContextRegistrar(actionManager);
        toolBarManager.buildIconToolBar(catalogsManagerPanel.getToolBar(), TOOLBAR_ID, actionContextRegistrar);
    }
    
    @Nonnull
    public CatalogsManagerPanel getCatalogsManagerPanel() {
        return catalogsManagerPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogsManagerPanel.setCatalog(catalog);
    }
}
