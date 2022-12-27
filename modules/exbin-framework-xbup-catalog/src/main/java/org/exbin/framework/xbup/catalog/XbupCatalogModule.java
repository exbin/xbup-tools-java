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
package org.exbin.framework.xbup.catalog;

import java.awt.Component;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import org.exbin.framework.api.Preferences;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.api.XBApplicationModule;
import org.exbin.framework.api.XBModuleRepositoryUtils;
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.action.api.PositionMode;
import org.exbin.xbup.plugin.XBModuleHandler;
import org.exbin.framework.action.api.ActionModuleApi;

/**
 * XBUP catalog manager module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupCatalogModule implements XBApplicationModule {

    public static final String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(XbupCatalogModule.class);

    private XBApplication application;
    private Preferences preferences;

    public XbupCatalogModule() {
    }

    @Override
    public void init(XBModuleHandler moduleHandler) {
        this.application = (XBApplication) moduleHandler;
    }

    @Override
    public void unregisterModule(String moduleId) {
    }

    @Nonnull
    public MenuManagement getDefaultMenuManagement() {
        return new MenuManagement() {
            @Override
            public void extendMenu(JMenu menu, Integer pluginId, String menuId, PositionMode positionMode) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void addMenuItem(Component menuItem, Integer pluginId, String menuId, PositionMode mode) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void insertMenu(JMenu menu, Integer pluginId, PositionMode positionMode) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void extendToolBar(JToolBar toolBar) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void insertMainPopupMenu(JPopupMenu popupMenu, int position) {
                // Temporary
                ActionModuleApi actionModule = application.getModuleRepository().getModuleByInterface(ActionModuleApi.class);
                actionModule.fillPopupMenu(popupMenu, position);
            }
        };
    }

    @Nullable
    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
