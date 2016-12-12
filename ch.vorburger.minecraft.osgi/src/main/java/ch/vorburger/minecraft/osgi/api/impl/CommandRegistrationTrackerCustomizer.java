package ch.vorburger.minecraft.osgi.api.impl;

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.plugin.PluginContainer;

// TODO replace this with OSGi DS equivalent (or abstract it so that it can be used for other things than Command)
public class CommandRegistrationTrackerCustomizer
    implements ServiceTrackerCustomizer<CommandRegistration, CommandRegistration> {

    private static final Logger LOG = LoggerFactory.getLogger(CommandRegistrationTrackerCustomizer.class);

    public static ServiceTracker<CommandRegistration, CommandRegistration> setUp(BundleContext context, PluginContainer pluginContainer) {
        CommandRegistrationTrackerCustomizer customizer = new CommandRegistrationTrackerCustomizer(context, pluginContainer);
        ServiceTracker<CommandRegistration, CommandRegistration> serviceTracker
            = new ServiceTracker<>(context, CommandRegistration.class , customizer);
        serviceTracker.open();
        return serviceTracker;
    }

    protected final BundleContext context;
    protected final PluginContainer pluginContainer;
    protected final ConcurrentMap<ServiceReference<CommandRegistration>, CommandMapping> commandMappings = new ConcurrentHashMap<>();

    protected CommandRegistrationTrackerCustomizer(BundleContext context, PluginContainer pluginContainer) {
        this.context = context;
        this.pluginContainer = pluginContainer;
    }

    @Override
    public CommandRegistration addingService(ServiceReference<CommandRegistration> reference) {
        CommandRegistration commandRegistration = context.getService(reference);
        Optional<CommandMapping> optionalCommandMapping; // = Optional.empty();
//        if (pluginContainer != null) { // in integration tests this is null
            optionalCommandMapping = Sponge.getCommandManager()
                    .register(pluginContainer, commandRegistration.callable(), commandRegistration.aliases());
//        }
        optionalCommandMapping.ifPresent(commandMapping -> commandMappings.put(reference, commandMapping));
        if (!optionalCommandMapping.isPresent()) {
            LOG.warn("Command register failed: {}", commandRegistration);
        } else {
            LOG.info("Command registered: {}", commandRegistration);
        }
        return commandRegistration;
    }

    @Override
    public void modifiedService(ServiceReference<CommandRegistration> reference, CommandRegistration service) {
        removedService(reference, service);
        addingService(reference);
    }

    @Override
    public void removedService(ServiceReference<CommandRegistration> reference, CommandRegistration service) {
        commandMappings.compute(reference, (k, v) -> { Sponge.getCommandManager().removeMapping(v); return null; });
    }

}
