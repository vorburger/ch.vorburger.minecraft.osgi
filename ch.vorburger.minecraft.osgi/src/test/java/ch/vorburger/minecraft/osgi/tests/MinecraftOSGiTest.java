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
        assertThat(testPluginBundleServices.length, is(1));
        assertThat(testPluginBundleServices[0].getProperty("objectClass"),
        		is(new String[] { "ch.vorburger.minecraft.osgi.api.CommandRegistration" }));

        testPluginBundle.uninstall();
        // TODO Test that the command got un-registered..

        // as in MinecraftSpongePlugin
        apiBootstrap.stop();
        osgiFramework.stop();
    }

}
