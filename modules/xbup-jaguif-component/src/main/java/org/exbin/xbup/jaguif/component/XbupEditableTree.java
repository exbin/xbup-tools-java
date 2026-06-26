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
package org.exbin.xbup.jaguif.component;

import java.io.IOException;
import java.io.InputStream;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.parser_tree.XBTTreeDocument;

/**
 * XBUP editable tree document.
 */
@NullMarked
public class XbupEditableTree extends XbupTree implements XBTEditableDocument {

    public XbupEditableTree(XBTEditableDocument document) {
        super(document);
    }

    @Override
    public void setRootBlock(XBTBlock block) {
        ((XBTEditableDocument) document).setRootBlock(block);
    }

    @Override
    public void setTailData(@Nullable InputStream source) throws IOException {
        ((XBTEditableDocument) document).setTailData(source);
    }

    @Override
    public void clear() {
        ((XBTEditableDocument) document).clear();
    }

    @Override
    public XBTBlock createNewBlock(@Nullable XBTBlock parent) {
        return ((XBTEditableDocument) document).createNewBlock(parent);
    }

    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        // TODO
        return ((XBTTreeDocument) document).fromStreamUB(stream);
    }
}
