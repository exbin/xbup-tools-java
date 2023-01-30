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
import javax.swing.JPopupMenu;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.def.gui.BinaryDataPanel;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Binary data editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataEditor {

    private BinaryDataPanel editorPanel = new BinaryDataPanel();
    private XBApplication application;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(BinaryDataEditor.class);

    public BinaryDataEditor() {
        popupMenu = new JPopupMenu();

        editorPanel.setPanelPopup(popupMenu);
    }

    @Nonnull
    public BinaryDataPanel getEditorPanel() {
        return editorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        editorPanel.setApplication(application);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        editorPanel.setCatalog(catalog);
    }
}
