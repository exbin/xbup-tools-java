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
package org.exbin.xbup.jaguif.catalog.item.revision;

import org.jspecify.annotations.NullMarked;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.component.action.AddItemAction;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.DeleteItemAction;
import org.exbin.jaguif.component.action.EditItemAction;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ContextStateManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextMonitoringManagement;
import org.exbin.jaguif.context.api.ContextMonitoringRegistration;
import org.exbin.xbup.jaguif.catalog.model.CatalogDefsTableModel;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.toolbar.api.ActionToolBarContribution;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.jaguif.catalog.item.revision.gui.CatalogItemEditRevsPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Catalog revisions editor.
 */
@NullMarked
public class CatalogRevisionsEditor {

    public static final String TOOLBAR_ID = "CatalogRevisionsEditor.toolBar";

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogRevisionsEditor.class);

    protected final CatalogItemEditRevsPanel catalogEditorPanel;
    protected final DefaultEditItemActions editActions;
    protected XBACatalog catalog;
    protected JPopupMenu popupMenu;

    protected ContextStateManagement itemContextStateManager;

    public CatalogRevisionsEditor() {
        catalogEditorPanel = new CatalogItemEditRevsPanel();
        editActions = new DefaultEditItemActions(EditItemMode.DIALOG);
        init();
    }
    
    private void init() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        itemContextStateManager = contextModule.createStateManager();
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Override
            public Action createAction() {
                return editActions.createAddItemAction();
            }

            @Override
            public String getContributionId() {
                return AddItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Override
            public Action createAction() {
                return editActions.createEditItemAction();
            }

            @Override
            public String getContributionId() {
                return EditItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Override
            public Action createAction() {
                return editActions.createDeleteItemAction();
            }

            @Override
            public String getContributionId() {
                return DeleteItemAction.ACTION_ID;
            }
        });
        CatalogRevisionsEditorController itemController = new CatalogRevisionsEditorController(catalogEditorPanel);
        itemContextStateManager.changeActiveState(ContextEditItem.class, itemController);
        itemContextStateManager.changeActiveState(DialogParentComponent.class, (DialogParentComponent) () -> catalogEditorPanel);
        catalogEditorPanel.addSelectionListener((lse) -> {
            itemContextStateManager.changeActiveState(ContextEditItem.class, itemController);        
        });
        ContextMonitoringManagement monitoringManagement = contextModule.createMonitoringManager(itemContextStateManager);
        ContextMonitoringRegistration monitoringRegistrar = contextModule.createMonitoringRegistrator(monitoringManagement, itemContextStateManager);
        toolBarManager.buildIconToolBar(catalogEditorPanel.getToolBar(), TOOLBAR_ID, monitoringRegistrar);

        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        popupMenu = new JPopupMenu();
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        JMenuItem addRevisionMenuItem = menuModule.actionToMenuItem(editActions.createAddItemAction());
        addRevisionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addRevisionMenuItem.text"));
        popupMenu.add(addRevisionMenuItem);
        JMenuItem editRevisionMenuItem = menuModule.actionToMenuItem(editActions.createEditItemAction());
        editRevisionMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editRevisionMenuItem.text"));
        popupMenu.add(editRevisionMenuItem);

        catalogEditorPanel.setPanelPopup(popupMenu);

        // TODO catalogEditorPanel.getSideToolBar(editActions);

        itemController.registerMonitoring(monitoringRegistrar);
        monitoringRegistrar.finish();
    }

    public void setCatalogItem(XBCItem item) {
        catalogEditorPanel.setCatalogItem(item);
    }

    public void setDefsModel(CatalogDefsTableModel defsTableModel) {
        catalogEditorPanel.setDefsModel(defsTableModel);
    }

    public CatalogItemEditRevsPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);

        itemContextStateManager.changeActiveState(XBACatalog.class, catalog);
    }

    public void persist() {
        catalogEditorPanel.persist();
    }
}
