package xyz.mythicalsystems.mythicallogin.MySQL;

import org.json.JSONObject;

import com.zaxxer.hikari.HikariDataSource;

import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;
import xyz.mythicalsystems.mythicallogin.Config.Config;
import xyz.mythicalsystems.mythicallogin.migrationm.MigrationM;
import xyz.mythicalsystems.mythicallogin.migrationm.databasemanager.sql.SQLDatabaseManager;
import xyz.mythicalsystems.mythicallogin.migrationm.util.ProgramInfo;

public class MySQLConnector {
    private HikariDataSource hikari;

    public MySQLConnector(String host, String port, String database, String username, String password) {
        hikari = new HikariDataSource();
        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikari.setUsername(username);
        hikari.setPassword(password);
        hikari.setMaximumPoolSize(10);
        hikari.setMinimumIdle(5);
        hikari.setConnectionTimeout(30000);
        hikari.setIdleTimeout(600000);
        hikari.setMaxLifetime(1800000000);
        hikari.setLeakDetectionThreshold(1800000000);
    }

    @SuppressWarnings("null")
    public static void checkLicense() {
        String licenseString = Config.getSetting().getString("PinLogin.LicenseKey");
        if (licenseString == null || licenseString.isEmpty()) {
            Main.logger.error(Main.class.getName(), "License is not set, stopping the plugin");
            MinecraftPlugin.getInstance().getProxy().stop();
        }
        if (licenseString.length() != 36) {
            Main.logger.error(Main.class.getName(), "Invalid license key length, stopping the plugin");
            MinecraftPlugin.getInstance().getProxy().stop();
        }
        String url = "https://api.mythical.systems/mythicallogin/license/" + licenseString + "/info";

        try {
            java.net.InetAddress address = java.net.InetAddress.getByName(new java.net.URL(url).getHost());
            if (!address.isReachable(5000)) {
                Main.logger.error(Main.class.getName(), "License server is offline, still allowing plugin to start");
            }
        } catch (Exception e) {
            Main.logger.error(Main.class.getName(),
                    "Failed to check if license server is reachable: " + e.getMessage());
        }

        int maxRetries = 3;
        int attempts = 0;
        boolean licenseValid = false;

        while (attempts < maxRetries && !licenseValid) {
            try {
                java.net.URL urlObj = new java.net.URL(url);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }

                conn.disconnect();

                JSONObject jsonResponse = new JSONObject(sb.toString());
                boolean success = jsonResponse.getBoolean("success");
                if (!success) {
                    throw new RuntimeException("License check failed: " + jsonResponse.getString("message"));
                }

                licenseValid = true;
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) {
                    Main.logger.error(Main.class.getName(),
                            "Failed to check license after " + maxRetries + " attempts: " + e.getMessage());
                    MinecraftPlugin.getInstance().getProxy().stop();
                } else {
                    Main.logger.warn(Main.class.getName(),
                            "License check attempt " + attempts + " failed: " + e.getMessage());
                }
            }
        }
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void close() {
        hikari.close();
    }

    public void reconnect() {
        hikari.close();
        hikari = new HikariDataSource(hikari);
    }

    public void tryConnection() {
        try {

            hikari.getConnection().close();
            Main.logger.info("MySQLConnector", "Connected to MySQL server");
            checkLicense();
            MigrationM migrationM = new MigrationM(new ProgramInfo(MinecraftPlugin.PLUGIN_NAME,
                    MinecraftPlugin.PLUGIN_VERSION, MinecraftPlugin.getInstance().getLogger()),
                    new SQLDatabaseManager(hikari));
            migrationM.loadMigrations("migrations", MinecraftPlugin.class);
            boolean success = migrationM.migrate();
            if (success) {
                Main.logger.info("MySQLConnector", "Migrations were successful");
            } else {
                Main.logger.error("MySQLConnector", "Migrations failed");
                MinecraftPlugin.getInstance().getProxy().stop();
            }
            hikari.getConnection().close();

        } catch (Exception e) {
            Main.logger.error("MySQLConnector", "Failed to connect to MySQL server: " + e.getMessage());
            MinecraftPlugin.getInstance().getProxy().stop();
        }
    }
}