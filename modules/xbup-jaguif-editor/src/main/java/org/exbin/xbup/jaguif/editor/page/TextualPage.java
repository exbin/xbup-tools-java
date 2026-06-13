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
package org.exbin.xbup.jaguif.editor.page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.nio.charset.Charset;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.exbin.jaguif.document.text.gui.TextPanel;
import org.exbin.jaguif.document.text.service.TextSearchService;
import org.exbin.jaguif.tabpages.api.AbstractTabPagesComponent;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.jaguif.component.XbupTree;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Text viewer of document.
 */
@ParametersAreNonnullByDefault
public class TextualPage extends AbstractTabPagesComponent implements XbupEditorPage {

    protected final JPanel wrapperPanel = new JPanel(new BorderLayout());
    protected final TextPanel textPanel;
    protected XbupTree xbupTree;

    public TextualPage() {
        putValue(KEY_NAME, "Text");
        putValue(KEY_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/exbin/xbup/jaguif/editor/resources/icons/16px/format-text-smallcaps.png")));
        textPanel = new TextPanel();
        textPanel.setNoBorder();
        textPanel.setEditable(false);
        wrapperPanel.add(textPanel, BorderLayout.CENTER);
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return wrapperPanel;
    }

    @Override
    public void setXbupTree(XbupTree xbupTree) {
        if (xbupTree == this.xbupTree) {
            return;
        }
        
        String text = "<!XBUP version=\"0.1\">\n";
//            XBTBlock parent = block.getParent();
//            if (parent == null) {
//                text += nodeAsText((XBTTreeNode) parent, "").toString();
//            }
        text = nodeAsText((XBTTreeNode) xbupTree.getRootBlock().orElse(null), "").toString();
        textPanel.setText(text);

        this.xbupTree = xbupTree;
    }

    public Color[] getDefaultColors() {
        return textPanel.getDefaultColors();
    }

    public void setCurrentColors(Color[] colors) {
        textPanel.setCurrentColors(colors);
    }

    public Font getDefaultFont() {
        return textPanel.getDefaultFont();
    }

    public void setCurrentFont(Font deriveFont) {
        textPanel.setCurrentFont(deriveFont);
    }

    public boolean changeLineWrap() {
        return textPanel.changeLineWrap();
    }

    public int getLineCount() {
        return textPanel.getLineCount();
    }

    public void gotoRelative(int charPos) {
        textPanel.gotoRelative(charPos);
    }

    public void gotoLine(int line) {
        textPanel.gotoLine(line);
    }

    public void findText(TextSearchService.FindTextParameters findTextParameters) {
        textPanel.findText(findTextParameters);
    }

    public void setCharset(Charset charset) {
        textPanel.setCharset(charset);
    }

    public Color[] getCurrentColors() {
        return textPanel.getCurrentColors();
    }

    public Font getCurrentFont() {
        return textPanel.getCurrentFont();
    }

    @Nonnull
    private StringBuffer nodeAsText(@Nullable XBTTreeNode node, String prefix) {
        StringBuffer result = new StringBuffer();
        result.append(prefix);
        if (node == null) {
            return result;
        }

        if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            result.append("[");
            for (long i = 0; i < node.getDataSize(); i++) {
                byte b = node.getBlockData().getByte(i);
                result.append(getHex(b));
            }
            result.append("]\n");
        } else {
            result.append("<").append(getCaption(node));
            if (node.getAttributesCount() > 2) {
                XBAttribute[] attributes = node.getAttributes();
                for (int i = 0; i < attributes.length; i++) {
                    XBAttribute attribute = attributes[i];
                    result.append(" ").append(i + 1).append("=\"").append(attribute.getNaturalLong()).append("\"");
                }
            }

            if (node.getChildren() != null) {
                result.append(">\n");
                XBTBlock[] children = node.getChildren();
                for (XBTBlock child : children) {
                    result.append(nodeAsText((XBTTreeNode) child, prefix + "  "));
                }
                result.append(prefix);
                result.append("</").append(getCaption(node)).append(">\n");
            } else {
                result.append("/>\n");
            }
        }
        return result;
    }

    public static String getHex(byte b) {
        byte low = (byte) (b & 0xf);
        byte hi = (byte) (b >> 0x8);
        return (Integer.toHexString(hi) + Integer.toHexString(low)).toUpperCase();
    }

    @Nonnull
    private String getCaption(XBTTreeNode node) {
        if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            return "Data Block";
        }
        XBACatalog catalog = xbupTree.getCatalog();
        XBBlockType blockType = node.getBlockType();
        if (catalog != null) {
            XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
            XBCBlockDecl blockDecl = (XBCBlockDecl) node.getBlockDecl();
            if (blockDecl != null) {
                XBCBlockSpec blockSpec = blockDecl.getBlockSpecRev().getParent();
                return nameService.getDefaultText(blockSpec);
            }
        }
        return "Unknown" + " (" + Integer.toString(((XBFBlockType) blockType).getGroupID().getInt()) + ", " + Integer.toString(((XBFBlockType) blockType).getBlockID().getInt()) + ")";
    }
}
