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
package ch.vorburger.minecraft.osgi;

import ch.vorburger.minecraft.osgi.api.AbstractPlugin;
import ch.vorburger.osgi.utils.BundleInstaller;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * Sponge powered Minecraft plugin which sets up an
 * OSGi Framework container to then HOT (re)load other plugins into.
 *
 * @author Michael Vorburger
 */
@Plugin(id = "osgi", name = "Vorburger.ch's OSGi-based HOT (re)load", version = "4.0.0-SNAPSHOT",
    description = "Loads and reloads other Minecraft plugins (as OSGi bundles)",
    url = "https://github.com/vorburger/ch.vorburger.minecraft.osgi",
    authors = "Michael Vorburger.ch")
public class MinecraftSpongePlugin extends AbstractPlugin {

    @Inject private Logger logger;
    // @Inject @DefaultConfig(sharedRoot = true) private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    // @Inject private Game game;

    private BundleInstaller osgiFramework;

    @Listener
    // public void onPreInit(GamePreInitializationEvent event) throws BundleException {
    public void onGameStartingServerEvent(GameStartingServerEvent event) throws Exception {
        logger.info("onGameStartingServerEvent()");
        osgiFramework = Bootstrap.bootstrapMinecraftOSGi("osgi", this);
    }

    @Listener
    public void disable(GameStoppingServerEvent event) throws Exception {
        if (osgiFramework != null) {
            osgiFramework.close();
        }
    }

}
