package webSocketMessages.serverMessages;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.NOTIFICATION;

public class Notification extends ServerMessage{

    String message;

    public String getMessage() {
        return message;
    }

    public Notification(ServerMessageType type, String notification) {
        super(type);
        this.message = notification;

    }
}
