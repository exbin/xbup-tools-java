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
package org.exbin.xbup.tool.manager.launcher;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPanel;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.App;
import org.exbin.framework.LauncherModule;
import org.exbin.framework.about.api.AboutModuleApi;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.addon.update.api.AddonUpdateModuleApi;
import org.exbin.framework.frame.api.ApplicationFrameHandler;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.help.online.api.HelpOnlineModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.preferences.api.Preferences;
import org.exbin.framework.preferences.api.PreferencesModuleApi;
import org.exbin.framework.ui.api.UiModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.xbup.catalog.XbupCatalogModule;
import org.exbin.framework.xbup.service.XbupServiceModule;
import org.exbin.xbup.core.parser.basic.XBHead;

/**
 * XBUP manager launcher module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ManagerLauncherModule implements LauncherModule {

    public ManagerLauncherModule() {
    }

    @Override
    public void launch(String[] args) {
        PreferencesModuleApi preferencesModule = App.getModule(PreferencesModuleApi.class);
        try {
            preferencesModule.setupAppPreferences(Class.forName("org.exbin.xbup.tool.manager.ManagerApp"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManagerLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        Preferences preferences = preferencesModule.getAppPreferences();
        ResourceBundle bundle = App.getModule(LanguageModuleApi.class).getBundle(ManagerLauncherModule.class);

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

            boolean verboseMode = cl.hasOption("v");
            boolean devMode = cl.hasOption("dev");
            Logger logger = Logger.getLogger("");
            try {
                logger.setLevel(Level.ALL);
                logger.addHandler(new XBHead.XBLogHandler(verboseMode));
            } catch (SecurityException ex) {
                // Ignore it in java webstart
            }

            WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
            final UiModuleApi uiModule = App.getModule(UiModuleApi.class);
            FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
            AboutModuleApi aboutModule = App.getModule(AboutModuleApi.class);
            HelpOnlineModuleApi helpOnlineModule = App.getModule(HelpOnlineModuleApi.class);
            OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
            XbupCatalogModule xbupCatalogModule = App.getModule(XbupCatalogModule.class);
            XbupServiceModule xbupServiceModule = App.getModule(XbupServiceModule.class);
            AddonUpdateModuleApi updateModule = App.getModule(AddonUpdateModuleApi.class);

            languageModule.setAppBundle(bundle);
            uiModule.initSwingUi();
            frameModule.createMainMenu();
            try {
                updateModule.setUpdateUrl(new URL(bundle.getString("update_url")));
                updateModule.setUpdateDownloadUrl(new URL(bundle.getString("update_download_url")));
            } catch (MalformedURLException ex) {
                Logger.getLogger(ManagerLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateModule.registerDefaultMenuItem();
            aboutModule.registerDefaultMenuItem();
            try {
                helpOnlineModule.setOnlineHelpUrl(new URL(bundle.getString("online_help_url")));
            } catch (MalformedURLException ex) {
                Logger.getLogger(ManagerLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            helpOnlineModule.registerOnlineHelpMenu();

            frameModule.registerExitAction();
            frameModule.registerStatusBarVisibilityActions();

            actionModule.registerMenuClipboardActions();

            optionsModule.registerMenuAction();

            uiModule.registerOptionsPanels();
            updateModule.registerOptionsPanels();

            ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();

            xbupServiceModule.setPreferences(preferences);
            JPanel servicePanel = xbupServiceModule.getServicePanel();
            frameHandler.setMainPanel(servicePanel);
            frameHandler.setDefaultSize(new Dimension(600, 400));
            optionsModule.initialLoadFromPreferences();
            frameHandler.loadMainMenu();
            frameHandler.loadMainToolBar();
            frameHandler.showFrame();
            updateModule.checkOnStart(frameHandler.getFrame());

            xbupServiceModule.openConnectionDialog(frameHandler.getFrame());
        } catch (ParseException ex) {
            Logger.getLogger(ManagerLauncherModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
