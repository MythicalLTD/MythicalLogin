package xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;

public class Help {
        // Player logic
        public Help(ProxiedPlayer player) {
                if (player.hasPermission(Permissions.USER_HELP)) {
                        player.sendMessage(
                                        new TextComponent(ChatTranslator
                                                        .Translate(Messages.getMessage().getString("Help.Bar"))));
                        player.sendMessage(new TextComponent(ChatTranslator.Translate("&r")));
                        player.sendMessage(
                                        new TextComponent(ChatTranslator
                                                        .Translate(Messages.getMessage().getString("Help.Title"))));
                        player.sendMessage(new TextComponent(ChatTranslator.Translate("&r")));
                        if (player.hasPermission(Permissions.USER_HELP)) {
                                player.sendMessage(
                                                new TextComponent(ChatTranslator.Translate(
                                                                Messages.getMessage().getString("Help.help"))));
                        }
                        if (player.hasPermission(Permissions.USER_LINK)) {
                                player.sendMessage(new TextComponent(
                                                ChatTranslator.Translate(
                                                                Messages.getMessage().getString("Help.discordLink"))));
                        }
                        if (player.hasPermission(Permissions.USER_UNLINK)) {
                                player.sendMessage(new TextComponent(
                                                ChatTranslator.Translate(Messages.getMessage()
                                                                .getString("Help.discordUnlink"))));
                        }
                        if (player.hasPermission(Permissions.USER_INVITE)) {
                                player.sendMessage(new TextComponent(
                                                ChatTranslator.Translate(Messages.getMessage()
                                                                .getString("Help.discordInvite"))));
                        }

                        if (player.hasPermission(Permissions.ADMIN)) {
                                if (player.hasPermission(Permissions.ADMIN_RELOAD)) {
                                        player.sendMessage(new TextComponent(
                                                        ChatTranslator.Translate(Messages.getMessage()
                                                                        .getString("Help.Admin.reload"))));
                                }

                                if (player.hasPermission(Permissions.ADMIN_FORCELOGIN)) {
                                        player.sendMessage(new TextComponent(
                                                        ChatTranslator.Translate(Messages.getMessage()
                                                                        .getString("Help.Admin.forceLogin"))));
                                }

                                if (player.hasPermission(Permissions.ADMIN_FORCELOGOUT)) {
                                        player.sendMessage(new TextComponent(
                                                        ChatTranslator.Translate(Messages.getMessage()
                                                                        .getString("Help.Admin.forceLogout"))));
                                }

                                if (player.hasPermission(Permissions.ADMIN_FORCEUNLINK)) {
                                        player.sendMessage(new TextComponent(
                                                        ChatTranslator.Translate(Messages.getMessage()
                                                                        .getString("Help.Admin.forceUnlink"))));
                                }

                                if (player.hasPermission(Permissions.ADMIN_FORCELOGOUTALL)) {
                                        player.sendMessage(new TextComponent(
                                                        ChatTranslator.Translate(Messages.getMessage()
                                                                        .getString("Help.Admin.forceLogoutAll"))));
                                }
                        }
                        player.sendMessage(new TextComponent(ChatTranslator.Translate("&r")));
                        player.sendMessage(
                                        new TextComponent(ChatTranslator
                                                        .Translate(Messages.getMessage().getString("Help.Bar"))));
                } else {
                        player.sendMessage(new TextComponent(
                                        ChatTranslator.Translate(
                                                        Messages.getMessage().getString("Global.NoPermission"))));
                }

        }

        // Console logic
        public Help(CommandSender sender) {
                sender.sendMessage(new TextComponent(ChatTranslator
                                .Translate("&cThis command can only be run by players, due to security reasons.")));
        }
}
