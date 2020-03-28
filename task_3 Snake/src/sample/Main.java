package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    static private int _width = 700;
    static private int _height = 400;
    public static int get_width() {
        return _width;
    }

    public static int get_height() {
        return _height;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("menu/menu.fxml"));
        primaryStage.setTitle("Snake");
        primaryStage.setMaximized(true);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        _width = gd.getDisplayMode().getWidth();
        _height = gd.getDisplayMode().getHeight();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
