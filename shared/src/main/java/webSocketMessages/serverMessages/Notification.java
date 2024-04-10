package webSocketMessages.serverMessages;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.NOTIFICATION;

public class Notification extends ServerMessage{

    String notification;

    public ServerMessageType getMessage() {
        return message;
    }

    ServerMessageType message = NOTIFICATION;

    public String getErrorMessage() {
        return notification;
    }

    public Notification(ServerMessageType type, String notification) {
        super(type);
        this.notification = notification;

    }
}
