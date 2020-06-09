package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import client.ClientLoginListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;

public class LoginScreenController implements ClientLoginListener {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField userNameTestField;

    @FXML
    private Button loginButton;

    Client client;

    @FXML
    void initialize() {
        client = new Client(this);

        userNameTestField.setOnAction(event -> {login();});
    }

    private void login(){
        if (userNameTestField.getText() != null && !userNameTestField.getText().isEmpty()){
            client.sendRegistrationRequest(userNameTestField.getText());
            userNameTestField.clear();
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
    public void loginIsSuccessful(){
        //open chat screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/chatScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Scene scene = new Scene(root);

        Stage primaryStage = (Stage)userNameTestField.getScene().getWindow();
        primaryStage.setOnCloseRequest(event -> {
            client.logout();
            System.exit(0);
        });

        ChatScreenController controller = loader.getController();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });

        controller.start(client);
    }

    public void loginButtonPressed(ActionEvent actionEvent) {
        login();
    }
}
