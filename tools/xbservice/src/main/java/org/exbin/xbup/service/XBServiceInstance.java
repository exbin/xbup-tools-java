/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.service;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.catalog.entity.service.XBEXDescService;
import org.exbin.xbup.catalog.entity.service.XBEXFileService;
import org.exbin.xbup.catalog.entity.service.XBEXHDocService;
import org.exbin.xbup.catalog.entity.service.XBEXIconService;
import org.exbin.xbup.catalog.entity.service.XBEXLangService;
import org.exbin.xbup.catalog.entity.service.XBEXNameService;
import org.exbin.xbup.catalog.entity.service.XBEXPlugService;
import org.exbin.xbup.catalog.entity.service.XBEXStriService;
import org.exbin.xbup.catalog.entity.service.XBEXUiService;
import org.exbin.xbup.catalog.update.XBCatalogServiceUpdateHandler;
import org.exbin.xbup.client.XBCatalogNetServiceClient;
import org.exbin.xbup.client.XBTCPServiceClient;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBDeclaration;
import org.exbin.xbup.core.block.declaration.catalog.XBCFormatDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.exbin.xbup.core.catalog.base.service.XBCXLangService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.exbin.xbup.core.catalog.base.service.XBCXPlugService;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.exbin.xbup.core.catalog.base.service.XBCXUiService;
import org.exbin.xbup.core.parser.XBProcessingException;

/**
 * Instance class for XBUP framework service.
 *
 * @version 0.2.1 2020/09/23
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBServiceInstance {

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("org/exbin/xbup/service/XBService");
    private boolean shallUpdate;
    private boolean verboseMode;
    private boolean rootCatalogMode;
    private boolean devMode;
    private boolean forceUpdate;
    private boolean derbyMode = false;
    private boolean debugMode = false;

    private String tcpipPort;
    private String tcpipInterface;
    private String derbyPath;
    private XBCatalogNetServiceServer serviceServer = null;
    private EntityManager entityManager = null;
    private XBAECatalog catalog = null;

    public XBServiceInstance() {
    }

    public void run() {
        Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("service_head"));
        Logger.getLogger(XBService.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");

        int tcpipPortInt;
        try {
            tcpipPortInt = Integer.parseInt(tcpipPort);
        } catch (NumberFormatException e) {
            tcpipPortInt = 22594; // Fallback to default port
            Logger.getLogger(XBServiceInstance.class.getName()).log(Level.SEVERE, "{0}{1}: {2}", new Object[]{resourceBundle.getString("error_warning"), resourceBundle.getString("error_tcpip_port"), tcpipPort});
        }

        Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "{0} {1}:{2}...", new Object[]{resourceBundle.getString("init_service"), tcpipInterface, Integer.toString(tcpipPortInt)});

        serviceServer = new XBCatalogNetServiceServer();
        serviceServer.setCatalogProvider(new XBTCPServiceServer.CatalogProvider() {
            @Override
            public XBACatalog createCatalog() {
                if (catalog == null) {
                    initCatalog();
                    performUpdate(); 
               } else {
                    XBServiceInstance.this.createCatalog();
                }

                return catalog;
            }
        });

        serviceServer.setDebugMode(debugMode);

        try {
            serviceServer.open(tcpipPortInt);
            serviceServer.run();
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "{0}.", resourceBundle.getString("stop_service_success"));
        } catch (IOException e) {
            Logger.getLogger(XBServiceInstance.class.getName()).log(Level.WARNING, "{0}: {1}", new Object[]{resourceBundle.getString("init_service_failed"), e});
        }
    }

    private void initCatalog() {
        Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "{0}...", resourceBundle.getString("init_catalog"));
        try {
            derbyMode = false;
            // Database Initialization
            String derbyHome = System.getProperty("user.home") + "/.java/.userPrefs/" + derbyPath;
            if (devMode) {
                derbyHome += "-dev";
            }
            System.setProperty("derby.system.home", derbyHome);
            try {
                createCatalog();
            } catch (DatabaseException | javax.persistence.PersistenceException e) {
                derbyMode = true;
                createCatalog();
            }

            if (catalog.isShallInit()) {
                catalog.initCatalog();
            }
        } catch (DatabaseException e) {
            System.err.println(resourceBundle.getString("init_catalog_failed") + ": " + e);
        }
    }

    private void createCatalog() {
        String persistenceUnitName;
        if (derbyMode) {
            persistenceUnitName = "XBServiceDerbyPU";

            EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            entityManager = emf.createEntityManager();
            catalog = createCatalog(entityManager);
        } else {
            if (rootCatalogMode) {
                if (devMode) {
                    persistenceUnitName = "XBServiceMySQLDevPU";
                } else {
                    persistenceUnitName = "XBServiceMySQLPU";
                }
            } else {
                persistenceUnitName = "XBServicePU";
            }

            EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            entityManager = emf.createEntityManager();
            catalog = createCatalog(entityManager);
        }

        // TODO Separate method?
        Long[] serviceFormatPath = new Long[3];
        serviceFormatPath[0] = 0l;
        serviceFormatPath[1] = 2l;
        serviceFormatPath[2] = 0l;
        XBCFormatDecl serviceFormatDecl = (XBCFormatDecl) catalog.findFormatTypeByPath(serviceFormatPath, 0);
        XBContext serviceContext = new XBContext();
        serviceFormatDecl.getGroupDecls().forEach(groupDeclservice -> {
            serviceContext.getGroups().add(XBDeclaration.convertCatalogGroup(groupDeclservice, catalog));
        });
        catalog.setRootContext(serviceContext);
    }

    private void performUpdate() {
        // TODO: Only single connection for testing purposes (no connection pooling yet)
        shallUpdate = (serviceServer.shallUpdate(catalog) || forceUpdate) && (!rootCatalogMode);
        try {
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("init_service_success"));
            Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
            if (shallUpdate) {
                // TODO: Should be threaded
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("init_update"));
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");

                if (catalog.isShallInit()) {
                    catalog.initCatalog();
                }

                if (serviceServer.shallUpdate(catalog)) {
                    // TODO: As there is currently no diff update available - wipe out entire database instead
                    EntityManagerFactory emfDrop = Persistence.createEntityManagerFactory(derbyMode ? "XBServiceDerbyPU-drop" : "XBServicePU-drop");
                    EntityManager emDrop = emfDrop.createEntityManager();
                    emDrop.setFlushMode(FlushModeType.AUTO);
                    catalog = (XBAECatalog) createCatalog(emDrop);
                    ((XBAECatalog) catalog).initCatalog();

                    int defaultPort = devMode ? XBTCPServiceClient.DEFAULT_DEV_PORT : XBTCPServiceClient.DEFAULT_PORT;
                    String mainCatalogHost = devMode ? XBTCPServiceClient.MAIN_DEV_CATALOG_HOST : XBTCPServiceClient.MAIN_CATALOG_HOST;
                    XBCatalogNetServiceClient mainClient = new XBCatalogNetServiceClient(mainCatalogHost, defaultPort);
                    XBCatalogServiceUpdateHandler updateHandler = new XBCatalogServiceUpdateHandler(catalog, mainClient);
                    updateHandler.init();
                    updateHandler.fireUsageEvent(false);
                    updateHandler.performUpdateMain();
                }

                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, resourceBundle.getString("done_update"));
                Logger.getLogger(XBServiceInstance.class.getName()).log(XBCatalogNetServiceServer.XB_SERVICE_STATUS, "");
            }
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBServiceInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private XBAECatalog createCatalog(EntityManager em) {
        XBAECatalog createdCatalog = new XBAECatalog(em);

        ((XBAECatalog) createdCatalog).addCatalogService(XBCXLangService.class, new XBEXLangService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXStriService.class, new XBEXStriService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXNameService.class, new XBEXNameService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXDescService.class, new XBEXDescService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXFileService.class, new XBEXFileService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXIconService.class, new XBEXIconService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXPlugService.class, new XBEXPlugService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXUiService.class, new XBEXUiService((XBAECatalog) createdCatalog));
        ((XBAECatalog) createdCatalog).addCatalogService(XBCXHDocService.class, new XBEXHDocService((XBAECatalog) createdCatalog));
        return createdCatalog;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public boolean isShallUpdate() {
        return shallUpdate;
    }

    public void setShallUpdate(boolean shallUpdate) {
        this.shallUpdate = shallUpdate;
    }

    public boolean isVerboseMode() {
        return verboseMode;
    }

    public void setVerboseMode(boolean verboseMode) {
        this.verboseMode = verboseMode;
    }

    public boolean isRootCatalogMode() {
        return rootCatalogMode;
    }

    public void setRootCatalogMode(boolean rootCatalogMode) {
        this.rootCatalogMode = rootCatalogMode;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isDerbyMode() {
        return derbyMode;
    }

    public void setDerbyMode(boolean derbyMode) {
        this.derbyMode = derbyMode;
    }

    public String getTcpipPort() {
        return tcpipPort;
    }

    public void setTcpipPort(String tcpipPort) {
        this.tcpipPort = tcpipPort;
    }

    public String getTcpipInterface() {
        return tcpipInterface;
    }

    public void setTcpipInterface(String tcpipInterface) {
        this.tcpipInterface = tcpipInterface;
    }

    public String getDerbyPath() {
        return derbyPath;
    }

    public void setDerbyPath(String derbyPath) {
        this.derbyPath = derbyPath;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
