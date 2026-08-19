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
package org.exbin.xbup.jaguif.catalog.item.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.action.api.DialogParentComponent;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.window.api.WindowModuleApi;
import org.exbin.xbup.jaguif.catalog.item.gui.CatalogAddItemPanel;
import org.exbin.jaguif.window.api.WindowHandler;
import org.exbin.jaguif.window.api.gui.DefaultControlPanel;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.jaguif.window.api.controller.DefaultControlController;

/**
 * Add item to catalog action.
 */
@NullMarked
public class AddCatalogItemAction extends AbstractAction {

    public static final String ACTION_ID = "addCatalogItem";

    protected final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AddCatalogItemAction.class);
    protected @Nullable XBACatalog catalog;

    protected @Nullable DialogParentComponent parentComponent;
    protected @Nullable XBCItem currentItem;
    protected @Nullable XBCItem resultItem;

    public AddCatalogItemAction() {
    }

    public void init() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.initAction(this, resourceBundle, ACTION_ID);
        putValue(ActionConsts.ACTION_DIALOG_MODE, true);
        putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
            @Override
            public void register(ContextChangeRegistration registrar) {
                registrar.registerChangeListener(DialogParentComponent.class, (instance) -> {
                    parentComponent = instance;
                });
                registrar.registerChangeListener(XBACatalog.class, (instance) -> {
                    catalog = instance;
                });
            }
        });
    }

    @Nullable
    public XBCItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(@Nullable XBCItem currentItem) {
        this.currentItem = currentItem;
    }

    @Nullable
    public XBCItem getResultItem() {
        return resultItem;
    }

    public void setParentComponent(DialogParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(@Nullable ActionEvent event) {
        resultItem = null;

        XBCNodeService nodeService = catalog == null ? null : catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = catalog == null ? null : catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = catalog == null ? null : catalog.getCatalogService(XBCRevService.class);
        XBCXNameService nameService = catalog == null ? null : catalog.getCatalogService(XBCXNameService.class);

        WindowModuleApi windowModule = App.getModule(WindowModuleApi.class);
        final CatalogAddItemPanel panel = new CatalogAddItemPanel();
        DefaultControlPanel controlPanel = new DefaultControlPanel();
        JPanel dialogPanel = windowModule.createDialogPanel(panel, controlPanel);
        final WindowHandler dialog = windowModule.createDialog(dialogPanel);
        windowModule.setWindowTitle(dialog, panel.getResourceBundle());
        controlPanel.setController((DefaultControlController.ControlActionType actionType) -> {
            switch (actionType) {
                case OK: {
                    // TODO: Use different transaction management later
                    EntityManager em = ((XBECatalog) catalog).getEntityManager();
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();

                    XBENode node = (XBENode) (currentItem instanceof XBCNode ? currentItem : currentItem.getParentItem().orElse(null));
                    XBEItem item = null;
                    switch (panel.getItemType()) {
                        case NODE: {
                            item = (XBENode) nodeService.createItem();
                            Long newXbIndex = nodeService.findMaxSubNodeXB(node);
                            item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                            break;
                        }
                        case BLOCK: {
                            item = (XBESpec) specService.createBlockSpec();
                            Long newXbIndex = specService.findMaxBlockSpecXB(node);
                            item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                            break;
                        }
                        case GROUP: {
                            item = (XBESpec) specService.createGroupSpec();
                            Long newXbIndex = specService.findMaxBlockSpecXB(node);
                            item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                            break;
                        }
                        case FORMAT: {
                            item = (XBESpec) specService.createFormatSpec();
                            Long newXbIndex = specService.findMaxBlockSpecXB(node);
                            item.setXBIndex(newXbIndex == null ? 0 : newXbIndex + 1);
                            break;
                        }
                        default: {
                            throw new IllegalStateException();
                        }
                    }

                    if (item == null) {
                        throw new IllegalStateException();
                    }
                    item.setParentItem(node);
                    if (item instanceof XBCNode) {
                        nodeService.persistItem((XBCNode) item);
                    } else {
                        specService.persistItem((XBCSpec) item);
                    }
                    ((XBEXNameService) nameService).setDefaultText(item, panel.getItemName());
                    em.flush();
                    transaction.commit();

                    resultItem = item;
                    break;
                }
                case CANCEL: {
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected action type " + actionType.name());
            }
            dialog.close();
        });
        dialog.showCentered(parentComponent.getComponent());
        dialog.dispose();
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
        this.catalog = catalog;
    }
}
