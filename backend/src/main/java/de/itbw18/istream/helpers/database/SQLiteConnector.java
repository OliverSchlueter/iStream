package de.itbw18.istream.helpers.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteConnector implements SQLConnector {

    private final String databasePath;
    private Connection connection;

    public SQLiteConnector(String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * Connects to the SQLite database.
     *
     * @return true if the connection was successful, false otherwise
     */
    @Override
    public boolean connect() {
        File databaseFile = new File(databasePath);
        if (!databaseFile.exists()) {
            Logger.getGlobal().info("Database file does not exist. Creating a new one...");
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
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
            if (connection != null) {
                connection.close();
                connection = null;
                Logger.getGlobal().info("Closed database connection");
            }
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