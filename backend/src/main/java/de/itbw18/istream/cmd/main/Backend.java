package de.itbw18.istream.cmd.main;

import de.itbw18.istream.helpers.database.SQLConnector;
import de.itbw18.istream.helpers.database.SQLiteConnector;
import de.itbw18.istream.server.HttpServer;
import de.itbw18.istream.stream.StreamHandler;
import de.itbw18.istream.stream.live.LiveHandler;
import de.itbw18.istream.stream.store.StreamStore;
import de.itbw18.istream.stream.store.StreamStoreImpl;
import de.itbw18.istream.streamconfig.StreamConfigHandler;
import de.itbw18.istream.streamconfig.store.StreamConfigStore;
import de.itbw18.istream.streamconfig.store.StreamConfigStoreImpl;
import de.itbw18.istream.streamconfig.store.database.StreamConfigDatabase_MySql;
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

        if (!sqlConnector.connect()) {
            LOGGER.error("Failed to connect to database");
            return;
        }

        UserStoreImpl userStore = new UserStoreImpl(new UserDatabase_MySql(sqlConnector));
        userStore.setup();
        UserHandler userHandler = new UserHandler(userStore);

        UserAccessHandler userAccessHandler = new UserAccessHandler(userStore);

        StreamConfigStore streamConfigStore = new StreamConfigStoreImpl(new StreamConfigDatabase_MySql(sqlConnector));
        streamConfigStore.setup();
        StreamConfigHandler streamConfigHandler = new StreamConfigHandler(streamConfigStore, userAccessHandler);

        StreamStore streamStore = new StreamStoreImpl();
        StreamHandler streamHandler = new StreamHandler(streamStore, userStore, userAccessHandler);

        LiveHandler liveHandler = new LiveHandler(userAccessHandler, streamStore);

        httpServer = new HttpServer(userAccessHandler, userHandler, streamConfigHandler, streamHandler, liveHandler);
    }

    public void start() {
        if (!sqlConnector.connect()) {
            System.out.println("Failed to connect to database");
            return;
        }
        httpServer.start(8080);
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }
}
