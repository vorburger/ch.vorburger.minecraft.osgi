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
package ch.vorburger.minecraft.osgi;

import ch.vorburger.minecraft.osgi.api.impl.ApiImplBootstrap;
import ch.vorburger.osgi.utils.BundleInstaller;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.spongepowered.api.plugin.PluginContainer;

public class Bootstrap {

    // TODO find a better name for this class and the factory method

    public static BundleInstaller bootstrapMinecraftOSGi(String osgiBasePath, PluginContainer pluginContainer) throws IOException, BundleException {
        File osgiBaseDirectory = new File(osgiBasePath);
        OSGiFrameworkWrapper osgiFramework = new OSGiFrameworkWrapper(osgiBaseDirectory);
        Bundle systemBundle = osgiFramework.start();

        ApiImplBootstrap apiBootstrap = new ApiImplBootstrap();
        apiBootstrap.start(systemBundle.getBundleContext(), null);

        return new Wrapper(osgiFramework, apiBootstrap);
    }

    private static class Wrapper implements BundleInstaller {

        private final BundleInstaller delegate;
        private final ApiImplBootstrap apiBootstrap;

        public Wrapper(BundleInstaller delegate, ApiImplBootstrap apiBootstrap) {
            super();
            this.delegate = delegate;
            this.apiBootstrap = apiBootstrap;
        }

        @Override
        public void close() throws Exception {
            apiBootstrap.stop();
            delegate.close();
        }

        @Override
        public List<Bundle> installBundles(File... locations) throws BundleException {
            return delegate.installBundles(locations);
        }

        @Override
        public List<Bundle> installBundles(String... locations) throws BundleException {
            return delegate.installBundles(locations);
        }

        @Override
        public BundleContext getBundleContext() {
            return delegate.getBundleContext();
        }

    }
}

