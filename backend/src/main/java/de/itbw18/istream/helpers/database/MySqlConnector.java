package de.itbw18.istream.helpers.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySqlConnector implements SQLConnector {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private Connection connection;

    public MySqlConnector(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * Connects to the database.
     *
     * @return true if the connection was successful, false otherwise
     */
    @Override
    public boolean connect() {
        final String url = "jdbc:mysql://" + host + ":" + port + "/";
        try {
            connection = DriverManager.getConnection(url, username, password);
            Logger.getGlobal().info("Connected to database");
        } catch (SQLException e) {
            Logger.getGlobal().warning("Could not connect to database: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Closes the database connection.
     *
     * @return true if the connection was closed successfully, false otherwise
     */
    @Override
    public boolean close() {
        try {
            connection.close();
            connection = null;
            Logger.getGlobal().info("Closed database connection");
        } catch (SQLException e) {
            Logger.getGlobal().warning("Could not close database connection: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Checks if the database connection is established.
     *
     * @return true if the connection is established, false otherwise
     */
    @Override
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Returns the database connection.
     *
     * @return the database connection
     */
    @Override
    public Connection getConnection() {
        return connection;
    }
}
