package de.itbw18.istream.helpers;

import io.javalin.websocket.*;

public abstract class WebsocketHandler {

    public void handle(WsConfig ws) {
        ws.onConnect(this::onConnect);
        ws.onClose(this::onClose);
        ws.onMessage(this::onMessage);
        ws.onBinaryMessage(this::onBinaryMessage);
        ws.onError(this::onError);
    }

    public abstract void onConnect(WsConnectContext ctx);

    public abstract void onClose(WsCloseContext ctx);

    public abstract void onMessage(WsMessageContext ctx);

    public abstract void onBinaryMessage(WsBinaryMessageContext ctx);

    public abstract void onError(WsErrorContext ctx);

}
