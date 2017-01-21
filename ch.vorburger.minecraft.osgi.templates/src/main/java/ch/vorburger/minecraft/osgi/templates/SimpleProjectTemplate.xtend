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

    // TODO make package flexible!
    override newProject() {
        #{"build.gradle" -> '''
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
              jcenter()
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

        ''', "bnd.bnd" -> '''
            Bundle-Activator: ${classes;IMPLEMENTS;org.osgi.framework.BundleActivator}

        ''', "src/main/java/demo/Activator.java" -> '''
            package demo;

            import ch.vorburger.minecraft.osgi.api.CommandRegistration;
            import org.osgi.framework.BundleActivator;
            import org.osgi.framework.BundleContext;
            import org.spongepowered.api.event.EventListener;

            public class Activator implements BundleActivator {

                @Override
                public void start(BundleContext context) throws Exception {
                    context.registerService(CommandRegistration.class, new HelloWorldCommand(), null);
                    context.registerService(EventListener.class, new ExampleEventListener(), null);
                }

                @Override
                public void stop(BundleContext context) throws Exception {
                }

            }

        ''', "src/main/java/demo/HelloWorldCommand.java" -> '''
            package demo;

            import ch.vorburger.minecraft.osgi.api.CommandRegistration;
            import com.google.common.collect.ImmutableList;
            import java.util.List;
            import org.spongepowered.api.command.CommandCallable;
            import org.spongepowered.api.command.CommandException;
            import org.spongepowered.api.command.CommandResult;
            import org.spongepowered.api.command.CommandSource;
            import org.spongepowered.api.command.args.CommandContext;
            import org.spongepowered.api.command.spec.CommandExecutor;
            import org.spongepowered.api.command.spec.CommandSpec;
            import org.spongepowered.api.text.Text;

            public class HelloWorldCommand implements CommandRegistration, CommandExecutor {

                @Override
                public List<String> aliases() {
                    return ImmutableList.of("helloworld", "hello", "test");
                }

                @Override
                public CommandCallable callable() {
                    return CommandSpec.builder()
                            .description(Text.of("Hello World Command"))
                            .executor(this)
                            .build();
                }

                @Override
                public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
                    src.sendMessage(Text.of("Hello World!"));
                    return CommandResult.success();
                }

            }

        ''', "src/main/java/demo/ExampleEventListener.java" -> '''
            package demo;

            import org.spongepowered.api.entity.living.player.Player;
            import org.spongepowered.api.event.EventListener;
            import org.spongepowered.api.event.network.ClientConnectionEvent;
            import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
            import org.spongepowered.api.text.Text;

            public class ExampleEventListener implements EventListener<ClientConnectionEvent.Join> {

                @Override
                public void handle(Join joinEvent) throws Exception {
                    Player player = joinEvent.getTargetEntity();
                    String name = player.getName();
                    player.sendMessage(Text.builder("Salut ").append(Text.of(name)).build());
                }

            }
        '''}
    }

}
