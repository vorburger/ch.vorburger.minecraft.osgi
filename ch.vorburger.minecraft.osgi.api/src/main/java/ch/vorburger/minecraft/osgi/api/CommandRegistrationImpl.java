package ch.vorburger.minecraft.osgi.api;

import java.util.Arrays;
import java.util.List;
import org.spongepowered.api.command.CommandCallable;

public class CommandRegistrationImpl implements CommandRegistration {

    private final CommandCallable callable;
    private final List<String> aliases;

    protected CommandRegistrationImpl(CommandCallable callable, List<String> aliases) {
        this.callable = callable;
        this.aliases = aliases;
    }

    protected CommandRegistrationImpl(CommandCallable callable, String... alias) {
        this(callable, Arrays.asList(alias));
    }

    public CommandCallable callable() {
        return callable;
    }

    public List<String> aliases() {
        return aliases;
    }

    @Override
    public String toString() {
        return "CommandRegistration[aliases=" + aliases + "; callable=" + callable.toString() + "]";
            // TODO use callable getHelp() or getUsage(), but what to pass as CommandSource?
    }
}
