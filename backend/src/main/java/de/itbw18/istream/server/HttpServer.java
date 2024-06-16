package de.itbw18.istream.server;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.itbw18.istream.helpers.Exclude;
import de.itbw18.istream.stream.StreamHandler;
import de.itbw18.istream.stream.chat.ChatHandler;
import de.itbw18.istream.stream.live.LiveHandler;
import de.itbw18.istream.streamconfig.StreamConfigHandler;
import de.itbw18.istream.user.UserAccessHandler;
import de.itbw18.istream.user.UserHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JsonMapper;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.eclipse.jetty.util.resource.Resource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.logging.Logger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HttpServer {

    private final UserHandler userHandler;
    private final UserAccessHandler userAccessHandler;
    private final StreamConfigHandler streamConfigHandler;
    private final StreamHandler streamHandler;
    private final LiveHandler liveHandler;
    private Javalin app;

    public HttpServer(UserAccessHandler userAccessHandler,
                      UserHandler userHandler,
                      StreamConfigHandler streamConfigHandler,
                      StreamHandler streamHandler,
                      LiveHandler liveHandler) {
        this.userHandler = userHandler;
        this.userAccessHandler = userAccessHandler;
        this.streamConfigHandler = streamConfigHandler;
        this.streamHandler = streamHandler;
        this.liveHandler = liveHandler;

        init();
    }

    private void init() {
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.http.asyncTimeout = 10000;

            config.jetty.modifyWebSocketServletFactory(factory -> {
                factory.setMaxBinaryMessageSize(1024 * 1024 * 150);
            });

            config.router.caseInsensitiveRoutes = true;

            URL frontendFolderURL = getClass().getResource("/public");
            if (frontendFolderURL != null && new File(frontendFolderURL.getFile()).exists()) {
                config.staticFiles.add("/public", Location.CLASSPATH);
                System.out.println("Serving frontend from " + frontendFolderURL.getFile());
            }

            Gson httpGson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes field) {
                            return field.getAnnotation(Exclude.class) != null;
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> aClass) {
                            return false;
                        }
                    })
                    .create();
            config.jsonMapper(new JsonMapper() {
                @Override
                public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                    return httpGson.toJson(obj, type);
                }

                @Override
                public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                    return httpGson.fromJson(json, targetType);
                }
            });

            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });

            try {
                config.registerPlugin(new OpenApiPlugin(openApiConfig ->
                        openApiConfig
                                .withDocumentationPath("/openapi.json")
                                .withDefinitionConfiguration((version, openApiDefinition) ->
                                        openApiDefinition
                                                .withInfo(openApiInfo ->
                                                        openApiInfo.description("iStream API")
                                                )
                                )));

                config.registerPlugin(new SwaggerPlugin(swaggerConfiguration -> {
                    swaggerConfiguration.setDocumentationPath("/openapi.json");
                }));
            } catch (Exception e) {
                Logger.getGlobal().severe("Could not register OpenAPI plugin");
            }

            config.router.apiBuilder(() -> {
                path("api/", () -> {
                    before("/*", userAccessHandler::authenticate);

                    crud("users/{user-id}", userHandler);
                    userHandler.handleExtra();
                    crud("stream-configs/{user-id}", streamConfigHandler);
                    crud("streams/{user-id}", streamHandler);

                    ws("streams/{user-id}/live", liveHandler::handle);

                    ChatHandler chatHandler = new ChatHandler();
                    ws("streams/{user-id}/chat", chatHandler::handle);

                    // not found
                    get("/*", this::apiNotFound);
                    post("/*", this::apiNotFound);
                    put("/*", this::apiNotFound);
                    delete("/*", this::apiNotFound);
                });

                // frontend
                path("/", () -> {
                    get("/register", this::frontendRedirect);
                    get("/login", this::frontendRedirect);
                    get("/watchview", this::frontendRedirect);
                });
            });
        });
    }

    public void start(int port) {
        app.start(port);
    }

    private void frontendRedirect(Context ctx) {
        try {
            ctx.result(Resource.newClassPathResource("public/index.html").getInputStream());
        } catch (IOException e) {
            ctx.result("Not found");
            ctx.status(HttpStatus.NOT_FOUND);
        }
        ctx.header("Content-Type", "text/html");
    }

    private void apiNotFound(Context ctx) {
        ctx.result("Not found");
        ctx.status(HttpStatus.NOT_FOUND);
    }

    public Javalin getApp() {
        return app;
    }
}
