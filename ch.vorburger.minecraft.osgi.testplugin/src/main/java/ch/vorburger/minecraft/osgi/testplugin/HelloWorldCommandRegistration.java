package ch.vorburger.minecraft.osgi.testplugin;

import ch.vorburger.minecraft.osgi.api.CommandRegistrationImpl;

// TODO @Service OSGi DS annotation
public class HelloWorldCommandRegistration extends CommandRegistrationImpl {

    public HelloWorldCommandRegistration() {
        super(new CommandsSetUp().helloCommandSpec(), "helloworld", "hello", "test");
    }

}
