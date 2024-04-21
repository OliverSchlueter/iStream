package de.itbw18.istream.stream.live;

import de.itbw18.istream.helpers.WebsocketHandler;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WriteCallback;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class LiveHandler extends WebsocketHandler {

    private List<Session> sessions = new ArrayList<>();

    @Override
    public void onConnect(WsConnectContext ctx) {
        System.out.println("Connected " + ctx.sessionId());
        sessions.add(ctx.session);
    }

    @Override
    public void onClose(WsCloseContext ctx) {
        System.out.println("Closed " + ctx.sessionId() + " " + ctx.status() + " " + ctx.reason());
        sessions.remove(ctx.session);
    }

    @Override
    public void onMessage(WsMessageContext ctx) {
        System.out.println("Message " + ctx.message());
    }

    @Override
    public void onBinaryMessage(WsBinaryMessageContext ctx) {
        System.out.println("Binary Message " + ctx.data().length);

        for (Session session : sessions) {
            session.getPolicy().setMaxBinaryMessageSize(1024 * 1024 * 150);
            session.getRemote().sendBytes(ByteBuffer.wrap(ctx.data()), new WriteCallback() {
                @Override
                public void writeFailed(Throwable x) {
                    x.printStackTrace();
                }

                @Override
                public void writeSuccess() {
                    System.out.println("Sent");
                }
            });
        }
    }

    @Override
    public void onError(WsErrorContext ctx) {

    }
}
