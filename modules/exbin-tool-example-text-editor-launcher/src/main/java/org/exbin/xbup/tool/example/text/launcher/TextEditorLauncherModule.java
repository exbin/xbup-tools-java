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
package org.exbin.xbup.tool.example.text.launcher;

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
import org.exbin.framework.App;
import org.exbin.framework.LauncherModule;
import org.exbin.framework.about.api.AboutModuleApi;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.manager.ActionManagerModule;
import org.exbin.framework.addon.manager.api.AddonManagerModuleApi;
import org.exbin.framework.editor.api.EditorModuleApi;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.editor.text.EditorTextModule;
import org.exbin.framework.editor.xbup.text.EditorXbupTextModule;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.frame.api.ApplicationFrameHandler;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.operation.undo.api.OperationUndoModuleApi;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.preferences.api.OptionsStorage;
import org.exbin.framework.preferences.api.PreferencesModuleApi;
import org.exbin.framework.text.encoding.TextEncodingModule;
import org.exbin.framework.ui.api.UiModuleApi;
import org.exbin.framework.ui.theme.api.UiThemeModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.xbup.core.parser.basic.XBHead;

/**
 * XBUP text editor launcher module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class TextEditorLauncherModule implements LauncherModule {

    private static boolean verboseMode = false;
    private static boolean devMode = false;

    public TextEditorLauncherModule() {
    }

    @Override
    public void launch(String[] args) {
        PreferencesModuleApi preferencesModule = App.getModule(PreferencesModuleApi.class);
        try {
            preferencesModule.setupAppPreferences(Class.forName("org.exbin.xbup.tool.example.text.editor.TextEditorApp"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TextEditorLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        OptionsStorage preferences = preferencesModule.getAppPreferences();
        ResourceBundle bundle = App.getModule(LanguageModuleApi.class).getBundle(TextEditorLauncherModule.class);

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
            EditorModuleApi editorModule = App.getModule(EditorModuleApi.class);
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
            AboutModuleApi aboutModule = App.getModule(AboutModuleApi.class);
            OperationUndoModuleApi undoModule = App.getModule(OperationUndoModuleApi.class);
            FileModuleApi fileModule = App.getModule(FileModuleApi.class);
            OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
            TextEncodingModule textEncodingModule = App.getModule(TextEncodingModule.class);
            EditorTextModule textEditorModule = App.getModule(EditorTextModule.class);
            EditorXbupTextModule textXbupEditorModule = App.getModule(EditorXbupTextModule.class);
            AddonManagerModuleApi addonManagerModule = App.getModule(AddonManagerModuleApi.class);
            addonManagerModule.setDevMode(devMode);
            ActionManagerModule actionManagerModule = App.getModule(ActionManagerModule.class);

            // TODO From module instead
            languageModule.setAppBundle(bundle);
            uiModule.initSwingUi();
            frameModule.createMainMenu();
            aboutModule.registerDefaultMenuItem();

            frameModule.registerExitAction();
            frameModule.registerBarsVisibilityActions();

            // Register clipboard editing actions
            fileModule.registerMenuFileHandlingActions();
            fileModule.registerToolBarFileHandlingActions();
            fileModule.registerRecenFilesMenuActions();
            fileModule.registerCloseListener();

            undoModule.registerMainMenu();
            undoModule.registerMainToolBar();

            // Register clipboard editing actions
            actionModule.registerClipboardTextActions();
            actionModule.registerMenuClipboardActions();
            actionModule.registerToolBarClipboardActions();

            optionsModule.registerMenuAction();

            textEncodingModule.loadFromPreferences(preferences);
            
            textEditorModule.registerFileTypes();
            textXbupEditorModule.registerFileTypes();
            textEditorModule.registerEditFindMenuActions();
            textEditorModule.registerEditFindToolBarActions();
            textEditorModule.registerToolsOptionsMenuActions();
            textEditorModule.registerOptionsMenuPanels();
            textEditorModule.registerWordWrapping();
            textEditorModule.registerGoToLine();

            textEditorModule.registerTextPopupMenu();

            textEditorModule.registerPropertiesMenu();
            textEditorModule.registerPrintMenu();

            addonManagerModule.registerAddonManagerMenuItem();
            
            uiModule.registerOptionsPanels();
            themeModule.registerOptionsPanels();
            actionManagerModule.registerOptionsPanels();
            fileModule.registerOptionsPanels();
            editorModule.registerOptionsPanels();
            textEditorModule.registerOptionsPanels();

            ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();
            EditorProvider editorProvider = textXbupEditorModule.createEditorProvider();
            editorModule.registerEditor("text", editorProvider);
            textEditorModule.registerStatusBar();
            textEditorModule.registerUndoHandler();

            frameHandler.setMainPanel(editorModule.getEditorComponent());
            frameHandler.setDefaultSize(new Dimension(600, 400));
            optionsModule.initialLoadFromPreferences();
            frameHandler.loadMainMenu();
            frameHandler.loadMainToolBar();
            frameHandler.showFrame();

            List fileArgs = cl.getArgList();
            if (!fileArgs.isEmpty()) {
                fileModule.loadFromFile((String) fileArgs.get(0));
            }
        } catch (ParseException | RuntimeException ex) {
            Logger.getLogger(TextEditorLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
