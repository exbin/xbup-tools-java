/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.tool.xbeditor;

import java.awt.Dimension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.api.Preferences;
import org.exbin.framework.XBBaseApplication;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.framework.client.api.ClientModuleApi;
import org.exbin.framework.editor.text.EditorTextModule;
import org.exbin.framework.editor.xbup.EditorXbupModule;
import org.exbin.framework.gui.about.api.GuiAboutModuleApi;
import org.exbin.framework.gui.editor.api.GuiEditorModuleApi;
import org.exbin.framework.gui.editor.api.EditorProvider;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.help.api.GuiHelpModuleApi;
import org.exbin.framework.gui.options.api.GuiOptionsModuleApi;
import org.exbin.framework.gui.undo.api.GuiUndoModuleApi;
import org.exbin.xbup.operation.undo.XBTLinearUndo;
import org.exbin.framework.api.XBApplicationModuleRepository;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.gui.docking.api.GuiDockingModuleApi;
import org.exbin.framework.gui.link.api.GuiLinkModuleApi;
import org.exbin.framework.gui.update.api.GuiUpdateModuleApi;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.action.api.GuiActionModuleApi;
import org.exbin.framework.gui.editor.api.EditorProviderVariant;

/**
 * The main class of the XBEditor application.
 *
 * @version 0.2.1 2021/12/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBEditor {

    private static final String BINARY_PLUGIN_ID = "binary";

    private static final String OPTION_HELP = "h";
    private static final String OPTION_VERBOSE = "v";
    private static final String OPTION_DEV = "dev";
    private static final String OPTION_SINGLE_FILE = "single_file";
    private static final String OPTION_MULTI_FILE = "multi_file";

    private static final ResourceBundle bundle = LanguageUtils.getResourceBundleByClass(XBEditor.class);

    private XBEditor() {
    }

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("");

        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption(OPTION_HELP, "help", false, bundle.getString("cl_option_help"));
            opt.addOption(OPTION_VERBOSE, false, bundle.getString("cl_option_verbose"));
            opt.addOption(OPTION_DEV, false, bundle.getString("cl_option_dev"));
            opt.addOption("nodev", false, bundle.getString("cl_option_nodev"));
            OptionGroup editorProviderType = new OptionGroup();
            editorProviderType.addOption(new Option(OPTION_SINGLE_FILE, bundle.getString("cl_option_single_file")));
            editorProviderType.addOption(new Option(OPTION_MULTI_FILE, bundle.getString("cl_option_multi_file")));
            opt.addOptionGroup(editorProviderType);
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption(OPTION_HELP)) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(bundle.getString("cl_syntax"), opt);
            } else {
                boolean verboseMode = cl.hasOption(OPTION_VERBOSE);
                boolean devMode = false;
                if (cl.hasOption("nodev")) {
                    if (cl.hasOption("dev")) {
                        logger.severe(bundle.getString("cl_error") + bundle.getString("cl_error_dev_conflict"));
                        return;
                    }
                } else {
                    devMode = cl.hasOption("dev") || "DEV".equals(bundle.getString("Application.mode"));
                }
                String editorProvideType = editorProviderType.getSelected();
                try {
                    logger.setLevel(Level.ALL);
                    logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                } catch (java.security.AccessControlException ex) {
                    // Ignore it in java webstart
                }

                XBBaseApplication app = new XBBaseApplication();
                Preferences preferences = app.createPreferences(XBEditor.class);
                app.setAppBundle(bundle, LanguageUtils.getResourceBaseNameBundleByClass(XBEditor.class));

                XBApplicationModuleRepository moduleRepository = app.getModuleRepository();
                moduleRepository.addClassPathModules();
                moduleRepository.addModulesFromManifest(XBEditor.class);
                moduleRepository.loadModulesFromPath(new File("plugins").toURI());
                moduleRepository.initModules();
                app.init();

                GuiFrameModuleApi frameModule = moduleRepository.getModuleByInterface(GuiFrameModuleApi.class);
                GuiEditorModuleApi editorModule = moduleRepository.getModuleByInterface(GuiEditorModuleApi.class);
                GuiActionModuleApi actionModule = moduleRepository.getModuleByInterface(GuiActionModuleApi.class);
                GuiAboutModuleApi aboutModule = moduleRepository.getModuleByInterface(GuiAboutModuleApi.class);
                GuiHelpModuleApi helpModule = moduleRepository.getModuleByInterface(GuiHelpModuleApi.class);
                GuiLinkModuleApi linkModule = moduleRepository.getModuleByInterface(GuiLinkModuleApi.class);
                GuiUndoModuleApi undoModule = moduleRepository.getModuleByInterface(GuiUndoModuleApi.class);
                GuiFileModuleApi fileModule = moduleRepository.getModuleByInterface(GuiFileModuleApi.class);
                GuiDockingModuleApi dockingModule = moduleRepository.getModuleByInterface(GuiDockingModuleApi.class);
                GuiUpdateModuleApi updateModule = moduleRepository.getModuleByInterface(GuiUpdateModuleApi.class);

                final ClientModuleApi clientModule = moduleRepository.getModuleByInterface(ClientModuleApi.class);
                GuiOptionsModuleApi optionsModule = moduleRepository.getModuleByInterface(GuiOptionsModuleApi.class);
                boolean multiFileMode = true;
                EditorProviderVariant editorProviderVariant = editorProvideType != null
                        ? (OPTION_SINGLE_FILE.equals(editorProvideType) ? EditorProviderVariant.SINGLE : EditorProviderVariant.MULTI)
                        : (multiFileMode ? EditorProviderVariant.MULTI : EditorProviderVariant.SINGLE);
                final EditorXbupModule xbupEditorModule = moduleRepository.getModuleByInterface(EditorXbupModule.class);
                final EditorTextModule textEditorModule = moduleRepository.getModuleByInterface(EditorTextModule.class);
                BinedModule binaryModule = moduleRepository.getModuleByInterface(BinedModule.class);
                xbupEditorModule.initEditorProvider(editorProviderVariant);
                EditorProvider editorProvider = xbupEditorModule.getEditorProvider();

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
                    linkModule.setOnlineHelpUrl(new URL(bundle.getString("online_help_url")));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
                linkModule.registerOnlineHelpMenu();

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
                undoModule.registerUndoManagerInMainMenu();
                XBTLinearUndo linearUndo = new XBTLinearUndo(null);
//                linearUndo.addUndoUpdateListener(new UndoUpdateListener() {
//                    @Override
//                    public void undoChanged() {
//                        ((AudioPanel) waveEditorModule.getEditorProvider()).repaint();
//                    }
//                });
                undoModule.setUndoHandler(linearUndo);
                xbupEditorModule.setUndoHandler(linearUndo);

                // Register clipboard editing actions
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
                xbupEditorModule.registerViewModeMenu();
                xbupEditorModule.registerSampleFilesSubMenuActions();
                xbupEditorModule.registerPropertiesMenuAction();

                textEditorModule.registerToolsOptionsMenuActions();
                textEditorModule.registerOptionsPanels();
                xbupEditorModule.registerOptionsPanels();
                updateModule.registerOptionsPanels();

                binaryModule.registerCodeAreaPopupEventDispatcher();

                ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();
                editorModule.registerEditor("xbup", editorProvider);

                xbupEditorModule.registerStatusBar();

                frameHandler.setMainPanel(editorModule.getEditorComponent());
//                frameHandler.setMainPanel(dockingModule.getDockingPanel());
                frameHandler.setDefaultSize(new Dimension(600, 400));
                optionsModule.initialLoadFromPreferences();
                frameHandler.showFrame();
                ((XbupFileHandler) editorProvider.getActiveFile().get()).postWindowOpened();
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
                if (fileArgs.size() > 0) {
                    fileModule.loadFromFile((String) fileArgs.get(0));
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
