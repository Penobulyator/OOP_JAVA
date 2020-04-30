package snake.controller;

import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;
import snake.observing.score.ScoreActionListener;
import Main.Main;
import snake.model.SnakeModel;
import snake.observing.ActionAdapter;
import snake.view.SnakeView;

import java.io.*;

public class SnakeController {

    private int width;
    private int height;
    private double fps;

    private String playerName;
    private int playerScore = 0;

    private SnakeView snakeView;
    private SnakeModel snakeModel;
    private ActionAdapter actionAdapter;

    private boolean keyWasTyped;
    private Direction lastDirection;

    private Timeline moveTimeline;

    public SnakeController(GameDifficulty gameDifficulty, String name) {
        this.playerName = name;

        switch (gameDifficulty) {

            case EASY:
                width = 15;
                height = 10;
                fps = 5;
                break;
            case MEDIUM:
                width = 30;
                height = 20;
                fps = 10;
                break;
            case HARD:
                width = 45;
                height = 30;
                fps = 15;
                break;
        }

        snakeView = new SnakeView(width, height);
    }

    public void startGame() {
        lastDirection = Direction.RIGHT;
        keyWasTyped = false;

        snakeModel = new SnakeModel(width, height);
        snakeModel.addScoreActionListener(new ScoreActionListener() {
            @Override
            public void increaseScore() {
                playerScore++;
            }
        });

        actionAdapter = new ActionAdapter(snakeModel, snakeView);

        setKeys();
        setTimeLine();

        snakeModel.start();
    }

    private void endGame() {
        moveTimeline.stop();

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src/resources/recordTable.txt"))) {
            //read and change record table
            RecordTable recordTable = (RecordTable) objectInputStream.readObject();
            recordTable.addRecord(playerName, playerScore);
            objectInputStream.close();
            //add record table to file
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src/resources/recordTable.txt"));
            objectOutputStream.writeObject(recordTable);
            objectOutputStream.close();

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        snakeView.endGame();
    }

    private void setTimeLine() {
        moveTimeline = new Timeline(new KeyFrame(
                Duration.millis(1000 / fps),
                ae -> {
                    try {
                        switch (lastDirection) {

                            case UP:
                                snakeModel.moveUp();
                                break;
                            case DOWN:
                                snakeModel.moveDown();
                                break;
                            case LEFT:
                                snakeModel.moveLeft();
                                break;
                            case RIGHT:
                                snakeModel.moveRight();
                                break;
                            default:
                                System.out.println("Some problem");
                        }
                    } catch (Exception e) {
                        endGame();
                    }
                    keyWasTyped = false;
                })
        );
        moveTimeline.setCycleCount(Animation.INDEFINITE);
        moveTimeline.play();
    }

    private void setKeys() {

        Main.getStage().getScene().setOnKeyPressed(event -> {
            try {
                switch (event.getCode()) {
                    case UP: {
                        if (!lastDirection.equals(Direction.DOWN) && !keyWasTyped) {
                            lastDirection = Direction.UP;
                        }
                        break;
                    }
                    case DOWN: {
                        if (!lastDirection.equals(Direction.UP) && !keyWasTyped) {
                            lastDirection = Direction.DOWN;
                        }
                        break;
                    }
                    case RIGHT: {
                        if (!lastDirection.equals(Direction.LEFT) && !keyWasTyped) {
                            lastDirection = Direction.RIGHT;
                        }
                        break;
                    }
                    case LEFT: {
                        if (!lastDirection.equals(Direction.RIGHT) && !keyWasTyped) {
                            lastDirection = Direction.LEFT;
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            keyWasTyped = true;
            moveTimeline.play();
        });
    }

    public Scene getScene() {
        return snakeView.getScene();
    }
}
