package server;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, server.Connection> connections = new ConcurrentHashMap<>();

    public void add(String authString, Session session) {
        var connection = new server.Connection(authString, session);
        connections.put(authString, connection);
    }

    public void remove(String authString) {
        connections.remove(authString);
    }

    public void broadcast(String excludeAuthString, String notification) throws IOException {
        var removeList = new ArrayList<server.Connection>();
        for (var c : connections.values()) {
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
            connections.remove(c.authString);
        }
    }

    public server.Connection getConnection(String authString, Session session) {
        if(!connections.contains(authString)) {
            add(authString, session);
        }
        return connections.get(authString);
    }

}
