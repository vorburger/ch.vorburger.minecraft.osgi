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
package ch.vorburger.minecraft.osgi.api.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventListener;

/**
 * Utility for EventListener.
 *
 * @author Michael Vorburger
 */
public final class EventListenerUtil {

    private static final String EVENT_LISTENER_HANDLE_METHOD_NAME = /* EventListener::handle */ "handle";

    private EventListenerUtil() { }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends Event> Class<T> getEventClass(EventListener<? super T> listener) {
        Class<? extends EventListener> listenerClass = listener.getClass();
        // We can't just do this:
        // Method handleMethod = listenerClass.getMethod(EVENT_LISTENER_HANDLE_METHOD_NAME, Event.class);
        // so we do this:
        Method handleMethod = Arrays.asList(listenerClass.getMethods()).stream()
                .filter(m -> m.getName().equals(EVENT_LISTENER_HANDLE_METHOD_NAME)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Failed to obtain 'handle' Method from listener: " + listener));
        return (Class<T>) handleMethod.getParameterTypes()[0];
    }

}
