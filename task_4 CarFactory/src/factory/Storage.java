package factory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Storage<T> {
    private BlockingQueue<T> objects;
    private AtomicInteger totalCount = new AtomicInteger(0);

    public Storage(int capacity) {
        objects = new LinkedBlockingQueue<>(capacity);
    }

    synchronized public void put(T obj) throws InterruptedException {
        objects.put(obj);
        totalCount.getAndIncrement();
        notifyAll();
    }

    public T take() throws InterruptedException {
        return objects.take();
    }
    public int getTotalCount(){
        return totalCount.get();
    }
    public int spaceLeft(){
        return objects.remainingCapacity();
    }
    public int currentSize(){
        return objects.size();
    }
}
