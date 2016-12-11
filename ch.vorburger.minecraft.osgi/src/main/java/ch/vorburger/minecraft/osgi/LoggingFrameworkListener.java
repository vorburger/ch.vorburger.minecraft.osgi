package ch.vorburger.minecraft.osgi;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFrameworkListener implements FrameworkListener {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingFrameworkListener.class);

    @Override
    public void frameworkEvent(FrameworkEvent event) {
        log(event);
    }

    public void log(FrameworkEvent event) {
        // if (event.getType() == FrameworkEvent.ERROR)
        if (event.getThrowable() == null) {
            LOG.info("FrameworkEvent {} : {}", getFrameworkEventTypeText(event.getType()),
                    event.getBundle().getSymbolicName());
        } else {
            LOG.info("FrameworkEvent {} : {}", getFrameworkEventTypeText(event.getType()),
                    event.getBundle().getSymbolicName(), event.getThrowable());
        }
    }

    private String getFrameworkEventTypeText(int type) {
        switch (type) {
            case FrameworkEvent.STARTED:
                return "Started";
            case FrameworkEvent.ERROR:
                return "Error";
            case FrameworkEvent.WARNING:
                return "Warning";
            case FrameworkEvent.INFO:
                return "Info";
            case FrameworkEvent.PACKAGES_REFRESHED:
                return "Packages Refreshed";
            case FrameworkEvent.STARTLEVEL_CHANGED:
                return "Start Level Changed";
            case FrameworkEvent.STOPPED:
                return "Stopped";
            case FrameworkEvent.STOPPED_BOOTCLASSPATH_MODIFIED:
                return "Stopped Bootclasspath Modified";
            case FrameworkEvent.STOPPED_UPDATE:
                return "Stopped Update";
            case FrameworkEvent.WAIT_TIMEDOUT:
                return "Wait Timeout";
            default:
                return type + " ???";
        }
    }
}
