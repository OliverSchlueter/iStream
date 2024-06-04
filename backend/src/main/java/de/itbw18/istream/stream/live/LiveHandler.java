package de.itbw18.istream.stream.live;

import de.itbw18.istream.helpers.WebsocketHandler;
import de.itbw18.istream.stream.Stream;
import de.itbw18.istream.stream.store.StreamStore;
import de.itbw18.istream.user.User;
import de.itbw18.istream.user.UserAccessHandler;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WriteCallback;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LiveHandler extends WebsocketHandler {

    private final Map<String, List<Session>> sessions = new ConcurrentHashMap<>();
    private final UserAccessHandler userAccessHandler;
    private final StreamStore streamStore;

    public LiveHandler(UserAccessHandler userAccessHandler, StreamStore streamStore) {
        this.userAccessHandler = userAccessHandler;
        this.streamStore = streamStore;
    }

    @Override
    public void onConnect(WsConnectContext ctx) {
        String streamId = ctx.pathParam("user-id");

        List<Session> streamSessions = sessions.getOrDefault(streamId, new ArrayList<>());
        streamSessions.add(ctx.session);
        sessions.put(streamId, streamSessions);

        streamStore.updateViewers(streamId, streamSessions.size());

        System.out.println("Connected " + ctx.sessionId() + " " + streamId);
    }

    @Override
    public void onClose(WsCloseContext ctx) {
        String streamId = ctx.pathParam("user-id");

        List<Session> streamSessions = sessions.getOrDefault(streamId, new ArrayList<>());
        streamSessions.remove(ctx.session);

        streamStore.updateViewers(streamId, streamSessions.size());

        System.out.println("Closed " + ctx.sessionId() + " " + ctx.status() + " " + ctx.reason());
    }

    @Override
    public void onMessage(WsMessageContext ctx) {
        System.out.println("Message " + ctx.message());
    }

    @Override
    public void onBinaryMessage(WsBinaryMessageContext ctx) {
        System.out.println("Binary Message " + ctx.data().length);

        String streamId = ctx.pathParam("user-id");

        userAccessHandler.authenticateWebSocket(ctx);
        User user = ctx.attribute("user");
        if (user == null || !Objects.equals(user.id(), streamId)) {
            ctx.send("Unauthorized");
            ctx.session.close(WsCloseStatus.BAD_GATEWAY.getCode(), "Unauthorized", null);
            return;
        }

        Stream stream = streamStore.getStream(user);
        if (stream == null) {
            ctx.send("Stream not found");
            ctx.session.close(WsCloseStatus.BAD_GATEWAY.getCode(), "Stream not found", null);
            return;
        }

        List<Session> streamSessions = sessions.getOrDefault(streamId, new ArrayList<>());
        for (Session session : streamSessions) {
            if (session == null || !session.isOpen()) {
                continue;
            }

            session.getPolicy().setMaxBinaryMessageSize(1024 * 1024 * 150);
            session.getRemote().sendBytes(ByteBuffer.wrap(ctx.data()), new WriteCallback() {
                @Override
                public void writeFailed(Throwable x) {
                    x.printStackTrace();
                }

                @Override
                public void writeSuccess() {
                }
            });
        }

        System.out.println("Sent to " + streamSessions.size() + " sessions");
    }

    @Override
    public void onError(WsErrorContext ctx) {

    }
}
