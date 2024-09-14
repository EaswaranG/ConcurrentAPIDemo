import java.util.concurrent.*;

public class ConcurrentAPIDemo {
    public static void main(String[] args) throws Exception {

        // Simple Threads
        simpleThreadDemo();
        simpleThreadWithTimeDelay();

        // Concurrency Api
        simpleExecutorServiceWithRunnable();
        simpleExecutorServiceWithCallable();
    }


    /**
     * This method is to understand 'Thread' and 'Task'
     */
    private static void simpleThreadDemo() {
        // This is a task which the thread will execute
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("This is a " + threadName + " thread task: ");
        };

        // main thread runs the task
        task.run();
        // New thread (Thread-0) is created
        Thread thread = new Thread(task);
        // Thread-0 will execture the task
        thread.start();
        // This line will be executed in unpredictable order based on the thread spun by the OS.
        System.out.println("End of Code");
    }

    /***
     * This thread is with time delay
     */
    private static void simpleThreadWithTimeDelay() {
        Runnable task = () -> {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.println("This is a " + threadName + " thread task: before timeout");
                // TimeUnit enum is used for timeout, Alternatively: Thread.sleep(3000);
                TimeUnit.SECONDS.sleep(3);
                System.out.println("This is a " + threadName + " thread task: after timeout");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread thread = new Thread(task);
        thread.start();
        System.out.println("End of Code");
    }

    /***
     * ExecutorService is introduced in Java 5 as part of ConcurrencyAPI.
     * We use Runnable interface for task in this method
     */
    private static void simpleExecutorServiceWithRunnable() {
        /* ExecutorService interface: Creating a new single thread pool with one worker thread.
        In case of thread failure, a new thread will be spun to do the subsequent tasks, unlike the equivalent factory method
        `newFixedThreadPool(1), will not be reconfigurable for additional threads.
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            System.out.println("This is a " + Thread.currentThread().getName() + " thread task: ");
        });

        // Same thread will be reused under the hood (pool-1-thread-1)
        executorService.submit(() -> {
            System.out.println("Reused executor thread: " + Thread.currentThread().getName());
            // If not shutdown, the application will keep running and waiting for tasks for this single thread
            // shutdown() waits for the task to complete, shutdownNow() interrupts and force shutdown the executor
            try {
                executorService.shutdown();
                executorService.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (!executorService.isTerminated()) {
                    executorService.shutdownNow();
                }
            }
        });

        // Note: future.cancel(true) : May interrupt running of the task
        System.out.println("Printing result future object: " + future.toString());
    }

    /***
     * ExecutorService supports Runnable and Callable. This method is example for Callable kind.
     * Callables are functional interfaces just like runnables but instead of being void they return a value.
     */
    private static void simpleExecutorServiceWithCallable() throws Exception {

        // Runs in main thread
        Callable<String> callableValueInMain = () -> Thread.currentThread().getName();

        // Runs in executor thread (pool-2-thread-1)
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<String> callableValueInExecutor = () -> {
            // Since submit will not wait for the task to complete, and the callables need to return, we use Future
            Future<?> future = executorService.submit( () -> Thread.currentThread().getName());
            executorService.shutdown();

            return  (String) future.get();
        };

        // call the task anytime to execute
        System.out.println("Callable Value Main Thread: "+ callableValueInMain.call());
        System.out.println("Callable Value Executor Thread: "+ callableValueInExecutor.call());
    }
}