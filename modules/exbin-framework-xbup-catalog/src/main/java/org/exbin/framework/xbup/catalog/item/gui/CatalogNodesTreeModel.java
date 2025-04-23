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
package org.exbin.framework.xbup.catalog.item.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;

/**
 * Table Model for Catalog Tree.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CatalogNodesTreeModel implements TreeModel {

    private XBCatalog catalog;
    private final List<TreeModelListener> treeModelListeners = new ArrayList<>();
    private CatalogNodesTreeItem rootItem = null;

    public CatalogNodesTreeModel() {
        this(null);
    }

    public CatalogNodesTreeModel(@Nullable XBCNode rootNode) {
        rootItem = rootNode == null ? null : new CatalogNodesTreeItem(rootNode);
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
        if (rootItem != null) {
            rootItem.updateNode();
        }
    }

    @Override
    public Object getRoot() {
        return rootItem;
    }

    @Nullable
    @Override
    public Object getChild(Object parent, int index) {
        if (parent == null) {
            return null;
        }

        return ((CatalogNodesTreeItem) parent).getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == null) {
            throw new NullPointerException("No parent");
        }

        return ((CatalogNodesTreeItem) parent).getChildren().size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return false;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((CatalogNodesTreeItem) parent).getChildren().indexOf(child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener tml) {
        treeModelListeners.add(tml);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener tml) {
        treeModelListeners.remove(tml);
    }

    @Nonnull
    public TreePath findPathForSpec(XBCSpec spec) {
        Long[] specPath = catalog.getSpecPath(spec);
        CatalogNodesTreeItem[] nodePath = new CatalogNodesTreeItem[specPath.length - 1];
        CatalogNodesTreeItem node = rootItem;
        for (int specPathDepth = 0; specPathDepth < specPath.length - 1; specPathDepth++) {
            Long specPathIndex = specPath[specPathDepth];
            List<CatalogNodesTreeItem> children = node.getChildren();
            for (CatalogNodesTreeItem child : children) {
                if (child.getNode().getXBIndex() == specPathIndex) {
                    nodePath[specPathDepth] = child;
                    node = child;
                    break;
                }
            }
        }

        return new TreePath(nodePath);
    }

    @ParametersAreNonnullByDefault
    public class CatalogNodesTreeItem {

        private XBCNode node;

        private String name;
        private boolean loaded = false;
        private final List<CatalogNodesTreeItem> children = new ArrayList<>();

        public CatalogNodesTreeItem(XBCNode node) {
            this.node = node;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public XBCNode getNode() {
            return node;
        }

        public void setNode(XBCNode node) {
            this.node = node;
            updateNode();
        }

        private void updateNode() {
            XBCXNameService nameService = catalog.getCatalogService(XBCXNameService.class);
            name = nameService.getDefaultText(node);
        }

        @Nonnull
        public List<CatalogNodesTreeItem> getChildren() {
            if (!loaded) {
                XBCNodeService nodeService = catalog.getCatalogService(XBCNodeService.class);
                List<XBCNode> subNodes = nodeService.getSubNodes(((XBCNode) node));
                for (XBCNode subNode : subNodes) {
                    CatalogNodesTreeItem subItem = new CatalogNodesTreeItem(subNode);
                    subItem.updateNode();
                    children.add(subItem);
                }

                loaded = true;
            }

            return children;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + Objects.hashCode(this.node);
            return hash;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CatalogNodesTreeItem other = (CatalogNodesTreeItem) obj;
            return Objects.equals(this.node, other.node);
        }
    }
}
