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

import static org.junit.Assert.assertEquals;

import ch.vorburger.minecraft.news.News;
import ch.vorburger.minecraft.news.NewsRepository;
import ch.vorburger.minecraft.news.NewsService;
import ch.vorburger.minecraft.news.internal.FileNewsRepository;
import ch.vorburger.minecraft.news.internal.NewsServiceImpl;
import java.io.File;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.lwts.runner.LaunchWrapperTestRunner;

// TODO change LaunchWrapperTestRunner so that instead of systemProperty 'lwts.tweaker' it can read an annotation
@RunWith(LaunchWrapperTestRunner.class)
public class NewsServiceTest {

    private static final File FILE = new File("build/news1.txt");

    @Test
    public final void testNewsService() throws CommandException {
        // TODO move somewhere else?
        // RegistryHelper.setFinalStatic(TextSerializers.class, "PLAIN", new PlainTextSerializer());

        NewsRepository newsRepository = new FileNewsRepository(FILE);
        NewsService newsService = new NewsServiceImpl(newsRepository);
        newsService.addNews(mockUser(), "There's news!");

        List<News> newsList = newsRepository.getNews(0);
        assertEquals("Text{There's news!}", newsList.get(0).message().toString());

        NewsRepository freshNewsRepository = new FileNewsRepository(FILE);
        freshNewsRepository.getNews(0);
        assertEquals("Text{There's news!}", newsList.get(0).message().toString());
    }

    // TODO move somewhere else?
    private User mockUser() {
        User mockedUser = Mockito.mock(User.class, MoreAnswers.exception());
        return mockedUser;
    }
}
