package ch.vorburger.minecraft.osgi.tests;

import java.io.File;
import java.util.List;
import org.junit.Test;
import org.osgi.framework.Bundle;
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

        List<Bundle> bundles = osgiFramework.installBundles("file:../ch.vorburger.minecraft.osgi.testplugin/target/osgi.testplugin-1.0.0-SNAPSHOT.jar");
        // TODO Test that the bundle got activated and command got registered..
        bundles.get(0).uninstall();
        // TODO Test that the command got un-registered..

        // as in MinecraftSpongePlugin
        apiBootstrap.stop();
        osgiFramework.stop();
    }

}
