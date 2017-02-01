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

import ch.vorburger.minecraft.news.ImmutableNews;
import ch.vorburger.minecraft.news.News;
import ch.vorburger.minecraft.news.NewsRepository;
import ch.vorburger.minecraft.news.NewsService;
import ch.vorburger.minecraft.utils.CommandExceptions;
import java.time.Instant;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRegistry;

    public NewsServiceImpl(NewsRepository newsRegistry) {
        super();
        this.newsRegistry = newsRegistry;
    }

    @Override
    public void addNews(User user, String text) throws CommandException {
        News news = ImmutableNews.builder().byUser(user).created(Instant.now()).message(Text.of(text)).build();
        CommandExceptions.doOrThrow("NewsRepository.addNews()", () -> newsRegistry.addNews(news));
    }

}
