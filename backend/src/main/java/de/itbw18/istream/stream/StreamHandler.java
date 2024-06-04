package de.itbw18.istream.stream;

import de.itbw18.istream.stream.store.StreamStore;
import de.itbw18.istream.user.User;
import de.itbw18.istream.user.store.UserStore;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiRequestBody;
import org.jetbrains.annotations.NotNull;

public class StreamHandler implements CrudHandler {

    private final StreamStore streamStore;
    private final UserStore userStore;

    public StreamHandler(StreamStore streamStore, UserStore userStore) {
        this.streamStore = streamStore;
        this.userStore = userStore;
    }

    @OpenApi(
            path = "/streams",
            methods = HttpMethod.POST,
            summary = "Start a stream",
            tags = {"Stream"},
            requestBody = @OpenApiRequestBody(
                    required = true,
                    content = @OpenApiContent(from = CreateStreamRequest.class)
            )
    )
    @Override
    public void create(@NotNull Context context) {
        User user = context.attribute("user");

        CreateStreamRequest createStreamRequest = context.bodyAsClass(CreateStreamRequest.class);
        if (createStreamRequest == null) {
            context.status(HttpStatus.BAD_REQUEST);
            context.result("Invalid request");
            return;
        }

        if (!createStreamRequest.streamer().equals(user.id())) {
            context.status(HttpStatus.FORBIDDEN);
            context.result("You can only start streams for yourself");
            return;
        }

        streamStore.startStream(user);
        context.status(HttpStatus.CREATED);
    }

    @OpenApi(
            path = "/streams",
            methods = HttpMethod.GET,
            summary = "Get all streams",
            tags = {"Stream"}
    )
    @Override
    public void getAll(@NotNull Context context) {
        context.json(streamStore.getAllStreams());
    }

    @OpenApi(
            path = "/streams/:id",
            methods = HttpMethod.GET,
            summary = "Get a stream",
            tags = {"Stream"}
    )
    @Override
    public void getOne(@NotNull Context context, @NotNull String id) {
        User streamer = userStore.getUserByID(id);
        if (streamer == null) {
            context.status(HttpStatus.NOT_FOUND);
            context.result("User not found");
            return;
        }

        context.json(streamStore.getStream(streamer));
    }

    public void update(@NotNull Context context, @NotNull String id) {
        context.status(HttpStatus.NOT_FOUND);
    }

    @OpenApi(
            path = "/streams/:id",
            methods = HttpMethod.DELETE,
            summary = "Stop a stream",
            tags = {"Stream"}
    )
    @Override
    public void delete(@NotNull Context context, @NotNull String id) {
        User user = context.attribute("user");

        if (!id.equals(user.id())) {
            context.status(HttpStatus.FORBIDDEN);
            context.result("You can only stop your own stream");
            return;
        }

        streamStore.stopStream(user);
        context.status(HttpStatus.NO_CONTENT);
    }

    record CreateStreamRequest(String streamer) {

    }
}
