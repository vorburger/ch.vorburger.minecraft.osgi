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
package ch.vorburger.minecraft.osgi.dev.internal;

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import ch.vorburger.minecraft.osgi.dev.BundleManager;
import ch.vorburger.minecraft.osgi.dev.commands.InstallCommand;
import ch.vorburger.osgi.builder.SourceInstallService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

    private SourceInstallService sourceInstallService;

    @Override
    public void start(BundleContext context) throws Exception {
        ServiceReference<SourceInstallService> ref = context.getServiceReference(SourceInstallService.class);
        if (ref != null) {
            sourceInstallService = context.getService(ref);
            BundleManager bundleManager = new BundleManager(sourceInstallService);
            context.registerService(BundleManager.class, bundleManager, null);
            context.registerService(CommandRegistration.class, new InstallCommand(bundleManager), null);
        } else {
            throw new IllegalStateException("SourceInstallService not found (yet, change start order; DS SCR later)");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (sourceInstallService != null) {
            sourceInstallService.close();
        }
    }

}
