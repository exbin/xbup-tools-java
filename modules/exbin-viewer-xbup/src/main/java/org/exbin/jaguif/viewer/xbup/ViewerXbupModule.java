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
package org.exbin.jaguif.viewer.xbup;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import org.exbin.jaguif.App;
import org.exbin.jaguif.Module;
import org.exbin.jaguif.ModuleUtils;
import org.exbin.jaguif.action.api.ActionContextRegistration;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.client.api.ClientConnectionListener;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.xbup.catalog.action.CatalogsManagerAction;
import org.exbin.jaguif.viewer.xbup.action.DocumentPropertiesAction;
import org.exbin.jaguif.viewer.xbup.action.ExportItemAction;
import org.exbin.jaguif.viewer.xbup.action.ItemPropertiesAction;
import org.exbin.jaguif.file.api.FileModuleApi;
import org.exbin.jaguif.xbup.catalog.XBFileType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.jaguif.contribution.api.PositionSequenceContributionRule;
import org.exbin.jaguif.contribution.api.SeparationSequenceContributionRule;
import org.exbin.jaguif.contribution.api.SequenceContribution;
import org.exbin.jaguif.menu.api.MenuDefinitionManagement;
import org.exbin.jaguif.viewer.xbup.settings.ServiceConnectionSettingsComponent;
import org.exbin.jaguif.frame.api.FrameModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsManagement;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.jaguif.viewer.xbup.contribution.DocumentPropertiesContribution;
import org.exbin.jaguif.viewer.xbup.contribution.ExportItemContribution;
import org.exbin.jaguif.viewer.xbup.contribution.ItemPropertiesContribution;
import org.exbin.jaguif.xbup.catalog.contribution.CatalogsManagerContribution;

/**
 * XBUP viewer module.
 */
@ParametersAreNonnullByDefault
public class ViewerXbupModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(ViewerXbupModule.class);
    public static final String XBUP_FILE_TYPE = "XBEditor.XBFileType";

    public static final String XBUP_POPUP_MENU_ID = MODULE_ID + ".xbupPopupMenu";
    public static final String XBUP_VIEWER_GROUP_ID = "xbupViewer";
    public static final String XBUP_VIEWER_CONNECTION_GROUP_ID = "xbupViewerConnection";
    public static final String DOC_STATUS_BAR_ID = "docStatusBar";
    public static final String SAMPLE_FILE_SUBMENU_ID = MODULE_ID + ".sampleFileSubMenu";

    private ResourceBundle resourceBundle;

    private StatusPanelHandler statusPanelHandler;

    private ServiceConnectionSettingsComponent catalogConnectionOptionsPage;

    private boolean devMode;

    public ViewerXbupModule() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ViewerXbupModule.class);
        }

        return resourceBundle;
    }

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        fileModule.addFileType(new XBFileType());
    }

    private void ensureSetup() {
        if (resourceBundle == null) {
            getResourceBundle();
        }
    }

    @Nonnull
    private StatusPanelHandler getStatusPanelHandler() {
        if (statusPanelHandler == null) {
            ensureSetup();
            statusPanelHandler = new StatusPanelHandler();
            statusPanelHandler.init(resourceBundle);
        }

        return statusPanelHandler;
    }

    @Nonnull
    private CatalogsManagerAction createCatalogBrowserAction() {
        ensureSetup();
        CatalogsManagerAction catalogBrowserAction = new CatalogsManagerAction();
        catalogBrowserAction.init();
        return catalogBrowserAction;
    }

    @Nonnull
    private ItemPropertiesAction createItemPropertiesAction() {
        ensureSetup();
        ItemPropertiesAction itemPropertiesAction = new ItemPropertiesAction();
        itemPropertiesAction.init();
        itemPropertiesAction.setDevMode(devMode);
        return itemPropertiesAction;
    }

    @Nonnull
    private DocumentPropertiesAction createDocumentPropertiesAction() {
        ensureSetup();
        DocumentPropertiesAction documentPropertiesAction = new DocumentPropertiesAction();
        documentPropertiesAction.init();
        return documentPropertiesAction;
    }

    @Nonnull
    public ExportItemAction createExportItemAction() {
        ensureSetup();
        ExportItemAction exportItemAction = new ExportItemAction();
        exportItemAction.init(resourceBundle);
        return exportItemAction;
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
        MenuDefinitionManagement mgmt = menuModule.getMainMenuManager(MODULE_ID).getSubMenu(MenuModuleApi.TOOLS_SUBMENU_ID);
        SequenceContribution contribution = new CatalogsManagerContribution();
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void registerSettings() {
        ensureSetup();
        OptionsSettingsModuleApi settingsModule = App.getModule(OptionsSettingsModuleApi.class);
        OptionsSettingsManagement settingsManagement = settingsModule.getMainSettingsManager();

        /* OptionsGroup xbupViewerGroup = settingsModule.createOptionsGroup(XBUP_VIEWER_GROUP_ID, resourceBundle);
        settingsManagement.registerGroup(xbupViewerGroup);
        settingsManagement.registerGroupRule(xbupViewerGroup, new ParentOptionsGroupRule("editor"));

        OptionsGroup xbupViewerConnectionGroup = settingsModule.createOptionsGroup(XBUP_VIEWER_CONNECTION_GROUP_ID, resourceBundle);
        settingsManagement.registerGroup(xbupViewerConnectionGroup);
        settingsManagement.registerGroupRule(xbupViewerConnectionGroup, new ParentOptionsGroupRule(xbupViewerGroup));
        catalogConnectionOptionsPage = new ServiceConnectionSettingsComponent();
        settingsManagement.registerPage(catalogConnectionOptionsPage);
        settingsManagement.registerPageRule(catalogConnectionOptionsPage, new GroupOptionsPageRule(xbupViewerConnectionGroup)); */
    }

    public void registerPropertiesMenuAction() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement mgmt = menuModule.getMainMenuManager(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution contribution = new DocumentPropertiesContribution();
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }

    public void registerItemPopupMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        menuModule.registerMenu(XBUP_POPUP_MENU_ID, MODULE_ID);
        MenuDefinitionManagement mgmt = menuModule.getMenuManager(XBUP_POPUP_MENU_ID, MODULE_ID);
        SequenceContribution contribution;
        // = mgmt.registerMenuItem(createAddItemAction());
        //mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
//        contribution = mgmt.registerMenuItem(getEditItemAction());
//        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));

        menuModule.registerClipboardMenuItems(XBUP_POPUP_MENU_ID, null, MODULE_ID, SeparationSequenceContributionRule.SeparationMode.AROUND);

//        contribution = mgmt.registerMenuItem(createImportItemAction());
//        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
        contribution = new ExportItemContribution();
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));

        contribution = new ItemPropertiesContribution();
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }

    @Nonnull
    public JPopupMenu createItemPopupMenu() {
        JPopupMenu itemPopupMenu = new JPopupMenu();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        ActionContextRegistration actionContextRegistrar = actionModule.createActionContextRegistrar(frameModule.getFrameHandler().getActionManager());
        menuModule.buildMenu(itemPopupMenu, XBUP_POPUP_MENU_ID, actionContextRegistrar);
        return itemPopupMenu;
    }

    @Nonnull
    public ClientConnectionListener getClientConnectionListener() {
        return getStatusPanelHandler().getClientConnectionListener();
    }

    public void setCatalog(XBACatalog catalog) {
        // TODO Separate menu handler
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ActiveContextManagement contextManager = frameModule.getFrameHandler().getContextManager();
        contextManager.changeActiveState(XBACatalog.class, catalog);
    }
}
