package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;

public class ForceLogin {
    public ForceLogin(ProxiedPlayer player, String[] args) {
        if (player.hasPermission(Permissions.ADMIN_FORCELOGIN)) {
            if (args.length == 2) {
                
                

            } else {
                player.sendMessage(new TextComponent(
                        ChatTranslator.Translate(Messages.getMessage().getString("Help.Admin.forceLogin"))));
            }
        } else {
            player.sendMessage(new TextComponent(
                    ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
        }
    }
}
