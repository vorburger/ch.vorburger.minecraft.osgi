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
package ch.vorburger.minecraft.osgi.tests;

import java.io.File;
import java.util.List;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import ch.vorburger.minecraft.osgi.OSGiFrameworkWrapper;
import ch.vorburger.minecraft.osgi.api.impl.ApiImplBootstrap;

public class MinecraftOSGiTest {

    @Test
    public void testMain() throws Exception {
        // as in MinecraftSpongePlugin
        File temp = new File("target/testOsgiFramework");
        OSGiFrameworkWrapper osgiFramework = new OSGiFrameworkWrapper(temp);
        Bundle systemBundle = osgiFramework.start();
        ApiImplBootstrap apiBootstrap = new ApiImplBootstrap();
        apiBootstrap.start(systemBundle.getBundleContext(), null);

        List<Bundle> bundles = osgiFramework.installBundles("file:../ch.vorburger.minecraft.osgi.testplugin/build/libs/ch.vorburger.minecraft.osgi.testplugin-1.0.0-SNAPSHOT.jar");
        Bundle testPluginBundle = bundles.get(0);
        // assert that JAR has valid OSGi MANIFEST.MF with Bundle ID
        assertThat(testPluginBundle.getSymbolicName(), is("ch.vorburger.minecraft.osgi.testplugin"));
        assertThat(testPluginBundle.getState(), is(Bundle.ACTIVE));
        ServiceReference<?>[] testPluginBundleServices = testPluginBundle.getRegisteredServices();
        assertThat(testPluginBundleServices, is(not(nullValue())));
        assertThat(testPluginBundleServices.length, is(2));
        assertThat(testPluginBundleServices[0].getProperty("objectClass"),
                is(new String[] { "ch.vorburger.minecraft.osgi.api.CommandRegistration" }));
        assertThat(testPluginBundleServices[1].getProperty("objectClass"),
                is(new String[] { "ch.vorburger.minecraft.osgi.api.Listeners" }));

        testPluginBundle.uninstall();
        // TODO Test that the command got un-registered..

        // as in MinecraftSpongePlugin
        apiBootstrap.stop();
        osgiFramework.stop();
    }

}
