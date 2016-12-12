package ch.vorburger.minecraft.osgi.api.impl;

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * Like Activator, but not an Activator, because this doesn't run in a real
 * bundle but as part of the root OSGi Framework.
 *
 * @author Michael Vorburger
 */
public class ApiImplBootstrap {

    private ServiceTracker<CommandRegistration, CommandRegistration> commandRegistrationTracker;

    public void start(BundleContext bundleContext, PluginContainer pluginContainer) {
        commandRegistrationTracker = CommandRegistrationTrackerCustomizer.setUp(bundleContext, pluginContainer);
    }

    public void stop() {
        commandRegistrationTracker.close();
    }

}
