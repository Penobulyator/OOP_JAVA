package snake.view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import snake.observing.score.ScoreActionListener;
import snake.observing.view.ViewAction;
import snake.observing.view.ViewActionListener;
import Main.Main;

import java.io.IOException;

public class SnakeView implements ViewActionListener {
    public static final double SCREEN_WIDTH = Main.width;//Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.8;
    public static final double SCREEN_HEIGHT = Main.height;//Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8;

    static private final String EMPTY_CELL_URL = "src/resources/emptyCell.jpg";

    public static double CELL_WIDTH;
    public static double CELL_HEIGHT;

    private final int width;//number of cells horizontally
    private final int height;//number of cells vertically

    private GridPane gridPane;
    private Text scoreField;

    private Scene scene;
    private int score = 0;

    ScoreActionListener scoreActionListener = new ScoreActionListener() {
        @Override
        public void increaseScore() {
            score++;
            scoreField.setText("Score: " + score);
        }
    };

    public SnakeView(int width, int height) {
        this.width = width;
        this.height = height;

        CELL_WIDTH = SCREEN_WIDTH / width;
        CELL_HEIGHT = SCREEN_HEIGHT / height;

        createContent();
    }

    private void createContent() {
        VBox root = new VBox();
        //create anchor pane

        root.setStyle("-fx-background-color: #e0eb11;");
        root.setAlignment(Pos.CENTER);

        //create score board
        scoreField = new Text("Score: 0");
        scoreField.setFont(new Font(64));

        root.getChildren().add(scoreField);

        //create grid pane
        gridPane = new GridPane();

        root.getChildren().add(gridPane);

        Image image = new Image("resources/snake/emptyCell.jpg");

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                ImageView imageView = new ImageView(image);

                imageView.setFitHeight(CELL_HEIGHT);
                imageView.setFitWidth(CELL_WIDTH);

                gridPane.add(imageView, i, j);
            }
        scene = new Scene(root);
    }

    public void endGame() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/gameEndMenu.fxml"));
            Main.getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(ViewAction viewAction) {
        ImageView imageView = new ImageView(viewAction.get_image());

        imageView.setFitWidth(CELL_WIDTH);
        imageView.setFitHeight(CELL_HEIGHT);

        gridPane.add(imageView, viewAction.get_point().x, viewAction.get_point().y);
    }

    public Scene getScene() {
        return scene;
    }

    public ScoreActionListener getScoreActionListener() {
        return scoreActionListener;
    }

}

