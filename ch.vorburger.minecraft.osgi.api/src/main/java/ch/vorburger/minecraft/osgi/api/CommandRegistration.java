package ch.vorburger.minecraft.osgi.api;

import java.util.List;
import org.spongepowered.api.command.CommandCallable;

public interface CommandRegistration {

    CommandCallable callable();

    List<String> aliases();

}
