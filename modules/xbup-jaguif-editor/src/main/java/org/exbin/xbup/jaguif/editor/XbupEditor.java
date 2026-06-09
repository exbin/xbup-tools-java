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
package org.exbin.xbup.jaguif.editor;

import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import org.exbin.jaguif.App;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.jaguif.component.block.XbupBlock;
import org.exbin.xbup.jaguif.component.page.XbupComponentPage;
import org.exbin.xbup.jaguif.editor.page.StructurePage;
import org.exbin.xbup.jaguif.component.page.XbupPagesPanel;
import org.exbin.xbup.jaguif.editor.page.BinaryPage;
import org.exbin.xbup.jaguif.editor.page.TextualPage;
import org.exbin.xbup.jaguif.editor.page.XbupEditorPage;

/**
 * Block editor.
 */
@ParametersAreNonnullByDefault
public class XbupEditor {

    protected final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupEditor.class);
    protected final XbupPagesPanel pagesPanel = new XbupPagesPanel();
    protected XbupTree xbupTree;

    public XbupEditor() {
        pagesPanel.addPage(new StructurePage());
        pagesPanel.addPage(new TextualPage());
        pagesPanel.addPage(new BinaryPage());
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setXbupTree(XbupTree xbupTree) {
        this.xbupTree = xbupTree;
        XbupBlock xbupBlock = new XbupBlock(xbupTree);
        List<XbupComponentPage> pages = pagesPanel.getPages();
        for (XbupComponentPage page : pages) {
            if (page instanceof XbupEditorPage) {
                ((XbupEditorPage) page).setXbupTree(xbupTree);
            }
        }
    }

    @Nonnull
    public JComponent getComponent() {
        return pagesPanel;
    }
}
