package java;

import com.sun.prism.PixelFormat;

import java.details.Detail;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DetailSupplier<DetailType extends Detail> extends Thread{
    private int delay; //in ms
    private final Storage<DetailType> storage;
    private int idCounter = 0;
    private Constructor<DetailType> constructor;


    public DetailSupplier(int delay, Storage<DetailType> storage, Class<DetailType> type) {
        this.storage = storage;
        this.delay = delay;

        try {
            this.constructor = type.getDeclaredConstructor(int.class);
        } catch (NoSuchMethodException e) {
            System.out.println("Can't find a constructor of " + type.toString());
        }
    }

    @Override
    public void run() {
        while(!currentThread().isInterrupted()){
            idCounter++;
            try {
                DetailType detail = constructor.newInstance();
                storage.put(detail);
                sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                currentThread().interrupt();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {

                System.out.println("Thread" + currentThread().getName() + " can't build a new instance");
                currentThread().interrupt();
                e.printStackTrace();
            }

        }
    }
}
