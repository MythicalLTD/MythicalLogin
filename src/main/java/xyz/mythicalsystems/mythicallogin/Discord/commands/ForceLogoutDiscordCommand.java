package xyz.mythicalsystems.mythicallogin.Discord.commands;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import xyz.mythicalsystems.mythicallogin.Config.Config;
import xyz.mythicalsystems.mythicallogin.Discord.Bot;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class ForceLogoutDiscordCommand extends Bot {
    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description)
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {

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
                UserDataHandler.setAllBlocked();
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Force logout successful!")
                        .setDescription("All users were logged out!")
                        .setColor(java.awt.Color.GREEN);
                interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                    java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                            .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                });

            }
        });
    }
}
