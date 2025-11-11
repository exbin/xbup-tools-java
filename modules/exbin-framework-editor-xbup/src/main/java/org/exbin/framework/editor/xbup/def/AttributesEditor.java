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
package org.exbin.framework.editor.xbup.def;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionContextRegistration;
import org.exbin.framework.action.api.ActionManagement;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.action.EditItemMode;
import org.exbin.framework.component.api.ContextEditItem;
import org.exbin.framework.context.api.ActiveContextManagement;
import org.exbin.framework.context.api.ContextModuleApi;
import org.exbin.framework.editor.xbup.def.action.AddAttributeAction;
import org.exbin.framework.editor.xbup.def.action.RemoveAttributesAction;
import org.exbin.framework.editor.xbup.def.gui.AttributesPanel;
import org.exbin.framework.editor.xbup.def.model.AttributesTableModel;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.toolbar.api.ToolBarManagement;
import org.exbin.framework.toolbar.api.ToolBarModuleApi;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Attributes editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AttributesEditor {

    public static final String TOOLBAR_ID = "AttributesEditor.toolBar";

    private AttributesPanel editorPanel = new AttributesPanel();
    private final AttributesTableModel attributesTableModel = new AttributesTableModel();
    private final DefaultEditItemActions editActions;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AttributesEditor.class);

    private AddAttributeAction addAttributeAction = new AddAttributeAction();
    private RemoveAttributesAction removeAttributesAction = new RemoveAttributesAction();

    public AttributesEditor() {
        ToolBarModuleApi toolBarModule = App.getModule(ToolBarModuleApi.class);
        ToolBarManagement toolBarManager = toolBarModule.createToolBarManager();
        toolBarManager.registerToolBar(TOOLBAR_ID, "");

        ContextModuleApi contextModule = App.getModule(ContextModuleApi.class);
        ActiveContextManagement contextManager = contextModule.createContextManager();
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        ActionManagement actionManager = actionModule.createActionManager(contextManager);
        editActions = new DefaultEditItemActions(EditItemMode.DIALOG);
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createAddItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createEditItemAction());
        toolBarManager.registerToolBarItem(TOOLBAR_ID, "", editActions.createDeleteItemAction());
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
        ActionContextRegistration actionContextRegistrar = actionModule.createActionContextRegistrar(actionManager);
        toolBarManager.buildToolBar(editorPanel.getToolBar(), "", actionContextRegistrar);

        popupMenu = new JPopupMenu();
        JMenuItem addAttributeMenuItem = actionModule.actionToMenuItem(editActions.createAddItemAction());
        LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
        addAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "addAttributeMenuItem.text"));
        popupMenu.add(addAttributeMenuItem);
        JMenuItem editAttributeMenuItem = actionModule.actionToMenuItem(editActions.createEditItemAction());
        editAttributeMenuItem.setText(languageModule.getActionWithDialogText(resourceBundle, "editAttributeMenuItem.text"));
        popupMenu.add(editAttributeMenuItem);

        editorPanel.setPanelPopup(popupMenu);
        editorPanel.setAttributesTableModel(attributesTableModel);

        editorPanel.addActions(editActions);

        addAttributeAction.setup();
        removeAttributesAction.setup();
    }

    @Nonnull
    public AttributesPanel getEditorPanel() {
        return editorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        editorPanel.setCatalog(catalog);

        addAttributeAction.setCatalog(catalog);
        removeAttributesAction.setCatalog(catalog);
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
