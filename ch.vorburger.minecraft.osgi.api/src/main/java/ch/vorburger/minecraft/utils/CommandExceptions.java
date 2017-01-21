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
package ch.vorburger.minecraft.utils;

import java.util.concurrent.Callable;
import org.spongepowered.api.command.CommandException;

/**
 * Utilities for {@link CommandException}.
 *
 * @author Michael Vorburger.ch
 */
public final class CommandExceptions {

    private CommandExceptions() { }

    // TODO find a better name for this method
    public static <T> T doAndWrap(String description, Callable<T> callable) throws CommandException {
        try {
            return callable.call();
        } catch (Exception cause) {
            throw new CommandException(Texts.fromThrowable(description, cause), cause, true);
        }
    }

}
