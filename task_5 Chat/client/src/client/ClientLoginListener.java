package client;

import java.io.IOException;

public interface ClientLoginListener {
    void alert(String warning);
    void loginIsSuccessful();
}
