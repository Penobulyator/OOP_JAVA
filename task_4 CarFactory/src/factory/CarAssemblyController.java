package factory;

import factory.threadPool.CarAssemblyTask;
import factory.threadPool.ThreadPool;

public class CarAssemblyController extends Thread{


    private final ThreadPool threadPool;
    private final Storage<Car> carStorage;

    private final int dealersCount;
    private final int workersCount;
    public CarAssemblyController(ThreadPool threadPool, Storage<Car> carStorage, int dealersCount, int workersCount) {
        this.threadPool = threadPool;
        this.carStorage = carStorage;
        this.dealersCount = dealersCount;
        this.workersCount = workersCount;
    }

    @Override
    public void run() {
        for (int i=0; i<workersCount;i++)
            threadPool.addTask(new CarAssemblyTask());
        while(!currentThread().isInterrupted()){
            try {
                int possibleTasks =  dealersCount - carStorage.currentSize();
                
                if (possibleTasks > 0){
                    for (int i=0; i<possibleTasks;i++)
                        threadPool.addTask(new CarAssemblyTask());
                }

                sleep(200);
//                synchronized (carStorage)
//                {
//                    carStorage.wait();
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                currentThread().interrupt();
            }
        }
    }
}
