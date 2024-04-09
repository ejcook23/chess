package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

@WebSocket
public class WebSocketServer {
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = readJson(msg, UserGameCommand.class);

        var conn = getConnection(command.getAuthString(), session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join(conn, msg);
                case JOIN_OBSERVER -> observe(conn, msg);
                case MAKE_MOVE -> move(conn, msg));
                case LEAVE -> leave(conn, msg);
                case RESIGN -> resign(conn, msg);
            }
        } else {
            Connection.sendError(session.getRemote(), "unknown user");
        }
    }

    private void resign(Object conn, String msg) {
    }

    private void leave(Object conn, String msg) {
    }

    private void move(Object conn, String msg) {
    }

    private void observe(Object conn, String msg) {
    }

    private void join(Object conn, String msg) {
    }

    private UserGameCommand readJson(String msg, Class<UserGameCommand> userGameCommandClass) {

    }


}
