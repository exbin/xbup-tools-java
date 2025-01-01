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
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import org.exbin.framework.App;
import org.exbin.framework.preferences.api.Preferences;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.editor.wave.options.impl.AudioDevicesOptionsImpl;
import org.exbin.framework.editor.wave.options.impl.WaveColorOptionsImpl;
import org.exbin.framework.editor.wave.options.gui.AudioDevicesOptionsPanel;
import org.exbin.framework.editor.wave.gui.AudioPanel;
import org.exbin.framework.editor.wave.gui.AudioStatusPanel;
import org.exbin.framework.editor.wave.options.gui.WaveColorOptionsPanel;
import org.exbin.framework.editor.wave.preferences.AudioDevicesPreferences;
import org.exbin.framework.editor.wave.preferences.WaveColorPreferences;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.file.api.FileType;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.action.api.NextToMode;
import org.exbin.framework.action.api.PositionMode;
import org.exbin.framework.action.api.SeparationMode;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.editor.wave.service.WaveColorService;
import org.exbin.framework.editor.wave.service.impl.WaveColorServiceImpl;
import org.exbin.framework.options.api.DefaultOptionsPage;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.ComponentActivationService;
import org.exbin.framework.action.api.GroupMenuContributionRule;
import org.exbin.framework.action.api.MenuContribution;
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.action.api.PositionMenuContributionRule;
import org.exbin.framework.action.api.RelativeMenuContributionRule;
import org.exbin.framework.action.api.SeparationMenuContributionRule;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.options.api.OptionsComponent;

/**
 * Audio editor module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditorWaveModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(EditorWaveModule.class);
    public static final String AUDIO_MENU_ID = MODULE_ID + ".audioMenu";
    public static final String AUDIO_OPERATION_MENU_ID = MODULE_ID + ".audioOperationMenu";
    public static final String AUDIO_POPUP_MENU_ID = MODULE_ID + ".audioPopupMenu";
    public static final String DRAW_MODE_SUBMENU_ID = MODULE_ID + ".drawSubMenu";
    public static final String ZOOM_MODE_SUBMENU_ID = MODULE_ID + ".zoomSubMenu";
    public static final String TOOLS_SELECTION_MENU_GROUP_ID = MODULE_ID + ".toolsSelectionMenuGroup";

    public static final String XBS_FILE_TYPE = "XBWaveEditor.XBSFileFilter";

    public static final String WAVE_STATUS_BAR_ID = "waveStatusBar";

    private AudioEditor editorProvider;
    private ResourceBundle resourceBundle;
    private AudioStatusPanel audioStatusPanel;
    private boolean playing = false;

    private AudioControlActions audioControlActions;
    private DrawingControlActions drawingControlActions;
    private EditToolActions editToolActions;
    private ZoomControlActions zoomControlActions;
    private AudioOperationActions audioOperationActions;

    public EditorWaveModule() {
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
            AudioEditor audioEditor = new AudioEditor();

            editorProvider = audioEditor;

            audioEditor.setStatusChangeListener(this::updateStatus);
            audioEditor.setWaveRepaintListener(this::updatePositionTime);

            audioEditor.setMouseMotionListener(new MouseMotionListener() {

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

            audioEditor.setPopupMenu(createPopupMenu());
        }

        return editorProvider;
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

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);

        String[] formats = new String[]{"wav", "aiff", "au"};
        for (String ext : formats) {
            if (ext.toLowerCase().equals(ext)) {
                fileModule.addFileType(new AudioFileType(ext));
            }
        }

        fileModule.addFileType(new XBSFileType());
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

    public void registerOptionsPanels() {
        OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
        WaveColorService waveColorService = new WaveColorServiceImpl(getEditorProvider());

        optionsModule.addOptionsPage(new DefaultOptionsPage<WaveColorOptionsImpl>() {
            @Override
            public OptionsComponent<WaveColorOptionsImpl> createPanel() {
                WaveColorOptionsPanel panel = new WaveColorOptionsPanel();
                panel.setWaveColorService(waveColorService);
                return panel;
            }

            @Override
            public ResourceBundle getResourceBundle() {
                return App.getModule(LanguageModuleApi.class).getBundle(WaveColorOptionsPanel.class);
            }

            @Override
            public WaveColorOptionsImpl createOptions() {
                return new WaveColorOptionsImpl();
            }

            @Override
            public void loadFromPreferences(Preferences preferences, WaveColorOptionsImpl options) {
                options.loadFromPreferences(new WaveColorPreferences(preferences));
            }

            @Override
            public void saveToPreferences(Preferences preferences, WaveColorOptionsImpl options) {
                options.saveToPreferences(new WaveColorPreferences(preferences));
            }

            @Override
            public void applyPreferencesChanges(WaveColorOptionsImpl options) {
                if (options.isUseDefaultColors()) {
                    waveColorService.setCurrentWaveColors(waveColorService.getCurrentWaveColors());
                } else {
                    Color[] colors = new Color[6];
                    colors[0] = intToColor(options.getWaveColor());
                    colors[1] = intToColor(options.getWaveFillColor());
                    colors[2] = intToColor(options.getWaveCursorColor());
                    colors[3] = intToColor(options.getWaveCursorWaveColor());
                    colors[4] = intToColor(options.getWaveBackgroundColor());
                    colors[5] = intToColor(options.getWaveSelectionColor());
                    waveColorService.setCurrentWaveColors(colors);
                }
            }

            @Nullable
            private Color intToColor(@Nullable Integer intValue) {
                return intValue == null ? null : new Color(intValue);
            }
        });
        optionsModule.addOptionsPage(new DefaultOptionsPage<AudioDevicesOptionsImpl>() {
            @Override
            public OptionsComponent<AudioDevicesOptionsImpl> createPanel() {
                return new AudioDevicesOptionsPanel();
            }

            @Override
            public ResourceBundle getResourceBundle() {
                return App.getModule(LanguageModuleApi.class).getBundle(AudioDevicesOptionsPanel.class);
            }

            @Override
            public AudioDevicesOptionsImpl createOptions() {
                return new AudioDevicesOptionsImpl();
            }

            @Override
            public void loadFromPreferences(Preferences preferences, AudioDevicesOptionsImpl options) {
                options.loadFromPreferences(new AudioDevicesPreferences(preferences));
            }

            @Override
            public void saveToPreferences(Preferences preferences, AudioDevicesOptionsImpl options) {
                options.saveToPreferences(new AudioDevicesPreferences(preferences));
            }

            @Override
            public void applyPreferencesChanges(AudioDevicesOptionsImpl options) {
                // TODO
            }
        });
    }

    public void registerToolsOptionsMenuActions() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.TOOLS_MENU_ID, getWaveColorAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.MIDDLE));
    }

    public void registerToolsMenuActions() {
        EditToolActions actions = getEditToolActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuGroup(ActionConsts.TOOLS_MENU_ID, TOOLS_SELECTION_MENU_GROUP_ID);
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuManagement.registerMenuRule(menuContribution, new SeparationMenuContributionRule(SeparationMode.AROUND));
        menuContribution = menuManagement.registerMenuItem(ActionConsts.TOOLS_MENU_ID, actions.getSelectionToolAction());
        menuManagement.registerMenuRule(menuContribution, new GroupMenuContributionRule(TOOLS_SELECTION_MENU_GROUP_ID));
        menuContribution = menuManagement.registerMenuItem(ActionConsts.TOOLS_MENU_ID, actions.getPencilToolAction());
        menuManagement.registerMenuRule(menuContribution, new GroupMenuContributionRule(TOOLS_SELECTION_MENU_GROUP_ID));
    }

    public AudioStatusPanel getAudioStatusPanel() {
        return audioStatusPanel;
    }

    @Nonnull
    private PropertiesAction createPropertiesAction() {
        ensureSetup();
        PropertiesAction propertiesAction = new PropertiesAction();
        propertiesAction.setup(editorProvider, resourceBundle);
        return propertiesAction;
    }

    @Nonnull
    private AudioControlActions getAudioControlActions() {
        if (audioControlActions == null) {
            ensureSetup();
            audioControlActions = new AudioControlActions();
            audioControlActions.setup(editorProvider, resourceBundle);
        }

        return audioControlActions;
    }

    @Nonnull
    private AudioOperationActions getAudioOperationActions() {
        if (audioOperationActions == null) {
            ensureSetup();
            audioOperationActions = new AudioOperationActions();
            audioOperationActions.setup(editorProvider, resourceBundle);
        }

        return audioOperationActions;
    }

    @Nonnull
    private DrawingControlActions getDrawingControlActions() {
        if (drawingControlActions == null) {
            ensureSetup();
            drawingControlActions = new DrawingControlActions();
            drawingControlActions.setup(editorProvider, resourceBundle);
        }

        return drawingControlActions;
    }

    @Nonnull
    private EditToolActions getEditToolActions() {
        if (editToolActions == null) {
            ensureSetup();
            editToolActions = new EditToolActions();
            editToolActions.setup(editorProvider, resourceBundle);
        }

        return editToolActions;
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

    @Nonnull
    private WaveColorAction getWaveColorAction() {
        ensureSetup();
        WaveColorAction waveColorAction = new WaveColorAction();
        waveColorAction.setup(editorProvider, resourceBundle);
        return waveColorAction;
    }

    public void registerPropertiesMenu() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.FILE_MENU_ID, createPropertiesAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
    }

    public void registerAudioMenu() {
        getAudioControlActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        menuManagement.registerMenu(AUDIO_MENU_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.MAIN_MENU_ID, AUDIO_MENU_ID, "Audio");
        menuManagement.registerMenuRule(menuContribution, new RelativeMenuContributionRule(NextToMode.AFTER, ActionConsts.VIEW_MENU_ID));
        menuContribution = menuManagement.registerMenuItem(AUDIO_MENU_ID, audioControlActions.getPlayAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(AUDIO_MENU_ID, audioControlActions.getStopAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
    }

    public void registerAudioOperationMenu() {
        getAudioOperationActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        menuManagement.registerMenu(AUDIO_OPERATION_MENU_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(AUDIO_MENU_ID, AUDIO_OPERATION_MENU_ID, "Operation");
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
        menuContribution = menuManagement.registerMenuItem(AUDIO_OPERATION_MENU_ID, audioOperationActions.getRevertAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
    }

    public void registerDrawingModeMenu() {
        getDrawingControlActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.VIEW_MENU_ID, DRAW_MODE_SUBMENU_ID, "Draw Mode");
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
    }

    public void registerZoomModeMenu() {
        getZoomControlActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(ActionConsts.VIEW_MENU_ID, ZOOM_MODE_SUBMENU_ID, "Zoom");
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
        menuManagement.registerMenu(ZOOM_MODE_SUBMENU_ID);
        menuContribution = menuManagement.registerMenuItem(ZOOM_MODE_SUBMENU_ID, zoomControlActions.getZoomUpAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(ZOOM_MODE_SUBMENU_ID, zoomControlActions.getNormalZoomAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(ZOOM_MODE_SUBMENU_ID, zoomControlActions.getZoomDownAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
    }

    public void bindZoomScrollWheel() {
        // ((AudioPanel) getEditorProvider()).
    }

    private JPopupMenu createPopupMenu() {
        getAudioControlActions();
        getDrawingControlActions();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        MenuManagement menuManagement = actionModule.getMenuManagement(MODULE_ID);
        menuManagement.registerMenu(AUDIO_POPUP_MENU_ID);
        MenuContribution menuContribution = menuManagement.registerMenuItem(AUDIO_POPUP_MENU_ID, audioControlActions.getPlayAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(AUDIO_POPUP_MENU_ID, audioControlActions.getStopAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));

        actionModule.registerClipboardMenuItems(AUDIO_POPUP_MENU_ID, MODULE_ID, SeparationMode.AROUND);
        menuManagement.registerMenu(DRAW_MODE_SUBMENU_ID);
        menuContribution = menuManagement.registerMenuItem(DRAW_MODE_SUBMENU_ID, drawingControlActions.getDotsModeAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(DRAW_MODE_SUBMENU_ID, drawingControlActions.getLineModeAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));
        menuContribution = menuManagement.registerMenuItem(DRAW_MODE_SUBMENU_ID, drawingControlActions.getIntegralModeAction());
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.TOP));

        menuContribution = menuManagement.registerMenuItem(AUDIO_POPUP_MENU_ID, DRAW_MODE_SUBMENU_ID, "Draw Mode");
        menuManagement.registerMenuRule(menuContribution, new PositionMenuContributionRule(PositionMode.BOTTOM));
        JPopupMenu popupMenu = new JPopupMenu();
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ComponentActivationService componentActivationService = frameModule.getFrameHandler().getComponentActivationService();
        menuManagement.buildMenu(popupMenu, AUDIO_POPUP_MENU_ID, componentActivationService);
        return popupMenu;
    }

    @ParametersAreNonnullByDefault
    public class XBSFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            String extension = getExtension(file);
            if (extension != null) {
                if (extension.length() < 3) {
                    return false;
                }
                return "xbs".contains(extension.substring(0, 3));
            }
            return false;
        }

        @Nonnull
        @Override
        public String getDescription() {
            return "XBUP Sound Files (*.xbs*)";
        }

        @Nonnull
        @Override
        public String getFileTypeId() {
            return XBS_FILE_TYPE;
        }
    }

    /**
     * Gets the extension part of file name.
     *
     * @param file Source file
     * @return extension part of file name
     */
    @Nullable
    public static String getExtension(File file) {
        String ext = null;
        String str = file.getName();
        int i = str.lastIndexOf('.');

        if (i > 0 && i < str.length() - 1) {
            ext = str.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
