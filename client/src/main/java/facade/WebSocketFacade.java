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
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }


}
