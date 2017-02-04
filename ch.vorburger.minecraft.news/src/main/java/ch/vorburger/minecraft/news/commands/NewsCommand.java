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
package ch.vorburger.minecraft.news.commands;

import static org.spongepowered.api.command.args.GenericArguments.onlyOne;
import static org.spongepowered.api.command.args.GenericArguments.requiringPermission;
import static org.spongepowered.api.command.args.GenericArguments.string;

import ch.vorburger.minecraft.news.NewsService;
import ch.vorburger.minecraft.osgi.api.CommandRegistration;
import ch.vorburger.minecraft.utils.CommandExceptions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * Command to both create new news entries and read news.
 *
 * @author Michael Vorburger
 */
public class NewsCommand implements CommandRegistration, CommandExecutor {

    private static final String ARG_NEWS = "text";

    private final NewsService newsService;

    public NewsCommand(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public List<String> aliases() {
        return ImmutableList.of("news");
    }

    @Override
    public CommandCallable callable() {
        return CommandSpec.builder()
                .description(Text.of("Read news"))
                .permission("ch.vorburger.news.read")
                .arguments(
                        requiringPermission(onlyOne(string(Text.of(ARG_NEWS))), "ch.vorburger.news.add"))
                .executor(this)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> optNews = args.<String> getOne(ARG_NEWS);
        if (optNews.isPresent()) {
            if (src instanceof Player) {
                Player playerSource = (Player) src;
                newsService.addNews(playerSource, optNews.get());
            } else {
                throw CommandExceptions.create("Only Player can add news");
            }
        } else {
            newsService.sendAllNews(src);
        }
        return CommandResult.success();
    }

}
