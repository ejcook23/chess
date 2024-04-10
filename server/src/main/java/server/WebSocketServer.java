package server;

import chess.ChessBoard;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLGameAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.ServerMessage.ServerMessageType;
import webSocketMessages.userCommands.*;
import server.websocket.Connection;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;
import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

@WebSocket
public class WebSocketServer {

    ConnectionManager connectionManager = new ConnectionManager();


    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = new Gson().fromJson(msg,UserGameCommand.class);


        assert command != null;
        var conn = connectionManager.getConnection(command.getAuthString(), session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join(conn, msg);
                case JOIN_OBSERVER -> observe(conn, msg);
                case MAKE_MOVE -> move(conn, msg);
                case LEAVE -> leave(conn, msg);
                case RESIGN -> resign(conn, msg);
            }
        } else {
            //Connection.sendError(session.getRemote(), "unknown user");
        }
    }



    private void resign(Connection conn, String msg) {
    }

    private void leave(Connection conn, String msg) {
    }

    private void move(Connection conn, String msg) {
    }

    private void observe(Connection conn, String msg) {
    }

    private void join(Connection conn, String msg) throws Exception {
        JoinPlayer joinPlayer = new Gson().fromJson(msg, JoinPlayer.class);
        // serialize into join player class, you have the gameID
        Integer gameID = joinPlayer.getGameID();

        // GET GAME DATA FROM DB USING GAMEID
        SQLGameAccess SQLGameAccess = new SQLGameAccess();
        GameData gameData = SQLGameAccess.getGameData(gameID);
        ChessBoard board = gameData.game().getBoard();
        // CREATE LOAD GAME MESSAGE TO SEND BACK
        LoadGame loadGame = new LoadGame(LOAD_GAME, board);
        String json = new Gson().toJson(loadGame);

        System.out.println("Sending message to client...");
        //SEND MESSAGE BACK TO CLIENT
        conn.send(json);


    }


}
