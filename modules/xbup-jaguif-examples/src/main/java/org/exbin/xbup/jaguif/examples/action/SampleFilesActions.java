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
package org.exbin.xbup.jaguif.examples.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.jaguif.App;
import org.exbin.jaguif.action.api.ActionConsts;
import org.exbin.jaguif.action.api.ActionContextChange;
import org.exbin.jaguif.action.api.ActionModuleApi;
import org.exbin.jaguif.context.api.ContextChangeRegistration;
import org.exbin.jaguif.docking.api.ContextDocking;
import org.exbin.jaguif.docking.api.DocumentDocking;
import org.exbin.jaguif.document.api.ContextDocument;

/**
 * Sample files handler.
 */
@ParametersAreNonnullByDefault
public class SampleFilesActions {

    private static final String SAMPLE_FILES_DIR = "/org/exbin/xbup/jaguif/examples/resources/samples/";

    private ResourceBundle resourceBundle;

    public SampleFilesActions() {
    }

    public void init(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public SampleHtmlFileAction createSampleHtmlFileAction() {
        SampleHtmlFileAction sampleHtmlFileAction = new SampleHtmlFileAction();
        sampleHtmlFileAction.init(resourceBundle);
        return sampleHtmlFileAction;
    }

    @Nonnull
    public SamplePictureFileAction createSamplePictureFileAction() {
        SamplePictureFileAction samplePictureFileAction = new SamplePictureFileAction();
        samplePictureFileAction.init(resourceBundle);
        return samplePictureFileAction;
    }

    @Nonnull
    public SampleTypesFileAction createSampleTypesFileAction() {
        SampleTypesFileAction sampleTypesFileAction = new SampleTypesFileAction();
        sampleTypesFileAction.init(resourceBundle);
        return sampleTypesFileAction;
    }

    @ParametersAreNonnullByDefault
    public static class SampleHtmlFileAction extends AbstractAction {

        public static final String ACTION_ID = "sampleHtmlFile";

        private DocumentDocking documentDocking;

        public SampleHtmlFileAction() {
        }

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocking.class, (instance) -> {
                        documentDocking = instance instanceof DocumentDocking ? (DocumentDocking) instance : null;
                        setEnabled(documentDocking != null);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO
//            xbupDocument.newFile();
//            XbupFileHandler xbupFile = (XbupFileHandler) xbupDocument.getActiveFile().get();
//            xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xhtml_example.xb");
        }
    }

    @ParametersAreNonnullByDefault
    public static class SamplePictureFileAction extends AbstractAction {

        public static final String ACTION_ID = "samplePictureFile";

        private DocumentDocking documentDocking;

        public SamplePictureFileAction() {
        }

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        documentDocking = instance instanceof DocumentDocking ? (DocumentDocking) instance : null;
                        setEnabled(documentDocking != null);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            documentDocking.newFile();
//            XbupFileHandler xbupFile = (XbupFileHandler) documentDocking.getActiveFile().get();
//            xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xblogo.xbp");
        }
    }

    @ParametersAreNonnullByDefault
    public static class SampleTypesFileAction extends AbstractAction {

        public static final String ACTION_ID = "sampleTypesFile";

        private DocumentDocking documentDocking;

        public SampleTypesFileAction() {
        }

        public void init(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            setEnabled(false);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ContextChangeRegistration registrar) {
                    registrar.registerChangeListener(ContextDocument.class, (instance) -> {
                        documentDocking = instance instanceof DocumentDocking ? (DocumentDocking) instance : null;
                        setEnabled(documentDocking != null);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            documentDocking.newFile();
//            XbupFileHandler xbupFile = (XbupFileHandler) documentDocking.getActiveFile().get();
//            xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xbtypes.xb");
        }
    }
}
