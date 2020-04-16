package model;

import com.sun.javafx.scene.traversal.Direction;
import observing.food.FoodAction;
import observing.food.FoodActionListener;
import observing.snake.SnakeAppearanceAction;
import observing.snake.SnakeMovementAction;
import observing.snake.SnakeActionListener;

import java.awt.*;
import java.util.*;

public class Model{

    static private int width;
    static private int height;

    static private final int START_SNAKE_SIZE = 5;

    static private Random rand = new Random();

    private LinkedList<Point> snakeCords = new LinkedList<>(); //first is tail, last is head
    private Point foodCords;
    private LinkedList<SnakeActionListener> snakeActionListeners = new LinkedList<>();//actually there will be only one listener
    private LinkedList<FoodActionListener> foodActionListeners = new LinkedList<>();//actually there will be only one listener

    public void addSnakeActionListener(SnakeActionListener snakeActionListener){
        snakeActionListeners.add(snakeActionListener);
    }

    public void addAppleActionListener(FoodActionListener foodActionListener){
        foodActionListeners.add(foodActionListener);
    }

    public Model(int width, int height){
        Model.width = width;
        Model.height = height;
    }

    private void placeNewFood(){
        //notify all food listeners that food has been removed
        for (FoodActionListener listener: foodActionListeners) {
            listener.notify(new FoodAction(foodCords, false));
        }
        chooseNewFoodCords();

        //notify all food listeners that food has been placed
        for (FoodActionListener listener: foodActionListeners) {
            listener.notify(new FoodAction(foodCords, true));
        }
    }
    private void chooseNewFoodCords(){
        foodCords.x = rand.nextInt (width);
        foodCords.y = rand.nextInt (height);

        //Check for collision. If there is a collision, call the same function again
        for (Point snakeBodyPartCords: snakeCords) {
            if (snakeBodyPartCords.equals (foodCords))
                chooseNewFoodCords();
        }
    }

    public void start(){
        //place snake
        for (int i=0;i<START_SNAKE_SIZE;i++)
            snakeCords.add(new Point(i, height /2));
        //notify listeners about snake appearance
        for (SnakeActionListener listener: snakeActionListeners) {
            listener.notify(new SnakeAppearanceAction (snakeCords));
        }
        foodCords = new Point(0, 0);
        placeNewFood();
    }
    private void move(int dx, int dy) throws Exception{
        /*CHECK FOR WARNINGS*/
        Point head = snakeCords.getLast();

        //check for walls
        if (head.x + dx >= width || head.x + dx < 0)
            throw new Exception("game over");
        if (head.y + dx >= height || head.y + dx < 0)
            throw new Exception("game over");

        //check for other snake body part crashes
        for (Point point: snakeCords) {
            if (point.x == head.x + dx && point.y == head.y + dy)
                throw new Exception("game over");
        }

        /*IF NO WARNING APPEARED*/
        snakeCords.addLast(new Point(head.x + dx, head.y + dy));
        if  (snakeCords.getLast().equals (foodCords)){ //if

            //notify all apple listeners that food has been removed
            for (FoodActionListener listener: foodActionListeners) {
                listener.notify(new FoodAction (foodCords, false));
            }

            //place new food
            placeNewFood();
            for (SnakeActionListener listener: snakeActionListeners) {
                listener.notify(new SnakeMovementAction(dx,dy, true));
            }

        }
        else{
            snakeCords.removeFirst();
            for (SnakeActionListener listener: snakeActionListeners) {
                listener.notify(new SnakeMovementAction(dx,dy, false));
            }
        }
    }
    private Direction getDirection(int dx, int dy) {
        if (dx == 1 && dy == 0)
            return Direction.RIGHT;
        else if (dx == -1 && dy == 0)
            return Direction.LEFT;
        else if (dx == 0 && dy == 1)
            return Direction.UP;
        else if (dx == 0 && dy == -1)
            return Direction.DOWN;
        else
            return null;
    }
    public void moveRight() throws Exception{
        move(1, 0);
    }

    public void moveLeft() throws Exception{
        move(-1, 0);
    }

    public void moveUp() throws Exception{
        move(0, -1);
    }

    public void moveDown() throws Exception{
        move(0, 1);
    }

}
