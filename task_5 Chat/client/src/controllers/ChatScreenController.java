package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import client.ClientChatListener;
import client.ClientLoginListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.stage.Stage;

public class ChatScreenController implements ClientChatListener {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea usersTextField;

    @FXML
    private TextArea chatTextField;

    @FXML
    private TextField messageTextField;

    Client client;

    @FXML
    void initialize() {
        messageTextField.setOnAction(event -> {sendMessage();});
    }

    public void start(Client client) {
        this.client = client;
        client.setClientChatListener(this);

        client.startToChat();
    }

    private void sendMessage(){
        if (!messageTextField.getText().isEmpty()){
            client.sendMessage(messageTextField.getText());
            messageTextField.clear();
        }
    }

    @Override
    public void alert(String warning) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText(warning);
                alert.show();
            }
        });
    }

    @Override
    public void updateUserList(ArrayList<String> names) {
        StringBuilder userNames = new StringBuilder();
        for (String name : names) {
            userNames.append(name).append("\n");
        }

        usersTextField.setText(userNames.toString());
    }

    @Override
    public void receiveMessage(String userName, String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatTextField.setText(chatTextField.getText().concat(userName + ": " + message + "\n"));
            }
        });
    }

    @Override
    public void userConnected(String userName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //type that user has connected
                chatTextField.setText(chatTextField.getText().concat(userName + " has connected!\n"));

                //add user to users list
                usersTextField.setText(usersTextField.getText().concat(userName + "\n"));
            }
        });

    }

    @Override
    public void userDisconnected(String userName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //type that user has disconnected
                chatTextField.setText(chatTextField.getText().concat(userName + " has disconnected!\n"));

                //remove user from users list
                usersTextField.setText(usersTextField.getText().replace(userName + "\n", ""));
            }
        });


    }

    public void messageEntered(DragEvent dragEvent) {

    }

    public void sendButtonPressed(ActionEvent actionEvent) {
        sendMessage();
    }
}
