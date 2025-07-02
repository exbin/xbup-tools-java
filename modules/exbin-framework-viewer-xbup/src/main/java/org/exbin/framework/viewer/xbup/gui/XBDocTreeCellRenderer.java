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
package org.exbin.framework.viewer.xbup.gui;

import java.awt.Color;
import java.awt.Component;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.exbin.framework.viewer.xbup.viewer.XbupTreeDocument;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Tree cell renderer for XBUP document tree.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDocTreeCellRenderer extends DefaultTreeCellRenderer {

    private XbupTreeDocument treeDocument;
    private final ImageIcon dataBlockIcon;

    public XBDocTreeCellRenderer() {
        super();
        dataBlockIcon = new ImageIcon(getClass().getResource("/org/exbin/framework/viewer/xbup/resources/icons/data-block-16x16.png"));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
//        setLeafIcon(leafIcon);
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof XBTTreeNode) {
            XBTTreeNode node = ((XBTTreeNode) value);
            ImageIcon icon = null;
            String caption = null;
            if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                if (node.getDataSize() == 0) {
                    caption = "Empty Block";
                } else {
                    caption = "Data Block";
                }
                icon = dataBlockIcon;
            } else {
                if (treeDocument != null) {
                    caption = treeDocument.getBlockCaption(node.getBlockDecl());
                }

                if (treeDocument != null) {
                    icon = treeDocument.getBlockIcon(node.getBlockDecl());
                }
            }

            if (caption != null) {
                setText(caption);
            } else {
                setText("Undefined");
                setForeground(Color.RED);
            }

            if (icon != null) {
                setIcon(icon);
            }
        }
        return this;
    }

    public void setTreeDocument(XbupTreeDocument treeDocument) {
        this.treeDocument = treeDocument;
    }
}
