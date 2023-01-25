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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.component.action.DefaultEditItemActions;
import org.exbin.framework.editor.xbup.def.action.AddAttributeAction;
import org.exbin.framework.editor.xbup.def.action.RemoveAttributesAction;
import org.exbin.framework.editor.xbup.def.gui.AttributesPanel;
import org.exbin.framework.editor.xbup.def.model.AttributesTableModel;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.xbup.core.catalog.XBACatalog;

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

        popupMenu = new JPopupMenu();
        JMenuItem addPluginMenuItem = ActionUtils.actionToMenuItem(editActions.getAddItemAction());
        addPluginMenuItem.setText(resourceBundle.getString("addAttributeMenuItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
        popupMenu.add(addPluginMenuItem);
        JMenuItem editPluginMenuItem = ActionUtils.actionToMenuItem(editActions.getEditItemAction());
        editPluginMenuItem.setText(resourceBundle.getString("editPluginMenuItem.text") + ActionUtils.DIALOG_MENUITEM_EXT);
        popupMenu.add(editPluginMenuItem);

        editorPanel.setPanelPopup(popupMenu);

        editorPanel.addFileActions(editActions);
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

}
