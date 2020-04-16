package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import observing.ActionAdapter;
import view.View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Controller{
    private int width = 15;
    private int height = 10;
    private View view = new View (width, height);
    private Model model = new Model (width, height);
    private ActionAdapter actionAdapter = new ActionAdapter (model, view);
    public Controller() throws Exception {
        model.start();
    }

    public Stage getStage(){
        return view.get_stage();
    }
}
