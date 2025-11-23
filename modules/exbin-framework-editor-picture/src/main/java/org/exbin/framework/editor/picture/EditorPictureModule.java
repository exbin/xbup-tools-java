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
import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.action.api.ActionContextRegistration;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.editor.picture.gui.ImageStatusPanel;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.menu.api.MenuDefinitionManagement;
import org.exbin.framework.contribution.api.PositionSequenceContributionRule;
import org.exbin.framework.contribution.api.RelativeSequenceContributionRule;
import org.exbin.framework.contribution.api.SeparationSequenceContributionRule;
import org.exbin.framework.contribution.api.SequenceContribution;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.menu.api.MenuModuleApi;

/**
 * Picture editor module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditorPictureModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(EditorPictureModule.class);
    public static final String ZOOM_MODE_SUBMENU_ID = MODULE_ID + ".zoomSubMenu";
    public static final String PICTURE_SUBMENU_ID = MODULE_ID + ".pictureMenu";
    public static final String PICTURE_OPERATION_MENU_ID = MODULE_ID + ".pictureOperationMenu";
    public static final String PICTURE_POPUP_MENU_ID = MODULE_ID + ".picturePopupMenu";

    public static final String IMAGE_STATUS_BAR_ID = "imageStatusBar";

    private ResourceBundle resourceBundle;
    private ImageStatusPanel imageStatusPanel;

    private ZoomControlActions zoomControlActions;
    private PictureOperationActions pictureOperationActions;

    public EditorPictureModule() {
    }

    private void ensureSetup() {
        if (resourceBundle == null) {
            getResourceBundle();
        }
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(EditorPictureModule.class);
        }

        return resourceBundle;
    }

    /* @Nonnull
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
    } */

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        String[] formats = ImageIO.getReaderFormatNames();
        for (String ext : formats) {
            if (ext.toLowerCase().equals(ext)) {
                fileModule.addFileType(new PictureFileType(ext));
            }
        }
    }

    /* private void updateCurrentPosition() {
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
    } */

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

        /*Optional<FileHandler> activeFile = editorProvider.getActiveFile();
        if (!activeFile.isPresent()) {
            return;
        }

        // TODO support for multi editor
        ImagePanel imagePanel = (ImagePanel) activeFile.get().getComponent();
        imagePanel.registerImageStatus(imageStatusPanel); */
    }

    public void registerUndoHandler() {
        // TODO ((ImageEditorProvider) editorProvider).registerUndoHandler();
    }

    public void registerPropertiesMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement menuManagement = menuModule.getMainMenuManager(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(createPropertiesAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }

    public void registerPrintMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement menuManagement = menuModule.getMainMenuManager(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(createPrintAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }

    @Nonnull
    private PictureOperationActions getPictureOperationActions() {
        if (pictureOperationActions == null) {
            ensureSetup();
            pictureOperationActions = new PictureOperationActions();
            pictureOperationActions.setup(resourceBundle);
        }

        return pictureOperationActions;
    }

    public void registerOptionsMenuPanels() {
//        getEncodingsHandler();
//        JMenu toolsEncodingMenu = encodingsHandler.getToolsEncodingMenu();
//        encodingsHandler.encodingsRebuild();

//        GuiMenuModuleApi menuModule = App.getModule(GuiMenuModuleApi.class);
//        menuModule.registerMenuItem(MenuModuleApi.TOOLS_SUBMENU_ID, MODULE_ID, encodingsHandler.getToolsEncodingMenu(), new MenuPosition(PositionMode.TOP_LAST));
    }

    public void registerSettings() {
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
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        menuModule.registerMenu(PICTURE_POPUP_MENU_ID, MODULE_ID);
        MenuDefinitionManagement menuManagement = menuModule.getMenuManager(PICTURE_POPUP_MENU_ID, MODULE_ID);
        menuModule.registerClipboardMenuItems(PICTURE_POPUP_MENU_ID, null, MODULE_ID, SeparationSequenceContributionRule.SeparationMode.AROUND);
        JPopupMenu popupMenu = new JPopupMenu();
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        ActionContextRegistration actionContextRegistrar = actionModule.createActionContextRegistrar(frameModule.getFrameHandler().getActionManager());
        menuModule.buildMenu(popupMenu, PICTURE_POPUP_MENU_ID, actionContextRegistrar);
        return popupMenu;
    }

    public void registerToolsOptionsMenuActions() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement menuManagement = menuModule.getMainMenuManager(MODULE_ID).getSubMenu(MenuModuleApi.TOOLS_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(createToolColorAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void registerZoomModeMenu() {
        getZoomControlActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement mgmt = menuModule.getMainMenuManager(MODULE_ID).getSubMenu(MenuModuleApi.VIEW_SUBMENU_ID);
        SequenceContribution menuContribution = mgmt.registerMenuItem(ZOOM_MODE_SUBMENU_ID, "Zoom");
        mgmt.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
        mgmt = mgmt.getSubMenu(ZOOM_MODE_SUBMENU_ID);
        menuContribution = mgmt.registerMenuItem(zoomControlActions.createZoomUpAction());
        mgmt.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = mgmt.registerMenuItem(zoomControlActions.createNormalZoomAction());
        mgmt.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = mgmt.registerMenuItem(zoomControlActions.createZoomDownAction());
        mgmt.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    @Nonnull
    private PropertiesAction createPropertiesAction() {
        ensureSetup();
        PropertiesAction propertiesAction = new PropertiesAction();
        propertiesAction.setup(resourceBundle);
        return propertiesAction;
    }

    @Nonnull
    private ToolColorAction createToolColorAction() {
        ensureSetup();
        ToolColorAction toolColorAction = new ToolColorAction();
        toolColorAction.setup(resourceBundle);
        return toolColorAction;
    }

    @Nonnull
    private PrintAction createPrintAction() {
        ensureSetup();
        PrintAction printAction = new PrintAction();
        printAction.setup(resourceBundle);
        return printAction;
    }

    @Nonnull
    private ZoomControlActions getZoomControlActions() {
        if (zoomControlActions == null) {
            ensureSetup();
            zoomControlActions = new ZoomControlActions();
            zoomControlActions.setup(resourceBundle);
        }

        return zoomControlActions;
    }

    public void registerPictureMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement mgmt = menuModule.getMenuManager(PICTURE_SUBMENU_ID, MODULE_ID);
        SequenceContribution menuContribution = mgmt.registerMenuItem(PICTURE_SUBMENU_ID, "Picture");
        mgmt.registerMenuRule(menuContribution, new RelativeSequenceContributionRule(RelativeSequenceContributionRule.NextToMode.AFTER, MenuModuleApi.VIEW_SUBMENU_ID));
//        mgmt.registerMenu(PICTURE_SUBMENU_ID);
    }

    public void registerPictureOperationMenu() {
        getPictureOperationActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement menuManagement = menuModule.getMenuManager(PICTURE_SUBMENU_ID, MODULE_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(pictureOperationActions.createRevertAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }
}
