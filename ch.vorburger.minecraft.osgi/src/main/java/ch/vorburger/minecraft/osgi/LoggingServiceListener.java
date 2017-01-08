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
