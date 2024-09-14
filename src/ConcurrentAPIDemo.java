import java.util.concurrent.TimeUnit;

public class ConcurrentAPIDemo {
    public static void main(String[] args) {

        // Simple Threads
        simpleThreadDemo();
        simpleThreadWithTimeDelay();

        // Concurrency Api
        simpleExecutorService();
    }


    /**
     * This method is to understand 'Thread' and 'Task'
     * */
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
    private static void simpleThreadWithTimeDelay(){
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
     *
     */
    private static void simpleExecutorService() {
    }
}