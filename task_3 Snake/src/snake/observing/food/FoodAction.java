package snake.observing.food;

import java.awt.*;

public class FoodAction {
    private boolean appeared;

    public boolean isAppeared() {
        return appeared;
    }

    public Point getCords() {
        return cords;
    }

    private Point cords;
    public FoodAction(Point cords, boolean appeared){
        this.cords = cords;
        this.appeared = appeared;
    }
}
