/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.tool.xbshell;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * XBUP Protocol Shell Browser Tool
 *
 * @version 0.2.0 2016/03/12
 * @author ExBin Project (http://exbin.org)
 */
public class Prompt {

    private File file;
    private String path;

    /**
     * Run prompt on given file.
     *
     * @param file
     */
    public void run(File file) {
        this.file = file;
        path = "";

        System.out.println("XBShell interface (version 0.2.1 DEV)");
        String command = "";
        byte[] input = new byte[30];
        do {
            try {
                System.out.print("xbup:" + path + "$ ");
                System.out.flush();
                System.in.read(input);
                command = (new String(input)).trim();
                if (command.equals("help")) {
                    System.out.println("XBShell 0.2.1 DEV (C) ExBin Project https://exbin.org");
                    System.out.println("Usage: xbshell [options] [path]filename");
                    System.out.println("Commands: help exit ls cp mv");
                } else if (command.equals("help")) {

                }
            } catch (IOException ex) {
                Logger.getLogger(Prompt.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!command.equals("exit"));
    }
}
