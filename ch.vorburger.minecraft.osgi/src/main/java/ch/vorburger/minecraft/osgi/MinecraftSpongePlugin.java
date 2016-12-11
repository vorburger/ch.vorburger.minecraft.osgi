package ch.vorburger.minecraft.osgi;

import com.google.inject.Inject;
import java.io.File;
import org.osgi.framework.BundleException;
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
@Plugin(id = "ch_vorburger_minecraft_osgi", name = "Vorburger.ch's OSGi-based HOT (re)load", version = "4.0.0-SNAPSHOT",
    description = "Loads and reloads other plugins on changes; useful for development.",
    // TODO " Currently requires patched Sponge (with support for HotPluginManager)",
    authors = "Michael Vorburger.ch")
public class MinecraftSpongePlugin {

    @Inject private Logger logger;
    // @Inject @DefaultConfig(sharedRoot = true) private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    // @Inject private Game game;

    private OSGiFrameworkWrapper osgiFramework;

    @Listener
    // public void onPreInit(GamePreInitializationEvent event) throws BundleException {
    public void onGameStartingServerEvent(GameStartingServerEvent event) throws BundleException {
        logger.info("onGameStartingServerEvent()");
        File frameworkStorageDirectory = new File("osgi");
        osgiFramework = new OSGiFrameworkWrapper(frameworkStorageDirectory);
        osgiFramework.start();
    }

    @Listener
    public void disable(GameStoppingServerEvent event) throws InterruptedException, BundleException {
        osgiFramework.stop();
    }

}
