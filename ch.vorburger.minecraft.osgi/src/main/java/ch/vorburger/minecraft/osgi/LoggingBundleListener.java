package ch.vorburger.minecraft.osgi;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingBundleListener implements BundleListener {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingBundleListener.class);

    @Override
    public void bundleChanged(BundleEvent event) {
        LOG.info("BundleEvent type: {}, bundle: {}, origin: {}", getBundleEventTypeText(event.getType()),
                event.getBundle().getSymbolicName(), event.getOrigin().getSymbolicName());
    }

    private String getBundleEventTypeText(int type) {
        switch (type) {
            case BundleEvent.INSTALLED:
                return "Installed";
            case BundleEvent.RESOLVED:
                return "Resolved";
            case BundleEvent.LAZY_ACTIVATION:
                return "Lazy Activation";
            case BundleEvent.STARTING:
                return "Starting";
            case BundleEvent.STARTED:
                return "Started";
            case BundleEvent.STOPPING:
                return "Stopping";
            case BundleEvent.STOPPED:
                return "Stopped";
            case BundleEvent.UPDATED:
                return "Updated";
            case BundleEvent.UNRESOLVED:
                return "Unresolved";
            case BundleEvent.UNINSTALLED:
                return "Uninstalled";
            default:
                return type + " ???";
            }
    }
}
