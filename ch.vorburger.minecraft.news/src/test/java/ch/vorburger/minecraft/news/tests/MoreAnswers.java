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

import org.mockito.AdditionalAnswers;
import org.mockito.Answers;
import org.mockito.stubbing.Answer;

/**
 * More {@link Answer} variants.
 *
 * @see Answers
 * @see AdditionalAnswers
 *
 * @author Michael Vorburger
 */
@SuppressWarnings("unchecked")
public final class MoreAnswers {

    private static final ThrowsMethodExceptionAnswer EXCEPTION
        = new ThrowsMethodExceptionAnswer();

    private MoreAnswers() {
    }

    /**
     * Returns Mockito (default) Answer which throws an UnstubbedMethodException.
     *
     * @see ThrowsMethodExceptionAnswer
     */
    public static <T> Answer<T> exception() {
        return (Answer<T>) EXCEPTION;
    }

}
