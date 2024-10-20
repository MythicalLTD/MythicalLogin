package xyz.mythicalsystems.mythicallogin.Discord.event;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

import java.awt.Color;

public class OnDms implements MessageCreateListener {

    /**
     * This method is called when a user sends a message to the bot!
     */
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().isBotUser()) {
            return;
        }

        if (event.getMessageAuthor().isYourself()) {
            return;
        }

        if (event.getMessage().isPrivateMessage()) {
            String message = event.getMessageContent();
            String user_id = event.getMessageAuthor().getIdAsString();

            if (message.startsWith("!login ")) {
                String[] args = message.split(" ");
                if (args.length == 2) {
                    String pin = args[1];
                    if (UserDataHandler.doesPinExist(pin)) {
                        try {
                            String uuid = UserDataHandler.getUserInfo(user_id,"discord_pin");
                            if (uuid == null) {
                                EmbedBuilder embedBuilder = new EmbedBuilder()
                                        .setTitle(getMessage("Bot.Commands.Login.NotLinked.Title"))
                                        .setColor(Color.RED)
                                        .setDescription(getMessage("Bot.Commands.Login.NotLinked.Description"));

                                event.getChannel().sendMessage(embedBuilder).thenAccept(
                                        msg -> {
                                            msg.addReaction("\u274C");
                                        });
                                return;
                            }
                            UserDataHandler.setUserInfo(user_id, "discord_pin", "None");
                            UserDataHandler.setUserInfo(user_id, "blocked", "false");
                            EmbedBuilder embedBuilder = new EmbedBuilder()
                                    .setTitle(getMessage("Bot.Commands.Login.Success.Title"))
                                    .setColor(Color.GREEN)
                                    .setDescription(getMessage("Bot.Commands.Login.Success.Description"));
                            event.getChannel().sendMessage(embedBuilder).thenAccept(msg -> {
                                msg.addReaction("\u2705");
                            });
                        } catch (Exception e) {
                            EmbedBuilder embedBuilder = new EmbedBuilder()
                                    .setTitle(getMessage("Bot.Commands.Login.AnErrorOccurred.Title"))
                                    .setColor(Color.RED)
                                    .setDescription(getMessage("Bot.Commands.Login.AnErrorOccurred.Description"));
                            event.getChannel().sendMessage(embedBuilder).thenAccept(msg -> {
                                msg.addReaction("\u274C");
                            });
                        }
                    } else {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(getMessage("Bot.Commands.Login.InvalidPin.Title"))
                                .setColor(Color.RED)
                                .setDescription(getMessage("Bot.Commands.Login.InvalidPin.Description"));
                        event.getChannel().sendMessage(embed).thenAccept(msg -> {
                            msg.addReaction("\u274C");
                        });
                    }
                }
            }
        }
    }

    /**
     * Get a message from the messages.yml file
     */
    private static String getMessage(String text) {
        return Messages.getMessage().getString(text);
    }
}
