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
import com.google.common.collect.Iterables;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColors;

public class NewsServiceImpl implements NewsService {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    private final NewsRepository newsRegistry;

    public NewsServiceImpl(NewsRepository newsRegistry) {
        super();
        this.newsRegistry = newsRegistry;
    }

    @Override
    public void addNews(User author, String plainText) throws CommandException {
        News news = ImmutableNews.builder().author(author).createdOn(Instant.now()).message(Text.of(plainText)).build();
        CommandExceptions.doOrThrow("NewsRepository.addNews()", () -> newsRegistry.addNews(news));
    }

    @Override
    public void sendAllNews(MessageReceiver msgReceiver) {
        Iterable<News> newsList = newsRegistry.getAllNews();
        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList.Builder paginationBuilder = paginationService.builder();
        paginationBuilder.title(Text.of("NEWS"));
        paginationBuilder.contents(Iterables.transform(newsList, news -> news2text(news)));
        paginationBuilder.sendTo(msgReceiver);
    }

    protected Text news2text(News news) {
        // TODO could use http://www.ocpsoft.org/prettytime/ for like "2 days ago" etc.
        // TODO DateTimeFormatter localizedDateTimeFormatter = dateTimeFormatter.withLocale(userLocale);
        String createdOnAsString = dateTimeFormatter.format(news.createdOn());
        String authorName = news.author().getName();

        return Text.of(TextColors.DARK_GRAY, createdOnAsString,
                 TextColors.DARK_GREEN, authorName,
                 news.message());
    }
}
