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
package ch.vorburger.minecraft.osgi.api;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.spongepowered.api.command.CommandCallable;

public class CommandRegistrationImpl implements CommandRegistration {

    private final CommandCallable callable;
    private final List<String> aliases;

    protected CommandRegistrationImpl(CommandCallable callable, List<String> aliases) {
        this.callable = callable;
        this.aliases = ImmutableList.copyOf(aliases);
    }

    protected CommandRegistrationImpl(CommandCallable callable, String... alias) {
        this(callable, ImmutableList.copyOf(alias));
    }

    @Override
    public CommandCallable callable() {
        return callable;
    }

    @Override
    public List<String> aliases() {
        return aliases;
    }

    @Override
    public String toString() {
        return "CommandRegistration[aliases=" + aliases + "; callable=" + callable.toString() + "]";
            // TODO use callable getHelp() or getUsage(), but what to pass as CommandSource?
    }
}
