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
package ch.vorburger.minecraft.news;

import java.io.IOException;

/**
 * Repository of You've got... News.
 *
 * @author Michael Vorburger
 */
public interface NewsRepository {

    void addNews(News news) throws IOException;

    Iterable<News> getNewsSince(/* TODO Instant since */);

    default Iterable<News> getAllNews() {
        return getNewsSince(/* new Instant of the dawn of time */);
    }

}
