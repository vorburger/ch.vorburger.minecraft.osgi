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

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;

/**
 * Utilities for {@link MessageReceiver}.
 *
 * @author Michael Vorburger.ch
 */
public final class MessageReceivers {

    private MessageReceivers() { }

    public static void sendException(MessageReceiver messageReceiver, String prefix, Throwable throwable) {
        messageReceiver.sendMessage(Texts.fromThrowable(prefix, throwable));
    }

    public static <V> void addCallback(CompletableFuture<V> future, MessageReceiver messageReceiver, SuccessCallback<V> callback) {
        future.whenComplete(new BiConsumer<V, Throwable>() {
            // similar to MessageReceivingFutureCallback
            @Override
            public void accept(V value, Throwable throwable) {
                if (throwable != null) {
                    onFailure(throwable);
                } else {
                    try {
                        callback.onSuccess(value);
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }
            }

            private void onFailure(Throwable throwable) {
                MessageReceivers.sendException(messageReceiver, "Failed: " + throwable.getMessage(), throwable);
            }
        });
    }

    public static <V> void addCallback(ListenableFuture<V> future, MessageReceiver messageReceiver, SuccessCallback<V> callback) {
        Futures.addCallback(future, new MessageReceivingFutureCallback<V>(messageReceiver) {
            @Override
            protected void onSuccessWithException(V value) throws Exception {
                callback.onSuccess(value);
            }
        });
    }

    @FunctionalInterface
    public static interface SuccessCallback<V> {
        void onSuccess(V value) throws Exception;
    }

    /**
     * Obtain a {@link CommandSource} from a {@link Subject}.
     * Either the Subject's real CommandSource, or a fake CommandSource implementation which just logs.
     * Useful instead of using null MessageReceiver.
     */
    public static MessageReceiver from(Subject subject) {
        Optional<CommandSource> optionalCommandSource = subject.getCommandSource();
        if (optionalCommandSource.isPresent()) {
            return optionalCommandSource.get();
        } else {
            return new LoggingMessageReceiver(subject);
        }
    }

    private static final class LoggingMessageReceiver implements MessageReceiver {

        private static final Logger LOG = LoggerFactory.getLogger(MessageReceivers.class);

        private final Object somethingThatHadNoCommandSource;
        private MessageChannel messageChannel;

        private LoggingMessageReceiver(Object somethingThatHadNoCommandSource) {
            this.somethingThatHadNoCommandSource = somethingThatHadNoCommandSource;
        }

        @Override
        public void sendMessage(Text message) {
            if (messageChannel != null) {
                messageChannel.send(somethingThatHadNoCommandSource, message);
            }
            LOG.info(message.toPlain());
        }

        @Override
        public MessageChannel getMessageChannel() {
            return messageChannel;
        }

        @Override
        public void setMessageChannel(MessageChannel channel) {
            this.messageChannel = channel;
        }

    }
}
