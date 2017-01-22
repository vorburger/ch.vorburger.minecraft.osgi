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

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import ch.vorburger.minecraft.utils.CommandExceptions;
import ch.vorburger.minecraft.utils.Texts;
import com.google.common.collect.ImmutableList;
import java.io.File;
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
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.WorldArchetypes;
import org.spongepowered.api.world.storage.WorldProperties;

public class ImportWorldCommand implements CommandRegistration, CommandExecutor {

    @Override
    public List<String> aliases() {
        return ImmutableList.of("world-import");
    }

    @Override
    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("Load an existing world"))
                // TODO .permission("ch.vorburger.worlds.command.load")
                .arguments(seq(
                        onlyOne(string(Text.of("uri"))))
                    )
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        String worldURI = args.<String>getOne("uri").get();
        // TODO (HACK) for now we'll just interpret this as a directory
        // the idea is to later handle URIs, like in BundleManager, and be able to import e.g. from GitHub!! ;)
        File worldSavesDir = Sponge.getGame().getSavesDirectory().resolve("world").toFile();
        // worlds apparently (learnt the hard way..) HAVE to be in the saves directory!
        // if later we want arbitrary dirs, or stream from git, we'd have to copy/move them here:
        File worldDirectory = new File(worldSavesDir, worldURI);
        if (!(worldDirectory.exists() && worldDirectory.isDirectory())) {
            throw new CommandException(Text.of("Must be an existing directory, but isn't: " + worldDirectory));
        }

        // TODO this WorldArchetype metadata should be read from some properties [JSON/YAML] file along the world/ (saves/ ?) dir.
        String newWorldName = worldDirectory.getName();
        WorldArchetype archetype = WorldArchetypes.OVERWORLD;

        WorldProperties loadedWorldProperties = CommandExceptions.getOrThrow("createWorldProperties: " + newWorldName,
                () -> Sponge.getServer().createWorldProperties(newWorldName, archetype));
        if (loadedWorldProperties != null) {
            Sponge.getServer().saveWorldProperties(loadedWorldProperties);

            if (!loadedWorldProperties.getWorldName().equals(newWorldName)) {
                commandSource.sendMessage(Texts.inRed("Uh oh; imported folder name does not match new world name... this is BAD!"));
            }

            // TODO .onClick(TextActions.executeCallback(/tpw) ..
            commandSource.sendMessage(Text.of("OK, created new world " + loadedWorldProperties.getWorldName()));
        } else {
            throw CommandExceptions.create("Failed to load: " + newWorldName);
        }
        return CommandResult.success();
    }

}
