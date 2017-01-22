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

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;

public class WorldCommands implements CommandRegistration {

    @Override
    public List<String> aliases() {
        return ImmutableList.of("world");
    }

    @Override
    public CommandCallable callable() {
        return CommandSpec.builder()
                .permission("ch.vorburger.worlds.command.")
                .child(new ListWorldsCommand().callable(), "list")
                .child(new TeleportToWorldCommand().callable(), "tp")
                .child(new ImportWorldCommand().callable(), "import")
                // Delete doesn't really seem to work all too well? Let's not offer it to users, yet:
                // .child(new DeleteWorldCommand(), "delete", "d", "rm")
                .child(new RenameWorldCommand().callable(), "rename")
                .child(new CreateWorldCommand().callable(), "create")
                .child(new ForkWorldCommand().callable(), "fork")
                .build();
    }

}
