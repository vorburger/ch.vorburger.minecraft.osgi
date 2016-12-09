package ch.vorburger.minecraft.osgi;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingServiceListener implements ServiceListener {

    private static final Logger LOG = LoggerFactory.getLogger(OSGiFrameworkWrapper.class);

    @Override
    public void serviceChanged(ServiceEvent event) {
        LOG.info("ServiceEvent type: {}, service: {}", event.getType(), event.getServiceReference());
        // TODO Use my code from ODL Gerrit to String-ify ServiceReference
    }

}
