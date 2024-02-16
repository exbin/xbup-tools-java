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
import org.exbin.framework.action.api.ActionConsts;
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
import org.exbin.framework.action.api.MenuGroup;
import org.exbin.framework.action.api.MenuPosition;
import org.exbin.framework.action.api.PositionMode;
import org.exbin.framework.action.api.SeparationMode;
import org.exbin.framework.action.api.ToolBarGroup;
import org.exbin.framework.action.api.ToolBarPosition;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.xbup.catalog.XBFileType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.ComponentActivationService;
import org.exbin.framework.preferences.api.Preferences;
import org.exbin.framework.editor.api.EditorModuleApi;
import org.exbin.framework.editor.api.EditorProviderVariant;
import org.exbin.framework.editor.api.MultiEditorProvider;
import org.exbin.framework.editor.xbup.action.SampleFilesActions;
import org.exbin.framework.editor.xbup.options.gui.ServiceConnectionPanel;
import org.exbin.framework.editor.xbup.options.impl.ServiceConnectionOptionsImpl;
import org.exbin.framework.editor.xbup.preferences.ServiceConnectionPreferences;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.options.api.DefaultOptionsPage;
import org.exbin.framework.options.api.OptionsComponent;
import org.exbin.framework.language.api.LanguageModuleApi;
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
    public static final String SAMPLE_FILE_SUBMENU_ID = MODULE_ID + ".sampleFileSubMenu";

    private XbupEditorProvider editorProvider;
    private ResourceBundle resourceBundle;
    private XBACatalog catalog;

    private StatusPanelHandler statusPanelHandler;
    private SampleFilesActions sampleFilesActions;

    private DefaultOptionsPage<ServiceConnectionOptionsImpl> catalogConnectionOptionsPage;

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
            ((MultiEditorProvider) editorProvider).addActiveFileChangeListener(e -> {
                // TODO updateActionStatus();
            });
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
            statusPanelHandler.setup(editorProvider, resourceBundle);
        }

        return statusPanelHandler;
    }

    @Nonnull
    private SampleFilesActions getSampleFilesActions() {
        if (sampleFilesActions == null) {
            ensureSetup();
            sampleFilesActions = new SampleFilesActions();
            sampleFilesActions.setup(editorProvider, resourceBundle);
        }

        return sampleFilesActions;
    }

    @Nonnull
    private CatalogsManagerAction createCatalogBrowserAction() {
        ensureSetup();
        CatalogsManagerAction catalogBrowserAction = new CatalogsManagerAction();
        catalogBrowserAction.setup();
        catalogBrowserAction.setCatalog(catalog);
        return catalogBrowserAction;
    }

    @Nonnull
    private ItemPropertiesAction createItemPropertiesAction() {
        ensureSetup();
        ItemPropertiesAction itemPropertiesAction = new ItemPropertiesAction();
        itemPropertiesAction.setup(editorProvider);
        itemPropertiesAction.setDevMode(devMode);
        return itemPropertiesAction;
    }

    @Nonnull
    private DocumentPropertiesAction createDocumentPropertiesAction() {
        ensureSetup();
        DocumentPropertiesAction documentPropertiesAction = new DocumentPropertiesAction();
        documentPropertiesAction.setup(editorProvider);
        return documentPropertiesAction;
    }

    @Nonnull
    public ImportItemAction createImportItemAction() {
        ensureSetup();
        ImportItemAction importItemAction = new ImportItemAction();
        importItemAction.setup(editorProvider, resourceBundle);
        return importItemAction;
    }

    @Nonnull
    public ExportItemAction createExportItemAction() {
        ensureSetup();
        ExportItemAction exportItemAction = new ExportItemAction();
        exportItemAction.setup(editorProvider, resourceBundle);
        return exportItemAction;
    }

    @Nonnull
    public AddItemAction createAddItemAction() {
        AddItemAction addItemAction = new AddItemAction();
        addItemAction.setup(editorProvider);
        return addItemAction;
    }

    @Nonnull
    public EditItemAction getEditItemAction() {
        EditItemAction editItemAction = new EditItemAction();
        editItemAction.setup(editorProvider);
        return editItemAction;
    }

    public void registerDocEditingMenuActions() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.registerMenuGroup(ActionConsts.EDIT_MENU_ID, new MenuGroup(EDIT_ITEM_MENU_GROUP_ID, new MenuPosition(PositionMode.BOTTOM), SeparationMode.AROUND));
        actionModule.registerMenuItem(ActionConsts.EDIT_MENU_ID, MODULE_ID, createAddItemAction(), new MenuPosition(EDIT_ITEM_MENU_GROUP_ID));
        actionModule.registerMenuItem(ActionConsts.EDIT_MENU_ID, MODULE_ID, getEditItemAction(), new MenuPosition(EDIT_ITEM_MENU_GROUP_ID));
    }

    public void registerDocEditingToolBarActions() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.registerToolBarGroup(ActionConsts.MAIN_TOOL_BAR_ID, new ToolBarGroup(EDIT_ITEM_TOOL_BAR_GROUP_ID, new ToolBarPosition(PositionMode.BOTTOM), SeparationMode.AROUND));
        actionModule.registerToolBarItem(ActionConsts.MAIN_TOOL_BAR_ID, MODULE_ID, createAddItemAction(), new ToolBarPosition(EDIT_ITEM_TOOL_BAR_GROUP_ID));
        actionModule.registerToolBarItem(ActionConsts.MAIN_TOOL_BAR_ID, MODULE_ID, getEditItemAction(), new ToolBarPosition(EDIT_ITEM_TOOL_BAR_GROUP_ID));
    }

    public void registerStatusBar() {
        getStatusPanelHandler();
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, DOC_STATUS_BAR_ID, statusPanelHandler.getDocStatusPanel());
        frameModule.switchStatusBar(DOC_STATUS_BAR_ID);
        // ((XBDocumentPanel) getEditorProvider()).registerTextStatus(docStatusPanel);
    }

    public void registerSampleFilesSubMenuActions() {
        getSampleFilesActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.registerMenu(SAMPLE_FILE_SUBMENU_ID, MODULE_ID);
        actionModule.registerMenuItem(ActionConsts.FILE_MENU_ID, MODULE_ID, SAMPLE_FILE_SUBMENU_ID, "Open Sample File", new MenuPosition(PositionMode.BOTTOM));
        actionModule.registerMenuItem(SAMPLE_FILE_SUBMENU_ID, MODULE_ID, sampleFilesActions.createSampleHtmlFileAction(), new MenuPosition(PositionMode.TOP));
        actionModule.registerMenuItem(SAMPLE_FILE_SUBMENU_ID, MODULE_ID, sampleFilesActions.createSamplePictureFileAction(), new MenuPosition(PositionMode.TOP));
        actionModule.registerMenuItem(SAMPLE_FILE_SUBMENU_ID, MODULE_ID, sampleFilesActions.createSampleTypesFileAction(), new MenuPosition(PositionMode.TOP));
    }

    public void registerCatalogBrowserMenu() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.registerMenuItem(ActionConsts.TOOLS_MENU_ID, MODULE_ID, createCatalogBrowserAction(), new MenuPosition(PositionMode.TOP));
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void registerOptionsPanels() {
        OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);

        catalogConnectionOptionsPage = new DefaultOptionsPage<ServiceConnectionOptionsImpl>() {

            private ServiceConnectionPanel panel;

            @Override
            public OptionsComponent<ServiceConnectionOptionsImpl> createPanel() {
                if (panel == null) {
                    panel = new ServiceConnectionPanel();
                }
                return panel;
            }

            @Nonnull
            @Override
            public ResourceBundle getResourceBundle() {
                return App.getModule(LanguageModuleApi.class).getBundle(ServiceConnectionPanel.class);
            }

            @Override
            public ServiceConnectionOptionsImpl createOptions() {
                return new ServiceConnectionOptionsImpl();
            }

            @Override
            public void loadFromPreferences(Preferences preferences, ServiceConnectionOptionsImpl options) {
                options.loadFromPreferences(new ServiceConnectionPreferences(preferences));
            }

            @Override
            public void saveToPreferences(Preferences preferences, ServiceConnectionOptionsImpl options) {
                options.saveToPreferences(new ServiceConnectionPreferences(preferences));
            }

            @Override
            public void applyPreferencesChanges(ServiceConnectionOptionsImpl options) {
                // options.getCatalogUpdateUrl();
            }
        };

        optionsModule.addOptionsPage(catalogConnectionOptionsPage);
    }

    public void registerPropertiesMenuAction() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.registerMenuItem(ActionConsts.FILE_MENU_ID, MODULE_ID, createDocumentPropertiesAction(), new MenuPosition(PositionMode.BOTTOM));
    }

    public void registerItemPopupMenu() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.registerMenu(XBUP_POPUP_MENU_ID, MODULE_ID);
        actionModule.registerMenuItem(XBUP_POPUP_MENU_ID, MODULE_ID, createAddItemAction(), new MenuPosition(PositionMode.TOP));
        actionModule.registerMenuItem(XBUP_POPUP_MENU_ID, MODULE_ID, getEditItemAction(), new MenuPosition(PositionMode.TOP));

        actionModule.registerClipboardMenuItems(XBUP_POPUP_MENU_ID, MODULE_ID, SeparationMode.AROUND);

        actionModule.registerMenuItem(XBUP_POPUP_MENU_ID, MODULE_ID, createImportItemAction(), new MenuPosition(PositionMode.BOTTOM));
        actionModule.registerMenuItem(XBUP_POPUP_MENU_ID, MODULE_ID, createExportItemAction(), new MenuPosition(PositionMode.BOTTOM));

        actionModule.registerMenuItem(XBUP_POPUP_MENU_ID, MODULE_ID, createItemPropertiesAction(), new MenuPosition(PositionMode.BOTTOM));
    }

    @Nonnull
    public JPopupMenu createItemPopupMenu() {
        JPopupMenu itemPopupMenu = new JPopupMenu();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ComponentActivationService componentActivationService = frameModule.getFrameHandler().getComponentActivationService();
        actionModule.buildMenu(itemPopupMenu, XBUP_POPUP_MENU_ID, componentActivationService);
        return itemPopupMenu;
    }

    @Nonnull
    public ClientConnectionListener getClientConnectionListener() {
        return getStatusPanelHandler().getClientConnectionListener();
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        editorProvider.setCatalog(catalog);
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        editorProvider.setPluginRepository(pluginRepository);
    }
}
