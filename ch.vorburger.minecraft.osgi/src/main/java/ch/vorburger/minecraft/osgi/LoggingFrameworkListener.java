package ch.vorburger.minecraft.osgi;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFrameworkListener implements FrameworkListener {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingFrameworkListener.class);

    // private static final Map<Integer, String> typeMap = ...

    @Override
    public void frameworkEvent(FrameworkEvent event) {
    	log(event);
    }

	public void log(FrameworkEvent event) {
        // if (event.getType() == FrameworkEvent.ERROR)
        if (event.getThrowable() == null) {
            LOG.info("FrameworkEvent {} : {}", event.getType(), event.getBundle().getSymbolicName());
        } else {
            LOG.info("FrameworkEvent {} : {}", event.getType(), event.getBundle().getSymbolicName(), event.getThrowable());
        }
	}

}
