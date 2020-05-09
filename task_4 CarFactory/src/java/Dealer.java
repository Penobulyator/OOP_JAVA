package java;

import java.sampleController.CarBuyingListener;

public class Dealer extends Thread {
    static private Storage<Car> storage;
    static private int delay;

    static private CarBuyingListener carBuyingListener;

    @Override
    public void run() {
        while (!currentThread().isInterrupted())
        try {
            Car newCar = storage.take();
            if (carBuyingListener != null)
                carBuyingListener.notify(newCar);
            sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
            currentThread().interrupt();
        }
    }

    public static void setDelay(int delay) {
        Dealer.delay = delay;
    }

    public static void setStorage(Storage<Car> storage) {
        Dealer.storage = storage;
    }


    public static void setCarBuyingListener(CarBuyingListener carBuyingListener) {
        Dealer.carBuyingListener = carBuyingListener;
    }
}
