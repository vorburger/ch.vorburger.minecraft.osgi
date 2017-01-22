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
package ch.vorburger.minecraft.utils;

import com.google.common.util.concurrent.FutureCallback;
import org.spongepowered.api.text.channel.MessageReceiver;

/**
 * FutureCallback which reports failures to a {@link MessageReceiver}.
 *
 * @author Michael Vorburger.ch
 */
public abstract class MessageReceivingFutureCallback<V> implements FutureCallback<V> {

    private final MessageReceiver messageReceiver;

    public MessageReceivingFutureCallback(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    protected abstract void onSuccessWithException(V value) throws Exception;

    @Override
    public final void onSuccess(V value) {
        try {
            onSuccessWithException(value);
        } catch (Exception e) {
            onFailure(e);
        }
    }

    @Override
    public final void onFailure(Throwable throwable) {
        // FIRST log, then sendMessage() - just in case sendMessage also fails for any reason:
        // TODO see during usage if this log is even needed, probably not, as it would likely duplicate:
        // log.error("Build or Install failed", throwable);
        MessageReceivers.sendException(messageReceiver, "Failed: " + throwable.getMessage(), throwable);
    }

}
