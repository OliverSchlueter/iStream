package de.itbw18.istream.cmd.e2e;

import de.itbw18.istream.server.HttpServer;
import de.itbw18.istream.stream.StreamHandler;
import de.itbw18.istream.stream.live.LiveHandler;
import de.itbw18.istream.stream.store.StreamStore;
import de.itbw18.istream.stream.store.StreamStoreImpl;
import de.itbw18.istream.streamconfig.StreamConfigHandler;
import de.itbw18.istream.streamconfig.store.StreamConfigStore;
import de.itbw18.istream.streamconfig.store.StreamConfigStoreImpl;
import de.itbw18.istream.streamconfig.store.database.StreamConfigDatabase_Fake;
import de.itbw18.istream.user.UserAccessHandler;
import de.itbw18.istream.user.UserHandler;
import de.itbw18.istream.user.store.UserStore;
import de.itbw18.istream.user.store.UserStoreImpl;
import de.itbw18.istream.user.store.database.UserDatabase_Fake;

public class BackendE2E {

    private HttpServer httpServer;
    private UserStore userStore;
    private StreamConfigStore streamConfigStore;
    private StreamStore streamStore;

    public void init() {
        userStore = new UserStoreImpl(new UserDatabase_Fake());
        userStore.setup();
        UserHandler userHandler = new UserHandler(userStore);

        UserAccessHandler userAccessHandler = new UserAccessHandler(userStore);

        streamConfigStore = new StreamConfigStoreImpl(new StreamConfigDatabase_Fake());
        streamConfigStore.setup();
        StreamConfigHandler streamConfigHandler = new StreamConfigHandler(streamConfigStore, userAccessHandler);

        streamStore = new StreamStoreImpl();
        StreamHandler streamHandler = new StreamHandler(streamStore, userStore);

        LiveHandler liveHandler = new LiveHandler(userAccessHandler, streamStore);

        httpServer = new HttpServer(userAccessHandler, userHandler, streamConfigHandler, streamHandler, liveHandler);
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public UserStore getUserStore() {
        return userStore;
    }

    public StreamConfigStore getStreamConfigStore() {
        return streamConfigStore;
    }

    public StreamStore getStreamStore() {
        return streamStore;
    }
}
