package observing.snake;

import java.awt.*;
import java.util.LinkedList;

public class SnakeAppearanceAction {

    private LinkedList<Point> snakeCords = new LinkedList<>();
    public SnakeAppearanceAction(LinkedList<Point> snakeCords){
        this.snakeCords = snakeCords;
    }
    public LinkedList<Point> getSnakeCords() {
        return snakeCords;
    }
}
