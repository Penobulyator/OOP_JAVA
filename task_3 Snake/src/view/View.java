package view;

import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Cell;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class View{
    public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static int _width;//number of cells horizontally
    public static int _height;//number of cells vertically
    public static double CELL_WIDTH;
    public static double CELL_HEIGHT;
    Stage _stage = new Stage();
    Scene _scene;
    GridPane _matrix;
    public Stage get_stage() {
        return _stage;
    }
    public View(int width, int height) {
        _width = width;
        _height = height;
        CELL_WIDTH = SCREEN_WIDTH / _width;
        CELL_HEIGHT = SCREEN_HEIGHT / _height;
        _matrix = new GridPane();
        for (int i = 0; i < _width; i++)
            for (int j = 0; j < _height; j++) {
                Rectangle rec = new Rectangle(CELL_WIDTH, CELL_HEIGHT);
                rec.setFill(Color.web("#D3C70F"));//yellow
                rec.setStroke(Color.web("#000000"));//black
                _matrix.add(rec, i, j);
            }
        _scene = new Scene(_matrix);
        //_stage.setFullScreen(true);
        _stage.setScene(_scene);
    }
}
