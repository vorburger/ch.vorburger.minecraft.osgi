package ch.vorburger.minecraft.osgi.testplugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("STDOUT started!");
        LOG.info("LOG started!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("STDOUT  stopped!");
        LOG.info("LOG stopped!");
    }

}
