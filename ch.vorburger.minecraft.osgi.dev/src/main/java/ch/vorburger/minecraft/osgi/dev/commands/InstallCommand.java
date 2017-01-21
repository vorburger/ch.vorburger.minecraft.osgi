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
package ch.vorburger.minecraft.osgi.dev.commands;

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import ch.vorburger.minecraft.osgi.dev.BundleManager;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * The "/osgi:install <uri>" command.
 *
 * @author Michael Vorburger.ch
 */
public class InstallCommand implements CommandRegistration, CommandExecutor {

    private final BundleManager bundleManager;

    public InstallCommand(BundleManager bundleManager) {
        this.bundleManager = bundleManager;
    }

    @Override
    public List<String> aliases() {
        return ImmutableList.of("install");
    }

    @Override
    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("installs an OSGi bundle"))
                // TODO .permission("osgi.install")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("uri"))))
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        String bundleURI = args.<String>getOne("uri").get();
        bundleManager.installBundle(commandSource, bundleURI);
        return CommandResult.success();
    }

}
