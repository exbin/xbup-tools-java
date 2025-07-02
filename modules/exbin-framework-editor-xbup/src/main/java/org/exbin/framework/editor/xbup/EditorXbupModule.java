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
package org.exbin.framework.editor.xbup;

import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.client.api.ClientConnectionListener;
import org.exbin.framework.editor.xbup.action.AddItemAction;
import org.exbin.framework.xbup.catalog.action.CatalogsManagerAction;
import org.exbin.framework.editor.xbup.action.DocumentPropertiesAction;
import org.exbin.framework.editor.xbup.action.EditItemAction;
import org.exbin.framework.editor.xbup.action.ExportItemAction;
import org.exbin.framework.editor.xbup.action.ImportItemAction;
import org.exbin.framework.editor.xbup.action.ItemPropertiesAction;
import org.exbin.framework.editor.xbup.viewer.XbupEditorProvider;
import org.exbin.framework.editor.xbup.viewer.XbupSingleEditorProvider;
import org.exbin.framework.editor.xbup.viewer.XbupMultiEditorProvider;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.xbup.catalog.XBFileType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.action.api.ActionContextService;
import org.exbin.framework.action.api.ComponentActivationListener;
import org.exbin.framework.menu.api.GroupMenuContributionRule;
import org.exbin.framework.toolbar.api.GroupToolBarContributionRule;
import org.exbin.framework.menu.api.MenuContribution;
import org.exbin.framework.menu.api.MenuManagement;
import org.exbin.framework.menu.api.PositionMenuContributionRule;
import org.exbin.framework.toolbar.api.PositionToolBarContributionRule;
import org.exbin.framework.menu.api.SeparationMenuContributionRule;
import org.exbin.framework.toolbar.api.SeparationToolBarContributionRule;
import org.exbin.framework.toolbar.api.ToolBarContribution;
import org.exbin.framework.toolbar.api.ToolBarManagement;
import org.exbin.framework.editor.api.EditorProviderVariant;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.menu.api.MenuModuleApi;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.framework.utils.ObjectUtils;

/**
 * XBUP editor module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditorXbupModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(EditorXbupModule.class);
    public static final String XB_FILE_TYPE = "XBEditor.XBFileType";

    public static final String XBUP_POPUP_MENU_ID = MODULE_ID + ".xbupPopupMenu";
    private static final String EDIT_ITEM_MENU_GROUP_ID = MODULE_ID + ".editItemMenuGroup";
    private static final String EDIT_ITEM_TOOL_BAR_GROUP_ID = MODULE_ID + ".editItemToolBarGroup";
    public static final String DOC_STATUS_BAR_ID = "docStatusBar";

    private XbupEditorProvider editorProvider;
    private ResourceBundle resourceBundle;
    private XBACatalog catalog;

    private StatusPanelHandler statusPanelHandler;

    private boolean devMode;

    public EditorXbupModule() {
    }

    public void initEditorProvider(EditorProviderVariant variant) {
        switch (variant) {
            case SINGLE: {
                editorProvider = createSingleEditorProvider();
                break;
            }
            case MULTI: {
                editorProvider = createMultiEditorProvider();
                break;
            }
            default:
                throw ObjectUtils.getInvalidTypeException(variant);
        }
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(EditorXbupModule.class);
        }

        return resourceBundle;
    }

    @Nonnull
    private XbupEditorProvider createSingleEditorProvider() {
        if (editorProvider == null) {
            editorProvider = new XbupSingleEditorProvider();
            ((XbupSingleEditorProvider) editorProvider).setDevMode(devMode);

//            final DocumentViewerProvider docPanel = (DocumentViewerProvider) editorProvider;
//
//            docPanel.getComponentPanel().setPopupMenu(createPopupMenu());
//            docPanel.addUpdateListener((ActionEvent e) -> {
//                if (docEditingHandler != null) {
//                    docEditingHandler.setAddEnabled(docPanel.isAddEnabled());
//                    docEditingHandler.setEditEnabled(docPanel.isEditEnabled());
//                    propertiesHandler.setEditEnabled(docPanel.isEditEnabled());
//                }
//            });
        }

        return editorProvider;
    }

    @Nonnull
    private XbupEditorProvider createMultiEditorProvider() {
        if (editorProvider == null) {
            editorProvider = new XbupMultiEditorProvider();
            ((XbupMultiEditorProvider) editorProvider).setDevMode(devMode);
        }

        return editorProvider;
    }

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        fileModule.addFileType(new XBFileType());
    }

    @Nonnull
    public EditorProvider getEditorProvider() {
        return Objects.requireNonNull(editorProvider, "Editor provider was not yet initialized");
    }

    public void setEditorProvider(XbupEditorProvider editorProvider) {
        this.editorProvider = editorProvider;
    }

    private void ensureSetup() {
        if (editorProvider == null) {
            getEditorProvider();
        }

        if (resourceBundle == null) {
            getResourceBundle();
        }
    }

    @Nonnull
    private StatusPanelHandler getStatusPanelHandler() {
        if (statusPanelHandler == null) {
            ensureSetup();
            statusPanelHandler = new StatusPanelHandler();
            statusPanelHandler.setup(resourceBundle);
        }

        return statusPanelHandler;
    }

    @Nonnull
    private CatalogsManagerAction createCatalogBrowserAction() {
        ensureSetup();
        CatalogsManagerAction catalogBrowserAction = new CatalogsManagerAction();
        catalogBrowserAction.setup();
        return catalogBrowserAction;
    }

    @Nonnull
    private ItemPropertiesAction createItemPropertiesAction() {
        ensureSetup();
        ItemPropertiesAction itemPropertiesAction = new ItemPropertiesAction();
        itemPropertiesAction.setup();
        itemPropertiesAction.setDevMode(devMode);
        return itemPropertiesAction;
    }

    @Nonnull
    private DocumentPropertiesAction createDocumentPropertiesAction() {
        ensureSetup();
        DocumentPropertiesAction documentPropertiesAction = new DocumentPropertiesAction();
        documentPropertiesAction.setup();
        return documentPropertiesAction;
    }

    @Nonnull
    public ImportItemAction createImportItemAction() {
        ensureSetup();
        ImportItemAction importItemAction = new ImportItemAction();
        importItemAction.setup(resourceBundle);
        return importItemAction;
    }

    @Nonnull
    public ExportItemAction createExportItemAction() {
        ensureSetup();
        ExportItemAction exportItemAction = new ExportItemAction();
        exportItemAction.setup(resourceBundle);
        return exportItemAction;
    }

    @Nonnull
    public AddItemAction createAddItemAction() {
        AddItemAction addItemAction = new AddItemAction();
        addItemAction.setup();
        return addItemAction;
    }

    @Nonnull
    public EditItemAction getEditItemAction() {
        EditItemAction editItemAction = new EditItemAction();
        editItemAction.setup();
        return editItemAction;
    }

    public void registerDocEditingMenuActions() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement mgmt = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.EDIT_SUBMENU_ID);
        MenuContribution contribution = mgmt.registerMenuGroup(EDIT_ITEM_MENU_GROUP_ID);
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.BOTTOM));
        mgmt.registerMenuRule(contribution, new SeparationMenuContributionRule(SeparationMenuContributionRule.SeparationMode.AROUND));
        contribution = mgmt.registerMenuItem(createAddItemAction());
        mgmt.registerMenuRule(contribution, new GroupMenuContributionRule(EDIT_ITEM_MENU_GROUP_ID));
        contribution = mgmt.registerMenuItem(getEditItemAction());
        mgmt.registerMenuRule(contribution, new GroupMenuContributionRule(EDIT_ITEM_MENU_GROUP_ID));
    }

    public void registerDocEditingToolBarActions() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement mgmt = toolBarModule.getMainToolBarManagement(MODULE_ID);
        ToolBarContribution contribution = mgmt.registerToolBarGroup(EDIT_ITEM_TOOL_BAR_GROUP_ID);
        mgmt.registerToolBarRule(contribution, new PositionToolBarContributionRule(PositionToolBarContributionRule.PositionMode.BOTTOM));
        mgmt.registerToolBarRule(contribution, new SeparationToolBarContributionRule(SeparationToolBarContributionRule.SeparationMode.AROUND));
        contribution = mgmt.registerToolBarItem(createAddItemAction());
        mgmt.registerToolBarRule(contribution, new GroupToolBarContributionRule(EDIT_ITEM_TOOL_BAR_GROUP_ID));
        contribution = mgmt.registerToolBarItem(getEditItemAction());
        mgmt.registerToolBarRule(contribution, new GroupToolBarContributionRule(EDIT_ITEM_TOOL_BAR_GROUP_ID));
    }

    public void registerStatusBar() {
        getStatusPanelHandler();
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, DOC_STATUS_BAR_ID, statusPanelHandler.getDocStatusPanel());
        frameModule.switchStatusBar(DOC_STATUS_BAR_ID);
        // ((XBDocumentPanel) getEditorProvider()).registerTextStatus(docStatusPanel);
    }

    public void registerCatalogBrowserMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement mgmt = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.TOOLS_SUBMENU_ID);
        MenuContribution contribution = mgmt.registerMenuItem(createCatalogBrowserAction());
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.TOP));
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void registerPropertiesMenuAction() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement mgmt = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        MenuContribution contribution = mgmt.registerMenuItem(createDocumentPropertiesAction());
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.BOTTOM));
    }

    public void registerItemPopupMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        menuModule.registerMenu(XBUP_POPUP_MENU_ID, MODULE_ID);
        MenuManagement mgmt = menuModule.getMenuManagement(XBUP_POPUP_MENU_ID, MODULE_ID);
        MenuContribution contribution = mgmt.registerMenuItem(createAddItemAction());
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.TOP));
        contribution = mgmt.registerMenuItem(getEditItemAction());
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.TOP));

        menuModule.registerClipboardMenuItems(XBUP_POPUP_MENU_ID, null, MODULE_ID, SeparationMenuContributionRule.SeparationMode.AROUND);

        contribution = mgmt.registerMenuItem(createImportItemAction());
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.BOTTOM));
        contribution = mgmt.registerMenuItem(createExportItemAction());
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.BOTTOM));

        contribution = mgmt.registerMenuItem(createItemPropertiesAction());
        mgmt.registerMenuRule(contribution, new PositionMenuContributionRule(PositionMenuContributionRule.PositionMode.BOTTOM));
    }

    @Nonnull
    public JPopupMenu createItemPopupMenu() {
        JPopupMenu itemPopupMenu = new JPopupMenu();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ActionContextService actionContextService = frameModule.getFrameHandler().getActionContextService();
        menuModule.buildMenu(itemPopupMenu, XBUP_POPUP_MENU_ID, actionContextService);
        return itemPopupMenu;
    }

    @Nonnull
    public ClientConnectionListener getClientConnectionListener() {
        return getStatusPanelHandler().getClientConnectionListener();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        editorProvider.setCatalog(catalog);

        // TODO Separate menu handler
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ComponentActivationListener componentActivationListener = frameModule.getFrameHandler().getComponentActivationListener();
        componentActivationListener.updated(XBACatalog.class, catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        editorProvider.setPluginRepository(pluginRepository);
    }
}
