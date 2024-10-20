package xyz.mythicalsystems.mythicallogin.Discord;

import java.time.Duration;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;

import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.Discord.commands.ForceLoginDiscordCommand;
import xyz.mythicalsystems.mythicallogin.Discord.commands.ForceLogoutDiscordCommand;
import xyz.mythicalsystems.mythicallogin.Discord.commands.ForceUnlinkDiscordCommand;
import xyz.mythicalsystems.mythicallogin.Discord.commands.HelpDiscordCommand;
import xyz.mythicalsystems.mythicallogin.Discord.commands.LinkDiscordCommand;
import xyz.mythicalsystems.mythicallogin.Discord.commands.UnLinkDiscordCommand;
import xyz.mythicalsystems.mythicallogin.Discord.event.OnDms;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;

public class Bot {
    public static DiscordApi bot;
    public Server server;
    public static Bot instance;
    /**
     * This method is used to start the bot!
     * 
     * @return void
     */
    public void start() {
        instance = this;

        new DiscordApiBuilder()
                .setToken(Main.CONFIG_DISCORD_TOKEN)
                .login()
                .thenAccept(this::onConnectToDiscord)
                .exceptionally(error -> {
                    Main.logger.warn("Bot", "Failed to connect to Discord: " + error.getMessage());
                    return null;
                });
    }

    private void onConnectToDiscord(DiscordApi api) {
        bot = api;
        Main.logger.info("Bot", "Connected to Discord as " + api.getYourself().getDiscriminatedName());
        Main.logger.info("Bot", "Open the following url to invite the bot: " + api.createBotInvite(Permissions.fromBitmask(84032)));

        /**
         * Register commands here!
         */

        HelpDiscordCommand.register("help", Messages.getMessage().getString("Bot.Commands.Help.Description"));
        LinkDiscordCommand.register("link", Messages.getMessage().getString("Bot.Commands.Link.Description"));
        UnLinkDiscordCommand.register("unlink", Messages.getMessage().getString("Bot.Commands.Unlink.Description"));
        ForceLoginDiscordCommand.register("forcelogin", "Force login a user!");
        ForceUnlinkDiscordCommand.register("forceunlink", "Force unlink a user!");
        ForceLogoutDiscordCommand.register("forcelogoutall", "Force logout all users!");

        /**
         * Register events here!
         */
        Main.logger.info("Bot", "Registering events...");
        api.addMessageCreateListener(new OnDms());
        Main.logger.info("Bot", "Events registered!");

    }
    /**
     * This method is used to stop the bot!
     *
     * @return void 
     */
    public void stop() {
        if (bot != null) {
            bot.disconnect();
            bot = null;
        }
    }

    /**
     * This method is used to send a message to a user!
     * 
     * @param message
     * @param user_id
     */
    public void sendMessageToUser(EmbedBuilder message, String user_id) {
        bot.getUserById(user_id).thenAccept(user -> {
            user.sendMessage(message).thenAccept(sentMessage -> {
                sentMessage.addReaction("\u2139");
            });
        });
    }
    /**
     * This method is used to send a message to a user!
     * 
     * @param message
     * @param user_id
     */
    public void sendMessageToUser(String message, String user_id) {
        bot.getUserById(user_id).thenAccept(user -> {
            user.sendMessage(message).thenAccept(sentMessage -> {
                sentMessage.addReaction("\u2139");
                sentMessage.deleteAfter(Duration.ofSeconds(3));
            });
        });
    }

    /**
     * This method is used to send a message to a channel!
     * 
     * @return
     */
    public static Bot getInstance() {
        return instance;
    }
}
