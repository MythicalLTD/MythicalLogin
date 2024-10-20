package xyz.mythicalsystems.mythicallogin.Minecraft.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.Permissions;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin.ForceLogin;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin.ForceLogout;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin.ForceLogoutAll;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin.ForceUnlink;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.admin.Reload;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user.Help;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user.Invite;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user.Link;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.user.UnLink;

public class PinLoginCommand extends Command implements TabExecutor {

    public PinLoginCommand() {
        super("pinlogin", null, "mythicallogin", "mll");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String subCommand = args[0];
            List<String> completions = new ArrayList<>();
            String partialCommand = subCommand.toLowerCase();

            if (!(sender instanceof ProxiedPlayer)) {
                completions.add("admin");
                completions.add("help");
                return completions;
            }

            if (sender.hasPermission((Permissions.USER_HELP))) {
                if ("help".startsWith(partialCommand)) {
                    completions.add("help");
                }
            }

            if (sender.hasPermission((Permissions.ADMIN)) || sender.hasPermission((Permissions.ADMIN_FORCELOGIN))
                    || sender.hasPermission((Permissions.ADMIN_FORCELOGOUT))
                    || sender.hasPermission((Permissions.ADMIN_FORCELOGOUTALL))
                    || sender.hasPermission((Permissions.ADMIN_FORCEUNLINK))) {
                if ("admin".startsWith(partialCommand)) {
                    completions.add("admin");
                }
            }

            if (sender.hasPermission(Permissions.USER_LINK)) {
                if ("link".startsWith(partialCommand)) {
                    completions.add("link");
                }
            }

            if (sender.hasPermission(Permissions.USER_UNLINK)) {
                if ("unlink".startsWith(partialCommand)) {
                    completions.add("unlink");
                }
            }

            if (sender.hasPermission(Permissions.USER_INVITE)) {
                if ("invite".startsWith(partialCommand)) {
                    completions.add("invite");
                }
            }

            return completions;
        } else if (args.length == 2) {
            String subCommand = args[0];
            String subSubCommand = args[1];
            List<String> completions = new ArrayList<>();
            String partialCommand = subSubCommand.toLowerCase();

            if (subCommand.equalsIgnoreCase("admin")) {
                if (sender.hasPermission(Permissions.ADMIN_RELOAD)) {
                    if ("reload".startsWith(partialCommand)) {
                        completions.add("reload");
                    }
                }

                if (sender.hasPermission(Permissions.ADMIN_FORCELOGIN)) {
                    if ("forcelogin".startsWith(partialCommand)) {
                        completions.add("forcelogin");
                        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                            if (player.getName().toLowerCase().startsWith(partialCommand)) {
                                completions.add(player.getName());
                            }
                        }
                    }
                }

                if (sender.hasPermission(Permissions.ADMIN_FORCELOGOUT)) {
                    if ("forcelogout".startsWith(partialCommand)) {
                        completions.add("forcelogout");
                        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                            if (player.getName().toLowerCase().startsWith(partialCommand)) {
                                completions.add(player.getName());
                            }
                        }
                    }
                }

                if (sender.hasPermission(Permissions.ADMIN_FORCELOGOUTALL)) {
                    if ("forcelogoutall".startsWith(partialCommand)) {
                        completions.add("forcelogoutall");
                    }
                }

                if (sender.hasPermission(Permissions.ADMIN_FORCEUNLINK)) {
                    if ("forceunlink".startsWith(partialCommand)) {
                        completions.add("forceunlink");
                        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                            if (player.getName().toLowerCase().startsWith(partialCommand)) {
                                completions.add(player.getName());
                            }
                        }
                    }
                }
            }
            return completions;
        }

        return ImmutableList.of();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length == 0) {
                sender.sendMessage(new TextComponent(
                        ChatTranslator.Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                return;
            }
            String subCommand = args[0];
            ProxiedPlayer player = (ProxiedPlayer) sender;

            switch (subCommand) {
                case "help":
                    new Help(player);
                    return;
                case "link":
                    new Link(player);
                    return;
                case "unlink":
                    new UnLink(player);
                    return;
                case "invite":
                    new Invite(player);
                    return;
                case "admin":
                    if (player.hasPermission(Permissions.ADMIN)) {
                        if (args.length == 1) {
                            new Help(player);
                            return;
                        }
                        String subSubCommand = args[1];
                        @SuppressWarnings("unused")
                        String target;
                        switch (subSubCommand) {
                            case "reload":
                                new Reload(player);
                                return;
                            case "forcelogin":
                                target = args[2];
                                new ForceLogin(player, args);
                                return;
                            case "forcelogout":
                                target = args[2];
                                new ForceLogout(player, args);
                                return;
                            case "forceunlink":
                                target = args[2];
                                new ForceUnlink(player, args);
                                return;
                            case "forcelogoutall":
                                new ForceLogoutAll(player);
                                return;
                            default:
                                sender.sendMessage(new TextComponent(
                                        ChatTranslator
                                                .Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                                return;
                        }
                    } else {
                        player.sendMessage(new TextComponent(
                                ChatTranslator.Translate(Messages.getMessage().getString("Global.NoPermission"))));
                    }
                    return;
                default:
                    sender.sendMessage(new TextComponent(
                            ChatTranslator.Translate(Messages.getMessage().getString("Global.InvalidCommand"))));
                    return;
            }
        } else {
            new Help(sender);
        }
    }

}
