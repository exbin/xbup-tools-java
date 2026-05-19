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
package org.exbin.xbup.jaguif.component.page;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Xbup page wrapper for component.
 */
@ParametersAreNonnullByDefault
public class BasicXbupComponentPage implements XbupComponentPage {

    protected final JComponent component;
    protected final String name;

    public BasicXbupComponentPage(String name, JComponent component) {
        this.name = name;
        this.component = component;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nonnull
    @Override
    public Optional<ImageIcon> getIcon() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public JComponent getComponent() {
        return component;
    }
}
