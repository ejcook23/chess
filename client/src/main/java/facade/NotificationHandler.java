package facade;


import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
    void onMessage(String notification) throws Exception;

}
