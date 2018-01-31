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
package ch.vorburger.minecraft.osgi.templates.tests;

import ch.vorburger.minecraft.osgi.templates.ProjectTemplate;
import ch.vorburger.minecraft.osgi.templates.ProjectWriter;
import ch.vorburger.minecraft.osgi.templates.SimpleProjectTemplate;
import ch.vorburger.osgi.builder.gradle.internal.GradleBuildService;
import ch.vorburger.osgi.builder.internal.BuildService;
import com.google.common.io.Files;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class SimpleProjectTemplateGenerateAndBuildTest {

    @Test
    public void generateProjectAndBuild() throws Exception {
        ProjectTemplate template = new SimpleProjectTemplate();
        File testTemplateProjectBaseDir = Files.createTempDir();
        try {
            new ProjectWriter().writeProject(template, testTemplateProjectBaseDir);
            try (BuildService bs = new GradleBuildService()) {
                bs.build(testTemplateProjectBaseDir, "build").get(1, TimeUnit.MINUTES);
            }
        } finally {
            FileUtils.deleteQuietly(testTemplateProjectBaseDir);
        }
    }
}
