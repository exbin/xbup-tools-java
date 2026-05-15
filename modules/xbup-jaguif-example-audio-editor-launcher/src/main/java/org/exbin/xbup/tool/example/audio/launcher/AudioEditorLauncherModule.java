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
package org.exbin.xbup.tool.example.audio.launcher;

import java.awt.Dimension;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.jaguif.App;
import org.exbin.jaguif.LauncherModule;
import org.exbin.jaguif.license.api.LicenseModuleApi;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.manager.ActionManagerModule;
import org.exbin.jaguif.addon.manager.api.AddonManagerModuleApi;
import org.exbin.jaguif.docking.api.DockingModuleApi;
import org.exbin.jaguif.docking.api.DocumentDocking;
import org.exbin.jaguif.document.api.DocumentModuleApi;
import org.exbin.jaguif.document.recent.DocumentRecentModule;
import org.exbin.xbup.jaguif.editor.wave.EditorWaveModule;
import org.exbin.xbup.jaguif.document.wave.XbupDocumentWaveModule;
import org.exbin.jaguif.file.api.FileModuleApi;
import org.exbin.jaguif.frame.api.FrameController;
import org.exbin.jaguif.frame.api.FrameModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.menu.popup.api.MenuPopupModuleApi;
import org.exbin.jaguif.operation.undo.api.OperationUndoModuleApi;
import org.exbin.jaguif.options.api.OptionsStorage;
import org.exbin.jaguif.options.api.OptionsModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.jaguif.ui.api.UiModuleApi;
import org.exbin.jaguif.ui.theme.api.UiThemeModuleApi;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.xbup.core.parser.basic.XBHead;

/**
 * XBUP example audio editor launcher module.
 */
@ParametersAreNonnullByDefault
public class AudioEditorLauncherModule implements LauncherModule {

    private static boolean verboseMode = false;
    private static boolean devMode = false;

    public AudioEditorLauncherModule() {
    }

    @Override
    public void launch(String[] args) {
        OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
        optionsModule.setupAppOptions();
        OptionsStorage optionsStorage = optionsModule.getAppOptions();
        ResourceBundle bundle = App.getModule(LanguageModuleApi.class).getBundle(AudioEditorLauncherModule.class);

        try {
            // Parameters processing
            Options opt = new Options();
            opt.addOption("h", "help", false, bundle.getString("cl_option_help"));
            opt.addOption("v", false, bundle.getString("cl_option_verbose"));
            opt.addOption("dev", false, bundle.getString("cl_option_dev"));
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption('h')) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp(bundle.getString("cl_syntax"), opt);
                return;
            }

            verboseMode = cl.hasOption("v");
            devMode = cl.hasOption("dev");
            Logger logger = Logger.getLogger("");
            try {
                logger.setLevel(Level.ALL);
                logger.addHandler(new XBHead.XBLogHandler(verboseMode));
            } catch (SecurityException ex) {
                // Ignore it in java webstart
            }

            WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
            final UiModuleApi uiModule = App.getModule(UiModuleApi.class);
            final UiThemeModuleApi themeModule = App.getModule(UiThemeModuleApi.class);
            themeModule.registerThemeInit();

            FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
            DocumentModuleApi documentModule = App.getModule(DocumentModuleApi.class);
            DocumentRecentModule documentRecentModule = App.getModule(DocumentRecentModule.class);
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            DockingModuleApi dockingModule = App.getModule(DockingModuleApi.class);
            MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
            MenuPopupModuleApi menuPopupModule = App.getModule(MenuPopupModuleApi.class);
            ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
            LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
            LicenseModuleApi licenseModule = App.getModule(LicenseModuleApi.class);
            OperationUndoModuleApi undoModule = App.getModule(OperationUndoModuleApi.class);
            FileModuleApi fileModule = App.getModule(FileModuleApi.class);
            OptionsSettingsModuleApi optionsSettingsModule = App.getModule(OptionsSettingsModuleApi.class);
            AddonManagerModuleApi addonManagerModule = App.getModule(AddonManagerModuleApi.class);
            addonManagerModule.registerBasicAddonManager();
            addonManagerModule.setDevMode(devMode);
            ActionManagerModule actionManagerModule = App.getModule(ActionManagerModule.class);

            final EditorWaveModule waveEditorModule = App.getModule(EditorWaveModule.class);
            XbupDocumentWaveModule xbupDocumentWaveModule = App.getModule(XbupDocumentWaveModule.class);

            languageModule.setAppBundle(bundle);
            uiModule.initSwingUi();
            frameModule.init();
            licenseModule.registerBasicPages();
            licenseModule.registerDefaultMenuItem();

            frameModule.registerExitAction();
            frameModule.registerBarsVisibilityActions();

            // Register clipboard editing actions
            dockingModule.registerMenuFileHandlingActions();
            dockingModule.registerToolBarFileHandlingActions();
            documentRecentModule.registerRecentFilesMenuActions();

            undoModule.registerMainMenu();
            undoModule.registerMainToolBar();
//                undoModule.registerUndoManagerInMainMenu();
            /* TODO XBTLinearUndo linearUndo = new XBTLinearUndo(null);
            linearUndo.addUndoUpdateListener(new XBUndoUpdateListener() {
                @Override
                public void undoCommandPositionChanged() {
                    ((AudioPanel) waveEditorModule.getEditorProvider()).repaint();
                }

                @Override
                public void undoCommandAdded(Command command) {
                    ((AudioPanel) waveEditorModule.getEditorProvider()).repaint();
                }
            });
            undoModule.setUndoHandler(linearUndo); */

            // Register clipboard editing actions
            menuPopupModule.registerDefaultClipboardPopupMenuWithIcons();
            menuModule.registerMenuClipboardActions();
            toolBarModule.registerToolBarClipboardActions();

            optionsSettingsModule.registerMenuAction();

            waveEditorModule.registerFileTypes();
            xbupDocumentWaveModule.registerFileTypes();
            xbupDocumentWaveModule.registerDocument();
            waveEditorModule.registerToolsMenuActions();
            waveEditorModule.registerToolsOptionsMenuActions();
            waveEditorModule.registerPropertiesMenu();
            waveEditorModule.registerAudioMenu();
            waveEditorModule.registerAudioOperationMenu();
            waveEditorModule.registerDrawingModeMenu();
            waveEditorModule.registerZoomModeMenu();
            waveEditorModule.bindZoomScrollWheel();

            addonManagerModule.registerAddonManagerMenuItem();

            uiModule.registerSettings();
            themeModule.registerSettings();
            actionManagerModule.registerSettings();
            fileModule.registerSettings();
            waveEditorModule.registerSettings();

            fileModule.registerFileProviders();

            FrameController frameController = frameModule.getFrameController();
            waveEditorModule.registerStatusBar();
            waveEditorModule.registerUndoHandler();

            DocumentDocking documentDocking = dockingModule.createDefaultDocking();
            frameModule.attachFrameContentComponent(documentDocking);
            frameController.setDefaultSize(new Dimension(600, 400));
            optionsSettingsModule.initialLoadFromPreferences();
            frameController.loadMainMenu();
            frameController.loadMainToolBar();
            frameController.showFrame();

            List fileArgs = cl.getArgList();
            if (!fileArgs.isEmpty()) {
                fileModule.openFile((String) fileArgs.get(0));
            }
        } catch (ParseException | RuntimeException ex) {
            Logger.getLogger(AudioEditorLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
