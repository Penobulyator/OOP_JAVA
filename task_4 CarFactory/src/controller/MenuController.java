package controller;

import factory.*;
import factory.threadPool.CarAssemblyTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import factory.threadPool.ThreadPool;
import factory.details.Accessory;
import factory.details.Body;
import factory.details.Engine;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.scene.control.Slider;

import javafx.scene.control.Label;
import javafx.util.Duration;

public class MenuController {

    private final int MIN_SUPPLIER_DELAY = 200; //ms
    private final int MAX_SUPPLIER_DELAY = 2000; //ms
    @FXML
    public Label totalEnginesCountLabel;

    @FXML
    public Label totalBodiesCountLabel;

    @FXML
    public Label totalAccessoriesCountLabel;

    @FXML
    public Label totalCarsCountLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Slider engineSpeedSlider;

    @FXML
    private Slider bodySpeedSlider;

    @FXML
    private Slider accessorySpeedSlider;

    @FXML
    private Slider dealersSpeedSlider;

    @FXML
    private Label storageEnginesCount;

    @FXML
    private Label storageBodiesCount; 

    @FXML
    private Label storageAccessoriesCount; 

    @FXML
    private Label storageCarsCount;

    private Storage<Body> bodyStorage;
    private Storage<Engine> engineStorage;
    private Storage<Accessory> accessoryStorage;

    private Storage<Car> carStorage;

    private ArrayList<DetailSupplier<Engine>> engineSuppliers;
    private ArrayList<DetailSupplier<Body>> bodySuppliers;
    private ArrayList<DetailSupplier<Accessory>> accessorySuppliers;

    @FXML
    public void initialize() {
        //create storage, dealers, suppliers, car factory
        buildModel();

        //setup gui refreshing
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),
                event -> refreshGui()
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void buildModel(){
        //get properties
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/resources/config.properties")) {

            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        //create storage
        bodyStorage = new Storage<>(Integer.parseInt(properties.getProperty("BodyStorageSize")));
        engineStorage = new Storage<>(Integer.parseInt(properties.getProperty("EngineStorageSize")));
        accessoryStorage = new Storage<>(Integer.parseInt(properties.getProperty("AccessoryStorageSize")));

        carStorage = new Storage<>(Integer.parseInt(properties.getProperty("CarStorageSize")));

        //create thread pool
        CarAssemblyTask.setBodyStorage(bodyStorage);
        CarAssemblyTask.setEngineStorage(engineStorage);
        CarAssemblyTask.setAccessoryStorage(accessoryStorage);
        CarAssemblyTask.setCarStorage(carStorage);

        ThreadPool threadPool = new ThreadPool(Integer.parseInt(properties.getProperty("Workers")));

        //create car assembly controller
        CarAssemblyController carAssemblyController = new CarAssemblyController(threadPool, carStorage,
                Integer.parseInt(properties.getProperty("Dealers")), Integer.parseInt(properties.getProperty("Workers")));

        threadPool.start();
        carAssemblyController.start();

        createSuppliers(properties);
        createDealers(properties);

        //setup logging

    }

    private void createSuppliers(Properties properties){
        int size = Integer.parseInt(properties.getProperty("EngineSuppliers"));
        engineSuppliers = new ArrayList<>(size);
        for (int i=0;i< size; i++)
        {
            DetailSupplier<Engine> supplier = new DetailSupplier<>(speedToDelay(engineSpeedSlider.getValue()), engineStorage, Engine.class);
            supplier.setName("Engine supplier " + i);
            engineSuppliers.add(supplier);
            supplier.start();
        }

        size = Integer.parseInt(properties.getProperty("BodySuppliers"));
        bodySuppliers = new ArrayList<>(size);
        for (int i=0;i< size; i++)
        {
            DetailSupplier<Body> supplier = new DetailSupplier<>(speedToDelay(bodySpeedSlider.getValue()), bodyStorage, Body.class);
            supplier.setName("Body supplier №" + i);
            bodySuppliers.add(supplier);
            supplier.start();
        }

        size = Integer.parseInt(properties.getProperty("AccessorySuppliers"));
        accessorySuppliers = new ArrayList<>(size);
        for (int i=0;i< size; i++)
        {
            DetailSupplier<Accessory> supplier = new DetailSupplier<>(speedToDelay(accessorySpeedSlider.getValue()), accessoryStorage, Accessory.class);
            supplier.setName("Accessory supplier " + i);
            accessorySuppliers.add(supplier);
            supplier.start();
        }

    }

    private void createDealers(Properties properties){
        if (Boolean.parseBoolean(properties.getProperty("Log"))){
            Logger logger = Logger.getLogger("Log");
            try {
                FileHandler fileHandler = new FileHandler("src/resources/factory.log");

                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);

                logger.addHandler(fileHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dealer.setLogger(logger);
        }

        Dealer.setStorage(carStorage);
        for (int i=0;i<Integer.parseInt(properties.getProperty("Dealers"));i++)
        {
            Dealer dealer = new Dealer();
            dealer.setName("Dealer " + i);
            dealer.start();
        }
    }

    private int speedToDelay(double speed){
        assert(speed >= 0);
        assert(speed <= 100);

        return  -(MAX_SUPPLIER_DELAY - MIN_SUPPLIER_DELAY) / 100 * (int)speed + MAX_SUPPLIER_DELAY;
    }

    private void refreshGui() {
        storageEnginesCount.setText(Integer.toString(engineStorage.currentSize()));
        storageBodiesCount.setText(Integer.toString(bodyStorage.currentSize()));
        storageAccessoriesCount.setText(Integer.toString(accessoryStorage.currentSize()));
        storageCarsCount.setText(Integer.toString(carStorage.currentSize()));

        totalEnginesCountLabel.setText(Integer.toString(engineStorage.getTotalCount()));
        totalBodiesCountLabel.setText(Integer.toString(bodyStorage.getTotalCount()));
        totalAccessoriesCountLabel.setText(Integer.toString(accessoryStorage.getTotalCount()));
        totalCarsCountLabel.setText(Integer.toString(carStorage.getTotalCount()));

        for (DetailSupplier<Engine> supplier: engineSuppliers) {
            supplier.setDelay(speedToDelay(engineSpeedSlider.getValue()));
        }
        for (DetailSupplier<Body> supplier: bodySuppliers) {
            supplier.setDelay(speedToDelay(bodySpeedSlider.getValue()));
        }
        for (DetailSupplier<Accessory> supplier: accessorySuppliers) {
            supplier.setDelay(speedToDelay(accessorySpeedSlider.getValue()));
        }
        Dealer.setDelay(speedToDelay(dealersSpeedSlider.getValue()));
    }
}

