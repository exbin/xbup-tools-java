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
package org.exbin.framework.xbup.catalog.item.property.gui;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.exbin.framework.App;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.visual.xbplugins.XBPicturePanel;

/**
 * Icon editing panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogEditIconPanel extends javax.swing.JPanel {

    private byte[] icon;
    private final XBPicturePanel mainPanel;
    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(CatalogEditIconPanel.class);

    public CatalogEditIconPanel(XBACatalog catalog, byte[] icon) {
        this.icon = icon;
        initComponents();

        mainPanel = new XBPicturePanel();
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.setIcon(icon != null ? new ImageIcon(icon) : null);
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new CatalogEditIconPanel(null, null));
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public byte[] getIcon() {
        Icon imageIcon = mainPanel.getIcon();
        if (imageIcon != null) {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(XBPicturePanel.toBufferedImage(((ImageIcon) imageIcon).getImage()), "png", arrayOutputStream);
                icon = arrayOutputStream.toByteArray();
            } catch (IOException ex) {
                Logger.getLogger(CatalogEditIconPanel.class.getName()).log(Level.SEVERE, null, ex);
                icon = null;
            }
        } else {
            icon = null;
        }

        return icon;
    }
}
