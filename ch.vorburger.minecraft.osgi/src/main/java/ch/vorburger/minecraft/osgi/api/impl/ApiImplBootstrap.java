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

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import ch.vorburger.minecraft.osgi.api.Listeners;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * Like Activator, but not an Activator, because this doesn't run in a real
 * bundle but as part of the root OSGi Framework.
 *
 * @author Michael Vorburger
 */
public class ApiImplBootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(ApiImplBootstrap.class);

    private ServiceTracker<Listeners, Listeners> listenersTracker;
    @SuppressWarnings("rawtypes")
    private ServiceTracker<EventListener, EventListener> eventListenerTracker;
    private ServiceTracker<CommandRegistration, CommandRegistration> commandRegistrationTracker;

    public void start(BundleContext bundleContext, PluginContainer pluginContainer) {
        listenersTracker = CommandRegistrationTrackerCustomizer.setUp(bundleContext, Listeners.class,
                new ListenersTrackerCustomizer(bundleContext, pluginContainer));
        eventListenerTracker = CommandRegistrationTrackerCustomizer.setUp(bundleContext, EventListener.class,
                new EventListenerTrackerCustomizer(bundleContext, pluginContainer));
        commandRegistrationTracker = CommandRegistrationTrackerCustomizer.setUp(bundleContext, CommandRegistration.class,
                new CommandRegistrationTrackerCustomizer(bundleContext, pluginContainer));
        LOG.info("started tracking Minecraft services for CommandRegistration, EventListener & Listeners");
    }

    public void stop() {
        listenersTracker.close();
        eventListenerTracker.close();
        commandRegistrationTracker.close();
    }

}
