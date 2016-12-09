package ch.vorburger.minecraft.osgi;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingBundleListener implements BundleListener {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingBundleListener.class);

    @Override
    public void bundleChanged(BundleEvent event) {
        LOG.info("BundleEvent type: {}, bundle: {}, origin: {}", event.getType(), event.getBundle().getSymbolicName(), event.getOrigin().getSymbolicName());
    }

}
