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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeRegistrar;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;

/**
 * Refresh catalog action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class RefreshCatalogAction extends AbstractAction {

    public static final String ACTION_ID = "refreshCatalogAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(RefreshCatalogAction.class);

    private XBACatalog catalog;
    private XBCNodeService nodeService;

    private XBCRoot resultRoot;

    private Component parentComponent;

    public RefreshCatalogAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ActionContextChangeRegistrar manager) {
                manager.registerUpdateListener(XBACatalog.class, (instance) -> {
                    catalog = instance;
                    nodeService = catalog == null ? null : catalog.getCatalogService(XBCNodeService.class);
                });
            }
        });
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Nullable
    public XBCRoot getResultRoot() {
        return resultRoot;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);

//        Component invoker = catalogTreePopupMenu.getInvoker();
//        if (invoker == catalogTree) {
//            reloadNodesTree();
//        } else {
//            setNode((XBCNode) (currentItem == null || currentItem instanceof XBCNode ? currentItem : currentItem.getParentItem().orElse(null)));
//        }
    }
}
