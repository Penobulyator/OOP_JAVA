package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Model;
import observing.ActionAdapter;
import view.View;

public class Controller{
    private int width = 15;
    private int height = 10;
    private double frequency = 0.4;

    private View view = new View (width, height);
    private Model model = new Model (width, height);
    private ActionAdapter actionAdapter = new ActionAdapter (model, view);

    private boolean wasUp = false;
    private boolean wasDown = false;
    private boolean wasRight = true;
    private boolean wasLeft = false;

    private Timeline moveTimeline;

    public Controller(){
        startGame();
    }
    private void startGame(){
        model.start();
        setKeys();
        setTimeLine();
    }
    private void setTimeLine(){
        moveTimeline = new Timeline(new KeyFrame(
                Duration.millis(1000 * frequency),
                ae -> {
                    try {
                        if (wasUp)
                            model.moveUp();

                        else if (wasDown)
                            model.moveDown();


                        else if (wasLeft)
                            model.moveLeft();

                        else if (wasRight)
                            model.moveRight();

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                })
        );
        moveTimeline.setCycleCount(Animation.INDEFINITE);
        moveTimeline.play();
    }
    private void setKeys(){

        view.get_stage().getScene().setOnKeyPressed(event ->  {
            try {
                switch (event.getCode()){
                    case UP: {
                        if (!wasDown){
                            wasUp = true;
                            wasRight = wasLeft = wasDown = false;
                            break;
                        }
                    }
                    case DOWN: {
                        if (!wasUp){
                            wasDown = true;
                            wasUp = wasLeft = wasRight = false;
                            break;
                        }
                    }
                    case RIGHT: {
                        if (!wasLeft){
                            wasRight = true;
                            wasUp = wasDown = wasLeft = false;
                            break;
                        }
                    }
                    case LEFT: {
                        if (!wasRight){
                            wasLeft = true;
                            wasUp = wasDown = wasRight = false;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public Stage getStage(){
        return view.get_stage();
    }
}
