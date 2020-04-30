package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import Main.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainMenuController {

    public AnchorPane anchorPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    void initialize() {

    }

    public void start(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/gameOptionsMenu.fxml"));
        Main.getStage().setScene(new Scene(root));
    }

    public void exit(ActionEvent actionEvent) {
        Main.getStage().close();
    }
}
