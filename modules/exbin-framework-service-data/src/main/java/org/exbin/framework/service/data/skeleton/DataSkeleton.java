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
package org.exbin.framework.service.data.skeleton;

import com.sun.org.glassfish.gmbal.ParameterNames;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.entity.service.XBEItemService;
import org.exbin.xbup.catalog.entity.service.XBEXInfoService;
import org.exbin.xbup.client.stub.XBPInfoStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXItemInfo;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBCXInfoService;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for data access.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class DataSkeleton {

    private final XBAECatalog catalog;

    public DataSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPInfoStub.NODE_INFO_PROCEDURE), (XBMultiProcedure) (XBBlockType blockType, XBOutput parameters, XBInput resultInput) -> {
            XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
            provider.begin();
            provider.matchType(blockType);
            XBAttribute index = provider.pullAttribute();
            provider.end();

            XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
            XBEXInfoService infoService = (XBEXInfoService) catalog.getCatalogService(XBCXInfoService.class);
            Optional<XBCItem> node = itemService.getItem(index.getNaturalLong());
            XBCXItemInfo info = infoService.getNodeInfo((XBCNode) node.get());

            XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
            listener.process(info == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(info.getId()));
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPInfoStub.INFOSCOUNT_INFO_PROCEDURE), (XBMultiProcedure) (XBBlockType blockType, XBOutput parameters, XBInput resultInput) -> {
            XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
            provider.begin();
            provider.matchType(blockType);
            provider.end();

            XBEXInfoService infoService = (XBEXInfoService) catalog.getCatalogService(XBCXInfoService.class);
            XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
            listener.process(new UBNat32(infoService.getItemsCount()));
        });
    }
}
