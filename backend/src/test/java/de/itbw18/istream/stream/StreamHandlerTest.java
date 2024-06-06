package de.itbw18.istream.stream;

import com.google.gson.Gson;
import de.itbw18.istream.cmd.e2e.BackendE2E;
import de.itbw18.istream.server.HttpServer;
import de.itbw18.istream.user.User;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class StreamHandlerTest {

    static Gson gson = new Gson();

    BackendE2E backend;
    Javalin app;

    @BeforeEach
    void setUp() {
        backend = new BackendE2E();
        backend.init();

        HttpServer httpServer = backend.getHttpServer();
        app = httpServer.getApp();
    }

    @Test
    void create() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            StreamHandler.CreateStreamRequest createRequest = new StreamHandler.CreateStreamRequest(user.id());
            Response resp = client.post("/api/streams", createRequest, builder -> {
                builder.addHeader("username", user.username());
                builder.addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.CREATED.getCode();

            Stream got = backend.getStreamStore().getStream(user);
            assert got != null;
            assert got.amountViewers() == 0;
            assert got.liveSince() > user.createdAt() - 1000 && got.liveSince() < user.createdAt() + 1000;
        });
    }

    @Test
    void create_other() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            StreamHandler.CreateStreamRequest createRequest = new StreamHandler.CreateStreamRequest("user2");
            Response resp = client.post("/api/streams", createRequest, builder -> {
                builder.addHeader("username", user.username());
                builder.addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.FORBIDDEN.getCode();
        });
    }

    @Test
    void create_unauthorized() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            StreamHandler.CreateStreamRequest createRequest = new StreamHandler.CreateStreamRequest(user.id());
            Response resp = client.post("/api/streams", createRequest, builder -> {
                builder.addHeader("username", "wrong user");
                builder.addHeader("password", "wrong password");
            });

            assert resp.code() == HttpStatus.UNAUTHORIZED.getCode();
        });
    }

    @Test
    void getAll() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);
            backend.getStreamStore().startStream(user);

            User user2 = new User(UUID.randomUUID().toString(), "user2", "user2@istream.com", "password2", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user2);
            backend.getStreamStore().startStream(user2);

            Response resp = client.get("/api/streams");

            assert resp.code() == HttpStatus.OK.getCode();
            Stream[] streams = gson.fromJson(resp.body().string(), Stream[].class);
            assert streams.length == 2;
            assert streams[0].streamer().equals(user.id()) || streams[0].streamer().equals(user2.id());
            assert streams[1].streamer().equals(user.id()) || streams[1].streamer().equals(user2.id());
        });
    }

    @Test
    void getOne() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);
            backend.getStreamStore().startStream(user);

            Response resp = client.get("/api/streams/" + user.id());

            assert resp.code() == HttpStatus.OK.getCode();
            Stream got = gson.fromJson(resp.body().string(), Stream.class);
            assert got.streamer().equals(user.id());
        });
    }

    @Test
    void getOne_notFound() {
        JavalinTest.test(app, (server, client) -> {
            Response resp = client.get("/api/streams/someUser");

            assert resp.code() == HttpStatus.NOT_FOUND.getCode();
        });
    }

    @Test
    void delete() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            backend.getStreamStore().startStream(user);

            Response resp = client.delete("/api/streams/" + user.id(), null, builder -> {
                builder.addHeader("username", user.username());
                builder.addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.OK.getCode();

            Stream got = backend.getStreamStore().getStream(user);
            assert got == null;
        });
    }

    @Test
    void delete_other() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            backend.getStreamStore().startStream(user);

            Response resp = client.delete("/api/streams/otherUser", null, builder -> {
                builder.addHeader("username", user.username());
                builder.addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.FORBIDDEN.getCode();
            assert resp.body().string().equals("You can only stop your own stream");
        });
    }
}