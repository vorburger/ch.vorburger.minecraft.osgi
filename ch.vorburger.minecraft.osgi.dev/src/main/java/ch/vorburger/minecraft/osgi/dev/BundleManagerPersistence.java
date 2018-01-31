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
package ch.vorburger.minecraft.osgi.dev;

import ch.vorburger.minecraft.utils.LoggingFutureCallback;
import ch.vorburger.osgi.builder.SourceInstallService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Persist installed bundles so that after server restart they can be re-installed.
 *
 * @author Michael Vorburger.ch
 */
class BundleManagerPersistence {

    private static final Logger LOG = LoggerFactory.getLogger(BundleManagerPersistence.class);

    // TODO (minor) don't hard-code osgi/ directory this here; find suitable config location from BundleContext
    private final File installedBundlesPersistenceFile = new File("osgi/installedBundles");

    private final SourceInstallService sourceInstallService;

    public BundleManagerPersistence(SourceInstallService sourceInstallService) {
        this.sourceInstallService = sourceInstallService;
    }

    public synchronized void add(File bundleFileOrDirectory) {
        try {
            String uri = bundleFileOrDirectory.getAbsolutePath();
            Files.append("\n" + uri, installedBundlesPersistenceFile, Charsets.UTF_8);
            LOG.info("Appended {} to {}", uri, installedBundlesPersistenceFile);
        } catch (IOException e) {
            LOG.error("Failed to append {}", installedBundlesPersistenceFile, e);
        }
    }

    public void installAndStartAll() {
        try {
            if (!installedBundlesPersistenceFile.exists()) {
                Files.write("", installedBundlesPersistenceFile, Charsets.UTF_8);
            }
            List<String> lines = Files.readLines(installedBundlesPersistenceFile, Charsets.UTF_8);
            lines = lines.stream().filter(line -> !line.trim().isEmpty()).collect(Collectors.toList());
            LOG.info("Found {} bundles in {}, going to install and start now..", lines.size(), installedBundlesPersistenceFile);
            for (String line : lines) {
                Futures.addCallback(sourceInstallService.installSourceBundle(new File(line)),
                        new LoggingFutureCallback<Bundle>(LOG) {
                            @Override
                            protected void onSuccessWithException(Bundle bundle) throws Exception {
                                bundle.start();
                            }
                        });
            }
        } catch (IOException e) {
            LOG.error("Failed to read " + installedBundlesPersistenceFile, e);
        }
    }

}
