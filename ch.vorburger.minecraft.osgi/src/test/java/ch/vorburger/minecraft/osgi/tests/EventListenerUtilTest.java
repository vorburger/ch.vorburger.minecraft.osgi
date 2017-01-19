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
package ch.vorburger.minecraft.osgi.tests;

import static org.junit.Assert.*;

import ch.vorburger.minecraft.osgi.api.impl.EventListenerUtil;
import org.junit.Test;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent.Login;

/**
 * Unit test for EventListenerUtil.
 *
 * @author Michael Vorburger
 */
public class EventListenerUtilTest {

    @Test
    public void getEventClass() {
        assertEquals(Login.class, EventListenerUtil.getEventClass(new TestEventListener()));
    }

    public static class TestEventListener implements EventListener<Login> {
        @Override
        public void handle(Login event) throws Exception {
        }
    }
}
