package snake.observing.snake;

public class SnakeMovementAction {
    private boolean increased;


    int dx;
    int dy;
    public SnakeMovementAction(int dx, int dy, boolean increased) {
        this.dx = dx;
        this.dy = dy;
        this.increased = increased;
    }
    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
    public boolean is_increased() {
        return increased;
    }
}
