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
package org.exbin.framework.editor.wave;

import org.exbin.framework.editor.wave.action.EditToolActions;
import org.exbin.framework.editor.wave.action.WaveColorAction;
import org.exbin.framework.editor.wave.action.ZoomControlActions;
import org.exbin.framework.editor.wave.action.AudioOperationActions;
import org.exbin.framework.editor.wave.action.AudioControlActions;
import org.exbin.framework.editor.wave.action.PropertiesAction;
import org.exbin.framework.editor.wave.action.DrawingControlActions;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.editor.wave.gui.AudioPanel;
import org.exbin.framework.editor.wave.gui.AudioStatusPanel;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.editor.wave.service.impl.WaveColorServiceImpl;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.action.api.ActionContextService;
import org.exbin.framework.contribution.api.GroupSequenceContributionRule;
import org.exbin.framework.contribution.api.PositionSequenceContributionRule;
import org.exbin.framework.contribution.api.RelativeSequenceContributionRule;
import org.exbin.framework.contribution.api.SeparationSequenceContributionRule;
import org.exbin.framework.contribution.api.SequenceContribution;
import org.exbin.framework.menu.api.MenuManagement;
import org.exbin.framework.editor.wave.settings.AudioDevicesSettingsComponent;
import org.exbin.framework.editor.wave.settings.AudioDevicesOptions;
import org.exbin.framework.editor.wave.settings.WaveColorSettingsComponent;
import org.exbin.framework.editor.wave.settings.WaveColorOptions;
import org.exbin.framework.editor.wave.service.WaveColorService;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.menu.api.MenuModuleApi;
import org.exbin.framework.options.settings.api.OptionsSettingsManagement;
import org.exbin.framework.options.settings.api.OptionsSettingsModuleApi;

/**
 * Audio editor module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditorWaveModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(EditorWaveModule.class);
    public static final String AUDIO_SUBMENU_ID = MODULE_ID + ".audioMenu";
    public static final String AUDIO_OPERATION_SUBMENU_ID = MODULE_ID + ".audioOperationMenu";
    public static final String AUDIO_POPUP_MENU_ID = MODULE_ID + ".audioPopupMenu";
    public static final String DRAW_MODE_SUBMENU_ID = MODULE_ID + ".drawSubMenu";
    public static final String ZOOM_MODE_SUBMENU_ID = MODULE_ID + ".zoomSubMenu";
    public static final String TOOLS_SELECTION_MENU_GROUP_ID = MODULE_ID + ".toolsSelectionMenuGroup";

    public static final String WAVE_STATUS_BAR_ID = "waveStatusBar";

    private AudioEditorProvider editorProvider;
    private ResourceBundle resourceBundle;
    private AudioStatusPanel audioStatusPanel;
    private boolean playing = false;
    private WaveColorServiceImpl waveColorService = new WaveColorServiceImpl();

    private AudioControlActions audioControlActions;
    private DrawingControlActions drawingControlActions;
    private EditToolActions editToolActions;
    private ZoomControlActions zoomControlActions;
    private AudioOperationActions audioOperationActions;

    public EditorWaveModule() {
    }

    private void ensureSetup() {
        if (editorProvider == null) {
            // getEditorProvider();
        }

        if (resourceBundle == null) {
            getResourceBundle();
        }
    }

    @Nonnull
    public void setEditorProvider(AudioEditorProvider editorProvider) {
        this.editorProvider = editorProvider;
        waveColorService.setEditorProvider(editorProvider);

        editorProvider.setStatusChangeListener(this::updateStatus);
        editorProvider.setWaveRepaintListener(this::updatePositionTime);

        editorProvider.setMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (editorProvider == null) {
                    return;
                }

                updatePositionTime();
            }
        });

        editorProvider.setPopupMenu(createPopupMenu());
    }

    public void registerUndoHandler() {
        editorProvider.registerUndoHandler();
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(EditorWaveModule.class);
        }

        return resourceBundle;
    }

    @Nonnull
    public WaveColorService getWaveColorService() {
        return waveColorService;
    }

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);

        String[] formats = new String[]{"wav", "aiff", "au"};
        for (String ext : formats) {
            if (ext.toLowerCase().equals(ext)) {
                fileModule.addFileType(new AudioFileType(ext));
            }
        }
    }

    private void updatePositionTime() {
        Optional<FileHandler> activeFile = editorProvider.getActiveFile();
        if (!activeFile.isPresent()) {
            return;
        }

        AudioPanel audioPanel = (AudioPanel) activeFile.get().getComponent();
        audioStatusPanel.setCurrentTime(audioPanel.getPositionTime());
    }

    private void updateStatus() {
        updatePositionTime();

        Optional<FileHandler> activeFile = editorProvider.getActiveFile();
        if (!activeFile.isPresent()) {
            return;
        }

        AudioPanel audioPanel = (AudioPanel) activeFile.get().getComponent();
        if (audioPanel.getIsPlaying() != playing) {
            playing = !playing;
            audioStatusPanel.setPlayButtonIcon(playing
                    ? new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/wave/resources/images/actions/pause16.png"))
                    : new javax.swing.ImageIcon(getClass().getResource("/org/exbin/framework/editor/wave/resources/images/actions/play16.png"))
            );
        }
    }

    public void registerStatusBar() {
        audioStatusPanel = new AudioStatusPanel(new AudioControlApi() {
            @Override
            public void performPlay() {
                Optional<FileHandler> activeFile = editorProvider.getActiveFile();
                if (!activeFile.isPresent()) {
                    throw new IllegalStateException();
                }

                AudioPanel audioPanel = (AudioPanel) activeFile.get().getComponent();
                audioPanel.performPlay();
            }

            @Override
            public void performStop() {
                Optional<FileHandler> activeFile = editorProvider.getActiveFile();
                if (!activeFile.isPresent()) {
                    throw new IllegalStateException();
                }

                AudioPanel audioPanel = (AudioPanel) activeFile.get().getComponent();
                audioPanel.performStop();
            }

            @Override
            public void setVolume(int volumeLevel) {
                Optional<FileHandler> activeFile = editorProvider.getActiveFile();
                if (!activeFile.isPresent()) {
                    throw new IllegalStateException();
                }

                AudioPanel audioPanel = (AudioPanel) activeFile.get().getComponent();
                audioPanel.setVolume(volumeLevel);
            }
        });

        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        frameModule.registerStatusBar(MODULE_ID, WAVE_STATUS_BAR_ID, audioStatusPanel);
        frameModule.switchStatusBar(WAVE_STATUS_BAR_ID);
    }

    public void registerSettings() {
        OptionsSettingsModuleApi settingsModule = App.getModule(OptionsSettingsModuleApi.class);
        OptionsSettingsManagement settingsManagement = settingsModule.getMainSettingsManager();
        
        settingsManagement.registerOptionsSettings(AudioDevicesOptions.class, (optionsStorage) -> new AudioDevicesOptions(optionsStorage));
        settingsManagement.registerOptionsSettings(WaveColorOptions.class, (optionsStorage) -> new WaveColorOptions(optionsStorage));

        /* OptionsGroup waveEditorGroup = settingsModule.createOptionsGroup("waveEditor", resourceBundle);
        settingsManagement.registerGroup(waveEditorGroup);
        settingsManagement.registerGroupRule(waveEditorGroup, new ParentOptionsGroupRule("editor"));

        OptionsGroup waveEditorColorGroup = settingsModule.createOptionsGroup("waveEditorColor", resourceBundle);
        settingsManagement.registerGroup(waveEditorColorGroup);
        settingsManagement.registerGroupRule(waveEditorColorGroup, new ParentOptionsGroupRule(waveEditorGroup));

        WaveColorSettingsComponent waveColorOptionsPage = new WaveColorSettingsComponent();
        waveColorOptionsPage.setWaveColorService(waveColorService);
        settingsManagement.registerPage(waveColorOptionsPage);
        settingsManagement.registerPageRule(waveColorOptionsPage, new GroupOptionsPageRule(waveEditorColorGroup));

        OptionsGroup waveEditorDeviceGroup = settingsModule.createOptionsGroup("waveEditorDevice", resourceBundle);
        settingsManagement.registerGroup(waveEditorDeviceGroup);
        settingsManagement.registerGroupRule(waveEditorDeviceGroup, new ParentOptionsGroupRule(waveEditorGroup));
        AudioDevicesSettingsComponent audioDevicesOptionsPage = new AudioDevicesSettingsComponent();
        settingsManagement.registerPage(audioDevicesOptionsPage);
        settingsManagement.registerPageRule(audioDevicesOptionsPage, new GroupOptionsPageRule(waveEditorDeviceGroup)); */
    }

    public void registerToolsOptionsMenuActions() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement menuManagement = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.TOOLS_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(getWaveColorAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.MIDDLE));
    }

    public void registerToolsMenuActions() {
        EditToolActions actions = getEditToolActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement menuManagement = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.TOOLS_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuGroup(TOOLS_SELECTION_MENU_GROUP_ID);
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuManagement.registerMenuRule(menuContribution, new SeparationSequenceContributionRule(SeparationSequenceContributionRule.SeparationMode.AROUND));
        menuContribution = menuManagement.registerMenuItem(actions.createSelectionToolAction());
        menuManagement.registerMenuRule(menuContribution, new GroupSequenceContributionRule(TOOLS_SELECTION_MENU_GROUP_ID));
        menuContribution = menuManagement.registerMenuItem(actions.createPencilToolAction());
        menuManagement.registerMenuRule(menuContribution, new GroupSequenceContributionRule(TOOLS_SELECTION_MENU_GROUP_ID));
    }

    public AudioStatusPanel getAudioStatusPanel() {
        return audioStatusPanel;
    }

    @Nonnull
    private PropertiesAction createPropertiesAction() {
        ensureSetup();
        PropertiesAction propertiesAction = new PropertiesAction();
        propertiesAction.setup(resourceBundle);
        return propertiesAction;
    }

    @Nonnull
    private AudioControlActions getAudioControlActions() {
        if (audioControlActions == null) {
            ensureSetup();
            audioControlActions = new AudioControlActions();
            audioControlActions.setup(resourceBundle);
        }

        return audioControlActions;
    }

    @Nonnull
    private AudioOperationActions getAudioOperationActions() {
        if (audioOperationActions == null) {
            ensureSetup();
            audioOperationActions = new AudioOperationActions();
            audioOperationActions.setup(resourceBundle);
        }

        return audioOperationActions;
    }

    @Nonnull
    private DrawingControlActions getDrawingControlActions() {
        if (drawingControlActions == null) {
            ensureSetup();
            drawingControlActions = new DrawingControlActions();
            drawingControlActions.setup(resourceBundle);
        }

        return drawingControlActions;
    }

    @Nonnull
    private EditToolActions getEditToolActions() {
        if (editToolActions == null) {
            ensureSetup();
            editToolActions = new EditToolActions();
            editToolActions.setup(resourceBundle);
        }

        return editToolActions;
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

    @Nonnull
    private WaveColorAction getWaveColorAction() {
        ensureSetup();
        WaveColorAction waveColorAction = new WaveColorAction();
        waveColorAction.setup(resourceBundle);
        return waveColorAction;
    }

    public void registerPropertiesMenu() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement menuManagement = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(createPropertiesAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }

    public void registerAudioMenu() {
        getAudioControlActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement menuManagement = menuModule.getMainMenuManagement(MODULE_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(AUDIO_SUBMENU_ID, "Audio");
        menuManagement.registerMenuRule(menuContribution, new RelativeSequenceContributionRule(RelativeSequenceContributionRule.NextToMode.AFTER, MenuModuleApi.VIEW_SUBMENU_ID));
        menuManagement = menuManagement.getSubMenu(AUDIO_SUBMENU_ID);
        menuContribution = menuManagement.registerMenuItem(audioControlActions.createPlayAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(audioControlActions.createStopAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void registerAudioOperationMenu() {
        getAudioOperationActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement menuManagement = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(AUDIO_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(AUDIO_OPERATION_SUBMENU_ID, "Operation");
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
        menuManagement = menuManagement.getSubMenu(AUDIO_OPERATION_SUBMENU_ID);
        menuContribution = menuManagement.registerMenuItem(audioOperationActions.createReverseAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void registerDrawingModeMenu() {
        getDrawingControlActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement menuManagement = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.VIEW_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(DRAW_MODE_SUBMENU_ID, "Draw Mode");
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }

    public void registerZoomModeMenu() {
        getZoomControlActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement menuManagement = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.VIEW_SUBMENU_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(ZOOM_MODE_SUBMENU_ID, "Zoom");
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
        menuManagement = menuManagement.getSubMenu(ZOOM_MODE_SUBMENU_ID);
        menuContribution = menuManagement.registerMenuItem(zoomControlActions.createZoomUpAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(zoomControlActions.createNormalZoomAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(zoomControlActions.createZoomDownAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void bindZoomScrollWheel() {
        // ((AudioPanel) getEditorProvider()).
    }

    private JPopupMenu createPopupMenu() {
        getAudioControlActions();
        getDrawingControlActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        menuModule.registerMenu(AUDIO_POPUP_MENU_ID, MODULE_ID);
        MenuManagement menuManagement = menuModule.getMenuManagement(AUDIO_POPUP_MENU_ID, MODULE_ID);
        SequenceContribution menuContribution = menuManagement.registerMenuItem(audioControlActions.createPlayAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(audioControlActions.createStopAction());
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));

        menuModule.registerClipboardMenuItems(AUDIO_POPUP_MENU_ID, null, MODULE_ID, SeparationSequenceContributionRule.SeparationMode.AROUND);

        menuContribution = menuManagement.registerMenuItem(DRAW_MODE_SUBMENU_ID, "Draw Mode");
        menuManagement.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
        MenuManagement subMgmt = menuManagement.getSubMenu(DRAW_MODE_SUBMENU_ID);
        menuContribution = subMgmt.registerMenuItem(DRAW_MODE_SUBMENU_ID, drawingControlActions.createDotsModeAction());
        subMgmt.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = subMgmt.registerMenuItem(DRAW_MODE_SUBMENU_ID, drawingControlActions.createLineModeAction());
        subMgmt.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        menuContribution = subMgmt.registerMenuItem(DRAW_MODE_SUBMENU_ID, drawingControlActions.createIntegralModeAction());
        subMgmt.registerMenuRule(menuContribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));

        JPopupMenu popupMenu = new JPopupMenu();
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ActionContextService actionContextService = frameModule.getFrameHandler().getActionContextService();
        menuModule.buildMenu(popupMenu, AUDIO_POPUP_MENU_ID, actionContextService);
        return popupMenu;
    }
}
