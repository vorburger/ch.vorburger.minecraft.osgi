package ch.vorburger.minecraft.osgi.testplugin;

import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandsSetUp {

/*
    TODO Build a general adapter from HelloWorldCommandRegistration to do something like this,
    so that this Plugin COULD also be used as a non-OSGi standard Sponge plugin?

    Optional<CommandMapping> register() {
        return Sponge.getCommandManager()
            .register(null, helloCommandSpec(), "helloworld", "hello", "test");
    }
*/
    CommandSpec helloCommandSpec() {
        return CommandSpec.builder()
                .description(Text.of("Hello World Command"))
                .executor(new HelloWorldCommand())
                .build();
    }
}
