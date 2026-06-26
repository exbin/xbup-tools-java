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
package org.exbin.xbup.jaguif.editor.wave.contribution;

import javax.swing.Action;
import org.exbin.jaguif.App;
import org.exbin.jaguif.contribution.api.ActionSequenceContribution;
import org.exbin.xbup.jaguif.editor.wave.EditorWaveModule;
import org.exbin.xbup.jaguif.editor.wave.action.AudioStopAction;
import org.jspecify.annotations.NullMarked;

/**
 * Audio stop contribution.
 */
@NullMarked
public class AudioStopContribution implements ActionSequenceContribution {

    public static final String CONTRIBUTION_ID = "audioStop";

    @Override
    public Action createAction() {
        AudioStopAction action = new AudioStopAction();
        EditorWaveModule editorWaveModule = App.getModule(EditorWaveModule.class);
        action.init(editorWaveModule.getResourceBundle());
        return action;
    }

    @Override
    public String getContributionId() {
        return CONTRIBUTION_ID;
    }
}
