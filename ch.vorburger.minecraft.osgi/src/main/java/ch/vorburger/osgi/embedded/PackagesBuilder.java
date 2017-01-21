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
package ch.vorburger.osgi.embedded;

import com.google.common.base.Joiner;
import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utility to build OSGi MANIFEST package declarations.
 *
 * @author Michael Vorburger
 */
public class PackagesBuilder {

    private final List<String> packages = new ArrayList<>();

    public PackagesBuilder addPackage(String name) {
        packages.add(name);
        return this;
    }

    public PackagesBuilder addPackage(String name, String version) {
        packages.add(name + ";version=\"" + version + "\"");
        return this;
    }

    public PackagesBuilder addPackageWithSubPackages(String basePackageName) {
        getSubPackageNames(basePackageName)
            .forEach(packageName -> packages.add(packageName));
        return this;
    }

    public PackagesBuilder addPackageWithSubPackages(String basePackageName, String version) {
        getSubPackageNames(basePackageName)
            .map(packageName -> packageName + ";version=\""+ version + "\"")
            .forEach(packageNameWithVersion -> packages.add(packageNameWithVersion));
        return this;
    }


    public String build() {
        return Joiner.on(",").join(packages);
    }

    @Override
    public String toString() {
        return build();
    }

    private Stream<String> getSubPackageNames(String basePackageName) {
        try {
            ClassPath classPath = ClassPath.from(getClass().getClassLoader());
            return classPath.getTopLevelClassesRecursive(basePackageName)
                    .stream().map(classInfo -> classInfo.getPackageName()).distinct();
        } catch (IOException e) {
            throw new IllegalStateException("ClassPath.from(classLoader) failed", e);
        }
    }

}
