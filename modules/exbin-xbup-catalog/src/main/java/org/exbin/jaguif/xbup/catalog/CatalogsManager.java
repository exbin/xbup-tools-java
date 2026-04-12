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
package org.exbin.jaguif.xbup.catalog;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionContextRegistration;
import org.exbin.jaguif.action.api.ActionManagement;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.component.action.AddItemAction;
import org.exbin.jaguif.xbup.catalog.action.AddCatalogAction;
import org.exbin.jaguif.xbup.catalog.action.DeleteCatalogAction;
import org.exbin.jaguif.xbup.catalog.action.EditCatalogAction;
import org.exbin.jaguif.xbup.catalog.gui.CatalogsManagerPanel;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.DeleteItemAction;
import org.exbin.jaguif.component.action.EditItemAction;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.toolbar.api.ActionToolBarContribution;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
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
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;

    public CatalogsManager() {
        catalogsManagerPanel = new CatalogsManagerPanel();
        editActions = new DefaultEditItemActions();
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
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createAddItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return AddItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createEditItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return EditItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createDeleteItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return DeleteItemAction.ACTION_ID;
            }
        });
        ContextEditItem contextEditItem = new ContextEditItem() {
            @Override
            public void performAddItem() {
                AddCatalogAction action = new AddCatalogAction();
                action.init();
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
                action.init();
                action.setParentComponent(catalogsManagerPanel);
                action.setActiveItem(catalogsManagerPanel.getSelectedItem());
                action.actionPerformed(null);
                catalogsManagerPanel.reload();
            }

            @Override
            public void performDeleteItem() {
                DeleteCatalogAction action = new DeleteCatalogAction();
                action.init();
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
