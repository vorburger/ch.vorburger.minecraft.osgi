package ch.vorburger.minecraft.osgi.tests;

import java.io.File;
import org.junit.Ignore;
import org.junit.Test;

import ch.vorburger.minecraft.osgi.OSGiFrameworkWrapper;

public class OSGiFramworkWrapperTest {

    @Test
    @Ignore // TODO re-activate, when proper service injection is implemented
    public void testMain() throws Exception {
        File temp = new File("target/testOsgiFramework");
        OSGiFrameworkWrapper wrapper = new OSGiFrameworkWrapper(temp);
        wrapper.start();
        // wrapper.installBundles("file:org.apache.felix.shell-1.4.2.jar", "file:org.apache.felix.shell.tui-1.4.1.jar");
        wrapper.installBundles("file:../ch.vorburger.minecraft.osgi.testplugin/target/osgi.testplugin-1.0.0-SNAPSHOT.jar");
        // TODO Test that the bundle activated and did something..
        wrapper.stop();
    }

}
