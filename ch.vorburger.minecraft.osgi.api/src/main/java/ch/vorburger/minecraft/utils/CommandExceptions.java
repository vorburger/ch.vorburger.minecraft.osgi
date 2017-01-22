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

    /**
     * Invoke 'callable' and return its value,
     * or rethrow any Exception from it wrapped in a CommandException,
     * with description.
     *
     * @param description a humand-readable description of the Callable (used in the CommandException)
     * @param callable the code to invoke
     * @return the value returned by the callable
     * @throws CommandException in case the callable failed with an Exception
     */
    public static <T> T getOrThrow(String description, Callable<T> callable) throws CommandException {
        try {
            return callable.call();
        } catch (Exception cause) {
            // TODO see isDeveloper() idea in Texts.fromThrowable
            throw new CommandException(Texts.fromThrowable(description, cause), cause, true);
        }
    }

    public static CommandException create(String message) {
        return new CommandException(Texts.inRed(message));
    }

}
