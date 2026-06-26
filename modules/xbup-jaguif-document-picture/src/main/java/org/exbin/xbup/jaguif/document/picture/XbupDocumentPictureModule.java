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
package org.exbin.xbup.jaguif.document.picture;

import java.io.File;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.swing.filechooser.FileFilter;
import org.exbin.jaguif.App;
import org.exbin.jaguif.Module;
import org.exbin.jaguif.ModuleUtils;
import org.exbin.jaguif.document.api.Document;
import org.exbin.jaguif.document.api.DocumentManagement;
import org.exbin.jaguif.document.api.DocumentModuleApi;
import org.exbin.jaguif.document.api.DocumentSource;
import org.exbin.jaguif.document.api.DocumentType;
import org.exbin.jaguif.file.api.FileDocumentSource;
import org.exbin.jaguif.file.api.FileType;
import org.exbin.jaguif.file.api.FileModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsManagement;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.jaguif.options.settings.api.SettingsOptionsProvider;

/**
 * Picture editor module.
 */
@NullMarked
public class XbupDocumentPictureModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(XbupDocumentPictureModule.class);
    public static final String XBP_FILE_TYPE = "XBPictureEditor.XBPFileType";
    public static final String IMAGE_DOCUMENT_ID = "image";

    public XbupDocumentPictureModule() {
    }

    public void registerDocument() {
        DocumentModuleApi documentModule = App.getModule(DocumentModuleApi.class);
        DocumentManagement documentManager = documentModule.getMainDocumentManager();
        documentManager.registerDocumentType(new DocumentType() {
            @Override
            public String getTypeId() {
                return IMAGE_DOCUMENT_ID;
            }

            @Override
            public XBImageDocument createDefaultDocument() {
                XBImageDocument binaryDocument = createImageDocument();
                binaryDocument.loadFrom(documentModule.createEmptyDocumentSource());
                return binaryDocument;
            }

            @Override
            public Optional<Document> createDocument(DocumentSource documentSource) {
                if (documentSource instanceof FileDocumentSource) {
                    XBImageDocument document = createImageDocument();
                    document.loadFrom(documentSource);
                    return Optional.of(document);
                }

                return Optional.empty();
            }

            private XBImageDocument createImageDocument() {
                XBImageDocument document = new XBImageDocument();
                OptionsSettingsModuleApi optionsSettingsModule = App.getModule(OptionsSettingsModuleApi.class);
                OptionsSettingsManagement settingsManager = optionsSettingsModule.getMainSettingsManager();
                SettingsOptionsProvider settingsOptionsProvider = settingsManager.getSettingsOptionsProvider();
//                document.applySettings(settingsOptionsProvider);
//                document.setContentData(new ByteArrayPagedData());
                return document;
            }
        });
    }

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        fileModule.addFileType(new XBPFileType());
    }

    /**
     * Gets the extension part of file name.
     *
     * @param file Source file
     * @return extension part of file name
     */
    @Nullable
    public static String getExtension(File file) {
        String ext = null;
        String str = file.getName();
        int i = str.lastIndexOf('.');

        if (i > 0 && i < str.length() - 1) {
            ext = str.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    @NullMarked
    public class XBPFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                if (extension.length() < 3) {
                    return false;
                }
                return "xbp".contains(extension.substring(0, 3));
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "XBUP Picture Files (*.xbp*)";
        }

        @Override
        public String getFileTypeId() {
            return XBP_FILE_TYPE;
        }
    }
}
