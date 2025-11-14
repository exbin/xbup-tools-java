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
package org.exbin.framework.editor.xbup.def.gui;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import org.exbin.auxiliary.binary_data.EmptyBinaryData;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.editor.api.EditorProviderVariant;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.options.api.utils.TestOptionsModule;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UiUtils;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for BinaryDataPanel.
 */
public class BinaryDataPanelTest {

    @Test
    @Ignore
    public void testPanel() {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            testApplication.addModule(OptionsModuleApi.MODULE_ID, new TestOptionsModule());
            testApplication.addModule(FileModuleApi.MODULE_ID, new org.exbin.framework.file.api.utils.TestFileModule());
            BinedModule binedModule = new BinedModule();
            binedModule.initEditorProvider(EditorProviderVariant.SINGLE);
            testApplication.addModule(BinedModule.MODULE_ID, binedModule);
            BinaryDataPanel binaryDataPanel = new BinaryDataPanel();
            binaryDataPanel.setContentData(EmptyBinaryData.getInstance());
            WindowUtils.invokeWindow(binaryDataPanel);
        });

        UiUtils.waitForUiThread();
    }
}
