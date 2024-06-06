package de.itbw18.istream.streamconfig;

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

class StreamConfigHandlerTest {

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

            StreamConfigHandler.CreateOrUpdateStreamConfigRequest createRequest = new StreamConfigHandler.CreateOrUpdateStreamConfigRequest("Stream Title", "My stream description", "JUST_CHATTING");
            Response resp = client.post("/api/stream-configs", createRequest, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.CREATED.getCode();

            StreamConfig got = backend.getStreamConfigStore().getStreamConfig(user.id());
            assert got != null;
            assert got.title().equals(createRequest.title());
            assert got.description().equals(createRequest.description());
            assert got.category().equals(StreamConfig.Category.valueOf(createRequest.category()));
        });
    }

    @Test
    void create_titleTooLong() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            StreamConfigHandler.CreateOrUpdateStreamConfigRequest createRequest = new StreamConfigHandler.CreateOrUpdateStreamConfigRequest(
                    "[!] This stream title is way tooooooooooooooooo long!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
                    "My stream description",
                    "JUST_CHATTING"
            );

            Response resp = client.post("/api/stream-configs", createRequest, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Title is too long");
        });
    }

    @Test
    void create_invalidCategory() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            StreamConfigHandler.CreateOrUpdateStreamConfigRequest createRequest = new StreamConfigHandler.CreateOrUpdateStreamConfigRequest(
                    "My stream title",
                    "My stream description",
                    "NOT_EXISTING_CATEGORY"
            );

            Response resp = client.post("/api/stream-configs", createRequest, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Invalid category");
        });
    }

    @Test
    void create_invalidRequestBody() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            Response resp = client.post("/api/stream-configs", null, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Invalid request body");
        });
    }

    @Test
    void create_unauthorized() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            StreamConfigHandler.CreateOrUpdateStreamConfigRequest createRequest = new StreamConfigHandler.CreateOrUpdateStreamConfigRequest("Stream Title", "My stream description", "JUST_CHATTING");
            Response resp = client.post("/api/stream-configs", createRequest, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", "wrong password");
            });

            assert resp.code() == HttpStatus.UNAUTHORIZED.getCode();
        });
    }

    @Test
    void getOne() {
        JavalinTest.test(app, (server, client) -> {
            StreamConfig exp = new StreamConfig("user1", "Stream Title", "My stream description", StreamConfig.Category.JUST_CHATTING);
            backend.getStreamConfigStore().createStreamConfig(exp);

            Response resp = client.get("/api/stream-configs/user1");

            assert resp.code() == HttpStatus.OK.getCode();
            StreamConfig got = gson.fromJson(resp.body().string(), StreamConfig.class);
            assert got.userId().equals(exp.userId());
            assert got.title().equals(exp.title());
            assert got.description().equals(exp.description());
            assert got.category().equals(exp.category());
        });
    }

    @Test
    void getOne_notFound() {
        JavalinTest.test(app, (server, client) -> {
            Response resp = client.get("/api/stream-configs/user123");

            assert resp.code() == HttpStatus.NOT_FOUND.getCode();
        });
    }

    @Test
    void update() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            backend.getStreamConfigStore().createStreamConfig(new StreamConfig(user.id(), "Old stream title", "Old description", StreamConfig.Category.JUST_CHATTING));

            StreamConfigHandler.CreateOrUpdateStreamConfigRequest updateRequest = new StreamConfigHandler.CreateOrUpdateStreamConfigRequest("My new stream title", "This is my new description", "MUSIC");
            Response resp = client.patch("/api/stream-configs/" + user.id(), updateRequest, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", user.password());
            });

            assert resp.code() == HttpStatus.OK.getCode();

            StreamConfig got = backend.getStreamConfigStore().getStreamConfig(user.id());
            assert got != null;
            assert got.title().equals(updateRequest.title());
            assert got.description().equals(updateRequest.description());
            assert got.category().equals(StreamConfig.Category.valueOf(updateRequest.category()));
        });
    }

    @Test
    void delete() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            backend.getStreamConfigStore().createStreamConfig(new StreamConfig(user.id(), "Stream title", "My description", StreamConfig.Category.JUST_CHATTING));

            Response resp = client.delete("/api/stream-configs/" + user.id(), null, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", user.password());
            });


            assert resp.code() == HttpStatus.OK.getCode();

            StreamConfig got = backend.getStreamConfigStore().getStreamConfig(user.id());
            assert got == null;
        });
    }

    @Test
    void delete_forbidden() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "password1", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            backend.getStreamConfigStore().createStreamConfig(new StreamConfig("user2", "Stream title", "My description", StreamConfig.Category.JUST_CHATTING));

            Response resp = client.delete("/api/stream-configs/user2", null, builder -> {
                builder.addHeader("username", user.username())
                        .addHeader("password", user.password());
            });


            assert resp.code() == HttpStatus.FORBIDDEN.getCode();

            StreamConfig got = backend.getStreamConfigStore().getStreamConfig("user2");
            assert got != null;
        });
    }
}