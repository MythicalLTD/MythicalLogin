package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;

public class Invite {
    public Invite(ProxiedPlayer player) {
        if (player.hasPermission(Permissions.USER_INVITE)) {
            player.sendMessage(
                    new TextComponent(ChatTranslator.Translate(Messages.getMessage().getString("Discord.Invite"))));
        } else {
            player.sendMessage(new TextComponent(
                    ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
        }
    }

}
