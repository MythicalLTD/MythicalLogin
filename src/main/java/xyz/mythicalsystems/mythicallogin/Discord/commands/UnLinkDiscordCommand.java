package xyz.mythicalsystems.mythicallogin.Discord.commands;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import xyz.mythicalsystems.mythicallogin.Config.Config;
import xyz.mythicalsystems.mythicallogin.Discord.Bot;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class UnLinkDiscordCommand extends Bot {

    public static void register(String name, String description) {
        SlashCommand command = SlashCommand.with(name, description)
                .createForServer(bot, Config.getSetting().getLong("Discord.guild"))
                .join();

        bot.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equals(command.getName())) {
                String user_id = interaction.getUser().getIdAsString();
                if (UserDataHandler.isDiscordLinked(user_id)) {
                    UserDataHandler.setUserInfo(user_id, "discord_id", "None");
                    UserDataHandler.setUserInfo(user_id, "discord_pin", "None");
                    UserDataHandler.setUserInfo(user_id, "blocked", "false");
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(getMessage("Bot.Commands.Unlink.Success.Title"))
                            .setDescription(getMessage("Bot.Commands.Unlink.Success.Description"))
                            .setColor(java.awt.Color.GREEN);
                    interaction.createImmediateResponder().addEmbed(embed).respond().thenAccept(message -> {
                        java.util.concurrent.Executors.newSingleThreadScheduledExecutor()
                                .schedule(() -> message.delete(), 10, java.util.concurrent.TimeUnit.SECONDS);
                    });
                } else {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(getMessage("Bot.Commands.Unlink.NotLinked.Title"))
                            .setDescription(getMessage("Bot.Commands.Unlink.NotLinked.Description"))
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
