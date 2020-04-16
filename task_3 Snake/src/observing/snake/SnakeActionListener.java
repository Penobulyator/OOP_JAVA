package observing.snake;

public interface SnakeActionListener {
    void notify(SnakeMovementAction snakeMovementAction);
    void notify(SnakeAppearanceAction snakeAppearanceAction);
}
