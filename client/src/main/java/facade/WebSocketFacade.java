package facade;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.*;

public class WebSocketFacade {

    private NotificationHandler notificationHandler;
    private Session session;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            System.out.print("WebSocketFacade.");
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    System.out.print("Client received message.");
                    try {
                        notificationHandler.onMessage(message);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void send(String msg) throws Exception {
        System.out.print("Client Sending...");
        this.session.getBasicRemote().sendText(msg);
    }


}
