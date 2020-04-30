package snake.observing.view;

import javafx.scene.image.Image;

import java.awt.*;

public class ViewAction {
    private Image image;
    private Point point;
    public ViewAction(Image image, Point point){
        this.image = image;
        this.point = point;
    }

    public Image get_image() {
        return image;
    }

    public Point get_point() {
        return point;
    }
}
