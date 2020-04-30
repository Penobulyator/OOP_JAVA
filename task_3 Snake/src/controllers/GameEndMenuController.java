package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import snake.controller.SnakeController;

import Main.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class GameEndMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void openMainMenu(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/mainMenu.fxml"));
            Main.getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void restart(ActionEvent event) {
        SnakeController controller = new SnakeController(GameOptionsController.getDefaultDifficulty(), GameOptionsController.getDefaultName());
        Main.getStage().setScene(controller.getScene());
        controller.startGame();
    }

    @FXML
    void showRecords(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/recordTable.fxml"));
            Main.getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() {

    }
}
