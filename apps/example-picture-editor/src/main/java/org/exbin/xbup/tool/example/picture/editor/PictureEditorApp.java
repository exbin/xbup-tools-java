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
package org.exbin.xbup.tool.example.picture.editor;

import java.io.File;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.basic.BasicApplication;

/**
 * The main class of the example picture application.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class PictureEditorApp {

    private PictureEditorApp() {
    }

    /**
     * Main method launching the application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        BasicApplication app = BasicApplication.createApplication(PictureEditorApp.class);
        app.init();
        App.launch(() -> {

            app.setAppDirectory(PictureEditorApp.class);
            app.addClassPathModules();
            app.addModulesFromManifest(PictureEditorApp.class);
            File appDirectory = app.getAppDirectory();
            if ("".equals(appDirectory.getPath())) {
                app.addModulesFrom(new File("lib").toURI());
                app.addModulesFrom(new File(BasicApplication.PLUGINS_DIRECTORY).toURI());
            } else {
                app.addModulesFrom(new File(app.getAppDirectory(), "lib").toURI());
                app.addModulesFrom(new File(app.getAppDirectory(), BasicApplication.PLUGINS_DIRECTORY).toURI());
            }
            app.initModules();

            App.launch("org.exbin.xbup.tool.example.picture.launcher.PictureEditorLauncherModule", args);            
        });
    }
}
