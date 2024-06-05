package de.itbw18.istream.user;

import com.google.gson.Gson;
import de.itbw18.istream.cmd.e2e.BackendE2E;
import de.itbw18.istream.server.HttpServer;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserHandlerTest {

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
            UserHandler.RegisterRequest registerRequest = new UserHandler.RegisterRequest("user1", "user1@istream.com", "My_cool.password123");
            Response resp = client.post("/api/users", registerRequest);

            assert resp.code() == HttpStatus.CREATED.getCode();

            User got = backend.getUserStore().getUserByUsername("user1");
            assert got != null;
            assert got.username().equals(registerRequest.username());
            assert got.email().equals(registerRequest.email());
            assert got.password().equals(registerRequest.password());
        });
    }

    @Test
    void create_invalidRequest() {
        JavalinTest.test(app, (server, client) -> {
            UserHandler.RegisterRequest registerRequest = new UserHandler.RegisterRequest(null, "user1@istream.com", "My_cool.password123");
            Response resp = client.post("/api/users", registerRequest);

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Invalid request body");
        });
    }

    @Test
    void create_invalidRequest2() {
        JavalinTest.test(app, (server, client) -> {
            UserHandler.RegisterRequest registerRequest = new UserHandler.RegisterRequest("user", null, "My_cool.password123");
            Response resp = client.post("/api/users", registerRequest);

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Invalid request body");
        });
    }

    @Test
    void create_invalidRequest3() {
        JavalinTest.test(app, (server, client) -> {
            UserHandler.RegisterRequest registerRequest = new UserHandler.RegisterRequest("user", "user1@istream.com", null);
            Response resp = client.post("/api/users", registerRequest);

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Invalid request body");
        });
    }

    @Test
    void create_invalidRequest4() {
        JavalinTest.test(app, (server, client) -> {
            Response resp = client.post("/api/users");

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Invalid request body");
        });
    }

    @Test
    void create_passwordLength() {
        JavalinTest.test(app, (server, client) -> {
            UserHandler.RegisterRequest registerRequest = new UserHandler.RegisterRequest("user1", "user1@istream.com", "short-pw");
            Response resp = client.post("/api/users", registerRequest);

            assert resp.code() == HttpStatus.BAD_REQUEST.getCode();
            assert resp.body().string().equals("Password must be at least 12 characters long");
        });
    }

    @Test
    void getAll() {
        JavalinTest.test(app, (server, client) -> {
            User user1 = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            User user2 = new User(UUID.randomUUID().toString(), "user2", "user2@istream.com", "user2_pw", System.currentTimeMillis() - 1000, new String[0], new String[0]);
            User user3 = new User(UUID.randomUUID().toString(), "user3", "user3@istream.com", "user3_pw", System.currentTimeMillis() - 3000, new String[0], new String[0]);

            backend.getUserStore().addUser(user1);
            backend.getUserStore().addUser(user2);
            backend.getUserStore().addUser(user3);

            Response resp = client.get("/api/users");

            assert resp.code() == HttpStatus.OK.getCode();

            User[] got = gson.fromJson(resp.body().string(), User[].class);
            assert got.length == 3;
            assert got[0].username().equals("user1");
            assert got[1].email().equals("user2@istream.com");
            assert got[2].password().equals("user3_pw");
            assert got[0].createdAt() == user1.createdAt();
        });
    }

    @Test
    void getOneById() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            Response resp = client.get("/api/users/" + user.id());
            assert resp.code() == HttpStatus.OK.getCode();

            User got = gson.fromJson(resp.body().string(), User.class);
            assert got.username().equals(user.username());
            assert got.email().equals(user.email());
            assert got.password().equals(user.password());
            assert got.createdAt() == user.createdAt();
        });
    }

    @Test
    void getOneByUsername() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User("", "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            Response resp = client.get("/api/users/user1");
            assert resp.code() == HttpStatus.OK.getCode();

            User got = gson.fromJson(resp.body().string(), User.class);
            assert got.username().equals(user.username());
            assert got.email().equals(user.email());
            assert got.password().equals(user.password());
            assert got.createdAt() == user.createdAt();
        });
    }

    @Test
    void getOneByEmail() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User("", "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            Response resp = client.get("/api/users/user1@istream.com");
            assert resp.code() == HttpStatus.OK.getCode();

            User got = gson.fromJson(resp.body().string(), User.class);
            assert got.username().equals(user.username());
            assert got.email().equals(user.email());
            assert got.password().equals(user.password());
            assert got.createdAt() == user.createdAt();
        });
    }

    @Test
    void getOne_notFound() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            Response resp = client.get("/api/users/user2");
            assert resp.code() == HttpStatus.NOT_FOUND.getCode();
            assert resp.body().string().equals("User not found");
        });
    }

    @Test
    void update() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            UserHandler.RegisterRequest updateRequest = new UserHandler.RegisterRequest("user1", "user1@istream.com", "new_very_long_password");

            Response resp = client.patch("/api/users/" + user.id(), updateRequest, builder -> {
                builder.addHeader("username", "user1");
                builder.addHeader("password", "user1_pw");
            });

            assert resp.code() == HttpStatus.OK.getCode();

            User got = backend.getUserStore().getUserByID(user.id());
            assert got.password().equals(updateRequest.password());
        });
    }

    @Test
    void update_unauthenticated() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            UserHandler.RegisterRequest updateRequest = new UserHandler.RegisterRequest("user1", "user1@istream.com", "new_very_long_password");

            Response resp = client.patch("/api/users/" + user.id(), updateRequest, builder -> {
                builder.addHeader("username", "wrong_user");
                builder.addHeader("password", "wrong_pw");
            });

            assert resp.code() == HttpStatus.UNAUTHORIZED.getCode();
        });
    }

    @Test
    void delete() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            Response resp = client.delete("/api/users/" + user.id(), null, builder -> {
                builder.addHeader("username", "user1");
                builder.addHeader("password", "user1_pw");
            });

            assert resp.code() == HttpStatus.OK.getCode();

            User got = backend.getUserStore().getUserByID(user.id());
            assert got == null;
        });
    }

    @Test
    void delete_unauthenticated() {
        JavalinTest.test(app, (server, client) -> {
            User user = new User(UUID.randomUUID().toString(), "user1", "user1@istream.com", "user1_pw", System.currentTimeMillis(), new String[0], new String[0]);
            backend.getUserStore().addUser(user);

            Response resp = client.delete("/api/users/" + user.id(), null, builder -> {
                builder.addHeader("username", "wrong_user");
                builder.addHeader("password", "wrong_pw");
            });

            assert resp.code() == HttpStatus.UNAUTHORIZED.getCode();
        });
    }
}