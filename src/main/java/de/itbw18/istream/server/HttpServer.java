package de.itbw18.istream.server;

import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.itbw18.istream.helpers.Exclude;
import de.itbw18.istream.stream.StreamHandler;
import de.itbw18.istream.stream.chat.ChatHandler;
import de.itbw18.istream.stream.live.LiveHandler;
import de.itbw18.istream.user.UserHandler;
import io.javalin.Javalin;
import io.javalin.config.SizeUnit;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JsonMapper;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.eclipse.jetty.util.resource.Resource;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HttpServer {

    private Javalin app;

    public HttpServer() {
        init();
    }

    private void init() {
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.http.asyncTimeout = 10000;

            config.jetty.modifyWebSocketServletFactory(factory -> {
                factory.setMaxBinaryMessageSize(1024 * 1024 *150);
            });

            config.router.caseInsensitiveRoutes = true;

            config.staticFiles.add("/public", Location.CLASSPATH);

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
                    UserHandler userHandler = new UserHandler();
                    crud("users/{user-id}", userHandler);

                    StreamHandler streamHandler = new StreamHandler();
                    crud("streams/{user-id}", streamHandler);

                    LiveHandler liveHandler = new LiveHandler();
                    ws("streams/{user-id}/live", liveHandler::handle);

                    ws("streams/1/live", liveHandler::handle);

                    ChatHandler chatHandler = new ChatHandler();
                    ws("streams/{user-id}/chat", chatHandler::handle);

                    // not found
                    get("/*", ctx -> ctx.result("API not found"));
                    post("/*", ctx -> ctx.result("API not found"));
                    put("/*", ctx -> ctx.result("API not found"));
                    delete("/*", ctx -> ctx.result("API not found"));
                });

                // fallback for website
                get("/", ctx -> {
                    ctx.result(Resource.newClassPathResource("frontend/index.html").getInputStream());
                    ctx.header("Content-Type", "text/html");
                });

            });
        });
    }

    public void start(int port) {
        app.start(port);
    }

}
