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
package org.exbin.framework.viewer.xbup.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.context.api.ContextChangeRegistration;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.viewer.xbup.gui.BlockPropertiesPanel;
import org.exbin.framework.viewer.xbup.document.XbupFileHandler;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.CloseControlPanel;
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
    private boolean devMode = false;

    private XbupFileHandler fileHandler;

    public ItemPropertiesAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        setEnabled(false);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerUpdateListener(FileHandler.class, (instance) -> {
                    fileHandler = instance instanceof XbupFileHandler ? (XbupFileHandler) instance : null;
                    setEnabled(fileHandler != null);
                });
            }
        });
//        viewerProvider.addItemSelectionListener((@Nullable XBTBlock item) -> {
//            setEnabled(item != null);
//        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBACatalog catalog = fileHandler.getCatalog();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        BlockPropertiesPanel panel = new BlockPropertiesPanel();
        panel.setCatalog(catalog);
        panel.setDevMode(devMode);
        panel.setBlock(fileHandler.getSelectedItem().get());
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        controlPanel.setController(() -> {
            dialog.close();
            dialog.dispose();
        });
        dialog.showCentered(fileHandler.getComponent());
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
