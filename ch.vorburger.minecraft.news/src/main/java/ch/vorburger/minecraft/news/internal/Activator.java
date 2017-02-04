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
package ch.vorburger.minecraft.news.internal;

import ch.vorburger.minecraft.news.NewsRepository;
import ch.vorburger.minecraft.news.NewsService;
import ch.vorburger.minecraft.news.commands.NewsCommand;
import ch.vorburger.minecraft.news.listeners.PlayerJoinListener;
import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import java.io.File;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        // TODO make directory of File("news.txt") configurable
        NewsRepository newsRepository = new FileNewsRepository(new File("news.txt"),
                TextSerializers.JSON, Sponge.getServer(),
                Sponge.getServiceManager().provideUnchecked(UserStorageService.class));
        NewsService newsService = new NewsServiceImpl(newsRepository);
        context.registerService(EventListener.class, new PlayerJoinListener(), null);
        context.registerService(CommandRegistration.class, new NewsCommand(newsService), null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

}
