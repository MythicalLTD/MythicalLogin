package xyz.mythicalsystems.mythicallogin.MySQL;

import java.net.InetSocketAddress;
import java.util.UUID;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.Messages.Messages;

public class UserDataHandler {
    public static final String TABLE_NAME = "mythicallogin_users";

    /**
     * Register a user in the database.
     * 
     * @param player The player to register.
     * 
     * @return void
     */
    public static void registerUser(ProxiedPlayer player) {
        if (Main.CONFIG_DEBUG_ENABLED) {
            Main.logger.info(UserDataHandler.class.getName(),
                    "(DEBUG) User " + player.getDisplayName() + " is attempting to register.");
        }
        UUID uuid = player.getUniqueId();
        String uuid_string = uuid.toString();
        String username = player.getName();
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        if (!isRegistered(uuid)) {
            try {
                Connection connection = Main.connection;
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO `" + TABLE_NAME
                                + "` (`uuid`, `username`, `first_ip`, `last_ip`) VALUES (?, ?, ?, ?)");
                statement.setString(1, uuid_string);
                statement.setString(2, username);
                statement.setString(3, ipAddress);
                statement.setString(4, ipAddress);
                statement.execute();
                statement.close();
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) User " + username + " has been registered.");
                }
            } catch (Exception e) {
                Main.logger.error(UserDataHandler.class.getName(), "Failed to register user: " + e.getMessage());
                player.disconnect(new TextComponent(Messages.getMessage().getString("Global.ErrorOnJoin")));
            }
        } else {
            if (Main.CONFIG_DEBUG_ENABLED) {
                Main.logger.info(UserDataHandler.class.getName(),
                        "(DEBUG) User " + username + " is already registered.");
            }
            return;
        }
    }

    /**
     * Unregister a user from the database.
     * 
     * @param player The player to unregister.
     */
    public static void unregisterUser(ProxiedPlayer player) {
        UUID uuid = player.getUniqueId();
        String uuid_string = uuid.toString();

        try {
            Connection connection = Main.connection;
            PreparedStatement statement = connection
                    .prepareStatement("DELETE FROM `" + TABLE_NAME + "` WHERE `uuid` = ?");
            statement.setString(1, uuid_string);
            statement.execute();
            statement.close();
            if (Main.CONFIG_DEBUG_ENABLED) {
                Main.logger.info(UserDataHandler.class.getName(),
                        "(DEBUG) User " + player.getName() + " has been unregistered.");
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(), "Failed to unregister user: " + e.getMessage());
            player.disconnect(new TextComponent(Messages.getMessage().getString("Global.ErrorOnJoin")));
        }
    }

    /**
     * Checks if a user is registered in the database.
     * 
     * @param uuid The UUID of the user to check.
     * 
     * @return boolean True if the user is registered, false if not.
     */
    public static boolean isRegistered(UUID uuid) {
        try {

            ResultSet result = Main.connection.createStatement()
                    .executeQuery("SELECT * FROM `" + TABLE_NAME + "` WHERE `uuid` = '" + uuid.toString() + "'");
            if (!result.next()) {
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) User " + uuid.toString() + " is not registered.");
                }
                result.close();
                return false;
            } else {
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) User " + uuid.toString() + " is registered.");
                }
                result.close();
                return true;
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(),
                    "Failed to check if user is registered: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update the IP address of a user in the database.
     * 
     * @param player The player to update the IP address for.
     * 
     * @return void
     */
    public static void updateIP(ProxiedPlayer player) {
        UUID uuid = player.getUniqueId();
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        try {
            Connection connection = Main.connection;
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE `" + TABLE_NAME + "` SET `last_ip` = ? WHERE `uuid` = ?");
            statement.setString(1, ipAddress);
            statement.setString(2, uuid.toString());
            statement.execute();
            statement.close();
            if (Main.CONFIG_DEBUG_ENABLED) {
                Main.logger.info(UserDataHandler.class.getName(),
                        "(DEBUG) User " + player.getName() + " has updated their IP.");
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(), "Failed to update user IP: " + e.getMessage());
            player.disconnect(new TextComponent(Messages.getMessage().getString("Global.ErrorOnJoin")));
        }
    }

    /**
     * Get user info from the database.
     * 
     * @param player The player to get info for.
     * @param info   The info to get.
     * 
     * @return String The value of the info.
     */
    public static String getUserInfo(ProxiedPlayer player, String info) {
        UUID uuid = player.getUniqueId();
        String uuid_string = uuid.toString();
        try {
            ResultSet result = Main.connection.createStatement()
                    .executeQuery("SELECT * FROM `" + TABLE_NAME + "` WHERE `uuid` = '" + uuid_string + "'");
            if (result.next()) {
                String value = result.getString(info);
                result.close();
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) User " + player.getName() + " has requested info: " + info + " = " + value);
                }
                return value;
            } else {
                result.close();
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) User " + player.getName() + " has requested info: " + info + " = null");
                }
                return null;
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(),
                    "Failed to get user info: " + e.getMessage());
            player.disconnect(new TextComponent(Messages.getMessage().getString("Global.ErrorOnJoin")));

            return null;
        }
    }

    /**
     * Set user info in the database.
     * 
     * @param player The player to set info for.
     * @param info   The info to set.
     * @param value  The value to set.
     * 
     * @return void
     */
    public static void setUserInfo(ProxiedPlayer player, String info, String value) {
        UUID uuid = player.getUniqueId();
        String uuid_string = uuid.toString();
        try {
            Connection connection = Main.connection;
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE `" + TABLE_NAME + "` SET `" + info + "` = ? WHERE `uuid` = ?");
            statement.setString(1, value);
            statement.setString(2, uuid_string);
            statement.execute();
            statement.close();
            if (Main.CONFIG_DEBUG_ENABLED) {
                Main.logger.info(UserDataHandler.class.getName(),
                        "(DEBUG) User " + player.getName() + " has set info: " + info + " = " + value);
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(), "Failed to set user info: " + e.getMessage());
            player.disconnect(new TextComponent(Messages.getMessage().getString("Global.ErrorOnJoin")));
        }
    }

    /**
     * Check if a user already linked his discord accounts
     * 
     * @param player The player to check.
     * 
     * @return boolean True if the user is linked, false if not.
     */
    public static boolean isDiscordLinked(ProxiedPlayer player) {
        try {
            if (Main.CONFIG_DEBUG_ENABLED) {
                Main.logger.info(UserDataHandler.class.getName(),
                        "(DEBUG) User " + player.getName() + " is attempting to check if linked to Discord.");
            }
            String discord_id = getUserInfo(player, "discord_id");
            if (discord_id == "None") {
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) User " + player.getName() + " is not linked to Discord.");
                }
                return false;
            } else {
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) User " + player.getName() + " is linked to Discord.");
                }
                return true;
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(),
                    "Failed to get user info: " + e.getMessage());
            player.disconnect(new TextComponent(Messages.getMessage().getString("Global.ErrorOnJoin")));

            return false;
        }
    }

    /**
     * Check if a pin exists in the database.
     * 
     * @param pin The pin to check.
     * 
     * @return boolean True if the pin exists, false if not.
     */
    public static boolean doesPinExist(String pin) {
        try {
            ResultSet result = Main.connection.createStatement()
                    .executeQuery("SELECT * FROM `mythicallogin_pins` WHERE `pin` = '" + pin + "'");
            if (!result.next()) {
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) Pin " + pin + " does not exist.");
                }
                result.close();
                return false;
            } else {
                if (Main.CONFIG_DEBUG_ENABLED) {
                    Main.logger.info(UserDataHandler.class.getName(),
                            "(DEBUG) Pin " + pin + " exists.");
                }
                result.close();
                return true;
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(),
                    "Failed to check if pin exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generate a pin
     * 
     * @return
     */
    public static String generatePin() {
        return String.valueOf((int) ((Math.random() * 9000) + 1000));
    }
}
