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
package org.exbin.framework.xbup.catalog.item.revision.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.data.model.CatalogRevsTableItem;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.gui.DefaultControlPanel;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.framework.xbup.catalog.item.revision.gui.CatalogSpecRevEditorPanel;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Add new revision action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditItemRevisionAction extends AbstractAction {

    public static final String ACTION_ID = "editCatalogItemRevisionAction";
    
    private XBApplication application;
    private XBACatalog catalog;

    private Component parentComponent;
    private CatalogRevsTableItem currentRevision;
    private CatalogRevsTableItem resultRevision;

    public EditItemRevisionAction() {
    }

    public void setup(XBApplication application) {
        this.application = application;
    }

    @Nullable
    public CatalogRevsTableItem getCurrentRevision() {
        return currentRevision;
    }

    public void setCurrentRevision(CatalogRevsTableItem currentRevision) {
        this.currentRevision = currentRevision;
    }

    @Nullable
    public CatalogRevsTableItem getResultRevision() {
        return resultRevision;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultRevision = null;
        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);
        CatalogSpecRevEditorPanel panel = new CatalogSpecRevEditorPanel();
        panel.setRevItem(currentRevision);
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        final WindowUtils.DialogWrapper dialog = frameModule.createDialog(panel, controlPanel);
        frameModule.setDialogTitle(dialog, panel.getResourceBundle());
        controlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                resultRevision = panel.getRevItem();
            }
            dialog.close();
        });
        dialog.showCentered(parentComponent);
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
