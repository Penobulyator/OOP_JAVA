package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import snake.controller.RecordTable;
import Main.Main;

import java.io.*;
import java.util.*;

public class RecordTableController {
    public VBox vbox;

    @FXML
    void initialize() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src/resources/java.recordTable.txt"))) {
            //read and change record table
            RecordTable recordTable = (RecordTable) objectInputStream.readObject();
            objectInputStream.close();
            //put records on vbox
            for (Map.Entry<String, Integer> record: recordTable.getSortedRecords()) {
                Text text = new Text(record.getKey() + "            " + record.getValue());
                text.setStyle("-fx-font-size: 60.0;");
                vbox.getChildren().add(text);
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/gameEndMenu.fxml"));
            Main.getStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

