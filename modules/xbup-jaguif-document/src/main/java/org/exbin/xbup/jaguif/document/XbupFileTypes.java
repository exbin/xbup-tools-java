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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.exbin.jaguif.file.api.FileType;
import org.exbin.jaguif.file.api.FileTypes;

/**
 * XBUP file types.
 */
@NullMarked
public class XbupFileTypes implements FileTypes {

    private Map<String, FileType> fileTypes = new HashMap<>();

    public XbupFileTypes() {
        XbupFileFilter xbType = new XbupFileFilter();
        fileTypes.put(xbType.getFileTypeId(), xbType);
    }

    @Override
    public boolean allowAllFiles() {
        return true;
    }

    @Override
    public Optional<FileType> getFileType(String fileTypeId) {
        return Optional.ofNullable(fileTypes.get(fileTypeId));
    }

    @Override
    public Collection<FileType> getFileTypes() {
        return fileTypes.values();
    }
}
