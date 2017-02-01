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

import java.io.Serializable;
import java.lang.reflect.Method;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ThrowsMethodExceptionAnswer implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -7316574192253912318L;

    /**
     * Use {@link MoreAnswers} to obtain an instance.
     */
    ThrowsMethodExceptionAnswer() {
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        Method method = invocation.getMethod();
        String msg = method.toGenericString() + " is not stubbed";
        throw new UnsupportedOperationException(msg);
    }

}
