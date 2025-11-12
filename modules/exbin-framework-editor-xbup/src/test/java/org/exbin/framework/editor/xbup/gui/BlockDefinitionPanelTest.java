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
package org.exbin.framework.editor.xbup.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.swing.SwingUtilities;
import org.exbin.framework.utils.TestApplication;
import org.exbin.framework.utils.UtilsModule;
import org.exbin.framework.utils.WindowUtils;
import org.junit.Test;

/**
 * Test for BlockDefinitionPanel.
 */
public class BlockDefinitionPanelTest {

    @Test
    public void testPanel() {
        TestApplication testApplication = UtilsModule.createTestApplication();
        testApplication.launch(() -> {
            testApplication.addModule(org.exbin.framework.language.api.LanguageModuleApi.MODULE_ID, new org.exbin.framework.language.api.utils.TestLanguageModule());
            WindowUtils.invokeWindow(new BlockDefinitionPanel());
        });

        Thread[] uiThread = new Thread[1];
        try {
            SwingUtilities.invokeAndWait(() -> {
                uiThread[0] = Thread.currentThread();
            });
            uiThread[0].join();
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(BlockDefinitionPanelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Prints supported formats to system console.
     *
     * @param li the line information.
     */
    private static void showFormats(Line.Info li) {
        if (li instanceof DataLine.Info) {
            AudioFormat[] afs = ((DataLine.Info) li).getFormats();
            for (AudioFormat af : afs) {
                System.out.println("        " + af.toString());
            }
        }
    }

    public static void testMixers() {
        // loop through all mixers, and all source and target lines within each mixer.
        Mixer.Info[] mis = AudioSystem.getMixerInfo();
        for (Mixer.Info mi : mis) {
            Mixer mixer = AudioSystem.getMixer(mi);
            // e.g. com.sun.media.sound.DirectAudioDevice
            System.out.println("mixer: " + mixer.getClass().getName());
            Line.Info[] lis = mixer.getSourceLineInfo();
            for (Line.Info li : lis) {
                System.out.println("    source line: " + li.toString());

                showFormats(li);
            }
            lis = mixer.getTargetLineInfo();
            for (Line.Info li : lis) {
                System.out.println("    target line: " + li.toString());
                showFormats(li);
            }
            Control[] cs = mixer.getControls();
            for (Control c : cs) {
                System.out.println("    control: " + c.toString());
            }
        }
    }

}
