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

import ch.vorburger.minecraft.utils.CommandExceptions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.WorldArchetypes;
import org.spongepowered.api.world.storage.WorldProperties;

public class CreateWorldCommand implements CommandExecutor {

    private static final String ARG_NAME = "name";

    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("Teleport to another world"))
                // TODO .permission("worlds.command.tpw")
                .arguments(seq(onlyOne(string(Text.of(ARG_NAME)))))
/*

            .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.flags()
                    .valueFlag(GenericArguments.catalogedElement(Text.of("dimensionType"), DimensionType.class), "d")
                    .valueFlag(GenericArguments.catalogedElement(Text.of("generatorType"), GeneratorType.class), "g")
                    .valueFlag(GenericArguments.catalogedElement(Text.of("modifier"), WorldGeneratorModifier.class), "m")
                    .valueFlag(GenericArguments.string(Text.of("seed")), "s").buildWith(GenericArguments.none()))

             .arguments(GenericArguments.optional(GenericArguments.string(Text.of("world"))), GenericArguments.optional(GenericArguments.catalogedElement(Text.of("dimensionType"), DimensionType.class)),
                    GenericArguments.optional(GenericArguments.catalogedElement(Text.of("generatorType"), GeneratorType.class)), GenericArguments.optional(GenericArguments.catalogedElement(Text.of("modifier"), WorldGeneratorModifier.class)))

 */
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        String newWorldName = args.<String>getOne(ARG_NAME).get();

        // TODO more configuration options!

        WorldProperties loadedWorldProperties = CommandExceptions.getOrThrow("createWorldProperties: " + newWorldName,
                () -> Sponge.getServer().createWorldProperties(newWorldName, WorldArchetypes.THE_SKYLANDS));
        commandSource.sendMessage(Text.of("Create new world!"));
        return CommandResult.success();
    }

}
