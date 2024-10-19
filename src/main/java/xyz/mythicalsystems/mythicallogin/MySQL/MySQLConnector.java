package xyz.mythicalsystems.mythicallogin.MySQL;
import com.zaxxer.hikari.HikariDataSource;

import xyz.mythicalsystems.mythicallogin.Main;
import xyz.mythicalsystems.mythicallogin.MinecraftPlugin;
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

            MigrationM migrationM = new MigrationM(new ProgramInfo(MinecraftPlugin.PLUGIN_NAME, MinecraftPlugin.PLUGIN_VERSION, MinecraftPlugin.getInstance().getLogger()), new SQLDatabaseManager(hikari));
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