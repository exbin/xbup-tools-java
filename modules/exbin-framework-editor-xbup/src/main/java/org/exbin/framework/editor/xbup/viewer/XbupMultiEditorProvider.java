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
package org.exbin.framework.editor.xbup.viewer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.swing.CodeAreaCore;
import org.exbin.framework.App;
import org.exbin.framework.bined.BinaryMultiEditorProvider;
import org.exbin.framework.editor.DefaultMultiEditorProvider;
import org.exbin.framework.editor.xbup.gui.BlockPropertiesPanel;
import org.exbin.framework.editor.MultiEditorUndoHandler;
import org.exbin.framework.editor.api.MultiEditorProvider;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.framework.utils.ClipboardActionsHandler;
import org.exbin.framework.utils.ClipboardActionsUpdateListener;
import org.exbin.framework.window.api.gui.CloseControlPanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.plugin.XBPluginRepository;
import org.exbin.framework.file.api.FileHandler;
import org.exbin.framework.operation.undo.api.UndoActionsHandler;
import org.exbin.framework.operation.undo.api.UndoFileHandler;
import org.exbin.framework.operation.undo.api.UndoUpdateListener;
import org.exbin.framework.window.api.WindowHandler;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.operation.Command;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.xbup.operation.undo.XBUndoUpdateListener;

/**
 * Multi editor provider.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupMultiEditorProvider extends DefaultMultiEditorProvider implements XbupEditorProvider, MultiEditorProvider {

    private XBACatalog catalog;

    private MultiEditorUndoHandler undoHandler = new MultiEditorUndoHandler();
    private ClipboardActionsHandler activeHandler;

    private XBPluginRepository pluginRepository;
    private final List<DocumentItemSelectionListener> itemSelectionListeners = new ArrayList<>();
    private ClipboardActionsUpdateListener clipboardActionsUpdateListener;

    private boolean devMode = false;

    public XbupMultiEditorProvider() {
        init();
    }

    private void init() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.addFlavorListener((FlavorEvent e) -> {
            // TODO updateClipboardActionsStatus();
        });
    }

    @Nonnull
    @Override
    public XBUndoHandler getUndoHandler() {
        return undoHandler;
    }

    @Override
    public void activeFileChanged() {
        super.activeFileChanged();

        undoHandler.setActiveFile(activeFile);
        // TODO
        componentActivationListener.updated(CodeAreaCore.class, null);
        UndoActionsHandler undoActionsHandler = null;
        if (activeFile instanceof UndoFileHandler) {
            XBUndoHandler undoHandler = ((UndoFileHandler) activeFile).getUndoHandler();
            undoActionsHandler = new UndoActionsHandler() {
                @Override
                public boolean canUndo() {
                    return undoHandler.canUndo();
                }

                @Override
                public boolean canRedo() {
                    return undoHandler.canRedo();
                }

                @Override
                public void performUndo() {
                    try {
                        undoHandler.performUndo();
                    } catch (Exception ex) {
                        Logger.getLogger(BinaryMultiEditorProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void performRedo() {
                    try {
                        undoHandler.performRedo();
                    } catch (Exception ex) {
                        Logger.getLogger(BinaryMultiEditorProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void performUndoManager() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void setUndoUpdateListener(UndoUpdateListener undoUpdateListener) {
                    undoHandler.addUndoUpdateListener(new XBUndoUpdateListener() {
                        @Override
                        public void undoCommandPositionChanged() {
                            undoUpdateListener.undoChanged();
                        }

                        @Override
                        public void undoCommandAdded(Command command) {
                            undoUpdateListener.undoChanged();
                        }
                    });
                }
            };
        }
        componentActivationListener.updated(UndoActionsHandler.class, undoActionsHandler);

        if (activeFile == null) {
            notifyItemSelectionChanged(null);
        } else {
            notifyItemSelectionChanged(((XbupFileHandler) activeFile).getSelectedItem().orElse(null));
        }

        if (clipboardActionsUpdateListener != null) {
            // TODO updateClipboardActionsStatus();
        }
    }

    @Nonnull
    @Override
    public XBACatalog getCatalog() {
        return catalog;
    }

    @Override
    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        if (activeFile != null) {
            ((XbupFileHandler) activeFile).setCatalog(catalog);
        }
    }

    @Nonnull
    @Override
    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    @Override
    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
        for (FileHandler fileHandler : fileHandlers) {
            XbupFileHandler xbupFileHandler = (XbupFileHandler) fileHandler;
            xbupFileHandler.setPluginRepository(pluginRepository);
        }
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
        // activeFile.setDevMode(devMode);
    }

    /*
    @Nonnull
    @Override
    public String getWindowTitle(String frameTitle) {
        XBTTreeDocument treeDocument = activeFile.getDocument();
        String title = treeDocument.getFileName();
        if (!"".equals(title)) {
            int pos;
            int newpos = 0;
            do {
                pos = newpos;
                newpos = title.indexOf(File.separatorChar, pos) + 1;
            } while (newpos > 0);
            return title.substring(pos) + " - " + frameTitle;
        }

        return frameTitle;
    } */

    /*
    @Override
    public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
        clipboardActionsUpdateListener = updateListener;
        // activeFile.setUpdateListener(updateListener);
    } */

    public void actionItemProperties() {
        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        BlockPropertiesPanel panel = new BlockPropertiesPanel();
        panel.setCatalog(catalog);
        if (activeFile instanceof XbupFileHandler) {
            panel.setBlock(((XbupFileHandler) activeFile).getSelectedItem().orElse(null));
        }
        CloseControlPanel controlPanel = new CloseControlPanel();
        final WindowHandler dialog = windowModule.createDialog(panel, controlPanel);
        controlPanel.setHandler(() -> {
            dialog.close();
            dialog.dispose();
        });
        dialog.showCentered(null);
    }

    @Override
    public void addItemSelectionListener(DocumentItemSelectionListener listener) {
        itemSelectionListeners.add(listener);
    }

    @Override
    public void removeItemSelectionListener(DocumentItemSelectionListener listener) {
        itemSelectionListeners.remove(listener);
    }

    private void notifyItemSelectionChanged(@Nullable XBTBlock item) {
        itemSelectionListeners.forEach(listener -> {
            listener.itemSelected(item);
        });
    }

    @Nonnull
    @Override
    public XbupFileHandler createFileHandler(int id) {
        XbupFileHandler fileHandler = new XbupFileHandler(id);
        fileHandler.addItemSelectionListener((block) -> {
            if (activeFile == fileHandler) {
                notifyItemSelectionChanged(block);
            }
        });
        fileHandler.setCatalog(catalog);
        fileHandler.setPluginRepository(pluginRepository);
        fileHandler.getUndoHandler().addUndoUpdateListener(new XBUndoUpdateListener() {
            @Override
            public void undoCommandPositionChanged() {
                undoHandler.notifyUndoUpdate();
            }

            @Override
            public void undoCommandAdded(Command cmnd) {
                undoHandler.notifyUndoCommandAdded(cmnd);
            }
        });

        return fileHandler;
    }

    @Nonnull
    @Override
    public String getName(FileHandler fileHandler) {
        String name = fileHandler.getTitle();
        if (!name.isEmpty()) {
            return name;
        }

        return "New File " + newFilesMap.get(fileHandler.getId());
    }
}
