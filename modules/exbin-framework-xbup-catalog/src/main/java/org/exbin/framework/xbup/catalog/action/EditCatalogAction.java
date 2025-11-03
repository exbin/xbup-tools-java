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
package org.exbin.framework.xbup.catalog.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeRegistrar;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.framework.window.api.gui.CloseControlPanel;
import org.exbin.framework.xbup.catalog.CatalogEditor;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Edit catalog action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditCatalogAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(EditCatalogAction.class);

    private XBACatalog catalog;

    private Component parentComponent;
    private XBCRoot activeItem;

    public EditCatalogAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ActionContextChangeRegistrar registrar) {
                registrar.registerUpdateListener(XBACatalog.class, (instance) -> {
                    catalog = instance;
                });
            }
        });
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public void setActiveItem(XBCRoot activeItem) {
        this.activeItem = activeItem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);

        CatalogEditor catalogEditor = new CatalogEditor();
        catalogEditor.setCatalog(catalog);
        catalogEditor.setCatalogRoot(activeItem);
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(catalogEditor.getCatalogEditorPanel(), controlPanel);
        controlPanel.setController(() -> {
            dialog.close();
            dialog.dispose();
        });
        windowModule.setWindowTitle(dialog, catalogEditor.getCatalogEditorPanel().getResourceBundle());
        dialog.showCentered(parentComponent);
    }
}
