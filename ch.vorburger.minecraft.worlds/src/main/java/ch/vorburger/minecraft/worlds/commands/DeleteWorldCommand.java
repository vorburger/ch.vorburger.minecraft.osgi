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
import static org.spongepowered.api.command.args.GenericArguments.world;

import ch.vorburger.minecraft.utils.MessageReceivers;
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

public class DeleteWorldCommand implements CommandExecutor {

    private static final String ARG_WORLD = "world";

    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("Teleport to another world"))
                // TODO .permission("worlds.command.tpw")
                .arguments(onlyOne(world(Text.of(ARG_WORLD))))
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties> getOne(ARG_WORLD).get();

        if (!Sponge.getServer().getUnloadedWorlds().contains(worldProperties)) {
            Sponge.getServer().loadWorld(worldProperties).ifPresent(world -> {
                Sponge.getServer().unloadWorld(world);
            });
        }

        MessageReceivers.addCallback(Sponge.getServer().deleteWorld(worldProperties), commandSource, status ->
            commandSource.sendMessage(Text.of("world delete status: " + status))
        );
        return CommandResult.success();
    }

}
