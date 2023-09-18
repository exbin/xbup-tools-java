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
package org.exbin.framework.editor.xbup.def;

import java.awt.Component;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.bined.BinEdFileHandler;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.bined.handler.CodeAreaPopupMenuHandler;
import org.exbin.framework.component.api.ActionsProvider;
import org.exbin.framework.component.api.toolbar.SideToolBar;
import org.exbin.framework.editor.xbup.BlockEditor;
import org.exbin.framework.editor.xbup.def.action.ExportDataAction;
import org.exbin.framework.editor.xbup.def.action.ImportDataAction;
import org.exbin.framework.editor.xbup.def.gui.BinaryDataPanel;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Binary data editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataEditor {

    private BinaryDataPanel editorPanel = new BinaryDataPanel();
    private XBApplication application;
    private XBACatalog catalog;
    private JPopupMenu popupMenu;
    private final ActionsProvider actions;

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(BinaryDataEditor.class);

    private ImportDataAction importDataAction = new ImportDataAction();
    private ExportDataAction exportDataAction = new ExportDataAction();

    public BinaryDataEditor() {
        actions = (SideToolBar sideToolBar) -> {
            sideToolBar.addAction(importDataAction);
            sideToolBar.addAction(exportDataAction);
        };

        editorPanel.addActions(actions);
    }

    @Nonnull
    public BinaryDataPanel getEditorPanel() {
        return editorPanel;
    }

    public void setApplication(XBApplication application) {
        this.application = application;
        editorPanel.setApplication(application);
        importDataAction.setup(application);
        exportDataAction.setup(application);

        BinedModule binedModule = application.getModuleRepository().getModuleByInterface(BinedModule.class);
        CodeAreaPopupMenuHandler codeAreaPopupMenuHandler = binedModule.createCodeAreaPopupMenuHandler(BinedModule.PopupMenuVariant.BASIC);
        popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                int clickedX = x;
                int clickedY = y;
                if (invoker instanceof JViewport) {
                    clickedX += ((JViewport) invoker).getParent().getX();
                    clickedY += ((JViewport) invoker).getParent().getY();
                }
                JPopupMenu popupMenu = binedModule.createBinEdComponentPopupMenu(codeAreaPopupMenuHandler, editorPanel.getComponentPanel(), clickedX, clickedY);
                popupMenu.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        codeAreaPopupMenuHandler.dropPopupMenu(BinedModule.BINARY_POPUP_MENU_ID);
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                    }
                });

                JMenuItem importDataMenuItem = ActionUtils.actionToMenuItem(importDataAction);
                importDataMenuItem.setText(importDataAction.getValue(Action.NAME) + ActionUtils.DIALOG_MENUITEM_EXT);
                popupMenu.add(importDataMenuItem);
                JMenuItem exportDataMenuItem = ActionUtils.actionToMenuItem(exportDataAction);
                exportDataMenuItem.setText(exportDataAction.getValue(Action.NAME) + ActionUtils.DIALOG_MENUITEM_EXT);
                popupMenu.add(exportDataMenuItem);

                popupMenu.show(invoker, x, y);
                binedModule.dropBinEdComponentPopupMenu();
            }
        };

        editorPanel.getComponentPanel().setPopupMenu(popupMenu);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setBlock(XBTTreeNode block) {
        try {
            BinEdFileHandler binaryDataFile = new BinEdFileHandler();
            binaryDataFile.loadFromStream(block.getData(), block.getDataSize());
            editorPanel.setFileHandler(binaryDataFile);
        } catch (IOException ex) {
            Logger.getLogger(BlockEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
