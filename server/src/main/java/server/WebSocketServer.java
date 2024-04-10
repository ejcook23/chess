package server;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthAccess;
import dataAccess.SQLGameAccess;
import dataAccess.SQLUserAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.ServerMessage.ServerMessageType;
import webSocketMessages.userCommands.*;
import server.websocket.Connection;

import java.io.IOException;
import java.util.Objects;

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
            sendError(conn, "Error: Unknown User!");

        }
    }

    private void sendError(Connection conn, String errorMsg) throws IOException {
        Error error = new Error(ERROR,errorMsg);
        String errorJson = new Gson().toJson(error);
        conn.send(errorJson);

    }


    private void resign(Connection conn, String msg) {
    }

    private void leave(Connection conn, String msg) {
    }

    private void move(Connection conn, String msg) {
    }

    private void observe(Connection conn, String msg) throws Exception {
        SQLGameAccess SQLGameAccess = new SQLGameAccess();
        SQLAuthAccess SQLAuthAccess = new SQLAuthAccess();
        JoinObserver joinObserver = new Gson().fromJson(msg, JoinObserver.class);
        // serialize into join observer class, now we have GameID
        Integer gameID = joinObserver.getGameID();

        // GET GAME DATA FROM DB USING GAMEID
        GameData gameData = SQLGameAccess.getGameData(gameID);
        String username = SQLAuthAccess.getUserFromToken(joinObserver.getAuthString());

        if(!SQLGameAccess.gameExistsByID(gameID)) {
            sendError(conn, "Error: Game does not exist by that ID!");
        } else if (username == null) {
            sendError(conn, "Error: Bad AuthToken!");
        } else {

            // CREATE LOAD GAME MESSAGE TO SEND BACK
            LoadGame loadGame = new LoadGame(LOAD_GAME, gameData);
            String json = new Gson().toJson(loadGame);

            System.out.println("Sending message to client...");
            //BROADCAST MESSAGE
            Notification message = new Notification(NOTIFICATION, username + " joined the game as an observer.");
            String notifJson = new Gson().toJson(message);
            connectionManager.broadcast(conn.authString, notifJson);
            // SEND LOADGAME BACK TO CLIENT
            conn.send(json);
        }
    }

    private void join(Connection conn, String msg) throws Exception {
        // NEED TO DO SOME CHECKS FIRST
        SQLGameAccess SQLGameAccess = new SQLGameAccess();
        SQLAuthAccess SQLAuthAccess = new SQLAuthAccess();
        JoinPlayer joinPlayer = new Gson().fromJson(msg, JoinPlayer.class);
        // serialize into join player class, you have the gameID
        Integer gameID = joinPlayer.getGameID();

        // GET GAME DATA FROM DB USING GAMEID
        GameData gameData = SQLGameAccess.getGameData(gameID);
        String username = SQLAuthAccess.getUserFromToken(joinPlayer.getAuthString());

        if(!SQLGameAccess.gameExistsByID(gameID)) {
            sendError(conn, "Error: Game does not exist by that ID!");
        } else if((!Objects.equals(gameData.blackUsername(), username)) && joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK) {
            sendError(conn, "Error: Sorry, black team already taken.");
        } else if((!Objects.equals(gameData.whiteUsername(), username)) && joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            sendError(conn, "Error: White team already taken.");
        } else {

            // CREATE LOAD GAME MESSAGE TO SEND BACK
            LoadGame loadGame = new LoadGame(LOAD_GAME, gameData);
            String json = new Gson().toJson(loadGame);

            System.out.println("Sending message to client...");
            //SEND MESSAGE BACK TO CLIENT
            Notification message = new Notification(NOTIFICATION, username + " Joined the game as " + joinPlayer.getPlayerColor().toString());
            String notifJson = new Gson().toJson(message);
            connectionManager.broadcast(conn.authString, notifJson);
            conn.send(json);
        }




    }


}
