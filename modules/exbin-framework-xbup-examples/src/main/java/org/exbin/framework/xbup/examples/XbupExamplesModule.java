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
package org.exbin.framework.xbup.examples;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleUtils;
import org.exbin.framework.contribution.api.PositionSequenceContributionRule;
import org.exbin.framework.contribution.api.SequenceContribution;
import org.exbin.framework.menu.api.MenuManagement;
import org.exbin.framework.xbup.examples.action.SampleFilesActions;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.menu.api.MenuModuleApi;

/**
 * XBUP examples module.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupExamplesModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(XbupExamplesModule.class);

    public static final String SAMPLE_FILE_SUBMENU_ID = MODULE_ID + ".sampleFileSubMenu";

    private ResourceBundle resourceBundle;

    private SampleFilesActions sampleFilesActions;

    private boolean devMode;

    public XbupExamplesModule() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupExamplesModule.class);
        }

        return resourceBundle;
    }

    private void ensureSetup() {
        if (resourceBundle == null) {
            getResourceBundle();
        }
    }

    @Nonnull
    private SampleFilesActions getSampleFilesActions() {
        if (sampleFilesActions == null) {
            ensureSetup();
            sampleFilesActions = new SampleFilesActions();
            sampleFilesActions.setup(resourceBundle);
        }

        return sampleFilesActions;
    }

    public void registerSampleFilesSubMenuActions() {
        getSampleFilesActions();
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuManagement mgmt = menuModule.getMainMenuManagement(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution contribution = mgmt.registerMenuItem(SAMPLE_FILE_SUBMENU_ID, resourceBundle.getString("sampleFileSubMenu.name"));
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
        mgmt = mgmt.getSubMenu(SAMPLE_FILE_SUBMENU_ID);
        contribution = mgmt.registerMenuItem(sampleFilesActions.createSampleHtmlFileAction());
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        contribution = mgmt.registerMenuItem(sampleFilesActions.createSamplePictureFileAction());
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
        contribution = mgmt.registerMenuItem(sampleFilesActions.createSampleTypesFileAction());
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.TOP));
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
