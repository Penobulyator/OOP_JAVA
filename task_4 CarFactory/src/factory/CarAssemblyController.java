package factory;

import factory.threadPool.CarAssemblyTask;
import factory.threadPool.ThreadPool;

public class CarAssemblyController extends Thread{


    private final ThreadPool threadPool;
    private final Storage<Car> carStorage;

    private final int dealersCount;
    public CarAssemblyController(ThreadPool threadPool, Storage<Car> carStorage, int dealersCount, int workersCount) {
        this.threadPool = threadPool;
        this.carStorage = carStorage;
        this.dealersCount = dealersCount;
    }

    @Override
    public void run() {
        while(!currentThread().isInterrupted()){
            try {
                int possibleTaskCount = carStorage.spaceLeft() - dealersCount;
                if(possibleTaskCount > 0)
                {
                    for (int i=0;i<possibleTaskCount;i++)
                        threadPool.addTask(new CarAssemblyTask());
                }
                synchronized (carStorage)
                {
                    carStorage.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                currentThread().interrupt();
            }
        }
    }
}
