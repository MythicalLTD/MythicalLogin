package xyz.mythicalsystems.mythicallogin.Discord.commands;

import java.util.Arrays;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import xyz.mythicalsystems.mythicallogin.Config.Config;
import xyz.mythicalsystems.mythicallogin.Discord.Bot;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class LinkDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description,
                Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING,
                                getMessage("Bot.Commands.Link.Args.Pin.Name"),
                                getMessage("Bot.Commands.Link.Args.Pin.Description"), true)))
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                String pin = interaction.getArgumentByName("pin").get().getStringValue().get();
                if (UserDataHandler.doesPinExist(pin)) {
                    String user_id = interaction.getUser().getIdAsString();

                    if (UserDataHandler.isDiscordLinked(user_id)) {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(getMessage("Bot.Commands.Link.AlreadyLinked.Title"))
                                .setDescription(getMessage("Bot.Commands.Link.AlreadyLinked.Description"))
                                .setColor(java.awt.Color.RED);
                        interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                            java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                    .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                        });
                    } else {
                        UserDataHandler.setUserInfo_PIN(pin, "discord_id", user_id);
                        UserDataHandler.setUserInfo(user_id, "discord_pin", "None");
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(getMessage("Bot.Commands.Link.Success.Title"))
                                .setDescription(getMessage("Bot.Commands.Link.Success.Description"))
                                .setColor(java.awt.Color.GREEN);
                        interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                            java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                    .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                        });

                    }
                } else {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(getMessage("Bot.Commands.Link.InvalidPin.Title"))
                            .setDescription(getMessage("Bot.Commands.Link.InvalidPin.Description"))
                            .setColor(java.awt.Color.RED);
                    interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                        java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                    });
                }
            }
        });
    }

    /**
     * Get a message from the messages.yml file
     */
    private static String getMessage(String text) {
        return Messages.getMessage().getString(text);
    }
}
