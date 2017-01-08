package ch.vorburger.minecraft.osgi;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingServiceListener implements ServiceListener {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingServiceListener.class);

    @Override
    public void serviceChanged(ServiceEvent event) {
        LOG.info("ServiceEvent type: {}, service: {}",
                getServiceEventTypeText(event.getType()), event.getServiceReference());
    }

    private String getServiceEventTypeText(int type) {
        switch (type) {
        case ServiceEvent.REGISTERED:
            return "Registered";
        case ServiceEvent.MODIFIED:
            return "Modified";
        case ServiceEvent.MODIFIED_ENDMATCH:
            return "Modified End Match";
        case ServiceEvent.UNREGISTERING:
            return "Unregistering";
        default:
            return type + " ???";
        }
    }

}
