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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.filechooser.FileFilter;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.file.api.FileType;
import org.exbin.framework.file.api.FileModuleApi;

/**
 * Picture editor module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class EditorXbupPictureModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(EditorXbupPictureModule.class);
    public static final String XBP_FILE_TYPE = "XBPictureEditor.XBPFileType";

    public EditorXbupPictureModule() {
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

    @ParametersAreNonnullByDefault
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

        @Nonnull
        @Override
        public String getFileTypeId() {
            return XBP_FILE_TYPE;
        }
    }
}
