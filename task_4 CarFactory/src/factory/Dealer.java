package factory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dealer extends Thread {
    static private Storage<Car> storage;
    static private AtomicInteger delay = new AtomicInteger(0);

    static private Logger logger;
    @Override
    public void run() {
        while (!currentThread().isInterrupted())
        try {
            Car car = storage.take();
            if (logger != null){
                String message = String.format("%s: Auto %d(Body: %d, Engine: %d, Accessory: %d)",
                        this.getName(), car.getId(), car.getBody().getId(), car.getEngine().getId(), car.getAccessory().getId());
                logger.log(Level.INFO, message);
            }
            sleep(delay.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
            currentThread().interrupt();
        }
    }

    public static void setLogger(Logger logger) {
        Dealer.logger = logger;
    }

    public static void setDelay(int delay) {
        Dealer.delay.set(delay);
    }

    public static void setStorage(Storage<Car> storage) {
        Dealer.storage = storage;
    }


}
