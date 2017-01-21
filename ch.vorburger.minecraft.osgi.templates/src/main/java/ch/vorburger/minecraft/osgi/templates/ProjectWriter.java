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
package ch.vorburger.minecraft.osgi.templates;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class ProjectWriter {

    public void writeProject(ProjectTemplate template, File baseDir) throws IOException {
        Map<String, String> files = template.newProject();
        for (Entry<String, String> entry : files.entrySet()) {
            File file = new File(baseDir, entry.getKey());
            File parentDirectory = file.getParentFile();
            if (!parentDirectory.exists()) {
                if (!file.getParentFile().mkdirs()) {
                    throw new IOException("Failed to mkdirs: " + parentDirectory);
                }
            }
            Files.write(entry.getValue(), file, Charsets.UTF_8);
        }
    }
}
