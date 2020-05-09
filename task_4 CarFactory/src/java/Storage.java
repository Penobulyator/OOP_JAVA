package java;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Storage<T> {
    private BlockingQueue<T> objects;

    public Storage(int capacity) {
        objects = new LinkedBlockingQueue<>(capacity);
    }

    public void put(T obj) throws InterruptedException {
        objects.put(obj);
        notify();
    }

    public T take() throws InterruptedException {
        return objects.take();
    }

    public int spaceLeft(){
        return objects.remainingCapacity();
    }
    public int currentSize(){
        return objects.size();
    }
}
