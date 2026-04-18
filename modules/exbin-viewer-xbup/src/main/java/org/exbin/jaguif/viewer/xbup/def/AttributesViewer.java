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
package org.exbin.jaguif.viewer.xbup.def;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionManagement;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.component.action.AddItemAction;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.DeleteItemAction;
import org.exbin.jaguif.component.action.EditItemAction;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextRegistration;
import org.exbin.jaguif.viewer.xbup.def.gui.AttributesPanel;
import org.exbin.jaguif.viewer.xbup.def.model.AttributesTableModel;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.toolbar.api.ActionToolBarContribution;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Attributes viewer.
 */
@ParametersAreNonnullByDefault
public class AttributesViewer {

    public static final String TOOLBAR_ID = "AttributesEditor.toolBar";

    private AttributesPanel viewerPanel = new AttributesPanel();
    private final AttributesTableModel attributesTableModel = new AttributesTableModel();
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AttributesViewer.class);

    public AttributesViewer() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ActiveContextManagement contextManager = contextModule.createContextManager();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        ActionManagement actionManager = actionModule.createActionManager(contextManager);
        editActions = new DefaultEditItemActions(EditItemMode.DIALOG);
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createAddItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return AddItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createEditItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return EditItemAction.ACTION_ID;
            }
        });
        toolBarManager.registerToolBarContribution(TOOLBAR_ID, "", new ActionToolBarContribution() {
            @Nonnull
            @Override
            public Action createAction() {
                return editActions.createDeleteItemAction();
            }

            @Nonnull
            @Override
            public String getContributionId() {
                return DeleteItemAction.ACTION_ID;
            }
        });
        ContextRegistration contextRegistrar = contextModule.createContextRegistrator();
        toolBarManager.buildToolBar(viewerPanel.getToolBar(), "", contextRegistrar);

        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        popupMenu = new JPopupMenu();
        JMenuItem addAttributeMenuItem = menuModule.actionToMenuItem(editActions.createAddItemAction());
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        addAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addAttributeMenuItem.text"));
        popupMenu.add(addAttributeMenuItem);
        JMenuItem editAttributeMenuItem = menuModule.actionToMenuItem(editActions.createEditItemAction());
        editAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editAttributeMenuItem.text"));
        popupMenu.add(editAttributeMenuItem);

        viewerPanel.setPanelPopup(popupMenu);
        viewerPanel.setAttributesTableModel(attributesTableModel);

        viewerPanel.addActions(editActions);
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
