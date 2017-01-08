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

import com.google.common.base.Joiner;
import com.google.common.reflect.ClassPath;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSGiFrameworkWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(OSGiFrameworkWrapper.class);

    private final LoggingFrameworkListener loggingFrameworkListener = new LoggingFrameworkListener();

    private final Framework framework;
    private final File bootBundlesDirectory;
    private final File hotBundlesDirectory;

    public OSGiFrameworkWrapper(File osgiBaseDirectory) throws IOException {
        this(new File(osgiBaseDirectory, "storage"),
             new File(osgiBaseDirectory, "boot"),
             new File(osgiBaseDirectory, "hot"));
    }

    @SuppressWarnings("deprecation")
    public OSGiFrameworkWrapper(File frameworkStorageDirectory, File bootBundlesDirectory, File hotBundlesDirectory) throws IOException {
        Map<String, String> config = new HashMap<>();
        // https://svn.apache.org/repos/asf/felix/releases/org.apache.felix.main-1.2.0/doc/launching-and-embedding-apache-felix.html#LaunchingandEmbeddingApacheFelix-configproperty
        config.put("felix.embedded.execution", "true");
        config.put(Constants.FRAMEWORK_EXECUTIONENVIRONMENT, "J2SE-1.8");
        config.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
        config.put(Constants.FRAMEWORK_STORAGE, frameworkStorageDirectory.getAbsolutePath());
        // not FRAMEWORK_SYSTEMPACKAGES but _EXTRA
        config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
                "org.slf4j;version=\"1.7\","
              + "ch.vorburger.minecraft.osgi.api,"
              + getSubPackages("org.spongepowered.api")
            );

        FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
        framework = frameworkFactory.newFramework(config);

        this.bootBundlesDirectory = bootBundlesDirectory;
        this.hotBundlesDirectory = hotBundlesDirectory;
    }

    private String getSubPackages(String basePackageName) throws IOException {
        ClassPath classPath = ClassPath.from(getClass().getClassLoader());
        Iterator<String> packageNames = classPath.getTopLevelClassesRecursive(basePackageName)
            .stream().map(classInfo -> classInfo.getPackageName()).distinct().iterator();
        return Joiner.on(",").join(packageNames);
    }

    /**
     * http://njbartlett.name/2011/07/03/embedding-osgi.html
     *
     * http://felix.apache.org/documentation/subprojects/apache-felix-framework/apache-felix-framework-launching-and-embedding.html
     *
     * @return OSGi Framework System Bundle (#0)
     */
    public Bundle start() throws BundleException {
        framework.init(loggingFrameworkListener);
        BundleContext frameworkBundleContext = framework.getBundleContext();
        frameworkBundleContext.addFrameworkListener(loggingFrameworkListener);
        frameworkBundleContext.addBundleListener(new LoggingBundleListener());
        frameworkBundleContext.addServiceListener(new LoggingServiceListener());
        framework.start();
        LOG.info("OSGi Framework init() & start() OK");

        // http://felix.apache.org/documentation/subprojects/apache-felix-file-install.html
        // assuming that org.apache.felix.fileinstall-*.jar is in bootBundlesDirectory
        System.setProperty("felix.fileinstall.dir", hotBundlesDirectory.getAbsolutePath());
        System.setProperty("felix.fileinstall.debug", "1");
        System.setProperty("felix.fileinstall.noInitialDelay", "true");

        File[] jarFiles = bootBundlesDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null) {
            LOG.warn("Nothing found in {} (does not exist?)", bootBundlesDirectory);
        } else {
            LOG.info("Going to (non-HOT) install {} OSGi System Boot Bundle JARs found in {}", jarFiles.length, bootBundlesDirectory);
            installBundles(jarFiles);
        }

        return framework;
    }

    public List<Bundle> installBundles(File... locations) throws BundleException {
        String[] locationsAsStringURI = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            File location = locations[i];
            locationsAsStringURI[i] = location.toURI().toASCIIString();
        }
        return installBundles(locationsAsStringURI);
    }

    public List<Bundle> installBundles(String... locations) throws BundleException {
        BundleContext frameworkBundleContext = framework.getBundleContext();
        List<Bundle> bundlesToInstall = new LinkedList<>();

        for (String location : locations) {
            bundlesToInstall.add(frameworkBundleContext.installBundle(location));
            LOG.info("Installed bundle from {}", location);
        }

        for (Bundle bundle : bundlesToInstall) {
            if (bundle.getHeaders().get(Constants.FRAGMENT_HOST) == null) {
                bundle.start();
                LOG.info("Started bundle {}", bundle.getSymbolicName());
            }
        }

        return bundlesToInstall;
    }

    public void stop() throws InterruptedException, BundleException {
        framework.stop();
        FrameworkEvent event = framework.waitForStop(7000); // 7s
        loggingFrameworkListener.log(event);
    }

}
