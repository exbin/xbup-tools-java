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
package org.exbin.framework.editor.wave.gui;

import java.net.URI;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.sound.sampled.AudioFormat;
import org.exbin.framework.App;
import org.exbin.framework.editor.wave.AudioFileHandler;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;

/**
 * Wave file properties panel.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class PropertiesPanel extends javax.swing.JPanel {

    private final java.util.ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(PropertiesPanel.class);

    public PropertiesPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileNameTextField = new javax.swing.JTextField();
        documentPanel = new javax.swing.JPanel();
        waveLengthLabel = new javax.swing.JLabel();
        waveLengthTextField = new javax.swing.JTextField();
        sampleRateLabel = new javax.swing.JLabel();
        sampleRateTextField = new javax.swing.JTextField();
        channelsLabel = new javax.swing.JLabel();
        channelsTextField = new javax.swing.JTextField();
        encodingLabel = new javax.swing.JLabel();
        encodingTextField = new javax.swing.JTextField();
        fileNameLabel = new javax.swing.JLabel();

        fileNameTextField.setEditable(false);

        documentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceBundle.getString("documentPanel.border.title"))); // NOI18N

        waveLengthLabel.setText(resourceBundle.getString("waveLengthLabel.text")); // NOI18N

        waveLengthTextField.setEditable(false);

        sampleRateLabel.setText(resourceBundle.getString("sampleRateLabel.text")); // NOI18N

        sampleRateTextField.setEditable(false);

        channelsLabel.setText(resourceBundle.getString("channelsLabel.text")); // NOI18N

        channelsTextField.setEditable(false);

        encodingLabel.setText(resourceBundle.getString("encodingLabel.text")); // NOI18N

        encodingTextField.setEditable(false);

        javax.swing.GroupLayout documentPanelLayout = new javax.swing.GroupLayout(documentPanel);
        documentPanel.setLayout(documentPanelLayout);
        documentPanelLayout.setHorizontalGroup(
            documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(waveLengthLabel)
                    .addComponent(sampleRateLabel)
                    .addComponent(channelsLabel)
                    .addComponent(encodingLabel)
                    .addComponent(sampleRateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                    .addComponent(channelsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                    .addComponent(encodingTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                    .addComponent(waveLengthTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
                .addContainerGap())
        );
        documentPanelLayout.setVerticalGroup(
            documentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentPanelLayout.createSequentialGroup()
                .addComponent(waveLengthLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waveLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sampleRateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sampleRateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(channelsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(encodingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(encodingTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        fileNameLabel.setText(resourceBundle.getString("fileNameLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fileNameLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(fileNameTextField)
                    .addComponent(documentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(documentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Nonnull
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setDocument(AudioFileHandler fileHandler) {
        AudioPanel audioPanel = (AudioPanel) fileHandler.getComponent();
        if (!audioPanel.isEmpty()) {
            Optional<URI> fileUri = fileHandler.getFileUri();
            fileNameTextField.setText(fileUri.isPresent() ? fileUri.get().toString() : "");
            waveLengthTextField.setText(audioPanel.getWaveLength());
            AudioFormat waveFormat = audioPanel.getWaveFormat();
            sampleRateTextField.setText(String.valueOf((long) waveFormat.getSampleRate()));
            channelsTextField.setText(String.valueOf(waveFormat.getChannels()));
            encodingTextField.setText(String.valueOf(waveFormat.getEncoding().toString()));
        }
    }

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new PropertiesPanel());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel channelsLabel;
    private javax.swing.JTextField channelsTextField;
    private javax.swing.JPanel documentPanel;
    private javax.swing.JLabel encodingLabel;
    private javax.swing.JTextField encodingTextField;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JLabel sampleRateLabel;
    private javax.swing.JTextField sampleRateTextField;
    private javax.swing.JLabel waveLengthLabel;
    private javax.swing.JTextField waveLengthTextField;
    // End of variables declaration//GEN-END:variables
}
