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
import org.spongepowered.api.world.storage.WorldProperties;

public class ListWorldsCommand implements CommandExecutor {

    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("lists available worlds on this server"))
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        for (WorldProperties wordProperties : Sponge.getServer().getAllWorldProperties()) {
            // TODO print a table with more properties (utility to format properties into table)
            // TODO make world name clickable to /tpw to
            String line = wordProperties.getWorldName() + " (#" + wordProperties.getUniqueId() + ")";
            if (commandSource instanceof Player) {
                Player player = (Player) commandSource;
                if (player.getLocation().getExtent().getName().equals(wordProperties.getWorldName())) {
                    line += " <==";
                }
            }
            commandSource.sendMessage(Text.of(line));
        }
        return CommandResult.success();
    }

}
