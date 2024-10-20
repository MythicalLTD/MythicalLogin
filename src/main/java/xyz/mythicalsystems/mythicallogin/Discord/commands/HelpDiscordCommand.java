package xyz.mythicalsystems.mythicallogin.Discord.commands;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import xyz.mythicalsystems.mythicallogin.Config.Config;
import xyz.mythicalsystems.mythicallogin.Discord.Bot;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;

public class HelpDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description)
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(Messages.getMessage().getString("Bot.Commands.Help.Embed.Title"))
                        .setDescription(Messages.getMessage().getString("Bot.Commands.Help.Embed.Description"))
                        .addField("`/help`", Messages.getMessage().getString("Bot.Commands.Help.Description"), true)
                        .addField("`/link`", Messages.getMessage().getString("Bot.Commands.Link.Description"), true)
                        .addField("`/unlink`", Messages.getMessage().getString("Bot.Commands.Unlink.Description"), true)
                        .setColor(Color.BLUE)
                        .setTimestampToNow();
                interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                    java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                            .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                });
            }
        });
    }
}
