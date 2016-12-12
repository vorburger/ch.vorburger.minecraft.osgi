package ch.vorburger.minecraft.osgi.testplugin;

import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        // System.out.println("STDOUT started!");
        LOG.info("starting and registering command..");
        try {
            // new CommandsSetUp().register();
            // TODO remove this when switching to annotation-based declarative services..
            context.registerService(CommandRegistration.class, new HelloWorldCommandRegistration(), null);
        } catch (Throwable t) {
            // we MUST catch and log, because Felix itself does not, and this gets lost..
            LOG.error("boum", t);
            // we rethrow, to fail the Bundle start
            throw t;
        }
        LOG.info("started!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        try {
            // System.out.println("STDOUT stopped!");
            // NB: It's not required to release, this is automatic:
            // context.ungetService(helloWorldCommandRegistrationServiceReference.getReference());
            // helloWorldCommandRegistrationServiceReference.unregister();
            LOG.info("stopped!");
        } catch (Throwable t) {
            LOG.error("boum", t);
            throw t;
        }
    }

/*
    private LogService getLogService() {
        ServiceReference ref = context.getServiceReference(LogService.class.getName());
        if (ref != null) {
            LogService log = (LogService) context.getService(ref);
            return log;

        }
        return null;
    }
*/
}
