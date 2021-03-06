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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * XBUP Protocol Shell Browser Tool
 *
 * @version 0.1.18 2009/08/02
 * @author ExBin Project (http://exbin.org)
 */
public class XBShell {

    private XBShell() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Options opt = new Options();

            opt.addOption("h", false, "Print help for this application");
            opt.addOption("u", true, "The username to use");
            opt.addOption("dsn", true, "The data source to use");
            opt.addOption("dev", false, "Development mode");

            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);

            if (cl.hasOption('h')) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp("xbsh", opt);
            } else {
//                System.out.println(cl.getOptionValue("u"));
//                System.out.println(cl.getOptionValue("dsn"));
                Prompt prompt = new Prompt();
                prompt.run(null);
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBShell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
