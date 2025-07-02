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
package org.exbin.framework.viewer.xbup;

import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.App;
import org.exbin.framework.client.api.ClientConnectionEvent;
import org.exbin.framework.client.api.ClientConnectionListener;
import org.exbin.framework.viewer.xbup.gui.XBDocStatusPanel;
import org.exbin.framework.frame.api.FrameModuleApi;

/**
 * Status panel handler.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class StatusPanelHandler {

    private ResourceBundle resourceBundle;

    private XBDocStatusPanel docStatusPanel;

    public StatusPanelHandler() {
    }

    public void setup(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    @Nonnull
    public XBDocStatusPanel getDocStatusPanel() {
        if (docStatusPanel == null) {
            docStatusPanel = new XBDocStatusPanel();
            FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
            frameModule.registerStatusBar(ViewerXbupModule.MODULE_ID, ViewerXbupModule.DOC_STATUS_BAR_ID, docStatusPanel);
            frameModule.switchStatusBar(ViewerXbupModule.DOC_STATUS_BAR_ID);
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
