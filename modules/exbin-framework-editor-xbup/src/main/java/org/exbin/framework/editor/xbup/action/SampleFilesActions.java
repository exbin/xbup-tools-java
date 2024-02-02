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
import javax.swing.Action;
import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.editor.xbup.viewer.XbupEditorProvider;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.editor.api.EditorProvider;
import org.exbin.framework.utils.ActionUtils;

/**
 * Sample files handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SampleFilesActions {

    public static final String SAMPLE_HTML_FILE_ACTION_ID = "sampleHtmlFileAction";
    public static final String SAMPLE_PICTURE_FILE_ACTION_ID = "samplePictureFileAction";
    public static final String SAMPLE_TYPES_FILE_ACTION_ID = "sampleTypesFileAction";
    private static final String SAMPLE_FILES_DIR = "/org/exbin/framework/editor/xbup/resources/samples/";

    private EditorProvider editorProvider;
    private ResourceBundle resourceBundle;

    private Action sampleHtmlFileAction;
    private Action samplePictureFileAction;
    private Action sampleTypesFileAction;

    public SampleFilesActions() {
    }

    public void setup(EditorProvider editorProvider, ResourceBundle resourceBundle) {
        this.editorProvider = editorProvider;
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public Action getSampleHtmlFileAction() {
        if (sampleHtmlFileAction == null) {
            sampleHtmlFileAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (editorProvider instanceof XbupEditorProvider) {
                        XbupEditorProvider provider = (XbupEditorProvider) editorProvider;
                        provider.newFile();
                        XbupFileHandler xbupFile = (XbupFileHandler) provider.getActiveFile().get();
                        xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xhtml_example.xb");
                    }
                }
            };
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(sampleHtmlFileAction, resourceBundle, SAMPLE_HTML_FILE_ACTION_ID);
        }
        return sampleHtmlFileAction;
    }

    @Nonnull
    public Action getSamplePictureFileAction() {
        if (samplePictureFileAction == null) {
            samplePictureFileAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (editorProvider instanceof XbupEditorProvider) {
                        XbupEditorProvider provider = (XbupEditorProvider) editorProvider;
                        provider.newFile();
                        XbupFileHandler xbupFile = (XbupFileHandler) provider.getActiveFile().get();
                        xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xblogo.xbp");
                    }
                }
            };
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(samplePictureFileAction, resourceBundle, SAMPLE_PICTURE_FILE_ACTION_ID);

        }
        return samplePictureFileAction;
    }

    @Nonnull
    public Action getSampleTypesFileAction() {
        if (sampleTypesFileAction == null) {
            sampleTypesFileAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (editorProvider instanceof XbupEditorProvider) {
                        XbupEditorProvider provider = (XbupEditorProvider) editorProvider;
                        provider.newFile();
                        XbupFileHandler xbupFile = (XbupFileHandler) provider.getActiveFile().get();
                        xbupFile.loadFromResourcePath(getClass(), SAMPLE_FILES_DIR + "xbtypes.xb");
                    }
                }
            };
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.initAction(sampleTypesFileAction, resourceBundle, SAMPLE_TYPES_FILE_ACTION_ID);

        }
        return sampleTypesFileAction;
    }
}
