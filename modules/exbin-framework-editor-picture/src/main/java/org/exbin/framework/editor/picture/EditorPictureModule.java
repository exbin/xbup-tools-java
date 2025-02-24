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
package org.exbin.framework.editor.picture;

import org.exbin.framework.editor.picture.action.PropertiesAction;
import org.exbin.framework.editor.picture.action.PictureOperationActions;
import org.exbin.framework.editor.picture.action.ZoomControlActions;
import org.exbin.framework.editor.picture.action.ToolColorAction;
import org.exbin.framework.editor.picture.action.PrintAction;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.editor.picture.gui.ImageStatusPanel;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.file.api.FileType;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.action.api.menu.MenuManagement;
import org.exbin.framework.action.api.NextToMode;
import org.exbin.framework.action.api.PositionMode;
import org.exbin.framework.action.api.SeparationMode;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.ActionContextService;
import org.exbin.framework.action.api.menu.MenuContribution;
import org.exbin.framework.action.api.menu.PositionMenuContributionRule;
import org.exbin.framework.action.api.menu.RelativeMenuContributionRule;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.frame.api.FrameModuleApi;

/**
 * Picture editor module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditorPictureModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(EditorPictureModule.class);
    public static final String ZOOM_MODE_SUBMENU_ID = MODULE_ID + ".zoomSubMenu";
    public static final String PICTURE_MENU_ID = MODULE_ID + ".pictureMenu";
    public static final String PICTURE_OPERATION_MENU_ID = MODULE_ID + ".pictureOperationMenu";
    public static final String PICTURE_POPUP_MENU_ID = MODULE_ID + ".picturePopupMenu";

    public static final String IMAGE_STATUS_BAR_ID = "imageStatusBar";

    private EditorProvider editorProvider;
    private ResourceBundle resourceBundle;
    private ImageStatusPanel imageStatusPanel;

    private ZoomControlActions zoomControlActions;
    private PictureOperationActions pictureOperationActions;

    public EditorPictureModule() {
    }

    private void ensureSetup() {
        if (editorProvider == null) {
            getEditorProvider();
        }

        if (resourceBundle == null) {
            getResourceBundle();
        }
    }

    @Nonnull
    public EditorProvider getEditorProvider() {
        if (editorProvider == null) {
            ImageEditorProvider imageEditor = new ImageEditorProvider();

            editorProvider = imageEditor;

            imageEditor.setMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (editorProvider == null) {
                        return;
                    }

                    updateCurrentPosition();
                }
            });
            imageEditor.setPopupMenu(createPopupMenu());
        }

        return editorProvider;
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(EditorPictureModule.class);
        }

        return resourceBundle;
    }

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        String[] formats = ImageIO.getReaderFormatNames();
        for (String ext : formats) {
            if (ext.toLowerCase().equals(ext)) {
                fileModule.addFileType(new PictureFileType(ext));
            }
        }
    }

    private void updateCurrentPosition() {
        if (imageStatusPanel != null) {
            Optional<FileHandler> activeFile = editorProvider.getActiveFile();
            if (!activeFile.isPresent()) {
                throw new IllegalStateException();
            }

            ImagePanel imagePanel = (ImagePanel) activeFile.get().getComponent();
            Point mousePosition = imagePanel.getMousePosition();
            double scale = imagePanel.getScale();
            if (mousePosition != null) {
                imageStatusPanel.setCurrentPosition(new Point((int) (mousePosition.x * scale), (int) (mousePosition.y * scale)));
            }
        }
    }

    public void registerStatusBar() {
        imageStatusPanel = new ImageStatusPanel(new ImageControlApi() {
            @Override
            public void editSelection() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, IMAGE_STATUS_BAR_ID, imageStatusPanel);
        frameModule.switchStatusBar(IMAGE_STATUS_BAR_ID);

        Optional<FileHandler> activeFile = editorProvider.getActiveFile();
        if (!activeFile.isPresent()) {
            return;
        }

        // TODO support for multi editor
        ImagePanel imagePanel = (ImagePanel) activeFile.get().getComponent();
        imagePanel.registerImageStatus(imageStatusPanel);
    }
    
    public void registerUndoHandler() {
        ((ImageEditorProvider) editorProvider).registerUndoHandler();
    }

    public void registerPropertiesMenu() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.FILE_MENU_ID, createPropertiesAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
    }

    public void registerPrintMenu() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.FILE_MENU_ID, MODULE_ID, createPrintAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
    }

    @Nonnull
    private PictureOperationActions getPictureOperationActions() {
        if (pictureOperationActions == null) {
            ensureSetup();
            pictureOperationActions = new PictureOperationActions();
            pictureOperationActions.setup(editorProvider, resourceBundle);
        }

        return pictureOperationActions;
    }

    public void registerOptionsMenuPanels() {
//        getEncodingsHandler();
//        JMenu toolsEncodingMenu = encodingsHandler.getToolsEncodingMenu();
//        encodingsHandler.encodingsRebuild();

//        GuiMenuModuleApi menuModule = App.getModule(GuiMenuModuleApi.class);
//        menuModule.registerMenuItem(ActionConsts.TOOLS_MENU_ID, MODULE_ID, encodingsHandler.getToolsEncodingMenu(), new MenuPosition(PositionMode.TOP_LAST));
    }

    public void registerOptionsPanels() {
//        GuiOptionsModuleApi optionsModule = App.getModule(GuiOptionsModuleApi.class);
//        WaveColorPanelApi textColorPanelFrame = new WaveColorPanelApi() {
//            @Override
//            public Color[] getCurrentWaveColors() {
//                return ((AudioPanel) getEditorProvider()).getAudioPanelColors();
//            }
//
//            @Override
//            public Color[] getDefaultWaveColors() {
//                return ((AudioPanel) getEditorProvider()).getDefaultColors();
//            }
//
//            @Override
//            public void setCurrentWaveColors(Color[] colors) {
//                ((AudioPanel) getEditorProvider()).setAudioPanelColors(colors);
//            }
//        };
    }

    @Nonnull
    private JPopupMenu createPopupMenu() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        menuManagement.registerMenu(PICTURE_POPUP_MENU_ID);
        actionModule.registerClipboardMenuItems(PICTURE_POPUP_MENU_ID, MODULE_ID, SeparationMode.AROUND);
        JPopupMenu popupMenu = new JPopupMenu();
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ActionContextService actionContextService = frameModule.getFrameHandler().getActionContextService();
        menuManagement.buildMenu(popupMenu, PICTURE_POPUP_MENU_ID, actionContextService);
        return popupMenu;
    }

    public void registerToolsOptionsMenuActions() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.TOOLS_MENU_ID, createToolColorAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
    }

    public void registerZoomModeMenu() {
        getZoomControlActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.VIEW_MENU_ID, ZOOM_MODE_SUBMENU_ID, "Zoom");
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
        menuManagement.registerMenu(ZOOM_MODE_SUBMENU_ID);
        menuContribution = menuManagement.registerMenuItem(ZOOM_MODE_SUBMENU_ID, zoomControlActions.createZoomUpAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(ZOOM_MODE_SUBMENU_ID, zoomControlActions.createNormalZoomAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(ZOOM_MODE_SUBMENU_ID, zoomControlActions.createZoomDownAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
    }

    @Nonnull
    private PropertiesAction createPropertiesAction() {
        ensureSetup();
        PropertiesAction propertiesAction = new PropertiesAction();
        propertiesAction.setup(editorProvider, resourceBundle);
        return propertiesAction;
    }

    @Nonnull
    private ToolColorAction createToolColorAction() {
        ensureSetup();
        ToolColorAction toolColorAction = new ToolColorAction();
        toolColorAction.setup(editorProvider, resourceBundle);
        return toolColorAction;
    }

    @Nonnull
    private PrintAction createPrintAction() {
        ensureSetup();
        PrintAction printAction = new PrintAction();
        printAction.setup(editorProvider, resourceBundle);
        return printAction;
    }

    @Nonnull
    private ZoomControlActions getZoomControlActions() {
        if (zoomControlActions == null) {
            ensureSetup();
            zoomControlActions = new ZoomControlActions();
            zoomControlActions.setup(editorProvider, resourceBundle);
        }

        return zoomControlActions;
    }

    public void registerPictureMenu() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        menuManagement.registerMenu(PICTURE_MENU_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.MAIN_MENU_ID, PICTURE_MENU_ID, "Picture");
        menuManagement.registerMenuRule(menuContribution, new RelativeMenuContributionRule(NextToMode.AFTER, ActionConsts.VIEW_MENU_ID));
    }

    public void registerPictureOperationMenu() {
        getPictureOperationActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(PICTURE_MENU_ID, pictureOperationActions.createRevertAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
    }
}
