package de.itbw18.istream.stream.live;

import de.itbw18.istream.helpers.WebsocketHandler;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WriteCallback;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LiveHandler extends WebsocketHandler {

    private Map<String, List<Session>> sessions = new ConcurrentHashMap<>();

    @Override
    public void onConnect(WsConnectContext ctx) {
        String streamId = ctx.pathParam("user-id");

        List<Session> currentSessions = sessions.getOrDefault(streamId, new ArrayList<>());
        currentSessions.add(ctx.session);
        sessions.put(streamId, currentSessions);

        System.out.println("Connected " + ctx.sessionId() + " " + streamId);
    }

    @Override
    public void onClose(WsCloseContext ctx) {
        String streamId = ctx.pathParam("user-id");

        sessions.getOrDefault(streamId, new ArrayList<>()).remove(ctx.session);
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
