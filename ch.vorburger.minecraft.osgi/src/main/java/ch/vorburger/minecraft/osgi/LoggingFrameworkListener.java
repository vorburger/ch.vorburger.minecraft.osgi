/**
 * ch.vorburger.minecraft.osgi
 *
 * Copyright (C) 2016 - 2017 Michael Vorburger.ch <mike@vorburger.ch>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
            LOG.error("FrameworkEvent {} : {}", getFrameworkEventTypeText(event.getType()),
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
