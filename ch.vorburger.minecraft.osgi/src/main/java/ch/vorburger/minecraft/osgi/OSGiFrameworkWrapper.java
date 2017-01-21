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

import ch.vorburger.osgi.embedded.PackagesBuilder;
import ch.vorburger.osgi.utils.BundleInstaller;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

public class OSGiFrameworkWrapper implements BundleInstaller {

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
        config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, new PackagesBuilder()
                .addPackage("org.slf4j", "1.7")
                .addPackage("ch.vorburger.minecraft.osgi.api")
                .addPackageWithSubPackages("com.google.common", "17.0.0")
                .addPackageWithSubPackages("org.spongepowered.api")
                .build()
            );

        FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
        framework = frameworkFactory.newFramework(config);

        this.bootBundlesDirectory = bootBundlesDirectory;
        this.hotBundlesDirectory = hotBundlesDirectory;
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

        return framework;
    }

    public List<Bundle> installBootBundles() throws BundleException {
        File[] jarFiles = bootBundlesDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null) {
            LOG.warn("Nothing found in {} (does not exist?)", bootBundlesDirectory);
            return Collections.emptyList();
        } else {
            LOG.info("Going to (non-HOT) install {} OSGi System Boot Bundle JARs found in {}", jarFiles.length, bootBundlesDirectory);
            return installBundles(jarFiles);
        }
    }

    @Override
    public List<Bundle> installBundles(File... locations) throws BundleException {
        String[] locationsAsStringURI = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            File location = locations[i];
            locationsAsStringURI[i] = location.toURI().toASCIIString();
        }
        return installBundles(locationsAsStringURI);
    }

    @Override
    public List<Bundle> installBundles(String... locations) throws BundleException {
        List<String> sortedLocations = Arrays.asList(locations);
        sortedLocations.sort((o1, o2) -> o1.compareTo(o2));
        LOG.info("Sorted locations; now going to install and start OSGi bundles in this order: {}", sortedLocations);

        BundleContext frameworkBundleContext = framework.getBundleContext();
        List<Bundle> bundlesToStart = new LinkedList<>();

        for (String location : sortedLocations) {
            try {
                bundlesToStart.add(frameworkBundleContext.installBundle(location));
                LOG.info("Installed bundle from {}", location);
            } catch (BundleException e) {
                LOG.error("Failed to install bundle from {}", location, e);
            }
        }

        for (Bundle bundle : bundlesToStart) {
            if (bundle.getHeaders().get(Constants.FRAGMENT_HOST) == null) {
                try {
                    bundle.start();
                    LOG.info("Started bundle {}", bundle.getSymbolicName());
                } catch (BundleException e) {
                    LOG.error("Failed to start bundle {}", bundle.getSymbolicName(), e);
                }
            }
        }

        return bundlesToStart;
    }

    public void stop() throws InterruptedException, BundleException {
        framework.stop();
        FrameworkEvent event = framework.waitForStop(7000); // 7s
        loggingFrameworkListener.log(event);
    }

    @Override
    public void close() throws Exception {
        this.stop();
    }

    @Override
    public BundleContext getBundleContext() {
        return framework.getBundleContext();
    }

}
