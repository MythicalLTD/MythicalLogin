package xyz.mythicalsystems.mythicallogin.Discord.event;

import java.util.UUID;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import xyz.mythicalsystems.mythicallogin.Messages.Messages;

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
                    /**if (Discord.doesPinExist(pin)) {
                        try {
                            String uuid = Discord.getUUIDFromDiscord(user_id);
                            if (uuid == null) {
                                EmbedBuilder embedBuilder = new EmbedBuilder()
                                        .setTitle(getMessage("Bot.Commands.Login.NotLinked.Title"))
                                        .setColor(Color.RED)
                                        .setDescription(getMessage("Bot.Commands.Login.NotLinked.Description"))
                                        .setFooter(Bot.copyright);

                                event.getChannel().sendMessage(embedBuilder).thenAccept(
                                        msg -> {
                                            msg.addReaction("\u274C");
                                        });
                                return;
                            }
                            Account.updateUser(UUID.fromString(uuid), "blocked", "false");
                            Discord.removePin(user_id);
                            EmbedBuilder embedBuilder = new EmbedBuilder()
                                    .setTitle(getMessage("Bot.Commands.Login.Success.Title"))
                                    .setColor(Color.GREEN)
                                    .setDescription(getMessage("Bot.Commands.Login.Success.Description"))
                                    .setFooter(Bot.copyright);
                            event.getChannel().sendMessage(embedBuilder).thenAccept(msg -> {
                                msg.addReaction("\u2705");
                            });
                        } catch (Exception e) {
                            EmbedBuilder embedBuilder = new EmbedBuilder()
                                    .setTitle(getMessage("Bot.Commands.Login.AnErrorOccurred.Title"))
                                    .setColor(Color.RED)
                                    .setDescription(getMessage("Bot.Commands.Login.AnErrorOccurred.Description"))
                                    .setFooter(Bot.copyright);
                            event.getChannel().sendMessage(embedBuilder).thenAccept(msg -> {
                                msg.addReaction("\u274C");
                            });
                        }
                    } else {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(getMessage("Bot.Commands.Login.InvalidPin.Title"))
                                .setColor(Color.RED)
                                .setDescription(getMessage("Bot.Commands.Login.InvalidPin.Description"))
                                .setFooter(Bot.copyright);
                        event.getChannel().sendMessage(embed).thenAccept(msg -> {
                            msg.addReaction("\u274C");
                        });
                    }**/
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
