package xyz.mythicalsystems.mythicallogin.MySQL;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import xyz.mythicalsystems.mythicallogin.Main;

public class UserDataHandler {

    /**
     * Register a user in the database.
     * 
     * @param player The player to register.
     * 
     * @return void
     */
    public static void registerUser(ProxiedPlayer player) {
        UUID uuid = player.getUniqueId();
        String uuid_string = uuid.toString();
        String username = player.getName();
        InetSocketAddress socketAddress = (InetSocketAddress) player.getSocketAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();

        if (isRegistered(uuid)) {
            try {
                Connection connection = Main.connection;
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO `mythicallogin_users` (`uuid`, `username`, `first_ip`, `last_ip`) VALUES (?, ?, ?, ?)");
                statement.setString(1, uuid_string);
                statement.setString(2, username);
                statement.setString(3, ipAddress);
                statement.setString(4, ipAddress);
                statement.executeUpdate();
                statement.close();
            } catch (Exception e) {
                Main.logger.error(UserDataHandler.class.getName(), "Failed to register user: " + e.getMessage());
            }
        } else {
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
                    .prepareStatement("DELETE FROM `mythicallogin_users` WHERE `uuid` = ?");
            statement.setString(1, uuid_string);
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(), "Failed to unregister user: " + e.getMessage());
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
                    .executeQuery("SELECT * FROM `mythicallogin_users` WHERE `uuid` = '" + uuid + "'");
            if (!result.next()) {
                result.close();
                return false;
            } else {
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
                    .prepareStatement("UPDATE `mythicallogin_users` SET `last_ip` = ? WHERE `uuid` = ?");
            statement.setString(1, ipAddress);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(), "Failed to update user IP: " + e.getMessage());
        }
    }
    /**
     * Get user info from the database.
     * 
     * @param player The player to get info for.
     * @param info The info to get.
     * 
     * @return String The value of the info.
     */
    public static String getUserInfo(ProxiedPlayer player, String info) {
        UUID uuid = player.getUniqueId();
        String uuid_string = uuid.toString();
        try {
            ResultSet result = Main.connection.createStatement()
                    .executeQuery("SELECT * FROM `mythicallogin_users` WHERE `uuid` = '" + uuid_string + "'");
            if (result.next()) {
                String value = result.getString(info);
                result.close();
                return value;
            } else {
                result.close();
                return null;
            }
        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(),
                    "Failed to get user info: " + e.getMessage());
            return null;
        }
    }

    /**
     * Set user info in the database.
     * 
     * @param player The player to set info for.
     * @param info The info to set.
     * @param value The value to set.
     * 
     * @return void 
     */
    public static void setUserInfo(ProxiedPlayer player, String info, String value) {
        UUID uuid = player.getUniqueId();
        String uuid_string = uuid.toString();
        try {
            Connection connection = Main.connection;
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE `mythicallogin_users` SET `" + info + "` = ? WHERE `uuid` = ?");
            statement.setString(1, value);
            statement.setString(2, uuid_string);
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            Main.logger.error(UserDataHandler.class.getName(), "Failed to set user info: " + e.getMessage());
        }
    }
}
