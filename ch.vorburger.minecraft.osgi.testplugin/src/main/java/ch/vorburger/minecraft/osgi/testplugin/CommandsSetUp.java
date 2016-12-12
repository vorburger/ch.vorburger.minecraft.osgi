package ch.vorburger.minecraft.osgi.testplugin;

import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandsSetUp {

    Optional<CommandMapping> register() {
        return Sponge.getCommandManager()
            .register(null, helloCommandSpec(), "helloworld", "hello", "test");
    }

    CommandSpec helloCommandSpec() {
        return CommandSpec.builder()
                .description(Text.of("Hello World Command"))
                .executor(new HelloWorldCommand())
                .build();
    }
}
