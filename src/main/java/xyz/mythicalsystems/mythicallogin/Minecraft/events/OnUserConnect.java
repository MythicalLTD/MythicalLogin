package xyz.mythicalsystems.mythicallogin.Minecraft.events;

import java.awt.Color;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.List;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Discord.Bot;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class OnUserConnect implements Listener {

    @EventHandler(priority = 127)
    public void onPlayerConnect(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        MinecraftPlugin.TASK_SCHEDULER.runAsync(MinecraftPlugin.getInstance(), () -> {
            UserDataHandler.registerUser(player);
            if (UserDataHandler.isDiscordLinked(player)) {
                String last_ip = UserDataHandler.getUserInfo(player, "last_ip");
                if (last_ip != null) {
                    String current_ip = ((InetSocketAddress) player.getSocketAddress()).getAddress()
                            .getHostAddress();
                    String isBlockedString = UserDataHandler.getUserInfo(player, "blocked");
                    String pin = UserDataHandler.generatePin();

                    if (last_ip.equals("forcelogin")) {
                        UserDataHandler.setUserInfo(player, "discord_pin", "None");
                        UserDataHandler.setUserInfo(player, "blocked", "false");
                        UserDataHandler.setUserInfo(player, "last_ip", current_ip);
                        sendForceloginMessage(player);
                        return;
                    }

                    String finalMsg = "";
                    List<String> loginRequiredMessage = Messages.getMessage().getStringList("Discord.LoginRequired");
                    for (String message : loginRequiredMessage) {
                        finalMsg += message.replace("{pin}", pin) + "\n";
                    }

                    if (!last_ip.equals(current_ip)) {
                        if (isBlockedString.equals("true")) {
                            UserDataHandler.setUserInfo(player, "discord_pin", pin);
                            player.disconnect(new TextComponent(ChatTranslator.Translate(finalMsg)));
                            return;
                        } else {
                            UserDataHandler.setUserInfo(player, "discord_pin", pin);
                            UserDataHandler.setUserInfo(player, "last_ip", current_ip);
                            UserDataHandler.setUserInfo(player, "blocked", "true");
                            sendJoinMessage(player);
                            player.disconnect(new TextComponent(ChatTranslator.Translate(finalMsg)));
                            return;
                        }
                    } else {
                        if (isBlockedString.equals("true")) {
                            UserDataHandler.setUserInfo(player, "discord_pin", pin);
                            sendJoinMessage(player);
                            player.disconnect(new TextComponent(ChatTranslator.Translate(finalMsg)));
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
        });
    }

    /**
     * This method is called when a user joins the server!
     *
     * @param player
     */
    public static void sendJoinMessage(ProxiedPlayer player) {
        Bot.getInstance().sendMessageToUser("<@" + UserDataHandler.getUserInfo(player, "discord_id") + ">",
                UserDataHandler.getUserInfo(player, "discord_id"));
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = formatter.format(System.currentTimeMillis());
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Title"))
                .setColor(Color.BLUE)
                .setDescription(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Description"))
                .setTimestampToNow()
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Name"),
                        player.getDisplayName())
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.IP"), ipAddress)
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Date"), date);
        Bot.getInstance()
                .sendMessageToUser(embedBuilder, UserDataHandler.getUserInfo(player, "discord_id"));

    }

    public static void sendForceloginMessage(ProxiedPlayer player) {
        Bot.getInstance().sendMessageToUser("<@" + UserDataHandler.getUserInfo(player, "discord_id") + ">",
                UserDataHandler.getUserInfo(player, "discord_id"));
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = formatter.format(System.currentTimeMillis());
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(Messages.getMessage().getString("Bot.Commands.Login.ForceloginRequest.Title"))
                .setColor(Color.BLUE)
                .setDescription(Messages.getMessage().getString("Bot.Commands.Login.ForceloginRequest.Description"))
                .setTimestampToNow()
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Name"),
                        player.getDisplayName())
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.IP"), ipAddress)
                .addField(Messages.getMessage().getString("Bot.Commands.Login.NewRequest.Date"), date);
        Bot.getInstance()
                .sendMessageToUser(embedBuilder, UserDataHandler.getUserInfo(player, "discord_id"));
    }

}
