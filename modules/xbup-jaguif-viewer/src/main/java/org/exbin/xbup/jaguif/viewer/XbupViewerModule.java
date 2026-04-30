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
package org.exbin.xbup.jaguif.viewer;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.exbin.jaguif.App;
import org.exbin.jaguif.Module;
import org.exbin.jaguif.ModuleUtils;
import org.exbin.xbup.jaguif.client.api.ClientConnectionListener;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextRegistration;
import org.exbin.xbup.jaguif.catalog.action.CatalogsManagerAction;
import org.exbin.xbup.jaguif.viewer.action.DocumentPropertiesAction;
import org.exbin.xbup.jaguif.viewer.action.ExportItemAction;
import org.exbin.xbup.jaguif.viewer.action.ItemPropertiesAction;
import org.exbin.jaguif.file.api.FileModuleApi;
import org.exbin.xbup.jaguif.catalog.XBFileType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.jaguif.contribution.api.PositionSequenceContributionRule;
import org.exbin.jaguif.contribution.api.SeparationSequenceContributionRule;
import org.exbin.jaguif.contribution.api.SequenceContribution;
import org.exbin.jaguif.menu.api.MenuDefinitionManagement;
import org.exbin.xbup.jaguif.viewer.settings.ServiceConnectionSettingsComponent;
import org.exbin.jaguif.frame.api.FrameModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsManagement;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.xbup.jaguif.viewer.contribution.DocumentPropertiesContribution;
import org.exbin.xbup.jaguif.viewer.contribution.ExportItemContribution;
import org.exbin.xbup.jaguif.viewer.contribution.ItemPropertiesContribution;
import org.exbin.xbup.jaguif.catalog.contribution.CatalogsManagerContribution;
import org.exbin.xbup.jaguif.document.XbupDocumentModule;
import org.exbin.xbup.jaguif.document.XbupDocumentViewProvider;

/**
 * XBUP viewer module.
 */
@ParametersAreNonnullByDefault
public class XbupViewerModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(XbupViewerModule.class);
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

    public XbupViewerModule() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupViewerModule.class);
        }

        return resourceBundle;
    }

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        fileModule.addFileType(new XBFileType());
    }

    @Nonnull
    private StatusPanelHandler getStatusPanelHandler() {
        if (statusPanelHandler == null) {
            statusPanelHandler = new StatusPanelHandler();
            statusPanelHandler.init(getResourceBundle());
        }

        return statusPanelHandler;
    }

    @Nonnull
    private CatalogsManagerAction createCatalogBrowserAction() {
        CatalogsManagerAction catalogBrowserAction = new CatalogsManagerAction();
        catalogBrowserAction.init();
        return catalogBrowserAction;
    }

    @Nonnull
    private ItemPropertiesAction createItemPropertiesAction() {
        ItemPropertiesAction itemPropertiesAction = new ItemPropertiesAction();
        itemPropertiesAction.init();
        itemPropertiesAction.setDevMode(devMode);
        return itemPropertiesAction;
    }

    @Nonnull
    private DocumentPropertiesAction createDocumentPropertiesAction() {
        DocumentPropertiesAction documentPropertiesAction = new DocumentPropertiesAction();
        documentPropertiesAction.init();
        return documentPropertiesAction;
    }

    @Nonnull
    public ExportItemAction createExportItemAction() {
        ExportItemAction exportItemAction = new ExportItemAction();
        exportItemAction.init(getResourceBundle());
        return exportItemAction;
    }
    
    public void registerXbupViewer() {
        XbupDocumentModule documentModule = App.getModule(XbupDocumentModule.class);
        documentModule.setDocumentViewProvider(new XbupDocumentViewProvider() {
            @Nonnull
            @Override
            public JComponent getComponent() {
                XbupViewer viewer = new XbupViewer();
                return viewer.getPanel();
            }
        });
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
        MenuDefinitionManagement mgmt = menuModule.getMainMenuDefinition(MODULE_ID).getSubMenu(MenuModuleApi.TOOLS_SUBMENU_ID);
        SequenceContribution contribution = new CatalogsManagerContribution();
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void registerSettings() {
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
        MenuDefinitionManagement mgmt = menuModule.getMainMenuDefinition(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution contribution = new DocumentPropertiesContribution();
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }

    public void registerItemPopupMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        menuModule.registerMenu(XBUP_POPUP_MENU_ID, MODULE_ID);
        MenuDefinitionManagement mgmt = menuModule.getMainMenuDefinition(XBUP_POPUP_MENU_ID, MODULE_ID);
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
        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ContextRegistration contextRegistrar = contextModule.createContextRegistrator(frameModule.getFrameController().getContextManager());
        menuModule.buildMenu(itemPopupMenu, XBUP_POPUP_MENU_ID, contextRegistrar);
        return itemPopupMenu;
    }

    @Nonnull
    public ClientConnectionListener getClientConnectionListener() {
        return getStatusPanelHandler().getClientConnectionListener();
    }

    public void setCatalog(XBACatalog catalog) {
        // TODO Separate menu handler
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ActiveContextManagement contextManager = frameModule.getFrameController().getContextManager();
        contextManager.changeActiveState(XBACatalog.class, catalog);
    }
}
