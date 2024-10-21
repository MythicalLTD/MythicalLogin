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

public class ForceUnlinkDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description,
                Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.USER,
                                Messages.getMessage().getString("Bot.Commands.Login.ForceUnlink.Args.Player.Name"),
                                Messages.getMessage()
                                        .getString("Bot.Commands.Login.ForceUnlink.Args.Player.Description"),
                                true)))
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                String userId = interaction.getOptionByName("user")
                        .flatMap(option -> option.getUserValue())
                        .map(user -> user.getIdAsString())
                        .orElse("Unknown");

                if (!interaction.getUser().isBotOwnerOrTeamMember()) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(Messages.getMessage().getString("Bot.PermissionDenied.Title"))
                            .setDescription(Messages.getMessage().getString("Bot.PermissionDenied.Description"))
                            .setColor(java.awt.Color.RED);
                    interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                        java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                    });
                    return;
                }

                if (userId == "Unknown") {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(Messages.getMessage().getString("Bot.InvalidUser.Title"))
                            .setDescription(Messages.getMessage().getString("Bot.InvalidUser.Description"))
                            .setColor(java.awt.Color.RED);
                    interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                        java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                    });
                } else {
                    if (!UserDataHandler.isDiscordLinked(userId)) {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(Messages.getMessage().getString("Bot.UserNotLinked.Title"))
                                .setDescription(Messages.getMessage().getString("Bot.UserNotLinked.Description"))
                                .setColor(java.awt.Color.RED);
                        interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                            java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                    .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                        });
                    } else {
                        UserDataHandler.setUserInfo(userId, "discord_pin", "None");
                        UserDataHandler.setUserInfo(userId, "discord_id", "None");
                        UserDataHandler.setUserInfo(userId, "blocked", "false");
                        UserDataHandler.setUserInfo(userId, "last_ip", "forcelogin");

                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(
                                        Messages.getMessage().getString("Bot.Commands.Login.ForceUnlink.Success.Title"))
                                .setDescription(Messages.getMessage()
                                        .getString("Bot.Commands.Login.ForceUnlink.Success.Description"))
                                .setColor(java.awt.Color.GREEN);
                        interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                            java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                    .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                        });
                    }
                }

            }
        });
    }

}
