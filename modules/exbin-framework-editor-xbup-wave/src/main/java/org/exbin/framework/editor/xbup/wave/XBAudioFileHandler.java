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
package org.exbin.framework.editor.xbup.wave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.file.api.FileType;
import org.exbin.xbup.audio.wave.XBWave;
import org.exbin.xbup.core.block.declaration.XBDeclaration;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.catalog.XBPCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.convert.XBTTypeUndeclaringFilter;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.convert.XBTEventListenerToListener;
import org.exbin.xbup.core.parser.token.event.convert.XBTListenerToEventListener;
import org.exbin.xbup.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullTypeDeclaringFilter;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBPSerialWriter;
import org.exbin.framework.editor.wave.AudioDocument;

/**
 * XBUP audio file handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBAudioFileHandler extends AudioDocument {

    @Override
    public void loadFromFile(URI fileUri, @Nullable FileType fileType) {
        if (fileType != null && EditorXbupWaveModule.XBS_FILE_TYPE.equals(fileType.getFileTypeId())) {
            try {
                File file = new File(fileUri);
                XBPCatalog catalog = new XBPCatalog();
                catalog.addFormatDecl(getContextFormatDecl());
                XBLFormatDecl formatDecl = new XBLFormatDecl(XBWave.XBUP_FORMATREV_CATALOGPATH);
                XBWave wave = new XBWave();
                XBDeclaration declaration = new XBDeclaration(formatDecl, wave);
                XBTPullTypeDeclaringFilter typeProcessing = new XBTPullTypeDeclaringFilter(catalog);
                typeProcessing.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(new FileInputStream(file))));
                XBPSerialReader reader = new XBPSerialReader(typeProcessing);
                reader.read(declaration);
                audioPanel.setWave(wave);
                this.fileUri = fileUri;
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBAudioFileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            notifyUndoChanged();
            return;
        }

        super.loadFromFile(fileUri, fileType);
    }

    @Override
    public void saveToFile(URI fileUri, @Nullable FileType fileType) {
        if (fileType != null && EditorXbupWaveModule.XBS_FILE_TYPE.equals(fileType.getFileTypeId())) {
            try {
                File file = new File(fileUri);
                FileOutputStream output = new FileOutputStream(file);

                XBPCatalog catalog = new XBPCatalog();
                catalog.addFormatDecl(getContextFormatDecl());
                XBLFormatDecl formatDecl = new XBLFormatDecl(XBWave.XBUP_FORMATREV_CATALOGPATH);
                XBDeclaration declaration = new XBDeclaration(formatDecl, audioPanel.getWave());
                declaration.realignReservation(catalog);
                XBTTypeUndeclaringFilter typeProcessing = new XBTTypeUndeclaringFilter(catalog);
                typeProcessing.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(new XBEventWriter(output))));
                XBPSerialWriter writer = new XBPSerialWriter(new XBTListenerToEventListener(typeProcessing));
                writer.write(declaration);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBAudioFileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            audioPanel.notifyFileSaved();
            notifyUndoChanged();
            return;
        }

        super.saveToFile(fileUri, fileType);
    }

    /**
     * Returns local format declaration when catalog or service is not
     * available.
     *
     * @return local format declaration
     */
    public XBLFormatDecl getContextFormatDecl() {
        /*XBLFormatDef formatDef = new XBLFormatDef();
         List<XBFormatParam> groups = formatDef.getFormatParams();
         XBLGroupDecl waveGroup = new XBLGroupDecl(new XBLGroupDef());
         List<XBGroupParam> waveBlocks = waveGroup.getGroupDef().getGroupParams();
         waveBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 5, 0, 0})));
         ((XBLGroupDef) waveGroup.getGroupDef()).provideRevision();
         groups.add(new XBFormatParamConsist(waveGroup));
         formatDef.realignRevision();

         XBLFormatDecl formatDecl = new XBLFormatDecl(formatDef);
         formatDecl.setCatalogPath(XBWave.XBUP_FORMATREV_CATALOGPATH);
         return formatDecl;*/

        XBPSerialReader reader = new XBPSerialReader(getClass().getResourceAsStream("/org/exbin/framework/editor/xbup/wave/resources/xbs_format_decl.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        try {
            reader.read(formatDecl);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBAudioFileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatDecl;
    }
}
