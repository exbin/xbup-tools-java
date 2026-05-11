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
package org.exbin.xbup.tool.service.launcher;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.jaguif.App;
import org.exbin.jaguif.LauncherModule;
import org.exbin.jaguif.ModuleUtils;
import org.exbin.jaguif.license.api.LicenseModuleApi;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.addon.update.api.AddonUpdateModuleApi;
import org.exbin.bined.jaguif.component.BinedComponentModule;
import org.exbin.bined.jaguif.inspector.BinedInspectorModule;
import org.exbin.xbup.jaguif.client.api.ClientModuleApi;
import org.exbin.jaguif.docking.api.DockingModuleApi;
import org.exbin.jaguif.docking.api.DocumentDocking;
import org.exbin.jaguif.document.api.DocumentModuleApi;
import org.exbin.jaguif.document.recent.DocumentRecentModule;
import org.exbin.jaguif.document.text.DocumentTextModule;
import org.exbin.xbup.jaguif.editor.XbupEditorModule;
import org.exbin.xbup.jaguif.viewer.XbupViewerModule;
import org.exbin.jaguif.file.api.FileModuleApi;
import org.exbin.jaguif.frame.api.FrameController;
import org.exbin.jaguif.frame.api.FrameModuleApi;
import org.exbin.jaguif.help.api.HelpModuleApi;
import org.exbin.jaguif.help.online.api.HelpOnlineModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.menu.popup.api.MenuPopupModuleApi;
import org.exbin.jaguif.operation.undo.api.OperationUndoModuleApi;
import org.exbin.jaguif.options.api.OptionsStorage;
import org.exbin.jaguif.options.api.OptionsModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.jaguif.ui.api.UiModuleApi;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.basic.XBHead;

/**
 * XBUP editor launcher module.
 */
@ParametersAreNonnullByDefault
public class ServiceLauncherModule implements LauncherModule {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(ServiceLauncherModule.class);

    private static final String XBUP_PLUGIN_ID = "xbup";

    private static final String OPTION_HELP = "h";
    private static final String OPTION_VERBOSE = "v";
    private static final String OPTION_DEV = "dev";
    private static final String OPTION_NODEV = "nodev";
    private static final String OPTION_SINGLE_FILE = "single_file";
    private static final String OPTION_MULTI_FILE = "multi_file";
    private static final String OPTION_FULLSCREEN = "fullscreen";

    public ServiceLauncherModule() {
    }

    @Override
    public void launch(String[] args) {
        OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
        optionsModule.setupAppOptions();
        OptionsStorage optionsStorage = optionsModule.getAppOptions();
        ResourceBundle bundle = App.getModule(LanguageModuleApi.class).getBundle(ServiceLauncherModule.class);

        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption(OPTION_HELP, "help", false, bundle.getString("cl_option_help"));
            opt.addOption(OPTION_VERBOSE, false, bundle.getString("cl_option_verbose"));
            opt.addOption(OPTION_DEV, false, bundle.getString("cl_option_dev"));
            opt.addOption(OPTION_NODEV, false, bundle.getString("cl_option_nodev"));
            opt.addOption(OPTION_FULLSCREEN, false, bundle.getString("cl_option_fullscreen"));
            OptionGroup editorProviderType = new OptionGroup();
            editorProviderType.addOption(new Option(OPTION_SINGLE_FILE, bundle.getString("cl_option_single_file")));
            editorProviderType.addOption(new Option(OPTION_MULTI_FILE, bundle.getString("cl_option_multi_file")));
            opt.addOptionGroup(editorProviderType);
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption(OPTION_HELP)) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(bundle.getString("cl_syntax"), opt);
                return;
            }

            Logger logger = Logger.getLogger("");
            boolean verboseMode = cl.hasOption(OPTION_VERBOSE);
            boolean devModeOpt = false;
            boolean fullScreenMode = cl.hasOption(OPTION_FULLSCREEN);
            if (cl.hasOption(OPTION_NODEV)) {
                if (cl.hasOption(OPTION_DEV)) {
                    logger.severe(bundle.getString("cl_error") + bundle.getString("cl_error_dev_conflict"));
                    return;
                }
            } else {
                devModeOpt = cl.hasOption(OPTION_DEV) || "DEV".equals(bundle.getString("Application.mode"));
            }
            final boolean devMode = devModeOpt;
            String editorProvideType = editorProviderType.getSelected();
            try {
                logger.setLevel(Level.ALL);
                logger.addHandler(new XBHead.XBLogHandler(verboseMode));
            } catch (SecurityException ex) {
                // Ignore it in java webstart
            }

//                Thread.currentThread().setContextClassLoader(moduleRepository.getContextClassLoader());
            WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
            final UiModuleApi uiModule = App.getModule(UiModuleApi.class);
            FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
            DocumentModuleApi documentModule = App.getModule(DocumentModuleApi.class);
            DocumentRecentModule documentRecentModule = App.getModule(DocumentRecentModule.class);
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
            MenuPopupModuleApi menuPopupModule = App.getModule(MenuPopupModuleApi.class);
            ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
            LicenseModuleApi licenseModule = App.getModule(LicenseModuleApi.class);
            LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
            HelpModuleApi helpModule = App.getModule(HelpModuleApi.class);
            HelpOnlineModuleApi helpOnlineModule = App.getModule(HelpOnlineModuleApi.class);
            OperationUndoModuleApi undoModule = App.getModule(OperationUndoModuleApi.class);
            FileModuleApi fileModule = App.getModule(FileModuleApi.class);
            DockingModuleApi dockingModule = App.getModule(DockingModuleApi.class);
            AddonUpdateModuleApi updateModule = App.getModule(AddonUpdateModuleApi.class);

            languageModule.setAppBundle(bundle);
            uiModule.initSwingUi();
            final ClientModuleApi clientModule = App.getModule(ClientModuleApi.class);
            OptionsSettingsModuleApi optionsSettingsModule = App.getModule(OptionsSettingsModuleApi.class);
            // TODO BinaryAppearanceOptions binaryAppearanceParameters = new BinaryAppearanceOptions(optionsStorage);

            final XbupViewerModule xbupViewerModule = App.getModule(XbupViewerModule.class);
            final XbupEditorModule xbupEditorModule = App.getModule(XbupEditorModule.class);
            final DocumentTextModule documentTextModule = App.getModule(DocumentTextModule.class);
            BinedComponentModule binaryModule = App.getModule(BinedComponentModule.class);
//                binaryModule.initEditorProvider(EditorProviderVariant.MULTI);
            binaryModule.registerCodeAreaPopupMenu();

            BinedInspectorModule binedInspectorModule = App.getModule(BinedInspectorModule.class);

            frameModule.init();
            xbupEditorModule.setDevMode(devMode);
            try {
                updateModule.setUpdateUrl(new URI(bundle.getString("update_url")).toURL());
                updateModule.setUpdateDownloadUrl(new URI(bundle.getString("update_download_url")).toURL());
            } catch (MalformedURLException | URISyntaxException ex) {
                Logger.getLogger(ServiceLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateModule.registerDefaultMenuItem();
            licenseModule.registerBasicPages();
            licenseModule.registerDefaultMenuItem();
            // helpModule.registerMainMenu();
            try {
                helpOnlineModule.setOnlineHelpUrl(new URI(bundle.getString("online_help_url")).toURL());
            } catch (MalformedURLException | URISyntaxException ex) {
                Logger.getLogger(ServiceLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            helpOnlineModule.registerOnlineHelpMenu();

            frameModule.registerExitAction();
            frameModule.registerBarsVisibilityActions();

            // Register clipboard editing actions
            dockingModule.registerMenuFileHandlingActions();

            dockingModule.registerToolBarFileHandlingActions();
            documentRecentModule.registerRecentFilesMenuActions();

            undoModule.registerMainMenu();
            undoModule.registerMainToolBar();
//                undoModule.registerUndoManagerInMainMenu();

            // Register clipboard editing actions
            menuPopupModule.registerDefaultClipboardPopupMenuWithIcons();
            menuModule.registerMenuClipboardActions();
            toolBarModule.registerToolBarClipboardActions();

            optionsSettingsModule.registerMenuAction();

            documentTextModule.registerEditFindMenuActions();
            documentTextModule.registerWordWrapping();
            documentTextModule.registerGoToLine();
            //                textEditorModule.registerPrintMenu();

            xbupEditorModule.setDevMode(devMode);
            xbupEditorModule.registerFileTypes();
            xbupEditorModule.registerCatalogBrowserMenu();
            xbupEditorModule.registerDocEditingMenuActions();
            xbupEditorModule.registerDocEditingToolBarActions();
            // xbupDocumentModule.registerPropertiesMenuAction();

            documentTextModule.registerToolsOptionsMenuActions();
            documentTextModule.registerSettings();
            xbupViewerModule.registerSettings();
            updateModule.registerSettings();

            binaryModule.registerCodeAreaPopupEventDispatcher();

            FrameController frameController = frameModule.getFrameController();

            xbupViewerModule.registerStatusBar();

            DocumentDocking documentDocking = dockingModule.createDefaultDocking();
            frameModule.attachFrameContentComponent(documentDocking);
            //                frameHandler.setMainPanel(dockingModule.getDockingPanel());
            frameController.setDefaultSize(new Dimension(600, 400));
            optionsSettingsModule.initialLoadFromPreferences();
            frameController.loadMainMenu();
            frameController.loadMainToolBar();
            frameController.showFrame();
            updateModule.checkOnStart(frameController.getFrame());

            clientModule.addClientConnectionListener(xbupViewerModule.getClientConnectionListener());
            clientModule.addPluginRepositoryListener((pluginRepository) -> {
                xbupEditorModule.setPluginRepository(pluginRepository);
            });
            clientModule.setDevMode(devMode);
            Thread connectionThread = new Thread(() -> {
                if (!clientModule.connectToService()) {
                    if (!clientModule.runLocalCatalog()) {
                        clientModule.useBuildInCatalog();
                    }
                }

                XBACatalog catalog = clientModule.getCatalog();
                xbupEditorModule.setCatalog(catalog);
            });

            connectionThread.start();

            List fileArgs = cl.getArgList();
            if (!fileArgs.isEmpty()) {
                fileModule.openFile((String) fileArgs.get(0));
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServiceLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
