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
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextParseException;
import org.spongepowered.api.text.serializer.TextSerializer;

/**
 * Implementation of NewsRepository which uses a plain text flat file for persistence.
 *
 * @author Michael Vorburger
 */
public class FileNewsRepository implements NewsRepository {

    private static final Charset CHARSET = Charsets.UTF_8;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;

    private final File file;
    private final Server server;
    private final TextSerializer textSerializer;
    private final UserStorageService userStorageService;

    private final List<News> allNews;

    public FileNewsRepository(File file, TextSerializer textSerializer, Server server, UserStorageService userStorageService) throws IOException {
        super();
        this.file = file;
        this.server = server;
        this.textSerializer = textSerializer;
        this.userStorageService = userStorageService;
        this.allNews = load();
    }

    @Override
    public synchronized void addNews(News news) throws IOException {
        StringBuilder sb = new StringBuilder();
        dateTimeFormatter.formatTo(news.createdOn(), sb);
        sb.append(' ');
        sb.append(news.author().getUniqueId().toString());
        sb.append(' ');
        sb.append(textSerializer.serialize(news.message()));
        sb.append('\n');
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        Files.append(sb.toString(), file, CHARSET);
        allNews.add(0, news);
    }

    private List<News> load() throws IOException {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return Files.readLines(file, CHARSET, new LineProcessor<List<News>>() {

            List<News> loadedNews = new ArrayList<>();

            @Override
            public boolean processLine(String line) throws IOException {
                try {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        int indexOfSpace1 = line.indexOf(' ');
                        String createdAsString = line.substring(0, indexOfSpace1);
                        int indexOfSpace2 = line.indexOf(' ', indexOfSpace1 + 1);
                        String uuidAsString = line.substring(indexOfSpace1 + 1, indexOfSpace2);
                        String textAsString = line.substring(indexOfSpace2 + 1);

                        Instant createdOn = dateTimeFormatter.parse(createdAsString, Instant::from);
                        UUID uuid = UUID.fromString(uuidAsString);
                        User byUser = getUser(uuid);
                        Text message = textSerializer.deserialize(textAsString);

                        loadedNews.add(ImmutableNews.builder().createdOn(createdOn).author(byUser).message(message).build());
                    }
                    return true;
                } catch (StringIndexOutOfBoundsException | DateTimeParseException | TextParseException | UserNotFoundException e) {
                    throw new IOException("Failed to read line of file " + file + ": " + line, e);
                }
            }

            @Override
            public List<News> getResult() {
                return loadedNews;
            }
        });
    }

    @Override
    public synchronized List<News> getNewsSince(/* TODO Instant since */) {
/*
        if (since < allNews.size()) {
            return Collections.unmodifiableList(allNews.subList(0, since + 1));
        } else {
            return Collections.emptyList();
        }
*/
        return Collections.unmodifiableList(allNews);
    }

    // TODO move this into a utils service
    // https://docs.spongepowered.org/master/en/plugin/offline-userplayer-data.html
    private User getUser(UUID playerUUID) throws UserNotFoundException {
        Optional<Player> optOnlineUser = server.getPlayer(playerUUID);
        if (optOnlineUser.isPresent()) {
            return optOnlineUser.get();
        }
        Optional<User> optOfflineUser = userStorageService.get(playerUUID);
        if (optOfflineUser.isPresent()) {
            return optOfflineUser.get();
        }
        throw new UserNotFoundException("Player with this UUID is not currently nor previously online: " + playerUUID);
    }
}
