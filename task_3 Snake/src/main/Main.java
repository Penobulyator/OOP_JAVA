package Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static final double width = 1200;
    public static final double height = 800;
    public static Stage getStage() {
        return stage;
    }

    private static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/mainMenu.fxml"));
        stage.setResizable(false);

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String args[]){
        launch(args);
    }
}