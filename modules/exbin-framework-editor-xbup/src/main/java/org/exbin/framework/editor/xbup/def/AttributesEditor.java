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
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.component.api.toolbar.EditItemActionsHandler;
import org.exbin.framework.component.api.toolbar.EditItemActionsUpdateListener;
import org.exbin.framework.editor.xbup.def.action.AddAttributeAction;
import org.exbin.framework.editor.xbup.def.action.RemoveAttributesAction;
import org.exbin.framework.editor.xbup.def.gui.AttributesPanel;
import org.exbin.framework.editor.xbup.def.model.AttributesTableModel;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.utils.LanguageUtils;
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

    private AttributesPanel editorPanel = new AttributesPanel();
    private final AttributesTableModel attributesTableModel = new AttributesTableModel();
    private final DefaultEditItemActions editActions;
    private XBApplication application;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(AttributesEditor.class);

    private AddAttributeAction addAttributeAction = new AddAttributeAction();
    private RemoveAttributesAction removeAttributesAction = new RemoveAttributesAction();

    public AttributesEditor() {
        editActions = new DefaultEditItemActions(DefaultEditItemActions.Mode.DIALOG);
        editActions.setEditItemActionsHandler(new EditItemActionsHandler() {
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

            @Override
            public void setUpdateListener(@Nonnull EditItemActionsUpdateListener updateListener) {
                editorPanel.addSelectionListener(updateListener);
            }
        });

        popupMenu = new JPopupMenu();
        JMenuItem addPluginMenuItem = ActionUtils.actionToMenuItem(editActions.getAddItemAction());
        addPluginMenuItem.setText(resourceBundle.getString("addPluginMenuItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
        popupMenu.add(addPluginMenuItem);
        JMenuItem editPluginMenuItem = ActionUtils.actionToMenuItem(editActions.getEditItemAction());
        editPluginMenuItem.setText(resourceBundle.getString("editPluginMenuItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
        popupMenu.add(editPluginMenuItem);

        editorPanel.setPanelPopup(popupMenu);

        editorPanel.addActions(editActions);
    }

    @Nonnull
    public AttributesPanel getEditorPanel() {
        return editorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        editorPanel.setApplication(application);

        addAttributeAction.setup(application);
        removeAttributesAction.setup(application);
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
    }
}
