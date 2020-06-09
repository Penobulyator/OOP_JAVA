package client;

import java.util.ArrayList;

public interface ClientChatListener {
    void alert(String warning);

    void updateUserList(ArrayList<String> names);

    void receiveMessage(String userName, String message);

    void userConnected(String username);

    void userDisconnected(String username);
}
