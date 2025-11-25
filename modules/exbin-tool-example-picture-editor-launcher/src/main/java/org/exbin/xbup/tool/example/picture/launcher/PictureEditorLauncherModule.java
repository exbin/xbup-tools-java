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
package org.exbin.xbup.tool.example.picture.launcher;

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
import org.exbin.framework.addon.manager.api.AddonManagerModuleApi;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.manager.ActionManagerModule;
import org.exbin.framework.docking.api.DockingModuleApi;
import org.exbin.framework.docking.api.DocumentDocking;
import org.exbin.framework.document.api.DocumentModuleApi;
import org.exbin.framework.document.recent.DocumentRecentModule;
import org.exbin.framework.editor.picture.EditorPictureModule;
import org.exbin.framework.editor.xbup.picture.EditorXbupPictureModule;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.frame.api.ComponentFrame;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.menu.api.MenuModuleApi;
import org.exbin.framework.menu.popup.api.MenuPopupModuleApi;
import org.exbin.framework.operation.undo.api.OperationUndoModuleApi;
import org.exbin.framework.options.api.OptionsStorage;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.framework.ui.api.UiModuleApi;
import org.exbin.framework.ui.theme.api.UiThemeModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.xbup.core.parser.basic.XBHead;

/**
 * XBUP example picture editor launcher module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class PictureEditorLauncherModule implements LauncherModule {

    private static boolean verboseMode = false;
    private static boolean devMode = false;

    public PictureEditorLauncherModule() {
    }

    @Override
    public void launch(String[] args) {
        OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
        try {
            optionsModule.setupAppOptions(Class.forName("org.exbin.xbup.tool.example.picture.editor.PictureEditorApp"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PictureEditorLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        OptionsStorage preferences = optionsModule.getAppOptions();
        ResourceBundle bundle = App.getModule(LanguageModuleApi.class).getBundle(PictureEditorLauncherModule.class);

        // Parameters processing
        try {
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
            DockingModuleApi dockingModule = App.getModule(DockingModuleApi.class);
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
            MenuPopupModuleApi menuPopupModule = App.getModule(MenuPopupModuleApi.class);
            ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
            LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
            AboutModuleApi aboutModule = App.getModule(AboutModuleApi.class);
            OperationUndoModuleApi undoModule = App.getModule(OperationUndoModuleApi.class);
            FileModuleApi fileModule = App.getModule(FileModuleApi.class);
            OptionsSettingsModuleApi optionsSettingsModule = App.getModule(OptionsSettingsModuleApi.class);
            AddonManagerModuleApi addonManagerModule = App.getModule(AddonManagerModuleApi.class);
            addonManagerModule.setDevMode(devMode);
            ActionManagerModule actionManagerModule = App.getModule(ActionManagerModule.class);

            final EditorPictureModule pictureEditorModule = App.getModule(EditorPictureModule.class);
            EditorXbupPictureModule pictureXbupEditorModule = App.getModule(EditorXbupPictureModule.class);

            languageModule.setAppBundle(bundle);
            uiModule.initSwingUi();
            frameModule.init();
            aboutModule.registerDefaultMenuItem();

            frameModule.registerExitAction();
            frameModule.registerBarsVisibilityActions();

            // Register clipboard editing actions
            dockingModule.registerMenuFileHandlingActions();
            dockingModule.registerToolBarFileHandlingActions();
            documentRecentModule.registerRecenFilesMenuActions();
            fileModule.registerCloseListener();

            undoModule.registerMainMenu();
            undoModule.registerMainToolBar();
//                undoModule.registerUndoManagerInMainMenu();

            // Register clipboard editing actions
            menuPopupModule.registerDefaultClipboardPopupMenuWithIcons();
            menuModule.registerMenuClipboardActions();
            toolBarModule.registerToolBarClipboardActions();

            optionsSettingsModule.registerMenuAction();

            pictureEditorModule.registerFileTypes();
            pictureXbupEditorModule.registerFileTypes();
            pictureEditorModule.registerToolsOptionsMenuActions();
            pictureEditorModule.registerOptionsMenuPanels();
            pictureEditorModule.registerPropertiesMenu();
            pictureEditorModule.registerPrintMenu();
            pictureEditorModule.registerZoomModeMenu();
            pictureEditorModule.registerPictureMenu();
            pictureEditorModule.registerPictureOperationMenu();

            addonManagerModule.registerAddonManagerMenuItem();

            uiModule.registerSettings();
            themeModule.registerSettings();
            actionManagerModule.registerSettings();
            fileModule.registerSettings();
            pictureEditorModule.registerSettings();
            
            ComponentFrame frameHandler = frameModule.getFrameHandler();

            DocumentDocking documentDocking = dockingModule.createDefaultDocking();
            frameModule.attachFrameContentComponent(documentDocking);
            pictureEditorModule.registerStatusBar();
            pictureEditorModule.registerUndoHandler();

            frameHandler.setDefaultSize(new Dimension(600, 400));
            optionsSettingsModule.initialLoadFromPreferences();
            frameHandler.loadMainMenu();
            frameHandler.loadMainToolBar();
            frameHandler.showFrame();

            List fileArgs = cl.getArgList();
            if (!fileArgs.isEmpty()) {
                fileModule.loadFromFile((String) fileArgs.get(0));
            }
        } catch (ParseException | RuntimeException ex) {
            Logger.getLogger(PictureEditorLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public enum BasicDockingType {
        SINGLE, MULTI;
    }
}
