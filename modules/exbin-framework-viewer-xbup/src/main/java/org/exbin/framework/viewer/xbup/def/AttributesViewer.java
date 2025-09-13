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
package org.exbin.framework.viewer.xbup.def;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.DefaultActionContextService;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.viewer.xbup.def.gui.AttributesPanel;
import org.exbin.framework.viewer.xbup.def.model.AttributesTableModel;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.toolbar.api.ToolBarManager;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Attributes viewer.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AttributesViewer {

    public static final String TOOLBAR_ID = "AttributesEditor.toolBar";

    private AttributesPanel viewerPanel = new AttributesPanel();
    private final AttributesTableModel attributesTableModel = new AttributesTableModel();
    private final DefaultEditItemActions actions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AttributesViewer.class);

    public AttributesViewer() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManager toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");
        DefaultActionContextService actionContextService = new DefaultActionContextService();
        actions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", actions.createAddItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", actions.createEditItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", actions.createDeleteItemAction());
        toolBarManager.buildToolBar(viewerPanel.getToolBar(), "", actionContextService);

        popupMenu = new JPopupMenu();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        JMenuItem addAttributeMenuItem = actionModule.actionToMenuItem(actions.createAddItemAction());
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        addAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addAttributeMenuItem.text"));
        popupMenu.add(addAttributeMenuItem);
        JMenuItem editAttributeMenuItem = actionModule.actionToMenuItem(actions.createEditItemAction());
        editAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editAttributeMenuItem.text"));
        popupMenu.add(editAttributeMenuItem);

        viewerPanel.setPanelPopup(popupMenu);
        viewerPanel.setAttributesTableModel(attributesTableModel);

        viewerPanel.addActions(actions);
    }

    @Nonnull
    public AttributesPanel getViewerPanel() {
        return viewerPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        viewerPanel.setCatalog(catalog);
    }

    public void setBlock(XBTTreeNode block) {
        List<XBAttribute> attributes = new ArrayList<>();
        XBFixedBlockType fixedBlockType = block.getFixedBlockType();
        attributes.add(fixedBlockType.getGroupID());
        if (!block.getSingleAttributeType()) {
            attributes.add(fixedBlockType.getBlockID());
            attributes.addAll(Arrays.asList(block.getAttributes()));
        }
        attributesTableModel.setAttribs(attributes);
        attributesTableModel.fireTableDataChanged();
    }
}
