package de.itbw18.istream.user;

import de.itbw18.istream.user.store.UserStore;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.websocket.WsContext;

public class UserAccessHandler {

    private final UserStore userStore;

    public UserAccessHandler(UserStore userStore) {
        this.userStore = userStore;
    }

    public void authenticate(Context context) {
        String username = context.header("username");
        String password = context.header("password");

        User user = userStore.getUserByUsername(username);
        if (user != null && user.password().equals(password)) {
            context.attribute("user", user);
        }
    }

    public void authorize(Context context) {
        User user = context.attribute("user");
        if (user == null) {
            context.status(HttpStatus.UNAUTHORIZED);
            context.result("Unauthorized");
        }
    }

    public void authenticateWebSocket(WsContext context) {
        String username = context.queryParam("username");
        String password = context.queryParam("password");
        if (username == null || password == null) {
            return;
        }

        User user = userStore.getUserByUsername(username);
        if (user != null && user.password().equals(password)) {
            context.attribute("user", user);
        }
    }
}
