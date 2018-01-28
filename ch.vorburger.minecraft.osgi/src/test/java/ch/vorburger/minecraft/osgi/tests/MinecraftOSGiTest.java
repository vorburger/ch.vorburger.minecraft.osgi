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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import ch.vorburger.minecraft.osgi.Bootstrap;
import ch.vorburger.osgi.utils.BundleInstaller;
import java.util.List;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class MinecraftOSGiTest {

    @Test
    public void testMain() throws Exception {
        BundleInstaller osgiFramework = Bootstrap.bootstrapMinecraftOSGi("target/testOsgiFramework", () -> null);

        List<Bundle> bundles = osgiFramework.installBundles("file:../ch.vorburger.minecraft.osgi.testplugin/build/libs/ch.vorburger.minecraft.osgi.testplugin-1.0.0-SNAPSHOT.jar");
        Bundle testPluginBundle = bundles.get(0);
        // assert that JAR has valid OSGi MANIFEST.MF with Bundle ID
        assertThat(testPluginBundle.getSymbolicName(), is("ch.vorburger.minecraft.osgi.testplugin"));
        assertThat(testPluginBundle.getState(), is(Bundle.ACTIVE));
        ServiceReference<?>[] testPluginBundleServices = testPluginBundle.getRegisteredServices();
        assertThat(testPluginBundleServices, is(not(nullValue())));
        // TODO un-comment when https://github.com/SpongePowered/SpongeCommon/pull/1090 is merged
        // assertThat(testPluginBundleServices.length, is(3));
        assertThat(testPluginBundleServices.length, is(2));
        assertThat(testPluginBundleServices[0].getProperty("objectClass"),
                is(new String[] { "ch.vorburger.minecraft.osgi.api.CommandRegistration" }));
        // TODO un-comment when https://github.com/SpongePowered/SpongeCommon/pull/1090 is merged
        // assertThat(testPluginBundleServices[1].getProperty("objectClass"),
        //        is(new String[] { "ch.vorburger.minecraft.osgi.api.Listeners" }));
        // assertThat(testPluginBundleServices[2].getProperty("objectClass"),
        assertThat(testPluginBundleServices[1].getProperty("objectClass"),
                is(new String[] { "org.spongepowered.api.event.EventListener" }));

        testPluginBundle.uninstall();
        // TODO Test that the command got un-registered..

        osgiFramework.close();
    }

}
