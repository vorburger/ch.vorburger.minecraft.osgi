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
import static org.spongepowered.api.command.args.GenericArguments.playerOrSource;
import static org.spongepowered.api.command.args.GenericArguments.seq;
import static org.spongepowered.api.command.args.GenericArguments.world;

import com.flowpowered.math.vector.Vector3d;
import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

public class TeleportToWorldCommand implements CommandExecutor {

    private static final String ARG_TARGET = "target";
    private static final String ARG_WORLD = "world";

    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("Teleport to another world"))
                .permission("world.command.tp")
                .arguments(seq(playerOrSource(Text.of(ARG_TARGET)), onlyOne(world(Text.of(ARG_WORLD)))))
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties> getOne(ARG_WORLD).get();
        commandSource.sendMessage(Text.of("OK, we'll teleport you into ",
                Text.of(TextColors.GOLD, worldProperties.getWorldName()),
                " in a moment..."));
        Optional<World> optWorld = Sponge.getServer().loadWorld(worldProperties);
        if (!optWorld.isPresent()) {
            throw new CommandException(Text.of("World [", Text.of(TextColors.AQUA, worldProperties.getWorldName()), "] was not found."));
        }
        World world = optWorld.get();

        for (Player target : args.<Player>getAll(ARG_TARGET)) {
            // TODO could persist last position of Player per world instead of jumping back to spawn
            Vector3d spawnPosition = worldProperties.getSpawnPosition().toDouble();
            target.transferToWorld(world.getName(), spawnPosition);
            target.setLocationSafely(target.getLocation());
        }
        return CommandResult.success();
    }

}
