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
package org.exbin.xbup.tool.xbpeditor;

import java.awt.Dimension;
import java.io.File;
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
import org.exbin.framework.editor.picture.EditorPictureModule;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.gui.about.api.GuiAboutModuleApi;
import org.exbin.framework.gui.editor.api.GuiEditorModuleApi;
import org.exbin.framework.gui.file.api.GuiFileModuleApi;
import org.exbin.framework.gui.frame.api.ApplicationFrameHandler;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.menu.api.GuiMenuModuleApi;
import org.exbin.framework.gui.options.api.GuiOptionsModuleApi;
import org.exbin.framework.gui.undo.api.GuiUndoModuleApi;
import org.exbin.xbup.operation.undo.XBTLinearUndo;
import org.exbin.xbup.operation.undo.XBUndoUpdateListener;
import org.exbin.framework.api.XBApplicationModuleRepository;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.xbup.operation.Command;

/**
 * The main class of the XBPEditor application.
 *
 * @version 0.2.0 2016/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBPEditor {

    private static boolean verboseMode = false;
    private static boolean devMode = false;
    private static ResourceBundle bundle;

    private XBPEditor() {
    }

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        try {
            bundle = LanguageUtils.getResourceBundleByClass(XBPEditor.class);
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
                Preferences preferences = app.createPreferences(XBPEditor.class);
                app.setAppBundle(bundle, LanguageUtils.getResourceBaseNameBundleByClass(XBPEditor.class));

                XBApplicationModuleRepository moduleRepository = app.getModuleRepository();
                moduleRepository.addClassPathModules();
                moduleRepository.addModulesFromManifest(XBPEditor.class);
                moduleRepository.loadModulesFromPath(new File("plugins").toURI());
                moduleRepository.initModules();
                app.init();

                GuiFrameModuleApi frameModule = moduleRepository.getModuleByInterface(GuiFrameModuleApi.class);
                GuiEditorModuleApi editorModule = moduleRepository.getModuleByInterface(GuiEditorModuleApi.class);
                GuiMenuModuleApi menuModule = moduleRepository.getModuleByInterface(GuiMenuModuleApi.class);
                GuiAboutModuleApi aboutModule = moduleRepository.getModuleByInterface(GuiAboutModuleApi.class);
                GuiUndoModuleApi undoModule = moduleRepository.getModuleByInterface(GuiUndoModuleApi.class);
                GuiFileModuleApi fileModule = moduleRepository.getModuleByInterface(GuiFileModuleApi.class);
                GuiOptionsModuleApi optionsModule = moduleRepository.getModuleByInterface(GuiOptionsModuleApi.class);
                final EditorPictureModule pictureEditorModule = moduleRepository.getModuleByInterface(EditorPictureModule.class);

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
                undoModule.registerUndoManagerInMainMenu();
                XBTLinearUndo linearUndo = new XBTLinearUndo(null);
                linearUndo.addUndoUpdateListener(new XBUndoUpdateListener() {
                    @Override
                    public void undoCommandPositionChanged() {
                        ((ImagePanel) pictureEditorModule.getEditorProvider()).repaint();
                    }

                    @Override
                    public void undoCommandAdded(Command command) {
                        ((ImagePanel) pictureEditorModule.getEditorProvider()).repaint();
                    }
                });
                undoModule.setUndoHandler(linearUndo);

                // Register clipboard editing actions
                menuModule.registerMenuClipboardActions();
                menuModule.registerToolBarClipboardActions();

                optionsModule.registerMenuAction();

                pictureEditorModule.registerFileTypes();
                pictureEditorModule.registerToolsOptionsMenuActions();
                pictureEditorModule.registerOptionsMenuPanels();
                pictureEditorModule.registerPropertiesMenu();
                pictureEditorModule.registerPrintMenu();
                pictureEditorModule.registerZoomModeMenu();
                pictureEditorModule.registerPictureMenu();
                pictureEditorModule.registerPictureOperationMenu();

                ApplicationFrameHandler frameHandler = frameModule.getFrameHandler();
                ImagePanel imagePanel = (ImagePanel) pictureEditorModule.getEditorProvider();
                editorModule.registerEditor("picture", imagePanel);
//                editorModule.registerUndoHandler();
                pictureEditorModule.registerStatusBar();
                pictureEditorModule.registerOptionsPanels();

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
            Logger.getLogger(XBPEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
