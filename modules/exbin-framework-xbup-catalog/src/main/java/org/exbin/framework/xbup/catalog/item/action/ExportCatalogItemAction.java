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
package org.exbin.framework.xbup.catalog.item.action;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.xbup.catalog.XBFileType;
import org.exbin.xbup.catalog.convert.XBCatalogXb;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Export catalog item action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ExportCatalogItemAction extends AbstractAction {

    public static final String ACTION_ID = "exportCatalogItemAction";

    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(ExportCatalogItemAction.class);

    private XBApplication application;
    private XBACatalog catalog;

    public ExportCatalogItemAction() {
    }

    public void setup(XBApplication application, XBACatalog catalog) {
        this.application = application;
        this.catalog = catalog;

        ActionUtils.setupAction(this, resourceBundle, ACTION_ID);
        putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(FrameModuleApi.class);

        JFileChooser exportFileChooser = new JFileChooser();
        XBFileType xbFileType = new XBFileType();
        exportFileChooser.addChoosableFileFilter(xbFileType);
        exportFileChooser.setAcceptAllFileFilterUsed(true);
        if (exportFileChooser.showSaveDialog(WindowUtils.getFrame(frameModule.getFrame())) == JFileChooser.APPROVE_OPTION) {
            XBCatalogXb catalogXb = new XBCatalogXb();
            catalogXb.setCatalog(catalog);
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(exportFileChooser.getSelectedFile());
                catalogXb.exportToXbFile(fileOutputStream);
                fileOutputStream.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExportCatalogItemAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExportCatalogItemAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}