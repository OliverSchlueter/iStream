package de.itbw18.istream.user;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class UserHandler implements CrudHandler {

    @Override
    public void create(@NotNull Context context) {

    }

    @Override
    public void getAll(@NotNull Context context) {
        context.result("get all users");
    }

    @Override
    public void getOne(@NotNull Context context, @NotNull String id) {
        context.result("get user with id " + id);
    }

    @Override
    public void update(@NotNull Context context, @NotNull String id) {

    }

    @Override
    public void delete(@NotNull Context context, @NotNull String id) {

    }
}
