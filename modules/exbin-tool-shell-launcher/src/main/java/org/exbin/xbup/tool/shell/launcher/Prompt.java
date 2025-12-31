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
package org.exbin.xbup.tool.shell.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.basic.BasicApplication;
import org.exbin.framework.ApplicationBundleKeys;
import org.exbin.framework.language.api.LanguageModuleApi;

/**
 * Shell browser tool for XBUP document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class Prompt {

    private final BasicApplication app;
    private File file;
    private String path;

    public Prompt(BasicApplication app) {
        this.app = app;
    }

    /**
     * Runs prompt on given file.
     *
     * @param file input file
     */
    public void run(File file) {
        this.file = file;
        path = "";

        ResourceBundle appBundle = App.getAppBundle();
        System.out.println("XBShell interface (version " + appBundle.getString(ApplicationBundleKeys.APPLICATION_VERSION) + ")");
        String command = "";
        byte[] input = new byte[30];
        do {
            try {
                System.out.print("xbup:" + path + "$ ");
                System.out.flush();
                System.in.read(input);
                command = (new String(input)).trim();
                if (command.equals("help")) {
                    System.out.println("XBUP Shell " + appBundle.getString(ApplicationBundleKeys.APPLICATION_VERSION) + " (C) ExBin Project https://exbin.org");
                    System.out.println("Usage: xbupshell [options] [path]filename");
                    System.out.println("Commands: help exit ls cp mv");
                } else if (command.equals("help")) {

                }
            } catch (IOException ex) {
                Logger.getLogger(Prompt.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!command.equals("exit"));
    }
}
