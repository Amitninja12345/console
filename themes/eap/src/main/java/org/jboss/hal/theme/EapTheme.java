/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.theme;

import com.google.gwt.core.client.GWT;
import org.jboss.hal.resources.Favicons;
import org.jboss.hal.resources.Logos;
import org.jboss.hal.resources.Theme;

/**
 * @author Harald Pehl
 */
public class EapTheme implements Theme {

    private static final Favicons FAVICONS = GWT.create(EapFavicons.class);
    private static final Logos LOGOS = GWT.create(EapLogos.class);

    @Override
    public String getId() {
        return "org.jboss.hal.theme.eap";
    }

    @Override
    public String getTitle() {
        return "EAP";
    }

    @Override
    public String getMainTitle() {
        return "JBoss";
    }

    @Override
    public String getSecondaryTitle() {
        return "Enterprise Application Platform";
    }

    @Override
    public String getDescription() {
        return "JBoss Enterprise Application Platform";
    }

    @Override
    public Favicons favicons() {
        return FAVICONS;
    }

    @Override
    public Logos logos() {
        return LOGOS;
    }
}
