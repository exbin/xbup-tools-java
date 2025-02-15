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
package org.exbin.xbup.service;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.exbin.framework.preferences.PreferencesWrapper;
import org.exbin.xbup.client.XBTCPServiceClient;
import org.exbin.xbup.core.parser.basic.XBHead;

/**
 * Console class for XBUP framework service.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class Service {

    private Service() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
/*        try {
            final ResourceBundle recourceBundle = ResourceBundle.getBundle("org/exbin/xbup/service/XBService");

            // Parameters processing
            Options opt = new Options();
            Logger logger = Logger.getLogger("");
            logger.setLevel(Level.ALL);

            opt.addOption("h", "help", false, recourceBundle.getString("cl_option_help"));
            opt.addOption("port", true, recourceBundle.getString("cl_option_port"));
            opt.addOption("ip", true, recourceBundle.getString("cl_option_ip"));
            opt.addOption("v", false, recourceBundle.getString("cl_option_verbose"));
            opt.addOption("nopref", false, recourceBundle.getString("cl_option_nopref"));
            opt.addOption("stop", false, recourceBundle.getString("cl_option_stop"));
            opt.addOption("log", true, recourceBundle.getString("cl_option_log"));
            opt.addOption("db", true, recourceBundle.getString("cl_option_db"));
            opt.addOption("rootcat", false, recourceBundle.getString("cl_option_rootcat"));
            opt.addOption("dev", false, recourceBundle.getString("cl_option_dev"));
            opt.addOption("update", false, recourceBundle.getString("cl_option_update"));
            //opt.addOption("derby", false, myBundle.getString("cl_option_derby"));
            //opt.addOption("noderby", false, myBundle.getString("cl_option_noderby"));

            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);

            if (cl.hasOption('h')) {
                logger.addHandler(new XBHead.XBLogHandler(false));
                logger.addHandler(new XBCatalogNetServiceServer.XBServiceLogHandler(true));
                Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, recourceBundle.getString("service_head"));
                Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
                HelpFormatter f = new HelpFormatter();
                f.printHelp(recourceBundle.getString("cl_syntax"), opt);
            } else {
                XBBaseApplication app = new XBBaseApplication();
                app.setAppDirectory(XBService.class);
                Preferences preferences = app.createPreferences(XBService.class);
                // Preferences processing
                boolean verboseMode = cl.hasOption("v") || Boolean.getBoolean(preferences.get("verbose", Boolean.toString(false)));
                boolean devMode = cl.hasOption("dev");
                boolean forceUpdate = cl.hasOption("update");
                if (devMode) {
                    Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "Set development mode.");
                }
                boolean rootCatalogMode = cl.hasOption("rootcat");
                if (rootCatalogMode) {
                    Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "Set root catalog mode.");
                }
//                if (cl.hasOption("nopref"))
                // 22594 is 0x5842 (XB)
                String tcpipPort = cl.getOptionValue("port", preferences.get("tcpip_port", ((devMode) ? String.valueOf(XBTCPServiceClient.DEFAULT_DEV_PORT) : String.valueOf(XBTCPServiceClient.DEFAULT_PORT))));
                String tcpipInterface = cl.getOptionValue("ip", preferences.get("tcpip_interface", "localhost"));
                logger.addHandler(new XBHead.XBLogHandler(verboseMode));
                logger.addHandler(new XBCatalogNetServiceServer.XBServiceLogHandler(true));

                XBServiceInstance server = new XBServiceInstance();
                server.setResourceBundle(recourceBundle);
                server.setDerbyPath(((PreferencesWrapper) preferences).getInnerPreferences().absolutePath());
                server.setTcpipInterface(tcpipInterface);
                server.setTcpipPort(tcpipPort);

                server.setDevMode(devMode);
                server.setVerboseMode(verboseMode);
                server.setForceUpdate(forceUpdate);
                server.setRootCatalogMode(rootCatalogMode);

                server.run();
            }
        } catch (ParseException ex) {
            Logger.getLogger(XBService.class.getName()).log(Level.SEVERE, null, ex);
        } */
    }
}
