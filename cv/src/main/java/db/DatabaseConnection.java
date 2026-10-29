package db;

import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:h2:./data/shop;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Server webServer;
    private static boolean initialized = false;

    public static synchronized Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (!initialized) {
            initSchema();
            initDataIfEmpty(conn);
            initialized = true;
        }
        return conn;
    }

    public static void startWebConsole() {
        try {
            if (webServer == null) {
                webServer = Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start();
                System.out.println("[H2] Webová konzole běží na: " + webServer.getURL());
                System.out.println("[H2] JDBC URL: " + URL + " (user: sa, password: prázdné)");
            }
        } catch (SQLException e) {
            System.err.println("[H2] Nepodařilo se spustit webovou konzoli: " + e.getMessage());
        }
    }

    public static void stopWebConsole() {
        if (webServer != null) {
            webServer.stop();
            webServer = null;
        }
    }

    public static void initSchema() {
        runScript("schema.sql");
    }

    public static void initData() {
        runScript("data.sql");
    }

    private static void initDataIfEmpty(Connection conn) {
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM items")) {
            if (rs.next() && rs.getInt(1) == 0) {
                initData();
            }
        } catch (SQLException ignored) {
        }
    }

    private static void runScript(String resourceName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             InputStream in = DatabaseConnection.class
                     .getClassLoader()
                     .getResourceAsStream(resourceName)) {

            if (in == null) {
                System.out.println("[DB] Skript " + resourceName + " nebyl nalezen na classpath, přeskakuji.");
                return;
            }

            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            statement.execute(sql);

        } catch (SQLException | IOException e) {
            throw new RuntimeException("Nepodařilo se spustit skript " + resourceName, e);
        }
    }
}