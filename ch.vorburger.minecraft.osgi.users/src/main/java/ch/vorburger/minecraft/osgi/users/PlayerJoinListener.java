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
package ch.vorburger.minecraft.osgi.users;

import ch.vorburger.osgi.gradle.SourceInstallService;
import java.io.File;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PlayerJoinListener implements EventListener<ClientConnectionEvent.Join> {

    private final SourceInstallService sourceInstallService;

    public PlayerJoinListener(SourceInstallService sourceInstallService) {
        this.sourceInstallService = sourceInstallService;
    }

    @Override
    public void handle(Join joinEvent) throws Exception {
        Player player = joinEvent.getTargetEntity();
        String name = player.getName();

        setUpDeveloper(player);

        player.sendMessage(Text.builder("HELO, welcome...").color(TextColors.GOLD).append(Text.of(name)).build());
        // player.sendTitle(title);
    }

    private void setUpDeveloper(Player player) {
        File devRoot = new File("dev");
        String uuid = player.getUniqueId().toString();
        File userProjects = new File(devRoot, uuid);
        File userProject1 = new File(userProjects, "project1");
        if (!userProject1.exists()) {
            userProject1.mkdirs();
            // TODO copy default project like testplugin into userProject1! Use Xtend template!
        }
        sourceInstallService.installSourceBundle(userProject1);
    }

}
