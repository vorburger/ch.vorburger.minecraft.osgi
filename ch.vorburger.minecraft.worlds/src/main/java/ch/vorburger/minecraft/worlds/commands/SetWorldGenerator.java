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

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.GeneratorTypes;

public class SetWorldGenerator implements CommandExecutor {

    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("set a world's generator"))
                .permission("ch.vorburger.worlds.command.setgenerator")
                // .arguments(seq(onlyOne(world(Text.of(ARG_WORLD))), onlyOne(string(Text.of(ARG_NEW_NAME)))))
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Player player = (Player) src;
            player.getWorld().getProperties().setGeneratorType(GeneratorTypes.AMPLIFIED);
            player.getWorld().getProperties().setThundering(true);
        }
        return CommandResult.success();
    }

}
