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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * Abstract base class utility for implementing OSGi ServiceTrackerCustomizer.
 *
 * TODO Remove when switching over to OSGi DS with SCR.
 *
 * @author Michael Vorburger
 */
public abstract class AbstractServiceTrackerCustomizer<T,R> implements ServiceTrackerCustomizer<T,T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final BundleContext context;
    protected final PluginContainer pluginContainer;
    private final ConcurrentMap<ServiceReference<T>, R> serviceMappings = new ConcurrentHashMap<>();

    public static <T,M> ServiceTracker<T, T> setUp(BundleContext context, Class<T> clazz, AbstractServiceTrackerCustomizer<T, M> customizer) {
        ServiceTracker<T, T> serviceTracker = new ServiceTracker<>(context, clazz, customizer);
        serviceTracker.open();
        return serviceTracker;
    }

    public AbstractServiceTrackerCustomizer(BundleContext context, PluginContainer pluginContainer) {
        this.context = context;
        this.pluginContainer = pluginContainer;
    }

    @Override
    public final T addingService(ServiceReference<T> reference) {
        T service = context.getService(reference);
        // in integration tests pluginContainer is null
        if (pluginContainer != null) {
            Optional<R> optionalRegistration = getRegistration(service);
            optionalRegistration.ifPresent(registration -> {
                serviceMappings.put(reference, registration);
                log.info("Registered: {}", service);
            });
            if (!optionalRegistration.isPresent()) {
                log.warn("Registeration failed: {}", service);
            }
        }
        return service;
    }

    @Override
    public final void modifiedService(ServiceReference<T> reference, T service) {
        removedService(reference, service);
        addingService(reference);
    }

    @Override
    public final void removedService(ServiceReference<T> reference, T service) {
        // in integration tests pluginContainer is null
        if (pluginContainer != null) {
            serviceMappings.compute(reference, (k, v) -> { removeRegistration(v); return null; });
        }
    }

    abstract protected Optional<R> getRegistration(T service);

    abstract protected void removeRegistration(R registration);
}
