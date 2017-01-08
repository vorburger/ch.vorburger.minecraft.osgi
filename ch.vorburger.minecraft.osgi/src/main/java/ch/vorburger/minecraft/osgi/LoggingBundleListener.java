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
