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
package org.exbin.xbup.tool.xbteditor;

import java.awt.Dimension;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.XBBaseApplication;
import org.exbin.framework.api.Preferences;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.framework.editor.text.EditorTextModule;
import org.exbin.framework.editor.text.gui.TextPanel;
import org.exbin.framework.gui.about.api.GuiAboutModuleApi;
import org.exbin.framework.gui.editor.api.GuiEditorModuleApi;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.options.api.GuiOptionsModuleApi;
import org.exbin.framework.gui.undo.api.GuiUndoModuleApi;
import org.exbin.framework.api.XBApplicationModuleRepository;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.action.api.GuiActionModuleApi;

/**
 * The main class of the XBTEditor application.
 *
 * @version 0.2.0 2016/11/30
 * @author ExBin Project (http://exbin.org)
 */
public class XBTEditor {

    private static boolean verboseMode = false;
    private static boolean devMode = false;
    private static ResourceBundle bundle;

    private XBTEditor() {
    }

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        try {
            bundle = LanguageUtils.getResourceBundleByClass(XBTEditor.class);
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
                verboseMode = cl.hasOption("v");
                devMode = cl.hasOption("dev");
                Logger logger = Logger.getLogger("");
                try {
                    logger.setLevel(Level.ALL);
                    logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                } catch (java.security.AccessControlException ex) {
                    // Ignore it in java webstart
                }

                XBBaseApplication app = new XBBaseApplication();
                Preferences preferences = app.createPreferences(XBTEditor.class);
                app.setAppBundle(bundle, LanguageUtils.getResourceBaseNameBundleByClass(XBTEditor.class));

                XBApplicationModuleRepository moduleRepository = app.getModuleRepository();
                moduleRepository.addClassPathModules();
                moduleRepository.addModulesFromManifest(XBTEditor.class);
                moduleRepository.initModules();
                app.init();

                GuiFrameModuleApi frameModule = moduleRepository.getModuleByInterface(GuiFrameModuleApi.class);
                GuiEditorModuleApi editorModule = moduleRepository.getModuleByInterface(GuiEditorModuleApi.class);
                GuiActionModuleApi actionModule = moduleRepository.getModuleByInterface(GuiActionModuleApi.class);
                GuiAboutModuleApi aboutModule = moduleRepository.getModuleByInterface(GuiAboutModuleApi.class);
                GuiUndoModuleApi undoModule = moduleRepository.getModuleByInterface(GuiUndoModuleApi.class);
                GuiFileModuleApi fileModule = moduleRepository.getModuleByInterface(GuiFileModuleApi.class);
                GuiOptionsModuleApi optionsModule = moduleRepository.getModuleByInterface(GuiOptionsModuleApi.class);
                EditorTextModule textEditorModule = moduleRepository.getModuleByInterface(EditorTextModule.class);

                frameModule.createMainMenu();
                aboutModule.registerDefaultMenuItem();

                frameModule.registerExitAction();
                frameModule.registerBarsVisibilityActions();

                // Register clipboard editing actions
                fileModule.registerMenuFileHandlingActions();
                fileModule.registerToolBarFileHandlingActions();
                fileModule.registerLastOpenedMenuActions();
                fileModule.registerCloseListener();

                undoModule.registerMainMenu();
                undoModule.registerMainToolBar();

                // Register clipboard editing actions
                actionModule.registerMenuClipboardActions();
                actionModule.registerToolBarClipboardActions();

                optionsModule.registerMenuAction();

                textEditorModule.registerFileTypes();
                textEditorModule.registerEditFindMenuActions();
                textEditorModule.registerEditFindToolBarActions();
                textEditorModule.registerToolsOptionsMenuActions();
                textEditorModule.registerOptionsMenuPanels();
                textEditorModule.registerWordWrapping();
                textEditorModule.registerGoToLine();
                textEditorModule.registerPropertiesMenu();
                textEditorModule.registerPrintMenu();

                ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();
                TextPanel textPanel = (TextPanel) textEditorModule.getEditorProvider();
                editorModule.registerEditor("text", textPanel);
                editorModule.registerUndoHandler();
                textEditorModule.registerStatusBar();
                textEditorModule.registerOptionsPanels();

                textEditorModule.loadFromPreferences(preferences);

                frameHandler.setMainPanel(editorModule.getEditorPanel());
                frameHandler.setDefaultSize(new Dimension(600, 400));
                optionsModule.initialLoadFromPreferences();
                frameHandler.show();

                List fileArgs = cl.getArgList();
                if (fileArgs.size() > 0) {
                    fileModule.loadFromFile((String) fileArgs.get(0));
                }
            }
        } catch (ParseException | RuntimeException ex) {
            Logger.getLogger(XBTEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
