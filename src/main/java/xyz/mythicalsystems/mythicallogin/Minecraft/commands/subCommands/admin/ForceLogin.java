package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class ForceLogin {
    public ForceLogin(ProxiedPlayer player, String[] args) {
        if (player.hasPermission(Permissions.ADMIN_FORCELOGIN)) {
            if (args.length == 3) {
                ProxiedPlayer target = MinecraftPlugin.getInstance().getProxy().getPlayer(args[2]);
                if (target != null) {
                    if (UserDataHandler.isRegistered(target.getUniqueId())) {
                        if (UserDataHandler.isDiscordLinked(target)) {
                            UserDataHandler.setUserInfo(target, "last_ip", "forcelogin");
                        } else {
                            player.sendMessage(new TextComponent(
                                    ChatTranslator.Translate(Messages.getMessage().getString("forceLogin.NotLinked"))));
                            return;
                        }
                    } else {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.PlayerNotRegistered"))));
                        return;
                    }

                    player.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("forceLogin.Success"))));
                } else {
                    player.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("forceLogin.Error"))));
                }
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
