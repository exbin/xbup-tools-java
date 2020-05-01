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
package org.exbin.xbup.tool.xbmanager;

import java.awt.Dimension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.XBBaseApplication;
import org.exbin.framework.api.Preferences;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.framework.gui.about.api.GuiAboutModuleApi;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.options.api.GuiOptionsModuleApi;
import org.exbin.framework.gui.service.ServiceManagerModule;
import org.exbin.framework.api.XBApplicationModuleRepository;
import org.exbin.framework.gui.link.api.GuiLinkModuleApi;
import org.exbin.framework.gui.update.api.GuiUpdateModuleApi;
import org.exbin.framework.gui.utils.LanguageUtils;

/**
 * The main class of the XBManager application.
 *
 * @version 0.2.0 2016/07/16
 * @author ExBin Project (http://exbin.org)
 */
public class XBManager {

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        final ResourceBundle bundle = LanguageUtils.getResourceBundleByClass(XBManager.class);
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
            } else {
                boolean verboseMode = cl.hasOption("v");
                boolean devMode = cl.hasOption("dev");
                Logger logger = Logger.getLogger("");
                try {
                    logger.setLevel(Level.ALL);
                    logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                } catch (java.security.AccessControlException ex) {
                    // Ignore it in java webstart
                }

                XBBaseApplication app = new XBBaseApplication();
                Preferences preferences = app.createPreferences(XBManager.class);
                app.setAppBundle(bundle, LanguageUtils.getResourceBaseNameBundleByClass(XBManager.class));

                XBApplicationModuleRepository moduleRepository = app.getModuleRepository();
                moduleRepository.addClassPathModules();
                moduleRepository.addModulesFromManifest(XBManager.class);
                moduleRepository.loadModulesFromPath(new File("plugins").toURI());
                moduleRepository.initModules();
                app.init();

                GuiFrameModuleApi frameModule = moduleRepository.getModuleByInterface(GuiFrameModuleApi.class);
                GuiMenuModuleApi menuModule = moduleRepository.getModuleByInterface(GuiMenuModuleApi.class);
                GuiAboutModuleApi aboutModule = moduleRepository.getModuleByInterface(GuiAboutModuleApi.class);
                GuiLinkModuleApi linkModule = moduleRepository.getModuleByInterface(GuiLinkModuleApi.class);
                GuiOptionsModuleApi optionsModule = moduleRepository.getModuleByInterface(GuiOptionsModuleApi.class);
                ServiceManagerModule serviceManagerModule = moduleRepository.getModuleByInterface(ServiceManagerModule.class);
                GuiUpdateModuleApi updateModule = moduleRepository.getModuleByInterface(GuiUpdateModuleApi.class);

                frameModule.createMainMenu();
                try {
                    updateModule.setUpdateUrl(new URL(bundle.getString("update_url")));
                    updateModule.setUpdateDownloadUrl(new URL(bundle.getString("update_download_url")));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(XBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateModule.registerDefaultMenuItem();
                aboutModule.registerDefaultMenuItem();
                try {
                    linkModule.setOnlineHelpUrl(new URL(bundle.getString("online_help_url")));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(XBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                linkModule.registerOnlineHelpMenu();

                frameModule.registerExitAction();
                frameModule.registerStatusBarVisibilityActions();

                menuModule.registerMenuClipboardActions();

                optionsModule.registerMenuAction();

                updateModule.registerOptionsPanels();

                ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();

                serviceManagerModule.setPreferences(preferences);
                JPanel servicePanel = serviceManagerModule.getServicePanel();
                frameHandler.setMainPanel(servicePanel);
                frameHandler.setDefaultSize(new Dimension(600, 400));
                optionsModule.initialLoadFromPreferences();
                frameHandler.show();
                updateModule.checkOnStart(frameHandler.getFrame());

                serviceManagerModule.openConnectionDialog(frameHandler.getFrame());
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
