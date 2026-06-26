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
package org.exbin.xbup.jaguif.document;

import java.io.File;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.swing.filechooser.FileFilter;
import org.exbin.jaguif.App;
import org.exbin.jaguif.file.api.FileType;

/**
 * Generic XBUP file type.
 */
@NullMarked
public class XbupFileFilter extends FileFilter implements FileType {
    
    public static final String EXTENSION_PREFIX = "xb";

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String extension = getExtension(file);
        if (extension != null) {
            if (extension.length() >= 2) {
                return extension.substring(0, 2).equals(EXTENSION_PREFIX);
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        XbupDocumentModule module = App.getModule(XbupDocumentModule.class);
        return module.getResourceBundle().getString("XbupFileFilter.description");
    }

    @Override
    public String getFileTypeId() {
        return XbupDocumentModule.XBUP_FILE_TYPE;
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
}
