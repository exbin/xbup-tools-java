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
package org.exbin.framework.editor.xbup.picture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.document.api.DocumentSource;
import org.exbin.framework.editor.picture.ImageDocument;
import org.exbin.framework.editor.picture.gui.ImagePanel;
import org.exbin.framework.file.api.FileDocumentSource;
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
import org.exbin.xbup.visual.picture.XBBufferedImage;

/**
 * XBUP image file handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBImageDocument extends ImageDocument {

    @Override
    public void loadFrom(DocumentSource documentSource) {
        if (documentSource instanceof FileDocumentSource) {
            FileType fileType = ((FileDocumentSource) documentSource).getFileType().orElse(null);
            if (EditorXbupPictureModule.XBP_FILE_TYPE.equals(fileType.getFileTypeId())) {
                try {
                    File file = ((FileDocumentSource) documentSource).getFile();
                    if (imagePanel.getImage() == null) {
                        imagePanel.initImage();
                    }

                    XBPCatalog catalog = new XBPCatalog();
                    catalog.addFormatDecl(getContextFormatDecl());
                    XBLFormatDecl formatDecl = new XBLFormatDecl(XBBufferedImage.XBUP_FORMATREV_CATALOGPATH);
                    XBBufferedImage bufferedImage = new XBBufferedImage(ImagePanel.toBufferedImage(imagePanel.getImage()));
                    XBDeclaration declaration = new XBDeclaration(formatDecl, bufferedImage);
                    XBTPullTypeDeclaringFilter typeProcessing = new XBTPullTypeDeclaringFilter(catalog);
                    typeProcessing.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(new FileInputStream(file))));
                    XBPSerialReader reader = new XBPSerialReader(typeProcessing);
                    reader.read(declaration);
                    imagePanel.setImage(bufferedImage.getImage());
                    // this.documentSource = documentSource;
                } catch (XBProcessingException | IOException ex) {
                    Logger.getLogger(XBImageDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }

        super.loadFrom(documentSource);
    }

    @Override
    public void saveTo(DocumentSource documentSource) {
        if (documentSource instanceof FileDocumentSource) {
            FileType fileType = ((FileDocumentSource) documentSource).getFileType().orElse(null);
            if (EditorXbupPictureModule.XBP_FILE_TYPE.equals(fileType.getFileTypeId())) {
                try {
                    File file = ((FileDocumentSource) documentSource).getFile();
                    FileOutputStream output = new FileOutputStream(file);

                    XBPCatalog catalog = new XBPCatalog();
                    catalog.addFormatDecl(getContextFormatDecl());
                    XBLFormatDecl formatDecl = new XBLFormatDecl(XBBufferedImage.XBUP_FORMATREV_CATALOGPATH);
                    XBDeclaration declaration = new XBDeclaration(formatDecl, new XBBufferedImage(ImagePanel.toBufferedImage(imagePanel.getImage())));
                    declaration.realignReservation(catalog);
                    XBTTypeUndeclaringFilter typeProcessing = new XBTTypeUndeclaringFilter(catalog);
                    typeProcessing.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(new XBEventWriter(output))));
                    XBPSerialWriter writer = new XBPSerialWriter(new XBTListenerToEventListener(typeProcessing));
                    writer.write(declaration);
                } catch (XBProcessingException | IOException ex) {
                    Logger.getLogger(XBImageDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }

        super.saveTo(documentSource);
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
         XBLGroupDecl bitmapGroup = new XBLGroupDecl(new XBLGroupDef());
         List<XBGroupParam> bitmapBlocks = bitmapGroup.getGroupDef().getGroupParams();
         bitmapBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 0, 1, 0})));
         bitmapBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 0, 2, 0})));
         ((XBLGroupDef) bitmapGroup.getGroupDef()).provideRevision();
         XBLGroupDecl paletteGroup = new XBLGroupDecl(new XBLGroupDef());
         List<XBGroupParam> paletteBlocks = paletteGroup.getGroupDef().getGroupParams();
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 1, 0})));
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 1, 0})));
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 2, 0})));
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 3, 0})));
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 4, 0})));
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 5, 0})));
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 6, 0})));
         paletteBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 4, 0, 1, 7, 0})));
         ((XBLGroupDef) paletteGroup.getGroupDef()).provideRevision();
         groups.add(new XBFormatParamConsist(bitmapGroup));
         groups.add(new XBFormatParamConsist(paletteGroup));
         formatDef.realignRevision();

         XBLFormatDecl formatDecl = new XBLFormatDecl(formatDef);
         formatDecl.setCatalogPath(XBBufferedImage.XBUP_FORMATREV_CATALOGPATH);*/

        XBPSerialReader reader = new XBPSerialReader(getClass().getResourceAsStream("/org/exbin/framework/editor/xbup/picture/resources/xbp_format_decl.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        try {
            reader.read(formatDecl);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBImageDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatDecl;
    }
}
