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
package ch.vorburger.minecraft.osgi.templates

class SimpleProjectTemplate implements ProjectTemplate {

    override newProject() {
        #{
            buildGradle(),
            bnd(),
            sources()
        }
    }

    def buildGradle() {
        "build.gradle" -> '''
            buildscript {
              repositories {
                jcenter()
              }
              dependencies {
                classpath 'biz.aQute.bnd:biz.aQute.bnd.gradle:3.3.0'
              }
            }

            repositories {
              mavenLocal()
              jcenter() // maven { url "http://repo.maven.apache.org/maven2" }
              maven { url "http://repo.spongepowered.org/maven" }
            }

            apply plugin: 'java'
            apply plugin: 'biz.aQute.bnd.builder'

            sourceCompatibility = 1.8
            targetCompatibility = 1.8

            dependencies {
              compile 'ch.vorburger.minecraft:osgi.api:1.0.0-SNAPSHOT'
              compile 'org.osgi:org.osgi.core:6.0.0'
            }
        '''
    }

    def bnd() {
        "bnd.bnd" -> '''
            Bundle-Activator: ${classes;IMPLEMENTS;org.osgi.framework.BundleActivator}
        '''
    }

    def sources() {
        // TODO make package flexible
        "src/main/java/test/Activator.java" -> '''
            package test;

            import org.osgi.framework.BundleActivator;
            import org.osgi.framework.BundleContext;

            public class Activator implements BundleActivator {

                @Override
                public void start(BundleContext context) throws Exception {
                }

                @Override
                public void stop(BundleContext context) throws Exception {
                }

            }
        '''
    }


}
