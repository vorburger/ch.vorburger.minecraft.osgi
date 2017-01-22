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
package ch.vorburger.minecraft.worlds.commands;

import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.seq;
import static org.spongepowered.api.command.args.GenericArguments.string;
import static org.spongepowered.api.command.args.GenericArguments.world;

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import ch.vorburger.minecraft.utils.CommandExceptions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

public class RenameWorldCommand implements CommandRegistration, CommandExecutor {

    private static final String ARG_WORLD = "world";
    private static final String ARG_NEW_NAME = "newName";

    @Override
    public List<String> aliases() {
        return ImmutableList.of("world-rename");
    }

    @Override
    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("rename a world"))
                // TODO .permission("ch.vorburger.worlds.command.rename")
                .arguments(seq(onlyOne(world(Text.of(ARG_WORLD))), onlyOne(string(Text.of(ARG_NEW_NAME)))))
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties> getOne(ARG_WORLD).get();
        String newWorldName = args.<String> getOne(ARG_NEW_NAME).get();

        // This is broken in 1.10.2-5.1.0; see https://github.com/SpongePowered/SpongeVanilla/issues/293

        // WorldProperties newWorldProperties =
                Sponge.getServer().renameWorld(worldProperties, newWorldName)
                    .orElseThrow(() -> CommandExceptions.create("Failed to rename"));

        return CommandResult.success();
    }

}
