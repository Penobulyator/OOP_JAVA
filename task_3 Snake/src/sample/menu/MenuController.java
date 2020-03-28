package sample.menu;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import sample.Main;
import sun.plugin.javascript.navig.Anchor;

public class MenuController implements Initializable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button menuPlayButton;

    @FXML
    private Button menuOptionsButton;

    @FXML
    private Button menuExitButton;

    @FXML
    public void initialize() {
        System.out.println("bbb");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("aaa");
        menuPlayButton.setLayoutX(Main.get_width() / 2);
        menuPlayButton.setLayoutY(Main.get_height() / 2);
        assert menuPlayButton != null : "fx:id=\"menuPlayButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert menuOptionsButton != null : "fx:id=\"menuOptionsButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert menuExitButton != null : "fx:id=\"menuExitButton\" was not injected: check your FXML file 'sample.fxml'.";
    }
}
