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
package org.exbin.framework.xbup.catalog.item.property;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPopupMenu;
import org.exbin.framework.App;
import org.exbin.framework.action.api.MenuManagement;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.xbup.catalog.item.property.gui.CatalogItemEditPropertiesPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Catalog properties editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogPropertiesEditor {

    private final CatalogItemEditPropertiesPanel catalogEditorPanel;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private XBCItem item;
    
    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogPropertiesEditor.class);

    public CatalogPropertiesEditor() {
        catalogEditorPanel = new CatalogItemEditPropertiesPanel();
    }

    @Nonnull
    public CatalogItemEditPropertiesPanel getCatalogEditorPanel() {
        return catalogEditorPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        catalogEditorPanel.setCatalog(catalog);
    }

    public XBCItem getItem() {
        return item;
    }

    public void setItem(XBCItem item) {
        this.item = item;
        catalogEditorPanel.setCatalogItem(item);
    }

    public void setMenuManagement(MenuManagement menuManagement) {
        menuManagement.insertMainPopupMenu(popupMenu, 5);
    }
    
    public void persist() {
        catalogEditorPanel.persist();
    }
}
