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
package ch.vorburger.minecraft.news.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

public class PlayerJoinListener implements EventListener<ClientConnectionEvent.Join> {

    @Override
    public void handle(Join joinEvent) throws Exception {
        Player player = joinEvent.getTargetEntity();
        String name = player.getName();

        player.sendTitle(
                Title.builder().fadeIn(60).stay(500).fadeOut(100)
                .title(Text.of(TextColors.WHITE, name).concat(Text.of(TextColors.GOLD, ", you've got NEWS!")))
                // TODO Make this a clickable action link.. but does that even work in titles?
                .subtitle(Text.of("Use /news to see what's changed on this server since you were last here..."))
                .build()
            );
        // player.sendMessage(Text.builder("HELO, welcome...").color(TextColors.GOLD).append(Text.of(name)).build());
    }

}
