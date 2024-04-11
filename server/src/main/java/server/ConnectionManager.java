package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, server.Connection> connections = new ConcurrentHashMap<>();
    private final HashMap<Integer, Map<String, Connection>> gameIdLobby = new HashMap<>();

    public void add(String authString, Session session, Integer gameID) {
        var connection = new server.Connection(authString, session);
        gameIdLobby.putIfAbsent(gameID, new HashMap<>());
        Map<String, Connection> gameConnections = gameIdLobby.get(gameID);
        gameConnections.put(authString, connection);
    }

    public void remove(String authString, Integer gameID) {
        var gameConnections = gameIdLobby.get(gameID);
        if(gameConnections == null) {
            throw new RuntimeException("Game lobby doesn't exist");
        }

        gameConnections.remove(authString);
    }

    public void broadcast(String excludeAuthString, String notification, Integer gameID) throws IOException {
        var removeList = new ArrayList<server.Connection>();
        var gameConnections = gameIdLobby.get(gameID);

        for (var c : gameConnections.values()) {

            if (c.session.isOpen()) {
                if (!c.authString.equals(excludeAuthString)) {
                    c.send(notification);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameConnections.remove(c.authString);
        }
    }

    public server.Connection createConnection(String authString, Session session) {

        return new Connection(authString, session);
    }

}
