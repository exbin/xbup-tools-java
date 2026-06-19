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
package org.exbin.xbup.jaguif.editor.def;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.exbin.jaguif.App;
import org.exbin.jaguif.component.action.AddItemAction;
import org.exbin.jaguif.component.action.DefaultEditItemActions;
import org.exbin.jaguif.component.action.DeleteItemAction;
import org.exbin.jaguif.component.action.EditItemAction;
import org.exbin.jaguif.component.action.EditItemMode;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.context.api.ActiveContextManagement;
import org.exbin.jaguif.context.api.ContextModuleApi;
import org.exbin.jaguif.context.api.ContextRegistration;
import org.exbin.jaguif.context.api.ContextUpdateManagement;
import org.exbin.xbup.jaguif.editor.def.action.AddAttributeAction;
import org.exbin.xbup.jaguif.editor.def.action.RemoveAttributesAction;
import org.exbin.xbup.jaguif.editor.def.gui.AttributesPanel;
import org.exbin.xbup.jaguif.editor.def.gui.AttributesTableModel;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.toolbar.api.ActionToolBarContribution;
import org.exbin.jaguif.toolbar.api.ToolBarManagement;
import org.exbin.jaguif.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Attributes editor.
 */
@ParametersAreNonnullByDefault
public class AttributesEditor {

    public static final String TOOLBAR_ID = "AttributesEditor.toolBar";

    protected AttributesPanel editorPanel = new AttributesPanel();
    protected final AttributesTableModel attributesTableModel = new AttributesTableModel();
    protected final DefaultEditItemActions editActions;
    protected XBACatalog catalog;
    protected JPopupMenu popupMenu;

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AttributesEditor.class);

    protected AddAttributeAction addAttributeAction = new AddAttributeAction();
    protected RemoveAttributesAction removeAttributesAction = new RemoveAttributesAction();

    public AttributesEditor() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ActiveContextManagement contextManager = contextModule.createContextManager();
        ContextUpdateManagement updateManager = contextModule.createContextUpdateManagement(contextManager);
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
        ContextEditItem contextEditItem = new ContextEditItem() {
            @Override
            public void performAddItem() {
                addAttributeAction.actionPerformed(null);
                JTable attributesTable = editorPanel.getAttributesTable();
                attributesTableModel.getAttribs().add(new UBNat32());
                attributesTableModel.fireTableDataChanged();
                attributesTable.revalidate();
            }

            @Override
            public void performEditItem() {
            }

            @Override
            public void performDeleteItem() {
                removeAttributesAction.actionPerformed(null);
                JTable attributesTable = editorPanel.getAttributesTable();
                int[] selectedRows = attributesTable.getSelectedRows();
                if (selectedRows.length > 0) {
                    Arrays.sort(selectedRows);
                    for (int index = selectedRows.length - 1; index >= 0; index--) {
                        attributesTableModel.getAttribs().remove(selectedRows[index]);
                    }

                    attributesTableModel.fireTableDataChanged();
                    attributesTable.clearSelection();
                    attributesTable.revalidate();
                }
            }

            @Override
            public boolean canAddItem() {
                return true;
            }

            @Override
            public boolean canEditItem() {
                return false;
            }

            @Override
            public boolean canDeleteItem() {
                return editorPanel.getSelectedRow() != null;
            }
        };
        contextManager.changeActiveState(ContextEditItem.class, contextEditItem);
        editorPanel.addSelectionListener((lse) -> {
            contextManager.changeActiveState(ContextEditItem.class, contextEditItem);        
        });
        ContextRegistration contextRegistrar = contextModule.createContextRegistrator();
        toolBarManager.buildToolBar(editorPanel.getToolBar(), "", contextRegistrar);

        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        popupMenu = new JPopupMenu();
        JMenuItem addAttributeMenuItem = menuModule.actionToMenuItem(editActions.createAddItemAction());
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        addAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addAttributeMenuItem.text"));
        popupMenu.add(addAttributeMenuItem);
        JMenuItem editAttributeMenuItem = menuModule.actionToMenuItem(editActions.createEditItemAction());
        editAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editAttributeMenuItem.text"));
        popupMenu.add(editAttributeMenuItem);

        editorPanel.setPanelPopup(popupMenu);
        editorPanel.setAttributesTableModel(attributesTableModel);

        // TODO editorPanel.getSideToolBar(editActions);

        addAttributeAction.init();
        removeAttributesAction.init();
    }

    @Nonnull
    public AttributesPanel getEditorPanel() {
        return editorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        editorPanel.setCatalog(catalog);
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
