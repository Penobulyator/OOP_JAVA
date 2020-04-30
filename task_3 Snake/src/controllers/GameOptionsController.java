package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import snake.controller.GameDifficulty;
import snake.controller.SnakeController;

import Main.Main;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOptionsController {
    private static String defaultName = "Player";
    private static GameDifficulty defaultDifficulty = GameDifficulty.MEDIUM;

    public static String getDefaultName() {
        return defaultName;
    }

    public static GameDifficulty getDefaultDifficulty() {
        return defaultDifficulty;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<GameDifficulty> gameDifficultyChoiceBox = new ChoiceBox<>();

    @FXML
    private TextField nameField;

    @FXML
    private Button goButton;

    @FXML
    public void go(ActionEvent event) {
        if (gameDifficultyChoiceBox.getValue() != null && nameField.getText()!= null){
            defaultName = nameField.getText();
            defaultDifficulty = gameDifficultyChoiceBox.getValue();
            SnakeController controller = new SnakeController(gameDifficultyChoiceBox.getValue(), nameField.getText());
            Main.getStage().setScene(controller.getScene());
            controller.startGame();

        }
    }

    @FXML
    void initialize() {
        nameField.setText(defaultName);
        loadChoiceBox();
    }
    private void loadChoiceBox(){
        gameDifficultyChoiceBox.getItems().addAll(GameDifficulty.values());
        gameDifficultyChoiceBox.setValue(defaultDifficulty);
    }
}
