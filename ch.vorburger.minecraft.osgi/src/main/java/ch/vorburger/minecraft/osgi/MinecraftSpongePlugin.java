package ch.vorburger.minecraft.osgi;

import com.google.inject.Inject;
import java.io.File;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * Sponge powered Minecraft plugin which sets up an
 * OSGi Framework container to then HOT (re)load other plugins into.
 *
 * @author Michael Vorburger
 */
@Plugin(id = "ch_vorburger_minecraft_osgi", name = "Vorburger.ch's OSGi Support", version = "1.0.0-SNAPSHOT")
public class MinecraftSpongePlugin {

    @Inject private Logger logger;
    // @Inject @DefaultConfig(sharedRoot = true) private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    // @Inject private Game game;

    private OSGiFrameworkWrapper wrapper;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) throws BundleException {
        logger.info("onPreInit()");
        File frameworkStorageDirectory = new File("osgi");
        wrapper = new OSGiFrameworkWrapper(frameworkStorageDirectory);
        wrapper.start();
        // wrapper.installBundles("file:../ch.vorburger.minecraft.osgi.testplugin/target/osgi.testplugin-1.0.0-SNAPSHOT.jar");
    }

    @Listener
    public void disable(GameStoppingServerEvent event) throws InterruptedException, BundleException {
        wrapper.stop();
    }

}
