package xyz.mythicalsystems.mythicallogin.Chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.mythicalsystems.mythicallogin.Config.Config;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;

public class ChatTranslator {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    /**
     * Translate messages and show color codes
     * 
     * @param message
     * 
     * @return
     */
    public static String Translate(String message) {
        message = message.replace("{prefix}", Messages.getMessage().getString("Global.Prefix"));
        message = message.replace("{discord_url}", Config.getSetting().getString("Discord.invite"));
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = ChatColor.translateAlternateColorCodes('§', message);
        return message;
    }

    /**
     * Translates hexadecimal color codes in a message to the corresponding color.
     *
     * @param message The message to translate.
     * @return The translated message.
     */
    public static String translateHexColorCodes(final String message) {
        final char colorChar = ChatColor.COLOR_CHAR;

        final Matcher matcher = HEX_PATTERN.matcher(message);
        final StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            final String group = matcher.group(1);

            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    /**
     * Transforms a string into a BaseComponent array with color codes applied.
     *
     * @param string The string to transform.
     * @return The transformed BaseComponent array.
     */
    @SuppressWarnings("deprecation")
    public static BaseComponent[] transformString(String string) {
        if (string == null)
            throw new NullPointerException("string cannot be null");
        return TextComponent
                .fromLegacyText(Translate(string));
    }
}
