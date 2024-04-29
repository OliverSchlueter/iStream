package de.itbw18.istream.cmd;

import de.itbw18.istream.helpers.database.SQLConnector;
import de.itbw18.istream.helpers.database.SQLiteConnector;
import de.itbw18.istream.server.HttpServer;
import de.itbw18.istream.user.UserAccessHandler;
import de.itbw18.istream.user.UserHandler;
import de.itbw18.istream.user.store.UserStoreImpl;
import de.itbw18.istream.user.store.database.UserDatabase_MySql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Backend {

    public static final Logger LOGGER = LoggerFactory.getLogger(Backend.class);

    private HttpServer httpServer;
    private SQLConnector sqlConnector;


    public void init() {
//        sqlConnector = new MySqlConnector("localhost", 3306, "root", "");
        sqlConnector = new SQLiteConnector("db.sqlite");


        UserStoreImpl userStore = new UserStoreImpl(new UserDatabase_MySql(sqlConnector));
        UserHandler userHandler = new UserHandler(userStore);

        UserAccessHandler userAccessHandler = new UserAccessHandler(userStore);

        httpServer = new HttpServer(userAccessHandler, userHandler);
    }

    public void start() {
        if (!sqlConnector.connect()) {
            System.out.println("Failed to connect to database");
            return;
        }
        httpServer.start(8080);
    }
}
