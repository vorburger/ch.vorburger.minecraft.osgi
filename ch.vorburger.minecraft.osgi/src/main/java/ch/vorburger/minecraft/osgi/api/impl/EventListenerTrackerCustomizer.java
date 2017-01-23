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

import java.util.Optional;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.plugin.PluginContainer;

// TODO replace this with OSGi DS equivalent
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EventListenerTrackerCustomizer
        // extends AbstractServiceTrackerCustomizer<EventListener<? extends Event>, EventListener<? extends Event>> {
        extends AbstractServiceTrackerCustomizer<EventListener, EventListener> {

    private static final Logger LOG = LoggerFactory.getLogger(EventListenerTrackerCustomizer.class);

    public EventListenerTrackerCustomizer(BundleContext context, PluginContainer pluginContainer) {
        super(context, pluginContainer);
    }

    @Override
    // protected Optional<EventListener<? extends Event>> getRegistration(EventListener<? extends Event> service) {
    protected Optional<EventListener> getRegistration(EventListener service) {
        Class eventClass = EventListenerUtil.getEventClass(service);
        Sponge.getEventManager().registerListener(pluginContainer, eventClass, service);
        LOG.info("EventListener registered for event class {} : {}", eventClass.getName(), service);
        return Optional.of(service);
    }

    @Override
    // protected void removeRegistration(EventListener<? extends Event> registration) {
    protected void removeRegistration(EventListener registration) {
        Sponge.getEventManager().unregisterListeners(registration);
    }

}
