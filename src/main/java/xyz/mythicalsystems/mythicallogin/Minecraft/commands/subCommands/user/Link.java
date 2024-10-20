package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class Link {

    public Link(ProxiedPlayer player) {
        if (player.hasPermission(Permissions.USER_LINK)) {            
            if (UserDataHandler.isDiscordLinked(player)) {
                player.sendMessage(new TextComponent(ChatTranslator.Translate(Messages.getMessage().getString("Discord.AlreadyLinked"))));
            } else {
                String pin = UserDataHandler.generatePin();
                UserDataHandler.setUserInfo(player, "discord_pin", pin);
                player.sendMessage(new TextComponent(ChatTranslator.Translate(Messages.getMessage().getString("Discord.Link").replace("{pin}", pin))));
            }

        } else {
            player.sendMessage(new TextComponent(ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
        }
    }
}
