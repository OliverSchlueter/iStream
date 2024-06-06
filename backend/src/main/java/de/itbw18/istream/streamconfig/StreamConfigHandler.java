package de.itbw18.istream.streamconfig;

import de.itbw18.istream.streamconfig.store.StreamConfigStore;
import de.itbw18.istream.user.User;
import de.itbw18.istream.user.UserAccessHandler;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiRequestBody;
import org.jetbrains.annotations.NotNull;

public class StreamConfigHandler implements CrudHandler {

    private final StreamConfigStore streamConfigStore;
    private final UserAccessHandler userAccessHandler;

    public StreamConfigHandler(StreamConfigStore streamConfigStore, UserAccessHandler userAccessHandler) {
        this.streamConfigStore = streamConfigStore;
        this.userAccessHandler = userAccessHandler;
    }

    @OpenApi(
            path = "/stream-config",
            methods = HttpMethod.POST,
            summary = "Create a stream config",
            tags = {"StreamConfig"},
            requestBody = @OpenApiRequestBody(
                    required = true,
                    content = @OpenApiContent(from = CreateOrUpdateStreamConfigRequest.class)
            )
    )
    @Override
    public void create(@NotNull Context context) {
        User user = context.attribute("user");
        userAccessHandler.authorize(context);

        CreateOrUpdateStreamConfigRequest request = context.bodyAsClass(CreateOrUpdateStreamConfigRequest.class);
        if (request == null) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Invalid request body");
            return;
        }

        StreamConfig streamConfig;
        try {
            streamConfig = request.validate(user.id());
        } catch (IllegalArgumentException e) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result(e.getMessage());
            return;
        }

        streamConfigStore.createStreamConfig(streamConfig);
        context.status(HttpStatus.CREATED);
    }

    @Override
    public void getAll(@NotNull Context context) {
        context.status(HttpStatus.NOT_FOUND);
    }

    @OpenApi(
            path = "/stream-config/:id",
            methods = HttpMethod.GET,
            summary = "Get a stream config",
            tags = {"StreamConfig"}
    )
    @Override
    public void getOne(@NotNull Context context, @NotNull String id) {
        StreamConfig streamConfig = streamConfigStore.getStreamConfig(id);
        if (streamConfig == null) {
            context.status(HttpStatus.NOT_FOUND);
            return;
        }

        context.json(streamConfig);
    }

    @OpenApi(
            path = "/stream-config/:id",
            methods = HttpMethod.PATCH,
            summary = "Update a stream config",
            tags = {"StreamConfig"},
            requestBody = @OpenApiRequestBody(
                    required = true,
                    content = @OpenApiContent(from = CreateOrUpdateStreamConfigRequest.class)
            )
    )
    @Override
    public void update(@NotNull Context context, @NotNull String id) {
        User user = context.attribute("user");

        StreamConfig streamConfig = streamConfigStore.getStreamConfig(id);
        if (streamConfig == null) {
            context.status(HttpStatus.NOT_FOUND);
            return;
        }

        if (!streamConfig.userId().equals(user.id())) {
            context.status(HttpStatus.FORBIDDEN);
            context.result("You are not allowed to update this stream config");
            return;
        }

        CreateOrUpdateStreamConfigRequest request = context.bodyAsClass(CreateOrUpdateStreamConfigRequest.class);
        if (request == null) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Invalid request body");
            return;
        }

        StreamConfig updatedStreamConfig;
        try {
            updatedStreamConfig = request.validate(user.id());
        } catch (IllegalArgumentException e) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result(e.getMessage());
            return;
        }

        streamConfigStore.updateStreamConfig(updatedStreamConfig);
    }

    @OpenApi(
            path = "/stream-config/:id",
            methods = HttpMethod.DELETE,
            summary = "Delete a stream config",
            tags = {"StreamConfig"}
    )
    @Override
    public void delete(@NotNull Context context, @NotNull String id) {
        User user = context.attribute("user");

        StreamConfig streamConfig = streamConfigStore.getStreamConfig(id);
        if (streamConfig == null) {
            context.status(HttpStatus.NOT_FOUND);
            return;
        }

        if (!streamConfig.userId().equals(user.id())) {
            context.status(HttpStatus.FORBIDDEN);
            context.result("You are not allowed to delete this stream config");
            return;
        }

        streamConfigStore.deleteStreamConfig(id);
    }

    record CreateOrUpdateStreamConfigRequest(String title, String description, String category) {

        StreamConfig validate(String userId) {
            StreamConfig.Category category;
            try {
                category = StreamConfig.Category.valueOf(this.category());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category");
            }

            if (title().length() > 150) {
                throw new IllegalArgumentException("Title is too long");
            }

            if (description().length() > 500) {
                throw new IllegalArgumentException("Description is too long");
            }

            return new StreamConfig(userId, title(), description(), category);
        }

    }
}
