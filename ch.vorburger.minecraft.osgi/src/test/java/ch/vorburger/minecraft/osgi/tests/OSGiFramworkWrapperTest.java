package ch.vorburger.minecraft.osgi.tests;

import org.junit.Test;

import ch.vorburger.minecraft.osgi.OSGiFrameworkWrapper;

public class OSGiFramworkWrapperTest {

    @Test
    public void testMain() throws Exception {
        OSGiFrameworkWrapper wrapper = new OSGiFrameworkWrapper();
        wrapper.start();
        // wrapper.installBundles("file:org.apache.felix.shell-1.4.2.jar", "file:org.apache.felix.shell.tui-1.4.1.jar");
        wrapper.installBundles("file:../ch.vorburger.minecraft.osgi.testplugin/target/osgi.testplugin-1.0.0-SNAPSHOT.jar");
        wrapper.stop();
    }

}
