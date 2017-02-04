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
package ch.vorburger.minecraft.news.tests;

import static com.google.common.truth.Truth.assertThat;

import ch.vorburger.minecraft.news.ImmutableNews;
import ch.vorburger.minecraft.news.News;
import ch.vorburger.minecraft.news.NewsRepository;
import ch.vorburger.minecraft.news.internal.FileNewsRepository;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.mockito.Mockito;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextParseException;
import org.spongepowered.api.text.serializer.TextSerializer;

/**
 * Unit Test for {@link ch.vorburger.minecraft.news.internal.FileNewsRepository}
 *
 * @author Michael Vorburger
 */
public class NewsRepositoryTest {

    private static final File FILE = new File("build/news1.txt");

    private final User mockUser = mockUser();

    @Test
    public final void testNewsRepository() throws IOException {
        FILE.delete();
        NewsRepository newsRepository = fileNewsRepository();
        News news = ImmutableNews.builder()
                .author(mockUser)
                .createdOn(Instant.now())
                .message(Text.of("There's news!")).build();
        newsRepository.addNews(news);

        Iterable<News> newsList = newsRepository.getAllNews();
        assertThat(newsList.iterator().next().message().toString()).contains("There's news!");

        NewsRepository freshNewsRepository = fileNewsRepository();
        Iterable<News> freshNewsList = freshNewsRepository.getAllNews();
        assertThat(freshNewsList).hasSize(1);
        assertThat(newsList.iterator().next().message().toString()).contains("There's news!");
    }

    private FileNewsRepository fileNewsRepository() throws IOException {
        // TODO move somewhere else
        Server mockServer = Mockito.mock(Server.class /* TODO , MoreAnswers.exception() */);
        UserStorageService mockUserStorageService = Mockito.mock(UserStorageService.class /* TODO , MoreAnswers.exception() */);
        Mockito.when(mockUserStorageService.get(Mockito.any(UUID.class))).thenReturn(Optional.of(mockUser));
        return new FileNewsRepository(FILE, newTestTextSerializer(), mockServer, mockUserStorageService);
    }

    // TODO move somewhere else
    private User mockUser() {
        UUID uuid = UUID.randomUUID();
        User mockedUser = Mockito.mock(User.class /* TODO , MoreAnswers.exception() */);
        Mockito.when(mockedUser.getUniqueId()).thenReturn(uuid);
        return mockedUser;
    }

    private TextSerializer newTestTextSerializer() {
        return new TextSerializer() {

            @Override
            public String serialize(Text text) {
                return text.toString();
            }

            @Override
            public Text deserialize(String input) throws TextParseException {
                return Text.of(input);
            }
        };
    }

}
