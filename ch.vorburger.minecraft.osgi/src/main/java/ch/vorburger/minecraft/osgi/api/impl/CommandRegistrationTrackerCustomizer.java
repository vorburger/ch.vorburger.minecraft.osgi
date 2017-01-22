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
import java.util.Optional;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.plugin.PluginContainer;

// TODO replace this with OSGi DS equivalent
public class CommandRegistrationTrackerCustomizer extends AbstractServiceTrackerCustomizer<CommandRegistration, CommandMapping> {

    private static final Logger LOG = LoggerFactory.getLogger(CommandRegistrationTrackerCustomizer.class);

    protected CommandRegistrationTrackerCustomizer(BundleContext context, PluginContainer pluginContainer) {
        super(context, pluginContainer);
    }

    @Override
    protected Optional<CommandMapping> getRegistration(CommandRegistration service) {
        if (service.callable() == null) {
            LOG.error("callable() null; skipping CommandRegistration: {}", service);
            return Optional.empty();
        }
        if (service.aliases() == null) {
            LOG.error("aliases() null; skipping CommandRegistration: {}", service);
            return Optional.empty();
        }
        return Sponge.getCommandManager().register(pluginContainer, service.callable(), service.aliases());
    }

    @Override
    protected void removeRegistration(CommandMapping r) {
         Sponge.getCommandManager().removeMapping(r);
    }

}
