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
package org.exbin.framework.editor.xbup;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.def.AttributesEditor;
import org.exbin.framework.editor.xbup.def.ParametersEditor;
import org.exbin.framework.editor.xbup.def.gui.BasicNodePanel;
import org.exbin.framework.editor.xbup.def.gui.BinaryDataPanel;
import org.exbin.framework.editor.xbup.gui.ModifyBlockPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * Modify block.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BlockEditor {
    
    private XBApplication application;
    private XBACatalog catalog;
    private XBPluginRepository pluginRepository;

    private ModifyBlockPanel modifyBlockPanel = new ModifyBlockPanel();
    private BasicNodePanel basicNodePanel = new BasicNodePanel();
    private AttributesEditor attributesEditor = new AttributesEditor();
    private ParametersEditor parametersEditor = new ParametersEditor();
    private BinaryDataPanel dataEditor = new BinaryDataPanel();

    public BlockEditor() {
    }

    public void setApplication(XBApplication application) {
        this.application = application;

        basicNodePanel.setApplication(application);
        attributesEditor.setApplication(application);
        parametersEditor.setApplication(application);
        dataEditor.setApplication(application);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        basicNodePanel.setCatalog(catalog);
        attributesEditor.setCatalog(catalog);
        parametersEditor.setCatalog(catalog);
        dataEditor.setCatalog(catalog);
    }

    @Nonnull
    public ModifyBlockPanel getPanel() {
        return modifyBlockPanel;
    }
}
