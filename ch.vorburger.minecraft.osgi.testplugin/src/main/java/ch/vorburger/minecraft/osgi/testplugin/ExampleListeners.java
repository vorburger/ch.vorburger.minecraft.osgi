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
package ch.vorburger.minecraft.osgi.testplugin;

import ch.vorburger.minecraft.osgi.api.Listeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

// TODO @Service OSGi DS annotation
public class ExampleListeners implements Listeners {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleListeners.class);

    @Listener
    public void onUserJoin(ClientConnectionEvent.Login loginEvent) {
        LOG.info("onUserJoin()");
        // NOTE there is also a Join instead of Login event type..
        Text text = Text.of("WELCOME");
        loginEvent.setMessage(text);
        // User user = loginEvent.getTargetUser();
    }

}
