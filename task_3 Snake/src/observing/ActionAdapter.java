package observing;

import javafx.scene.image.Image;
import model.Model;
import observing.food.FoodAction;
import observing.food.FoodActionListener;
import observing.snake.SnakeAppearanceAction;
import observing.snake.SnakeMovementAction;
import observing.snake.SnakeActionListener;
import observing.view.ViewAction;
import observing.view.ViewActionListener;

import java.awt.*;
import java.util.LinkedList;

//converts SnakeAction and AppleAction to ViewAction
public class ActionAdapter implements SnakeActionListener, FoodActionListener {
    static private final String EMPTY_CELL_URL = "resources/emptyCell.jpg";
    static private final String FOOD_URL = "resources/food.jpg";
    static private final String SNAKE_BODY_URL = "resources/snakeBody.jpg";
    static private final String SNAKE_HEAD_URL = "resources/snakeHead.jpg";
    private LinkedList<Point> snakeCords;
    private  ViewActionListener viewActionListener;

    public ActionAdapter(Model model, ViewActionListener viewActionListener){
        this.viewActionListener = viewActionListener;
        model.addSnakeActionListener(this);
        model.addAppleActionListener(this);
    }
    @Override
    public void notify(FoodAction appleAction) {
        if (appleAction.isAppeared() == false){
            //if food was removed
            ViewAction foodRemovedAction = new ViewAction(new Image(EMPTY_CELL_URL), appleAction.getCords());
            viewActionListener.notify(foodRemovedAction);
        }
        else{
            //if food was placed
            ViewAction foodRemovedAction = new ViewAction(new Image(FOOD_URL), appleAction.getCords());
            viewActionListener.notify(foodRemovedAction);
        }
    }

    @Override
    public void notify(SnakeMovementAction snakeMovementAction) {
        /*MOVE HEAD*/
        //notify ViewActionListener that head has been moved
        ViewAction headMovedAction = new ViewAction(new Image(SNAKE_BODY_URL), snakeCords.getLast());
        viewActionListener.notify(headMovedAction);

        //put new head in list
        Point newSnakeHeadCords = new Point(snakeCords.getLast());
        newSnakeHeadCords.move(newSnakeHeadCords.x + snakeMovementAction.getDx(), newSnakeHeadCords.y + snakeMovementAction.getDy());
        snakeCords.addLast(newSnakeHeadCords);

        //notify ViewActionListener about new head position
        ViewAction newHeadAppearedAction = new ViewAction(new Image(SNAKE_HEAD_URL), newSnakeHeadCords);
        viewActionListener.notify(newHeadAppearedAction);

        //check if snake tail was removed
        if (!snakeMovementAction.is_increased()){
            //notify ViewActionListener that tail was removed
            ViewAction tailRemovedAction  = new ViewAction(new Image(EMPTY_CELL_URL), snakeCords.getFirst());
            viewActionListener.notify(tailRemovedAction);

            //remove tail from list
            snakeCords.removeFirst();
        }
    }

    @Override
    public void notify(SnakeAppearanceAction snakeAppearanceAction) {
        snakeCords = new LinkedList<>(snakeAppearanceAction.getSnakeCords());
        for (int i=0; i< snakeCords.size() - 1; i++){
            ViewAction bodyAppearanceAction = new ViewAction(new Image(SNAKE_BODY_URL), snakeCords.get(i));
            viewActionListener.notify(bodyAppearanceAction);
        }

        ViewAction headAppearanceAction = new ViewAction(new Image(SNAKE_HEAD_URL), snakeCords.getLast());
        viewActionListener.notify(headAppearanceAction);
    }
}
