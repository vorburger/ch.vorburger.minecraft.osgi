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

import ch.vorburger.minecraft.news.News;
import ch.vorburger.minecraft.news.NewsRepository;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.spongepowered.api.text.serializer.TextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;

public class FileNewsRepository implements NewsRepository {

    private static final Charset CHARSET = Charsets.UTF_8;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
    private static final TextSerializer jsonTextSerializer = TextSerializers.JSON;

    private final File file;

    private final List<News> allNews = new CopyOnWriteArrayList<>();

    public FileNewsRepository(File file) {
        super();
        this.file = file;
    }

    @Override
    public synchronized void addNews(News news) throws IOException {
        StringBuilder sb = new StringBuilder();
        dateTimeFormatter.formatTo(news.created(), sb);
        sb.append(' ');
        sb.append(news.byUser().getUniqueId().toString());
        sb.append(' ');
        sb.append(jsonTextSerializer.serialize(news.message()));
        Files.append(sb.toString(), file, CHARSET);
        allNews.add(0, news);
    }

    @Override
    public synchronized List<News> getNews(int since) {
        return Collections.unmodifiableList(allNews.subList(0, since + 1));
    }
}
