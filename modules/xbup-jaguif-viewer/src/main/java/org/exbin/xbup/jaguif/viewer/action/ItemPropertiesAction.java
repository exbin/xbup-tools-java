/*
 * Copyright (C) ExBin Project, https://exbin.org
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
package org.exbin.xbup.jaguif.viewer.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.document.api.ContextDocument;
import org.exbin.xbup.jaguif.viewer.gui.BlockPropertiesPanel;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.xbup.jaguif.document.XbupTreeDocument;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.gui.CloseControlPanel;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Item properties action.
 */
@ParametersAreNonnullByDefault
public class ItemPropertiesAction extends AbstractAction {

    public static final String ACTION_ID = "itemProperties";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(ItemPropertiesAction.class);
    private boolean devMode = false;

    private XbupTreeDocument xbupDocument;
    private DialogParentComponent dialogParentComponent;

    public ItemPropertiesAction() {
    }

    public void init() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        setEnabled(false);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                    xbupDocument = instance instanceof XbupTreeDocument ? (XbupTreeDocument) instance : null;
                    setEnabled(xbupDocument != null);
                });
                registrar.registerChangeListener(DialogParentComponent.class, (DialogParentComponent instance) -> {
                    dialogParentComponent = instance;
                });
            }
        });
//        viewerProvider.addItemSelectionListener((@Nullable XBTBlock item) -> {
//            setEnabled(item != null);
//        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        XBACatalog catalog = xbupDocument.getCatalog();
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        BlockPropertiesPanel panel = new BlockPropertiesPanel();
        panel.setCatalog(catalog);
        panel.setDevMode(devMode);
        panel.setBlock(xbupDocument.getSelectedItem().get());
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        controlPanel.setController(() -> {
            dialog.close();
            dialog.dispose();
        });
        dialog.showCentered(dialogParentComponent.getComponent());
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
