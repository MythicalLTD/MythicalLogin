package xyz.mythicalsystems.mythicallogin.Discord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.server.Server;

import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.Config.Config;
import xyz.mythicalsystems.mythicallogin.Discord.event.OnDms;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;

public class Bot {
    public static DiscordApi bot;
    public Server server;
    public static Bot instance;

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
        Main.logger.info("Bot", "Open the following url to invite the bot: " + api.createBotInvite());

        /**
         * Register commands here!
         */


        /**
         * Register events here!
         */
        Main.logger.info("Bot", "Registering events...");
        api.addMessageCreateListener(new OnDms());
        Main.logger.info("Bot", "Events registered!");

    }

    public void stop() {
        if (bot != null) {
            bot.disconnect();
            bot = null;
        }
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
