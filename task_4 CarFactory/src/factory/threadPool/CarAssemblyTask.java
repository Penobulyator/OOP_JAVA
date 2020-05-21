package factory.threadPool;

import factory.Car;
import factory.Storage;
import factory.details.Accessory;
import factory.details.Body;
import factory.details.Engine;

public class CarAssemblyTask implements Runnable{
    static private Storage<Body> bodyStorage;
    static private Storage<Engine> engineStorage;
    static private Storage<Accessory> accessoryStorage;

    static private Storage<Car> carStorage;

    static private Integer idCounter = 0;
    static private final Object countLock = new Object();

    @Override
    public void run() {
        try {
            Body body = bodyStorage.take();
            Engine engine = engineStorage.take();
            Accessory accessory = accessoryStorage.take();
            int id;
            synchronized (countLock){
                id = idCounter;
                idCounter++;
            }
            carStorage.put(new Car(id, body, engine, accessory));

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }


    public static void setCarStorage(Storage<Car> carStorage) {
        CarAssemblyTask.carStorage = carStorage;
    }

    public static void setBodyStorage(Storage<Body> bodyStorage) {
        CarAssemblyTask.bodyStorage = bodyStorage;
    }

    public static void setEngineStorage(Storage<Engine> engineStorage) {
        CarAssemblyTask.engineStorage = engineStorage;
    }

    public static void setAccessoryStorage(Storage<Accessory> accessoryStorage) {
        CarAssemblyTask.accessoryStorage = accessoryStorage;
    }
}