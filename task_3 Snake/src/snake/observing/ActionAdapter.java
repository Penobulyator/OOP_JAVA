package snake.observing;

import javafx.scene.image.Image;
import snake.observing.food.FoodAction;
import snake.observing.food.FoodActionListener;
import snake.observing.snake.SnakeActionListener;
import snake.observing.snake.SnakeAppearanceAction;
import snake.observing.snake.SnakeMovementAction;
import snake.observing.view.ViewAction;
import snake.model.SnakeModel;
import snake.view.SnakeView;

import java.awt.*;
import java.util.LinkedList;

//converts SnakeAction and AppleAction to ViewAction
public class ActionAdapter implements SnakeActionListener, FoodActionListener {
    static private final String EMPTY_CELL_URL = "resources/snake/emptyCell.jpg";
    static private final String FOOD_URL = "resources/snake/food.jpg";
    static private final String SNAKE_BODY_URL = "resources/snake/snakeBody.jpg";
    static private final String SNAKE_HEAD_URL = "resources/snake/snakeHead.jpg";
    private LinkedList<Point> snakeCords;
    private SnakeView snakeView;

    public ActionAdapter(SnakeModel snakeModel, SnakeView snakeView){
        this.snakeView = snakeView;
        snakeModel.addSnakeActionListener(this);
        snakeModel.addFoodActionListener(this);
        snakeModel.addScoreActionListener(snakeView.getScoreActionListener());
    }
    @Override
    public void notify(FoodAction appleAction) {
        if (!appleAction.isAppeared()){
            //if food was removed
            ViewAction foodRemovedAction = new ViewAction(new Image(EMPTY_CELL_URL), appleAction.getCords());
            snakeView.notify(foodRemovedAction);
        }
        else{
            //if food was placed
            ViewAction foodRemovedAction = new ViewAction(new Image(FOOD_URL), appleAction.getCords());
            snakeView.notify(foodRemovedAction);
        }
    }

    @Override
    public void notify(SnakeMovementAction snakeMovementAction) {
        /*MOVE HEAD*/
        //notify ViewActionListener that head has been moved
        ViewAction headMovedAction = new ViewAction(new Image(SNAKE_BODY_URL), snakeCords.getLast());
        snakeView.notify(headMovedAction);

        //put new head in list
        Point newSnakeHeadCords = new Point(snakeCords.getLast());
        newSnakeHeadCords.move(newSnakeHeadCords.x + snakeMovementAction.getDx(), newSnakeHeadCords.y + snakeMovementAction.getDy());
        snakeCords.addLast(newSnakeHeadCords);

        //notify ViewActionListener about new head position
        ViewAction newHeadAppearedAction = new ViewAction(new Image(SNAKE_HEAD_URL), newSnakeHeadCords);
        snakeView.notify(newHeadAppearedAction);

        //check if java.snake tail was removed
        if (!snakeMovementAction.is_increased()){
            //notify ViewActionListener that tail was removed
            ViewAction tailRemovedAction  = new ViewAction(new Image(EMPTY_CELL_URL), snakeCords.getFirst());
            snakeView.notify(tailRemovedAction);

            //remove tail from list
            snakeCords.removeFirst();
        }
    }

    @Override
    public void notify(SnakeAppearanceAction snakeAppearanceAction) {
        snakeCords = new LinkedList<>(snakeAppearanceAction.getSnakeCords());
        for (int i=0; i< snakeCords.size() - 1; i++){
            ViewAction bodyAppearanceAction = new ViewAction(new Image(SNAKE_BODY_URL), snakeCords.get(i));
            snakeView.notify(bodyAppearanceAction);
        }

        ViewAction headAppearanceAction = new ViewAction(new Image(SNAKE_HEAD_URL), snakeCords.getLast());
        snakeView.notify(headAppearanceAction);
    }
}
