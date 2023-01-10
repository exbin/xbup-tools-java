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
package org.exbin.framework.xbup.catalog.item.revision.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.data.model.CatalogRevsTableItem;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.gui.DefaultControlPanel;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.framework.xbup.catalog.item.revision.gui.CatalogSpecRevEditorPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Add new revision action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class RemoveItemRevisionAction extends AbstractAction {

    public static final String ACTION_ID = "addCatalogItemRevisionAction";
    
    private XBApplication application;
    private XBACatalog catalog;

    private Component parentComponent;
    private XBCNode currentNode;
    private int currentCount;
    private String resultName;
    private byte[] resultData;

    public RemoveItemRevisionAction() {
    }

    public void setup(XBApplication application) {
        this.application = application;
    }

    @Nullable
    public XBCNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(XBCNode currentNode) {
        this.currentNode = currentNode;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    @Nullable
    public String getResultName() {
        return resultName;
    }

    @Nullable
    public byte[] getResultData() {
        return resultData;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultName = null;
        resultData = null;
//        int selectedRow = itemRevisionsTable.getSelectedRow();
//        CatalogRevsTableItem revItem = revsModel.getRowItem(selectedRow);
//
//        if (updateList.contains(revItem)) {
//            updateList.remove(revItem);
//        }
//
//        removeList.add(revItem);
//        revsModel.getRevs().remove(revItem);
//        revsModel.fireTableDataChanged();
//        defsModel.updateDefRevisions();
//        updateItemStatus();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
