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
package org.exbin.xbup.jaguif.editor.def.gui;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import org.exbin.jaguif.utils.TestApplication;
import org.exbin.jaguif.utils.UtilsModule;
import org.exbin.jaguif.utils.WindowUtils;
import org.junit.Test;

/**
 * Test for NodeBlockPanel.
 */
public class NodeBlockPanelTest {

    @Test
    public void testPanel() {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.jaguif.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.jaguif.language.api.TestLanguageModule());
            testApplication.addModule(org.exbin.jaguif.toolbar.api.ToolBarModuleApi.MODULE_ID, new org.exbin.jaguif.toolbar.ToolBarModule());
            testApplication.addModule(org.exbin.jaguif.contribution.api.ContributionModuleApi.MODULE_ID, new org.exbin.jaguif.contribution.ContributionModule());
            testApplication.addModule(org.exbin.jaguif.context.api.ContextModuleApi.MODULE_ID, new org.exbin.jaguif.context.ContextModule());
            testApplication.addModule(org.exbin.jaguif.menu.api.MenuModuleApi.MODULE_ID, new org.exbin.jaguif.menu.MenuModule());
            testApplication.addModule(org.exbin.jaguif.action.api.ActionModuleApi.MODULE_ID, new org.exbin.jaguif.action.ActionModule());
            WindowUtils.wrapInWindow(new NodeBlockPanel());
        });
    }
}
