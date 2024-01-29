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
package org.exbin.framework.editor.xbup.def.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;

/**
 * Add attribute action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class AddAttributeAction extends AbstractAction {

    public static final String ACTION_ID = "addAttributeAction";

    private final ResourceBundle resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(AddAttributeAction.class);

    public AddAttributeAction() {
    }

    public void setup() {
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        actionModule.setupAction(this, resourceBundle, ACTION_ID);
    }

    public void setCatalog(@Nullable XBACatalog catalog) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
