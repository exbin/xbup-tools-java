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
package org.exbin.framework.viewer.xbup.document;

import javax.annotation.Nonnull;
import org.exbin.framework.document.api.Document;
import org.exbin.xbup.core.block.XBTDocument;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;

/**
 * XBUP document.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XbupDocument extends Document {

    @Nonnull
    XBTDocument getDocument();

    @Nonnull
    XBACatalog getCatalog();

    @Nonnull
    XBPluginRepository getPluginRepository();
}
