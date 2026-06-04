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
package org.exbin.xbup.jaguif.data.gui;

import org.exbin.jaguif.action.ActionModule;
import org.exbin.jaguif.component.ComponentModule;
import org.exbin.jaguif.component.api.ContextEditItem;
import org.exbin.jaguif.component.api.ContextMoveItem;
import org.exbin.jaguif.component.api.action.EditItemActions;
import org.exbin.jaguif.component.api.action.EmptyContextEditItem;
import org.exbin.jaguif.component.api.action.MoveItemActions;
import org.exbin.jaguif.component.api.action.EmptyContextMoveItem;
import org.exbin.jaguif.operation.undo.OperationUndoModule;
import org.exbin.jaguif.operation.undo.api.EmptyUndoRedo;
import org.exbin.jaguif.operation.undo.api.UndoRedoState;
import org.exbin.jaguif.utils.TestApplication;
import org.exbin.jaguif.utils.UiUtils;
import org.exbin.jaguif.utils.UtilsModule;
import org.exbin.jaguif.utils.WindowUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for DefinitionEditorPanel.
 */
public class DefinitionEditorPanelTest {

    @Test
    @Ignore
    public void testPanel() {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.jaguif.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.jaguif.language.api.TestLanguageModule());
            OperationUndoModule operationUndoModule = new OperationUndoModule();
            testApplication.addModule(OperationUndoModule.MODULE_ID, operationUndoModule);
            ActionModule guiActionModule = new ActionModule();
            testApplication.addModule(ActionModule.MODULE_ID, guiActionModule);
            ComponentModule guiComponentModule = new ComponentModule();
            testApplication.addModule(ComponentModule.MODULE_ID, guiComponentModule);

            DefinitionEditorPanel definitionEditorPanel = new DefinitionEditorPanel();
//            UndoRedoState undoRedoHandler = new EmptyUndoRedo();
//            definitionEditorPanel.setUndoHandler(undoRedoHandler, operationUndoModule.createUndoActions());
//            EmptyTextClipboardSupport clipboardActionsController = new EmptyTextClipboardSupport();
//            definitionEditorPanel.setClipboardController(clipboardActionsController, guiActionModule.getClipboardActions());
            WindowUtils.invokeWindow(definitionEditorPanel);

            ContextMoveItem moveItems = new EmptyContextMoveItem();
            MoveItemActions moveItemActions = guiComponentModule.createMoveItemActions(moveItems);
            ContextEditItem editItems = new EmptyContextEditItem();
            EditItemActions editItemActions = guiComponentModule.createEditItemActions(editItems);
            definitionEditorPanel.registerToolBarActions(editItemActions, moveItemActions);
        });

        UiUtils.waitForUiThread();
    }
}
