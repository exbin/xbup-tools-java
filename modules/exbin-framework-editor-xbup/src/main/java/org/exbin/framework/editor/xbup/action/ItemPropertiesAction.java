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
package org.exbin.framework.editor.xbup.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.editor.xbup.gui.BlockPropertiesPanel;
import org.exbin.framework.editor.xbup.viewer.XbupEditorProvider;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.CloseControlPanel;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Item properties action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ItemPropertiesAction extends AbstractAction {

    public static final String ACTION_ID = "itemPropertiesAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ItemPropertiesAction.class);
    private XbupEditorProvider viewerProvider;
    private boolean devMode = false;

    public ItemPropertiesAction() {
    }

    public void setup(XbupEditorProvider viewerProvider) {
        this.viewerProvider = viewerProvider;

        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        setEnabled(false);
        viewerProvider.addItemSelectionListener((@Nullable XBTBlock item) -> {
            setEnabled(item != null);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBACatalog catalog = viewerProvider.getCatalog();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        BlockPropertiesPanel panel = new BlockPropertiesPanel();
        panel.setCatalog(catalog);
        panel.setDevMode(devMode);
        XbupFileHandler xbupFile = (XbupFileHandler) viewerProvider.getActiveFile().get();
        panel.setBlock(xbupFile.getSelectedItem().get());
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        controlPanel.setHandler(() -> {
            dialog.close();
            dialog.dispose();
        });
        dialog.showCentered(viewerProvider.getEditorComponent());
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
