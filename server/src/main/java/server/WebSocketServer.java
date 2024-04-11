package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.SqlAuthAccess;
import dataAccess.SqlGameAccess;
import dataAccess.SqlUserAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketServer {

    ConnectionManager connectionManager = new ConnectionManager();
    SqlGameAccess SQLGameAccess = new SqlGameAccess();
    SqlAuthAccess SQLAuthAccess = new SqlAuthAccess();
    SqlUserAccess SQLUserAccess = new SqlUserAccess();


    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = new Gson().fromJson(msg,UserGameCommand.class);


        assert command != null;
        Connection conn = connectionManager.getConnection(command.getAuthString(), session);
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


    private void resign(Connection conn, String msg) throws Exception {
        Resign resignPlayer = new Gson().fromJson(msg, Resign.class);
        // serialize into join player class, you have the gameID
        Integer gameID = resignPlayer.getGameID();

        GameData gameData = SQLGameAccess.getGameData(gameID);
        String username = SQLAuthAccess.getUserFromToken(resignPlayer.getAuthString());

        if(gameData == null) {
            sendError(conn, "Error: The game is already over or the gameID is invalid.");
        } else {
            if( (Objects.equals(gameData.blackUsername(), username)) || (Objects.equals(gameData.whiteUsername(), username)) ) {

                SQLGameAccess.updateGameID(gameID,0);

                System.out.println("\n (websocket resign) Sending message to multiple clients...");
                //SEND MESSAGE BACK TO CLIENT
                Notification message = new Notification(NOTIFICATION, username + " resigned from the game.");
                String notifJson = new Gson().toJson(message);
                connectionManager.broadcast(null, notifJson);


            } else {
                sendError(conn, "Error: You are not a player and cannot resign.");
            }
        }



    }

    private void leave(Connection conn, String msg) throws Exception {
        Leave leavePlayer = new Gson().fromJson(msg, Leave.class);
        // serialize into join player class, you have the gameID
        Integer gameID = leavePlayer.getGameID();

        GameData gameData = SQLGameAccess.getGameData(gameID);
        String username = SQLAuthAccess.getUserFromToken(leavePlayer.getAuthString());

        if(!SQLGameAccess.gameExistsByID(gameID)) {
            sendError(conn, "Error: Game does not exist by that ID!");
        } else {

            // remove from DB
            if(Objects.equals(gameData.blackUsername(), username)) {
                SQLGameAccess.setBlackUser(gameID,null);
            } else if(Objects.equals(gameData.whiteUsername(), username)) {
                SQLGameAccess.setBlackUser(gameID,null);
            }

            System.out.println("(websocket leave) Sending message to clients...");
            //SEND MESSAGE BACK TO CLIENT
            Notification message = new Notification(NOTIFICATION, username + " has left the game.");
            String notifJson = new Gson().toJson(message);
            connectionManager.broadcast(conn.authString, notifJson);
        }

    }

    private void move(Connection conn, String msg) throws Exception {
        MakeMove makeMove = new Gson().fromJson(msg, MakeMove.class);
        Integer gameID = makeMove.getGameID();
        ChessMove move = makeMove.getMove();
        GameData gameData = SQLGameAccess.getGameData(gameID);
        String username = SQLAuthAccess.getUserFromToken(makeMove.getAuthString());

        if(gameData == null) {
            sendError(conn, "Error: Game does not exist by that ID, or game is over.");
        } else {
            // CHECK IF IT IS THE USERS TURN
            if ( !(Objects.equals(gameData.blackUsername(), username)) && !(Objects.equals(gameData.whiteUsername(), username)) ) {
                sendError(conn, "Error: You are an observer and cannot make moves.");

            } else if ( (Objects.equals(gameData.blackUsername(), username) && (gameData.game().getTeamTurn() != ChessGame.TeamColor.BLACK)) || (Objects.equals(gameData.whiteUsername(), username) && (gameData.game().getTeamTurn() != ChessGame.TeamColor.WHITE)) ) {
                sendError(conn, "Error: It is not your turn!");

            } else {

                try {
                    gameData.game().makeMove(move);
                    String chessGameJson = new Gson().toJson(gameData.game());
                    SQLGameAccess.updateGame(gameID, chessGameJson);

                    GameData updatedGame = SQLGameAccess.getGameData(gameID);

                    // BROADCAST LOAD GAME

                    // BROADCAST LOAD GAME
                    LoadGame message = new LoadGame(LOAD_GAME, updatedGame);
                    String loadGameJson = new Gson().toJson(message);
                    connectionManager.broadcast(null, loadGameJson);

                } catch (InvalidMoveException e) {
                    sendError(conn, "Error: Invalid move! | " + e.getMessage());
                }
            }




        }





    }

    private void observe(Connection conn, String msg) throws Exception {
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
            conn.send(json);

            System.out.println("\n(websocket observe) Sending message to client...");
            //BROADCAST MESSAGE
            Notification message = new Notification(NOTIFICATION, username + " joined the game as an observer.");
            String notifJson = new Gson().toJson(message);
            connectionManager.broadcast(conn.authString, notifJson);

        }
    }

    private void join(Connection conn, String msg) throws Exception {
        // NEED TO DO SOME CHECKS FIRST
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
            conn.send(json);

            System.out.println("\n (websocket join) Sending message to client...");
            //SEND MESSAGE BACK TO CLIENT
            Notification message = new Notification(NOTIFICATION, username + " Joined the game as " + joinPlayer.getPlayerColor().toString());
            String notifJson = new Gson().toJson(message);
            connectionManager.broadcast(conn.authString, notifJson);

        }




    }


}
