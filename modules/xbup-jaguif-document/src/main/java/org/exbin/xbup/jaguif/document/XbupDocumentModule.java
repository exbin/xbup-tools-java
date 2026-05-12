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
package org.exbin.xbup.jaguif.document;

import java.util.Optional;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.jaguif.App;
import org.exbin.jaguif.Module;
import org.exbin.jaguif.ModuleUtils;
import org.exbin.xbup.jaguif.document.action.DocumentPropertiesAction;
import org.exbin.jaguif.file.api.FileModuleApi;
import org.exbin.xbup.jaguif.catalog.XBFileType;
import org.exbin.jaguif.contribution.api.PositionSequenceContributionRule;
import org.exbin.jaguif.contribution.api.SequenceContribution;
import org.exbin.jaguif.document.api.Document;
import org.exbin.jaguif.document.api.DocumentManagement;
import org.exbin.jaguif.document.api.DocumentModuleApi;
import org.exbin.jaguif.document.api.DocumentSource;
import org.exbin.jaguif.document.api.DocumentType;
import org.exbin.jaguif.file.api.FileDocumentSource;
import org.exbin.jaguif.menu.api.MenuDefinitionManagement;
import org.exbin.jaguif.language.api.LanguageModuleApi;
import org.exbin.jaguif.menu.api.MenuModuleApi;
import org.exbin.jaguif.options.settings.api.OptionsSettingsManagement;
import org.exbin.jaguif.options.settings.api.OptionsSettingsModuleApi;
import org.exbin.jaguif.options.settings.api.SettingsOptionsProvider;
import org.exbin.xbup.jaguif.document.contribution.DocumentPropertiesContribution;
import org.exbin.xbup.jaguif.editor.XbupEditor;

/**
 * XBUP viewer module.
 */
@ParametersAreNonnullByDefault
public class XbupDocumentModule implements Module {

    public static final String MODULE_ID = ModuleUtils.getModuleIdByApi(XbupDocumentModule.class);
    public static final String XBUP_FILE_TYPE = "XBEditor.XBFileType";
    public static final String XBUP_DOCUMENT_ID = "xbup";

    public static final String XBUP_POPUP_MENU_ID = MODULE_ID + ".xbupPopupMenu";
    public static final String XBUP_VIEWER_GROUP_ID = "xbupViewer";
    public static final String XBUP_VIEWER_CONNECTION_GROUP_ID = "xbupViewerConnection";
    public static final String SAMPLE_FILE_SUBMENU_ID = MODULE_ID + ".sampleFileSubMenu";

    private ResourceBundle resourceBundle;
    protected XbupDocumentViewProvider documentViewProvider;

    private boolean devMode;

    public XbupDocumentModule() {
    }

    @Nonnull
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = App.getModule(LanguageModuleApi.class).getBundle(XbupDocumentModule.class);
        }

        return resourceBundle;
    }

    public void registerDocument() {
        DocumentModuleApi documentModule = App.getModule(DocumentModuleApi.class);
        DocumentManagement documentManager = documentModule.getMainDocumentManager();
        documentManager.registerDocumentType(new DocumentType() {
            @Nonnull
            @Override
            public String getTypeId() {
                return XBUP_DOCUMENT_ID;
            }

            @Nonnull
            @Override
            public XbupTreeDocument createDefaultDocument() {
                XbupTreeDocument xbupDocument = createXbupDocument();
                xbupDocument.loadFrom(documentModule.createEmptyDocumentSource());
                return xbupDocument;
            }

            @Nonnull
            @Override
            public Optional<Document> createDocument(DocumentSource documentSource) {
                if (documentSource instanceof FileDocumentSource) {
                    XbupTreeDocument document = createXbupDocument();
                    document.loadFrom(documentSource);
                    return Optional.of(document);
                }

                return Optional.empty();
            }

            @Nonnull
            private XbupTreeDocument createXbupDocument() {
                XbupTreeDocument document = new XbupTreeDocument();
                OptionsSettingsModuleApi optionsSettingsModule = App.getModule(OptionsSettingsModuleApi.class);
                OptionsSettingsManagement settingsManager = optionsSettingsModule.getMainSettingsManager();
                SettingsOptionsProvider settingsOptionsProvider = settingsManager.getSettingsOptionsProvider();
//                document.applySettings(settingsOptionsProvider);
//                document.setContentData(new ByteArrayPagedData());

                XbupEditor editor = new XbupEditor();
                editor.setTreeDocument(document.getXbupTree());
                document.setDocumentComponent(editor.getComponent());
                return document;
            }
        });
    }

    public void registerXbupViewer() {
        /* XbupDocumentModule documentModule = App.getModule(XbupDocumentModule.class);
        documentModule.setDocumentViewProvider(new XbupDocumentViewProvider() {
            @Nonnull
            @Override
            public JComponent getComponent() {
                XbupEditor editor = new XbupEditor();
                editor.setTreeDocument(editor);
                return editor.getComponent();
            }
        }); */
    }

    public void registerFileTypes() {
        FileModuleApi fileModule = App.getModule(FileModuleApi.class);
        fileModule.addFileType(new XBFileType());
    }

    @Nonnull
    private DocumentPropertiesAction createDocumentPropertiesAction() {
        DocumentPropertiesAction documentPropertiesAction = new DocumentPropertiesAction();
        documentPropertiesAction.init();
        return documentPropertiesAction;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public void setDocumentViewProvider(XbupDocumentViewProvider documentViewProvider) {
        this.documentViewProvider = documentViewProvider;
    }

    public void registerSettings() {
        OptionsSettingsModuleApi settingsModule = App.getModule(OptionsSettingsModuleApi.class);
        OptionsSettingsManagement settingsManagement = settingsModule.getMainSettingsManager();

        /* OptionsGroup xbupViewerGroup = settingsModule.createOptionsGroup(XBUP_VIEWER_GROUP_ID, resourceBundle);
        settingsManagement.registerGroup(xbupViewerGroup);
        settingsManagement.registerGroupRule(xbupViewerGroup, new ParentOptionsGroupRule("editor"));

        OptionsGroup xbupViewerConnectionGroup = settingsModule.createOptionsGroup(XBUP_VIEWER_CONNECTION_GROUP_ID, resourceBundle);
        settingsManagement.registerGroup(xbupViewerConnectionGroup);
        settingsManagement.registerGroupRule(xbupViewerConnectionGroup, new ParentOptionsGroupRule(xbupViewerGroup));
        catalogConnectionOptionsPage = new ServiceConnectionSettingsComponent();
        settingsManagement.registerPage(catalogConnectionOptionsPage);
        settingsManagement.registerPageRule(catalogConnectionOptionsPage, new GroupOptionsPageRule(xbupViewerConnectionGroup)); */
    }

    public void registerPropertiesMenuAction() {
        MenuModuleApi menuModule = App.getModule(MenuModuleApi.class);
        MenuDefinitionManagement mgmt = menuModule.getMainMenuDefinition(MODULE_ID).getSubMenu(MenuModuleApi.FILE_SUBMENU_ID);
        SequenceContribution contribution = new DocumentPropertiesContribution();
        mgmt.registerMenuContribution(contribution);
        mgmt.registerMenuRule(contribution, new PositionSequenceContributionRule(PositionSequenceContributionRule.PositionMode.BOTTOM));
    }
}
