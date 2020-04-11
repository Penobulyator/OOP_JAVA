package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller {
    private View _view = new View(30, 20);
    private Model _model = new Model(30, 20);
    private KeyListener _keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };
    public Controller(){

    }

    public Stage getStage(){
        return _view.get_stage();
    }
}
