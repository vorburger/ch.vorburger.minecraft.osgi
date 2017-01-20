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
package ch.vorburger.minecraft.osgi.templates.test;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import ch.vorburger.minecraft.osgi.templates.ProjectTemplate;
import ch.vorburger.minecraft.osgi.templates.ProjectWriter;
import ch.vorburger.minecraft.osgi.templates.SimpleProjectTemplate;
import ch.vorburger.osgi.gradle.SourceInstallService;
import com.google.common.io.Files;
import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@RunWith(PaxExam.class)
public class SimpleProjectTemplateOSGiTest implements AutoCloseable {

    final File testBundleProjectDir = new File("../ch.vorburger.osgi.gradle.test.bundle.provider");
    final File testBundleSourceFile = new File(testBundleProjectDir, "src/main/java/ch/vorburger/osgi/gradle/test/bundle/provider/TestServiceImpl.java");

    @Inject BundleContext bundleContext;
    @Inject SourceInstallService sourceInstallService;

/*
    @Before
    public void before() {
        bundleContext.addBundleListener(new LoggingBundleListener());
        bundleContext.addServiceListener(new LoggingServiceListener());
    }
*/
    @After
    @Override
    public void close() throws Exception {
        sourceInstallService.close();
    }

    @Configuration
    public Option[] config() {
        return options(
                systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
                mavenBundle("com.google.guava", "guava", "18.0"),
                mavenBundle("com.google.guava", "guava", "20.0"),
                // wrappedBundle(maven("org.awaitility", "awaitility", "2.0.0")),
                bundle("file:../../ch.vorburger.osgi.gradle/org.gradle.tooling.osgi/build/libs/org.gradle.tooling.osgi-3.3.jar"),
                bundle("file:../../ch.vorburger.osgi.gradle/ch.vorburger.osgi.gradle/build/libs/ch.vorburger.osgi.gradle-1.0.0-SNAPSHOT.jar"),
                wrappedBundle(maven("org.eclipse.xtext", "org.eclipse.xtext.xbase.lib", "2.10.0")),
                wrappedBundle(maven("org.eclipse.xtend", "org.eclipse.xtend.lib", "2.10.0")),
                wrappedBundle(maven("org.eclipse.xtend", "org.eclipse.xtend.lib.macro", "2.10.0")),
                bundle("file:../ch.vorburger.minecraft.osgi.templates/build/libs/ch.vorburger.minecraft.osgi.templates-1.0.0-SNAPSHOT.jar"),
                wrappedBundle(maven("commons-io", "commons-io", "2.5")),
                junitBundles());
    }

    @Test
    public void generateProjectAndTestLoadingItIntoOSGi() throws Exception {
        assertNotNull(sourceInstallService);
        ProjectTemplate template = new SimpleProjectTemplate();
        File testTemplateProjectBaseDir = Files.createTempDir();
        File testOsgiFrameworkDir = Files.createTempDir();
        try {
            new ProjectWriter().writeProject(template, testTemplateProjectBaseDir);
            Future<Bundle> futureBundle = sourceInstallService.installSourceBundle(testTemplateProjectBaseDir);
            Bundle bundle = futureBundle.get(30, TimeUnit.SECONDS);
            bundle.start();
            // TODO stop continously building it now
        } finally {
            FileUtils.deleteQuietly(testTemplateProjectBaseDir);
        }
    }
}
