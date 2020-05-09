package java;
import java.carFactory.CarFactory;

public class CarAssemblyController extends Thread{


    private final CarFactory carFactory;
    private final Storage<Car> carStorage;

    private final int dealersCount;

    public CarAssemblyController(CarFactory carFactory, Storage<Car> carStorage, int dealersCount, int workersCount) {
        this.carFactory = carFactory;
        this.carStorage = carStorage;

        this.dealersCount = dealersCount;
    }

    @Override
    public void run() {
        while(!currentThread().isInterrupted()){
            try {
                if(carStorage.spaceLeft() - dealersCount > 0)
                    carFactory.addTasks(carStorage.spaceLeft() - dealersCount);
                wait(); //wait until new car is appeared
            } catch (InterruptedException e) {
                e.printStackTrace();
                currentThread().interrupt();
            }
        }
    }
}
