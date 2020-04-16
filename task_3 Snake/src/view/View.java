package view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import observing.view.ViewAction;
import observing.view.ViewActionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View implements ViewActionListener {
    static private final String EMPTY_CELL_URL = "src/resources/emptyCell.jpg";

    public static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.8;
    public static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8;

    public static double CELL_WIDTH;
    public static double CELL_HEIGHT;

    Stage stage = new Stage();
    GridPane gridPane = new GridPane();
    Scene scene = new Scene (gridPane);

    private final int width;//number of cells horizontally
    private final int height;//number of cells vertically

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    public View(int width, int height) {
        gridPane.setGridLinesVisible(false);

        this.width = width;
        this.height = height;

        CELL_WIDTH = SCREEN_WIDTH / width;
        CELL_HEIGHT = SCREEN_HEIGHT / height;

        Image image = new Image("resources/emptyCell.jpg");

        gridPane.setBackground(Background.EMPTY);

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(CELL_HEIGHT);
                imageView.setFitWidth(CELL_WIDTH);
                gridPane.add(imageView, i, j);
            }
        stage.setScene (scene);
        //_stage.setFullScreen(true);
    }

    public Stage get_stage() {
        return stage;
    }

    @Override
    public void notify(ViewAction viewAction) {
        ImageView imageView = new ImageView(viewAction.get_image());

        imageView.setFitWidth(CELL_WIDTH);
        imageView.setFitHeight(CELL_HEIGHT);

        gridPane.add(imageView, viewAction.get_point().x, viewAction.get_point().y);
    }
}
