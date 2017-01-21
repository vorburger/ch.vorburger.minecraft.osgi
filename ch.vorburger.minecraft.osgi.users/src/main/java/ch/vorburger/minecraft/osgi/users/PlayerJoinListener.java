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

import ch.vorburger.minecraft.osgi.templates.ProjectWriter;
import ch.vorburger.minecraft.osgi.templates.SimpleProjectTemplate;
import ch.vorburger.osgi.gradle.SourceInstallService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.IOException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PlayerJoinListener implements EventListener<ClientConnectionEvent.Join> {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerJoinListener.class);

    private final SourceInstallService sourceInstallService;

    public PlayerJoinListener(SourceInstallService sourceInstallService) {
        this.sourceInstallService = sourceInstallService;
    }

    @Override
    public void handle(Join joinEvent) throws Exception {
        Player player = joinEvent.getTargetEntity();
        String name = player.getName();

        setUpDeveloper(player);
        // player.sendTitle(title);
        player.sendMessage(Text.builder("HELO, welcome...").color(TextColors.GOLD).append(Text.of(name)).build());
        // TODO show end-user the URL of the editor!
    }

    // TODO return CompletableFuture<URI> with editor URL for user to open!
    private void setUpDeveloper(Player player) throws IOException {
        File devRoot = new File("dev");
        String uuid = player.getUniqueId().toString();
        File userProjects = new File(devRoot, uuid);
        File userProject1 = new File(userProjects, "project1");
        if (!userProject1.exists()) {
            userProject1.mkdirs();
            new ProjectWriter().writeProject(new SimpleProjectTemplate(), userProject1);
        }
        ListenableFuture<Bundle> bundleFuture = sourceInstallService.installSourceBundle(userProject1);
        // TODO extract a lambda helper for this typical pattern...
        Futures.addCallback(bundleFuture, new FutureCallback<Bundle>() {

            @Override
            public void onSuccess(Bundle bundle) {
                try {
                    bundle.start();
                    player.sendMessage(Text.builder("Install successful").color(TextColors.GREEN).append().build());
                } catch (BundleException e) {
                    onFailure(e);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                // FIRST log, then sendMessage() - just in case sendMessage also fails
                LOG.error("Build or Install failed", throwable);
                player.sendMessage(Text.builder("Install failed:" + throwable.getMessage()).color(TextColors.RED).append().build());
            }
        } );
    }

}
