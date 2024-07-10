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
package org.exbin.xbup.tool.xbeditor;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.exbin.framework.App;
import org.exbin.framework.preferences.api.Preferences;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.framework.client.api.ClientModuleApi;
import org.exbin.framework.editor.text.EditorTextModule;
import org.exbin.framework.editor.xbup.EditorXbupModule;
import org.exbin.framework.about.api.AboutModuleApi;
import org.exbin.framework.editor.api.EditorModuleApi;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.help.api.HelpModuleApi;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.operation.undo.api.OperationUndoModuleApi;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.docking.api.DockingModuleApi;
import org.exbin.framework.help.online.api.HelpOnlineModuleApi;
import org.exbin.framework.addon.update.api.AddonUpdateModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.basic.BasicApplication;
import org.exbin.framework.bined.inspector.BinedInspectorModule;
import org.exbin.framework.editor.api.EditorProviderVariant;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.preferences.api.PreferencesModuleApi;
import org.exbin.framework.frame.api.ApplicationFrameHandler;
import org.exbin.framework.ui.api.UiModuleApi;

/**
 * The main class of the XBEditor application.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBEditor {

    private static final String XBUP_PLUGIN_ID = "xbup";

    private static final String OPTION_HELP = "h";
    private static final String OPTION_VERBOSE = "v";
    private static final String OPTION_DEV = "dev";
    private static final String OPTION_NODEV = "nodev";
    private static final String OPTION_SINGLE_FILE = "single_file";
    private static final String OPTION_MULTI_FILE = "multi_file";
    private static final String OPTION_FULLSCREEN = "fullscreen";

    private XBEditor() {
    }

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        BasicApplication app = new BasicApplication();
        app.init();

        app.setAppDirectory(XBEditor.class);
        app.addClassPathModules();
        app.addModulesFromManifest(XBEditor.class);
        app.initModules();

        App.launch(() -> {
            PreferencesModuleApi preferencesModule = App.getModule(PreferencesModuleApi.class);
            preferencesModule.setupAppPreferences(XBEditor.class);
            Preferences preferences = preferencesModule.getAppPreferences();
            ResourceBundle bundle = App.getModule(LanguageModuleApi.class).getBundle(XBEditor.class);

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
                } catch (java.security.AccessControlException ex) {
                    // Ignore it in java webstart
                }

//                Thread.currentThread().setContextClassLoader(moduleRepository.getContextClassLoader());
                WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
                final UiModuleApi uiModule = App.getModule(UiModuleApi.class);
                FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
                EditorModuleApi editorModule = App.getModule(EditorModuleApi.class);
                ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
                AboutModuleApi aboutModule = App.getModule(AboutModuleApi.class);
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
                OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
                boolean multiFileMode = true;
                EditorProviderVariant editorProviderVariant = editorProvideType != null
                        ? (OPTION_SINGLE_FILE.equals(editorProvideType) ? EditorProviderVariant.SINGLE : EditorProviderVariant.MULTI)
                        : (multiFileMode ? EditorProviderVariant.MULTI : EditorProviderVariant.SINGLE);
                final EditorXbupModule xbupEditorModule = App.getModule(EditorXbupModule.class);
                final EditorTextModule textEditorModule = App.getModule(EditorTextModule.class);
                BinedModule binaryModule = App.getModule(BinedModule.class);
                xbupEditorModule.initEditorProvider(editorProviderVariant);
                EditorProvider editorProvider = xbupEditorModule.getEditorProvider();
                editorModule.registerEditor(XBUP_PLUGIN_ID, editorProvider);
//                binaryModule.initEditorProvider(EditorProviderVariant.MULTI);
                binaryModule.setEditorProvider(editorProvider);
                binaryModule.registerCodeAreaPopupMenu();

                BinedInspectorModule binedInspectorModule = App.getModule(BinedInspectorModule.class);
                binedInspectorModule.setEditorProvider(editorProvider);

                frameModule.createMainMenu();
                xbupEditorModule.setDevMode(devMode);
                try {
                    updateModule.setUpdateUrl(new URL(bundle.getString("update_url")));
                    updateModule.setUpdateDownloadUrl(new URL(bundle.getString("update_download_url")));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateModule.registerDefaultMenuItem();
                aboutModule.registerDefaultMenuItem();
                helpModule.registerMainMenu();
                try {
                    helpOnlineModule.setOnlineHelpUrl(new URL(bundle.getString("online_help_url")));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                helpOnlineModule.registerOnlineHelpMenu();

                frameModule.registerExitAction();
                frameModule.registerBarsVisibilityActions();

                // Register clipboard editing actions
                fileModule.registerMenuFileHandlingActions();
                if (editorProviderVariant == EditorProviderVariant.MULTI) {
                    editorModule.registerMenuFileCloseActions();
                }

                fileModule.registerToolBarFileHandlingActions();
                fileModule.registerCloseListener();
                fileModule.registerRecenFilesMenuActions();

                undoModule.registerMainMenu();
                undoModule.registerMainToolBar();
//                undoModule.registerUndoManagerInMainMenu();

                // Register clipboard editing actions
                actionModule.registerClipboardTextActions();
                actionModule.registerMenuClipboardActions();
                actionModule.registerToolBarClipboardActions();

                optionsModule.registerMenuAction();

                textEditorModule.registerEditFindMenuActions();
                textEditorModule.registerWordWrapping();
                textEditorModule.registerGoToLine();
                //                textEditorModule.registerPrintMenu();

                xbupEditorModule.setDevMode(devMode);
                xbupEditorModule.registerFileTypes();
                xbupEditorModule.registerCatalogBrowserMenu();
                xbupEditorModule.registerDocEditingMenuActions();
                xbupEditorModule.registerDocEditingToolBarActions();
                xbupEditorModule.registerSampleFilesSubMenuActions();
                xbupEditorModule.registerPropertiesMenuAction();

                textEditorModule.registerToolsOptionsMenuActions();
                textEditorModule.registerOptionsPanels();
                xbupEditorModule.registerOptionsPanels();
                updateModule.registerOptionsPanels();

                binaryModule.registerCodeAreaPopupEventDispatcher();

                ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();

                xbupEditorModule.registerStatusBar();

                frameHandler.setMainPanel(editorModule.getEditorComponent());
                //                frameHandler.setMainPanel(dockingModule.getDockingPanel());
                frameHandler.setDefaultSize(new Dimension(600, 400));
                optionsModule.initialLoadFromPreferences();
                frameHandler.loadMainMenu();
                frameHandler.loadMainToolBar();
                frameHandler.showFrame();
                if (editorProviderVariant == EditorProviderVariant.SINGLE) {
                    ((XbupFileHandler) editorProvider.getActiveFile().get()).postWindowOpened();
                }
                updateModule.checkOnStart(frameHandler.getFrame());

                clientModule.addClientConnectionListener(xbupEditorModule.getClientConnectionListener());
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
                    fileModule.loadFromFile((String) fileArgs.get(0));
                }
            } catch (ParseException ex) {
                Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
