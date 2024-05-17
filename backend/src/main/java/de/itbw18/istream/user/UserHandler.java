package de.itbw18.istream.user;

import de.itbw18.istream.user.store.UserStore;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserHandler implements CrudHandler {

    private final UserStore userStore;

    public UserHandler(UserStore userStore) {
        this.userStore = userStore;
    }

    public void handleExtra() {
        io.javalin.apibuilder.ApiBuilder.get("/validate-user", this::validate);
    }


    @Override
    public void create(@NotNull Context context) {
        RegisterRequest registerRequest = context.bodyAsClass(RegisterRequest.class);
        if (registerRequest == null) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Invalid request body");
            return;
        }

        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Invalid request body");
            return;
        }

        if (userStore.getUserByUsername(registerRequest.username()) != null || userStore.getUserByEmail(registerRequest.email()) != null) {
            context.status(HttpStatus.CONFLICT);
            context.result("User already exists");
            return;
        }

        if (registerRequest.password().length() < 12) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Password must be at least 12 characters long");
            return;
        }

        User user = new User(
                UUID.randomUUID().toString(),
                registerRequest.username(),
                registerRequest.email(),
                registerRequest.password(),
                System.currentTimeMillis(),
                new String[0],
                new String[0]
        );

        if (!userStore.addUser(user)) {
            context.status(HttpStatus.INTERNAL_SERVER_ERROR);
            context.result("Failed to create user");
            return;
        }

        context.status(HttpStatus.CREATED);
    }

    @Override
    public void getAll(@NotNull Context context) {
        context.json(userStore.getUsers());
    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String id) {
        User user = userStore.getUserByID(id);
        if (user == null) {
            user = userStore.getUserByUsername(id);
        }

        if (user == null) {
            user = userStore.getUserByEmail(id);
        }

        if (user == null) {
            context.status(HttpStatus.NOT_FOUND);
            context.result("User not found");
            return;
        }

        context.json(user);
    }

    @Override
    public void update(@NotNull Context context, @NotNull String id) {
        User user = context.attribute("user");
        if (user == null || !user.id().equals(id)) {
            context.status(HttpStatus.UNAUTHORIZED);
            context.result("Unauthorized");
            return;
        }

        RegisterRequest registerRequest = context.bodyAsClass(RegisterRequest.class);
        if (registerRequest == null) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Invalid request body");
            return;
        }

        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Invalid request body");
            return;
        }

        if (registerRequest.password().length() < 12) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Password must be at least 12 characters long");
            return;
        }

        User updatedUser = new User(
                user.id(),
                registerRequest.username(),
                registerRequest.email(),
                registerRequest.password(),
                user.createdAt(),
                user.following(),
                user.followers()
        );

        if (!userStore.updateUser(updatedUser)) {
            context.status(HttpStatus.INTERNAL_SERVER_ERROR);
            context.result("Failed to update user");
            return;
        }
    }

    @Override
    public void delete(@NotNull Context context, @NotNull String id) {
        User user = context.attribute("user");
        if (user == null || !user.id().equals(id)) {
            context.status(HttpStatus.UNAUTHORIZED);
            context.result("Unauthorized");
            return;
        }

        if (!userStore.deleteUser(user)) {
            context.status(HttpStatus.INTERNAL_SERVER_ERROR);
            context.result("Failed to delete user");
            return;
        }

        context.status(HttpStatus.OK);
    }

    public void validate(Context context) {
        User user = context.attribute("user");
        if (user == null) {
            context.status(HttpStatus.UNAUTHORIZED);
            context.result("Unauthorized");
            return;
        }

        context.status(HttpStatus.OK);
    }

    public record RegisterRequest(String username, String email, String password) {
    }
}
