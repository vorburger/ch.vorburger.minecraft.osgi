/**
 * ch.vorburger.minecraft.osgi
 *
 * Copyright (C) 2016 - 2017 Michael Vorburger.ch <mike@vorburger.ch>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.vorburger.minecraft.osgi.users;

import ch.vorburger.osgi.gradle.SourceInstallService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.EventListener;

// TODO remove once @Component on UsersPlugin (UsersComponent?) works
public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    private SourceInstallService sourceInstallService;

    @Override
    public void start(BundleContext context) throws Exception {
        ServiceReference<SourceInstallService> ref = context.getServiceReference(SourceInstallService.class);
        if (ref != null) {
            sourceInstallService = context.getService(ref);
            PlayerJoinListener playerJoinListener = new PlayerJoinListener(sourceInstallService);
            context.registerService(EventListener.class, playerJoinListener, null);
        } else {
            LOG.error("start: No SourceInstallService found! :(");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (sourceInstallService != null) {
            sourceInstallService.close();
        }
    }

}
