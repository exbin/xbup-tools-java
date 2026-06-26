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
package org.exbin.xbup.jaguif.examples;

import java.util.ResourceBundle;
import org.jspecify.annotations.NullMarked;
import javax.swing.Action;
import org.exbin.jaguif.App;
import org.exbin.jaguif.Module;
import org.exbin.jaguif.ModuleUtils;
import org.exbin.jaguif.contribution.api.PositionSequenceContributionRule;
import org.exbin.jaguif.contribution.api.SequenceContribution;
import org.exbin.jaguif.menu.api.MenuDefinitionManagement;
import org.exbin.xbup.jaguif.examples.action.SampleFilesActions;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.ActionMenuContribution;
import org.exbin.jaguif.menu.api.MenuModuleApi;

/**
 * XBUP examples module.
 */
@NullMarked
public class XbupExamplesModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(XbupExamplesModule.class);

    public static final String SAMPLE_FILE_SUBMENU_ID = MODULE_ID + ".sampleFileSubMenu";

    private ResourceBundle resourceBundle;

    private SampleFilesActions sampleFilesActions;

    private boolean devMode;

    public XbupExamplesModule() {
    }

    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupExamplesModule.class);
        }

        return resourceBundle;
    }

    private SampleFilesActions getSampleFilesActions() {
        if (sampleFilesActions == null) {
            sampleFilesActions = new SampleFilesActions();
            sampleFilesActions.init(getResourceBundle());
        }

        return sampleFilesActions;
    }

    public void registerSampleFilesSubMenuActions() {
        getSampleFilesActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement mgmt = menuModule.getMainMenuDefinition(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution contribution = mgmt.registerMenuItem(SAMPLE_FILE_SUBMENU_ID, resourceBundle.getString("sampleFileSubMenu.name"));
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
        mgmt = mgmt.getSubMenu(SAMPLE_FILE_SUBMENU_ID);
        contribution = new ActionMenuContribution() {
            @Override
            public Action createAction() {
                return sampleFilesActions.createSampleHtmlFileAction();
            }

            @Override
            public String getContributionId() {
                return SampleFilesActions.SampleHtmlFileAction.ACTION_ID;
            }
        };
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        contribution = new ActionMenuContribution() {
            @Override
            public Action createAction() {
                return sampleFilesActions.createSamplePictureFileAction();
            }

            @Override
            public String getContributionId() {
                return SampleFilesActions.SamplePictureFileAction.ACTION_ID;
            }
        };
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        contribution = new ActionMenuContribution() {
            @Override
            public Action createAction() {
                return sampleFilesActions.createSampleTypesFileAction();
            }

            @Override
            public String getContributionId() {
                return SampleFilesActions.SampleTypesFileAction.ACTION_ID;
            }
        };
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
