package java.carFactory;

import java.Car;
import java.CarAssemblyController;
import java.Storage;
import java.details.Accessory;
import java.details.Body;
import java.details.Engine;
import java.util.concurrent.atomic.AtomicInteger;

public class CarFactory extends Thread{
    private AtomicInteger taskCount = new AtomicInteger(0);

    static private Storage<Car> carStorage;

    static private Storage<Body> bodyStorage;
    static private Storage<Engine> engineStorage;
    static private Storage<Accessory> accessoryStorage;


    static private CarAssemblyController carAssemblyController;

    public CarFactory(int count) {
        for (int i=0;i<count;i++)
            new TaskWorker().start();
    }

    public void addTasks(int count){
        taskCount.set(taskCount.get() + count);
    }

    public static class CarAssemblyTask implements Runnable{

        @Override
        public void run() {
            try {
                Body body = bodyStorage.take();
                Engine engine = engineStorage.take();
                Accessory accessory = accessoryStorage.take();

                carStorage.put(new Car(body, engine, accessory));
                if (carAssemblyController != null)
                    carAssemblyController.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }


    }

    private final class TaskWorker extends Thread{
        @Override
        public void run() {
            while (taskCount.get() > 0){
                new CarAssemblyTask().run();
            }
        }
    }

    public static void setCarStorage(Storage<Car> carStorage) {
        CarFactory.carStorage = carStorage;
    }

    public static void setBodyStorage(Storage<Body> bodyStorage) {
        CarFactory.bodyStorage = bodyStorage;
    }

    public static void setEngineStorage(Storage<Engine> engineStorage) {
        CarFactory.engineStorage = engineStorage;
    }

    public static void setAccessoryStorage(Storage<Accessory> accessoryStorage) {
        CarFactory.accessoryStorage = accessoryStorage;
    }

    public static void setCarAssemblyController(CarAssemblyController carAssemblyController) {
        CarFactory.carAssemblyController = carAssemblyController;
    }
}
