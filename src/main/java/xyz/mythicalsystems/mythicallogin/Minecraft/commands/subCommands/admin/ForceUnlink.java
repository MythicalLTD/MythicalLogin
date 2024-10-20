package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;
import xyz.mythicalsystems.mythicallogin.MySQL.UserDataHandler;

public class ForceUnlink {
    public ForceUnlink(ProxiedPlayer player, String[] args) {
        if (player.hasPermission(Permissions.ADMIN_FORCEUNLINK)) {
            if (args.length == 3) {
                ProxiedPlayer target = MinecraftPlugin.getInstance().getProxy().getPlayer(args[2]);
                if (target != null) {
                    if (UserDataHandler.isRegistered(target.getUniqueId())) {
                        if (UserDataHandler.isDiscordLinked(target)) {
                            UserDataHandler.setUserInfo(target, "discord_id", "None");
                            UserDataHandler.setUserInfo(target, "discord_pin", "None");
                            UserDataHandler.setUserInfo(target, "blocked", "false");
                        } else {
                            player.sendMessage(new TextComponent(
                                    ChatTranslator
                                            .Translate(Messages.getMessage().getString("forceUnlink.NotLinked"))));
                            return;
                        }
                    } else {
                        player.sendMessage(new TextComponent(
                                ChatTranslator
                                        .Translate(Messages.getMessage().getString("Global.PlayerNotRegistered"))));
                        return;
                    }

                    player.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("forceUnlink.Success"))));
                } else {
                    player.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("forceUnlink.Error"))));
                }
            } else {
                player.sendMessage(new TextComponent(
                        ChatTranslator.Translate(Messages.getMessage().getString("Help.Admin.forceUnlink"))));
            }
        }
    }
}
