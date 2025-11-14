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
package org.exbin.framework.data.gui;

import org.exbin.framework.action.ActionModule;
import org.exbin.framework.action.api.clipboard.EmptyTextClipboardSupport;
import org.exbin.framework.component.ComponentModule;
import org.exbin.framework.component.api.ContextEditItem;
import org.exbin.framework.component.api.ContextMoveItem;
import org.exbin.framework.component.api.action.EditItemActions;
import org.exbin.framework.component.api.action.EmptyContextEditItem;
import org.exbin.framework.component.api.action.MoveItemActions;
import org.exbin.framework.component.api.action.EmptyContextMoveItem;
import org.exbin.framework.operation.undo.OperationUndoModule;
import org.exbin.framework.operation.undo.api.EmptyUndoRedo;
import org.exbin.framework.operation.undo.api.UndoRedoState;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UiUtils;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
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
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            OperationUndoModule operationUndoModule = new OperationUndoModule();
            testApplication.addModule(OperationUndoModule.MODULE_ID, operationUndoModule);
            ActionModule guiActionModule = new ActionModule();
            testApplication.addModule(ActionModule.MODULE_ID, guiActionModule);
            ComponentModule guiComponentModule = new ComponentModule();
            testApplication.addModule(ComponentModule.MODULE_ID, guiComponentModule);

            DefinitionEditorPanel definitionEditorPanel = new DefinitionEditorPanel();
            UndoRedoState undoRedoHandler = new EmptyUndoRedo();
            definitionEditorPanel.setUndoHandler(undoRedoHandler, operationUndoModule.createUndoActions());
            EmptyTextClipboardSupport clipboardActionsController = new EmptyTextClipboardSupport();
            definitionEditorPanel.setClipboardController(clipboardActionsController, guiActionModule.getClipboardActions());
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
