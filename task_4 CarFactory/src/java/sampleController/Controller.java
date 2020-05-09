package java.sampleController;

import javafx.fxml.FXML;

import java.Car;
import java.Dealer;
import java.Storage;
import java.carFactory.CarFactory;
import java.details.Accessory;
import java.details.Body;
import java.details.Engine;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sampleController.CarBuyingListener;
import java.util.Properties;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Slider;
import javafx.scene.control.TableView;

import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Slider dealersSpeedSlider;

    @FXML
    private Slider engineSpeedSlider;

    @FXML
    private Slider bodySpeedSlider;

    @FXML
    private Slider accessorySpeedSlider;

    @FXML
    private Label enginesCount;

    @FXML
    private Label bodiesCount;

    @FXML
    private Label accessoriesCount;

    @FXML
    private Label carsCount;

    @FXML
    private TableView<?> table;



    Storage<Body> bodyStorage;
    Storage<Engine> engineStorage;
    Storage<Accessory> accessoryStorage;

    Storage<Car> carStorage;
    @FXML
    void initialize() {
        System.out.println("aaaaaaaaaaaa");
        buildModel();
    }
    private void buildModel(){

        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/resources/config.properties")) {

            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        bodyStorage = new Storage<>(Integer.parseInt(properties.getProperty("BodyStorageSize")));
        engineStorage = new Storage<>(Integer.parseInt(properties.getProperty("EngineStorageSize")));
        accessoryStorage = new Storage<>(Integer.parseInt(properties.getProperty("AccessoryStorageSize")));

        carStorage = new Storage<>(Integer.parseInt(properties.getProperty("CarStorageSize")));

        Dealer.setStorage(carStorage);

        CarFactory.setBodyStorage(bodyStorage);
        CarFactory.setEngineStorage(engineStorage);
        CarFactory.setAccessoryStorage(accessoryStorage);
        CarFactory.setCarStorage(carStorage);

        Dealer.setCarBuyingListener(new CarBuyingListener() {
            @Override
            public void notify(Car car) {
                //add car to table
            }
        });
    }
    private class Refresher extends Thread{
        private final int DELAY = 500; //ms

        @Override
        public void run() {
            while (!currentThread().isInterrupted()){
                enginesCount.setText(Integer.toString(engineStorage.currentSize()));
                bodiesCount.setText(Integer.toString(bodyStorage.currentSize()));
                accessoriesCount.setText(Integer.toString(accessoryStorage.currentSize()));

                carsCount.setText(Integer.toString(carStorage.currentSize()));



            }
        }
    }
}

