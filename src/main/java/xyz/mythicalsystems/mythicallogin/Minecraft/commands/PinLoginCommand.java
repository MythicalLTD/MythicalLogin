package xyz.mythicalsystems.mythicallogin.Minecraft.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.mythicalsystems.mythicallogin.Chat.ChatTranslator;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;
import xyz.mythicalsystems.mythicallogin.Minecraft.commands.subCommands.Help;

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

            if (sender.hasPermission(("mythicallogin.help"))) {
                if ("help".startsWith(partialCommand)) {
                    completions.add("help");
                }
            }

            if (sender.hasPermission(("mythicallogin.admin"))) {
                if ("reload".startsWith(partialCommand)) {
                    completions.add("reload");
                }
                if ("admin".startsWith(partialCommand)) {
                    completions.add("admin");
                }
            }

            if (sender.hasPermission("mythicallogin.user.link")) {
                if ("link".startsWith(partialCommand)) {
                    completions.add("link");
                }
            }

            if (sender.hasPermission("mythicallogin.user.unlink")) {
                if ("unlink".startsWith(partialCommand)) {
                    completions.add("unlink");
                }
            }

            if (sender.hasPermission("mythicallogin.user.invite")) {
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
                if (sender.hasPermission("mythicallogin.admin.reload")) {
                    if ("reload".startsWith(partialCommand)) {
                        completions.add("reload");
                    }
                }

                if (sender.hasPermission("mythicallogin.admin.forcelogin")) {
                    if ("forcelogin".startsWith(partialCommand)) {
                        completions.add("forcelogin");
                    }
                }

                if (sender.hasPermission("mythicallogin.admin.forcelogout")) {
                    if ("forcelogout".startsWith(partialCommand)) {
                        completions.add("forcelogout");
                    }
                }

                if (sender.hasPermission("mythicallogin.admin.forcelogoutall")) {
                    if ("forcelogoutall".startsWith(partialCommand)) {
                        completions.add("forcelogoutall");
                    }
                }

                if (sender.hasPermission("mythicallogin.admin.forceunlink")) {
                    if ("forceunlink".startsWith(partialCommand)) {
                        completions.add("forceunlink");
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
