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
package org.exbin.xbup.tool.shell;

import java.io.File;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.basic.BasicApplication;
import org.exbin.framework.basic.ModuleFileLocation;

/**
 * Shell browser tool for XBUP document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ShellApp {

    private ShellApp() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicApplication app = BasicApplication.createApplication(ShellApp.class);
        app.init();
        App.launch(() -> {
            app.setAppDirectory(ShellApp.class);
            app.setupAddons();
            File appDirectory = app.getAppDirectory();
            if ("".equals(appDirectory.getPath())) {
                app.addModulesFrom(new File(BasicApplication.PLUGINS_DIRECTORY).toURI(), ModuleFileLocation.PLUGIN);
                app.addModulesFrom(new File("lib").toURI(), ModuleFileLocation.LIBRARY);
            } else {
                app.addModulesFrom(new File(appDirectory, BasicApplication.PLUGINS_DIRECTORY).toURI(), ModuleFileLocation.PLUGIN);
                app.addModulesFrom(new File(appDirectory, "lib").toURI(), ModuleFileLocation.LIBRARY);
            }
            app.addClassPathModules();
            app.addModulesFromManifest(ShellApp.class);
            app.initModules();

            App.launch("org.exbin.xbup.tool.shell.launcher.ShellLauncherModule", args);
        });
    }
}
