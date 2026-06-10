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
package org.exbin.xbup.jaguif.viewer.settings;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.jaguif.options.settings.api.SettingsOptions;
import org.exbin.jaguif.options.api.OptionsStorage;

/**
 * XBUP service options.
 */
@ParametersAreNonnullByDefault
public class ServiceConnectionOptions implements SettingsOptions {

    public static final String KEY_SERVICE_CONNECTION_ALLOWED = "serviceConnectionAllowed";
    public static final String KEY_SERVICE_CONNECTION_URL = "serviceConnectionURL";
    public static final String KEY_CATALOG_UPDATE_ALLOWED = "catalogUpdateAllowed";
    public static final String KEY_CATALOG_UPDATE_URL = "catalogUpdateURL";

    protected final OptionsStorage storage;

    public ServiceConnectionOptions(OptionsStorage storage) {
        this.storage = storage;
    }

    public boolean isServiceConnectionAllowed() {
        return storage.getBoolean(KEY_SERVICE_CONNECTION_ALLOWED, true);
    }

    public void setServiceConnectionAllowed(boolean allowed) {
        storage.putBoolean(KEY_SERVICE_CONNECTION_ALLOWED, allowed);
    }

    @Nonnull
    public Optional<String> getServiceConnectionUrl() {
        return storage.get(KEY_SERVICE_CONNECTION_URL);
    }

    public void setServiceConnectionUrl(String connectionUrl) {
        storage.put(KEY_SERVICE_CONNECTION_URL, connectionUrl);
    }

    public boolean isCatalogUpdateAllowed() {
        return storage.getBoolean(KEY_CATALOG_UPDATE_ALLOWED, true);
    }

    public void setCatalogUpdateAllowed(boolean allowed) {
        storage.putBoolean(KEY_CATALOG_UPDATE_ALLOWED, allowed);
    }

    @Nonnull
    public Optional<String> getCatalogUpdateUrl() {
        return storage.get(KEY_CATALOG_UPDATE_URL);
    }

    public void setCatalogUpdateUrl(String updateUrl) {
        storage.put(KEY_CATALOG_UPDATE_URL, updateUrl);
    }

    @Override
    public void copyTo(SettingsOptions options) {
        ServiceConnectionOptions with = (ServiceConnectionOptions) options;
        with.setCatalogUpdateAllowed(isCatalogUpdateAllowed());
        with.setCatalogUpdateUrl(getCatalogUpdateUrl().orElse(null));
        with.setServiceConnectionAllowed(isServiceConnectionAllowed());
        with.setServiceConnectionUrl(getServiceConnectionUrl().orElse(null));
    }
}
