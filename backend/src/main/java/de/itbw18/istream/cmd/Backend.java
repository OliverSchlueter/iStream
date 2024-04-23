package de.itbw18.istream.cmd;

import de.itbw18.istream.helpers.database.MySqlConnector;
import de.itbw18.istream.helpers.database.SQLConnector;
import de.itbw18.istream.server.HttpServer;

import java.util.logging.Logger;

public class Backend {

    private HttpServer httpServer;
    private SQLConnector sqlConnector;
    private Logger logger;

    public void init() {
        sqlConnector = new MySqlConnector("localhost", 3306, "root", "");
        httpServer = new HttpServer();
    }

    public void start() {
        if (!sqlConnector.connect()) {
            System.out.println("Failed to connect to database");
            return;
        }
        httpServer.start(8080);
    }

    public Logger getLogger() {
        return logger;
    }
}
