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
package org.exbin.xbup.jaguif.viewer;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.jaguif.App;
import org.exbin.xbup.jaguif.client.api.ClientConnectionEvent;
import org.exbin.xbup.jaguif.client.api.ClientConnectionListener;
import org.exbin.jaguif.frame.api.FrameModuleApi;
import org.exbin.xbup.jaguif.viewer.gui.XBDocStatusPanel;

/**
 * Status panel handler.
 */
@ParametersAreNonnullByDefault
public class StatusPanelHandler {

    private ResourceBundle resourceBundle;

    private XBDocStatusPanel docStatusPanel;

    public StatusPanelHandler() {
    }

    public void init(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public XBDocStatusPanel getDocStatusPanel() {
        if (docStatusPanel == null) {
            docStatusPanel = new XBDocStatusPanel();
            FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
            frameModule.registerStatusBar(XbupViewerModule.MODULE_ID, XbupViewerModule.DOC_STATUS_BAR_ID, docStatusPanel);
            frameModule.switchStatusBar(XbupViewerModule.DOC_STATUS_BAR_ID);
            // ((XBDocumentPanel) getEditorProvider()).registerTextStatus(docStatusPanel);
        }

        return docStatusPanel;
    }

    @Nonnull
    public ClientConnectionListener getClientConnectionListener() {
        return (ClientConnectionEvent connectionEvent) -> {
            docStatusPanel.setConnectionStatus(connectionEvent.getConnectionStatus());
        };
    }
}
