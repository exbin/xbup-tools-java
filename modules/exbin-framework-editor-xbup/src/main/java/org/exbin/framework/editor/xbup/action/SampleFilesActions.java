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
package org.exbin.framework.editor.xbup.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionContextChange;
import org.exbin.framework.action.api.ActionContextChangeManager;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.editor.xbup.viewer.XbupEditorProvider;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.editor.api.EditorProvider;

/**
 * Sample files handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SampleFilesActions {

    private static final String SAMPLE_FILES_DIR = "/org/exbin/framework/editor/xbup/resources/samples/";

    private ResourceBundle resourceBundle;

    public SampleFilesActions() {
    }

    public void setup(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public SampleHtmlFileAction createSampleHtmlFileAction() {
        SampleHtmlFileAction sampleHtmlFileAction = new SampleHtmlFileAction();
        sampleHtmlFileAction.setup(resourceBundle);
        return sampleHtmlFileAction;
    }

    @Nonnull
    public SamplePictureFileAction createSamplePictureFileAction() {
        SamplePictureFileAction samplePictureFileAction = new SamplePictureFileAction();
        samplePictureFileAction.setup(resourceBundle);
        return samplePictureFileAction;
    }

    @Nonnull
    public SampleTypesFileAction createSampleTypesFileAction() {
        SampleTypesFileAction sampleTypesFileAction = new SampleTypesFileAction();
        sampleTypesFileAction.setup(resourceBundle);
        return sampleTypesFileAction;
    }

    @ParametersAreNonnullByDefault
    public static class SampleHtmlFileAction extends AbstractAction {

        public static final String ACTION_ID = "sampleHtmlFileAction";

        private EditorProvider editorProvider;

        public SampleHtmlFileAction() {
        }

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeManager manager) {
                    manager.registerUpdateListener(EditorProvider.class, (instance) -> {
                        editorProvider = instance;
                        setEnabled(editorProvider instanceof XbupEditorProvider);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            editorProvider.newFile();
            XbupFileHandler xbupFile = (XbupFileHandler) editorProvider.getActiveFile().get();
            xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xhtml_example.xb");
        }
    }

    @ParametersAreNonnullByDefault
    public static class SamplePictureFileAction extends AbstractAction {

        public static final String ACTION_ID = "samplePictureFileAction";

        private EditorProvider editorProvider;

        public SamplePictureFileAction() {
        }

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeManager manager) {
                    manager.registerUpdateListener(EditorProvider.class, (instance) -> {
                        editorProvider = instance;
                        setEnabled(editorProvider instanceof XbupEditorProvider);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            editorProvider.newFile();
            XbupFileHandler xbupFile = (XbupFileHandler) editorProvider.getActiveFile().get();
            xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xblogo.xbp");
        }
    }

    @ParametersAreNonnullByDefault
    public static class SampleTypesFileAction extends AbstractAction {

        public static final String ACTION_ID = "sampleTypesFileAction";

        private EditorProvider editorProvider;

        public SampleTypesFileAction() {
        }

        public void setup(ResourceBundle resourceBundle) {
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(this, resourceBundle, ACTION_ID);
            putValue(ActionConsts.ACTION_CONTEXT_CHANGE, new ActionContextChange() {
                @Override
                public void register(ActionContextChangeManager manager) {
                    manager.registerUpdateListener(EditorProvider.class, (instance) -> {
                        editorProvider = instance;
                        setEnabled(editorProvider instanceof XbupEditorProvider);
                    });
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            editorProvider.newFile();
            XbupFileHandler xbupFile = (XbupFileHandler) editorProvider.getActiveFile().get();
            xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xbtypes.xb");
        }
    }
}
