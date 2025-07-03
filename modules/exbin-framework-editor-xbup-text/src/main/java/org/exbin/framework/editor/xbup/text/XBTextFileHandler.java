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
package org.exbin.framework.editor.xbup.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.editor.text.TextEditorProvider;
import org.exbin.framework.editor.text.TextFileHandler;
import org.exbin.framework.file.api.FileType;
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
import org.exbin.xbup.core.type.XBEncodingText;

/**
 * XBUP text file handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTextFileHandler extends TextFileHandler {

    @Override
    public void loadFromFile(URI fileUri, @Nullable FileType fileType) {
        if (fileType != null && EditorXbupTextModule.XBT_FILE_TYPE.equals(fileType.getFileTypeId())) {
            try {
                File file = new File(fileUri);
                XBPCatalog catalog = new XBPCatalog();
                catalog.addFormatDecl(getContextFormatDecl());
                XBLFormatDecl formatDecl = new XBLFormatDecl(XBEncodingText.XBUP_FORMATREV_CATALOGPATH);
                XBEncodingText encodingText = new XBEncodingText();
                XBDeclaration declaration = new XBDeclaration(formatDecl, encodingText);
                XBTPullTypeDeclaringFilter typeProcessing = new XBTPullTypeDeclaringFilter(catalog);
                typeProcessing.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(new FileInputStream(file))));
                XBPSerialReader reader = new XBPSerialReader(typeProcessing);
                reader.read(declaration);
                textPanel.changeCharset(encodingText.getCharset());
                textPanel.setText(encodingText.getValue());
                this.fileUri = fileUri;
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTextFileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            textPanel.setModified(false);
            notifyUndoChanged();
            return;
        }

        super.loadFromFile(fileUri, fileType);
    }

    @Override
    public void saveToFile(URI fileUri, FileType fileType) {
        if (fileType != null && EditorXbupTextModule.XBT_FILE_TYPE.equals(fileType.getFileTypeId())) {
            try {
                File file = new File(fileUri);

                XBEncodingText encodingString = new XBEncodingText();
                encodingString.setValue(textPanel.getText());
                encodingString.setCharset(textPanel.getCharset());

                try (FileOutputStream output = new FileOutputStream(file)) {
                    XBPCatalog catalog = new XBPCatalog();
                    catalog.addFormatDecl(getContextFormatDecl());
                    XBLFormatDecl formatDecl = new XBLFormatDecl(XBEncodingText.XBUP_FORMATREV_CATALOGPATH);
                    XBDeclaration declaration = new XBDeclaration(formatDecl, encodingString);
                    declaration.realignReservation(catalog);
                    XBTTypeUndeclaringFilter typeProcessing = new XBTTypeUndeclaringFilter(catalog);
                    typeProcessing.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(new XBEventWriter(output))));
                    XBPSerialWriter writer = new XBPSerialWriter(new XBTListenerToEventListener(typeProcessing));
                    writer.write(declaration);
                    this.fileUri = fileUri;
                }
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTextFileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            textPanel.setModified(false);
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
    @Nullable
    public XBLFormatDecl getContextFormatDecl() {
        /*XBLFormatDef formatDef = new XBLFormatDef();
         List<XBFormatParam> groups = formatDef.getFormatParams();
         XBLGroupDecl stringGroup = new XBLGroupDecl(new XBLGroupDef());
         List<XBGroupParam> stringBlocks = stringGroup.getGroupDef().getGroupParams();
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 0, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 1, 1, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 2, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 3, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 4, 0})));
         ((XBLGroupDef) stringGroup.getGroupDef()).provideRevision();
         groups.add(new XBFormatParamConsist(stringGroup));
         formatDef.realignRevision();

         XBLFormatDecl formatDecl = new XBLFormatDecl(formatDef);
         formatDecl.setCatalogPath(XBEncodingText.XBUP_FORMATREV_CATALOGPATH);
         return formatDecl;*/

        XBPSerialReader reader = new XBPSerialReader(getClass().getResourceAsStream("/org/exbin/framework/editor/xbup/text/resources/xbt_format_decl.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        try {
            reader.read(formatDecl);
        } catch (XBProcessingException | IOException ex) {
            return null;
        }
        return formatDecl;
    }
}
