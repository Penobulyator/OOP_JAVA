package factory.threadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool extends Thread{
    private final BlockingQueue<Runnable> workingQueue;


    public ThreadPool(int workersCount) {
        workingQueue = new LinkedBlockingQueue<>();
        for (int i=0;i<workersCount;i++)
            new TaskWorker().start();
    }

    public void addTask(Runnable task){
        workingQueue.add(task);
    }

    private final class TaskWorker extends Thread{
        @Override
        public void run() {
            while(!currentThread().isInterrupted()){
                    try {
                        if (workingQueue.size() > 0)
                            workingQueue.take().run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        currentThread().interrupt();
                    }
            }
        }
    }


}
