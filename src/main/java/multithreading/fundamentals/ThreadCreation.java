package multithreading.fundamentals;

public class ThreadCreation {

    private static String threadName;

    public static void main(String[] args) throws InterruptedException {


        createAndRunThreadWithRunnable("CREATE_AND_RUN_WITH_RUNNABLE");

        createAndTunThreadDirectly("CREATE_AND_RUN_WITH_DIRECTLY");


    }

    private static void createAndRunThreadWithRunnable(String identifierPrefix) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                threadName = Thread.currentThread().getName();
                System.out.println("Running thread [" + threadName + "]");
                System.out.printf("Thread [%s] priority is: %s%n", threadName, Thread.currentThread().getPriority());
            }
        });

        thread.setName(identifierPrefix);
        thread.setPriority(Thread.MAX_PRIORITY);


        System.out.println(identifierPrefix + ": In thread [" + Thread.currentThread().getName() + "] -> BEFORE starting new thread.");
        thread.start();
        Thread.sleep(1000);
        System.out.println(identifierPrefix + ": In thread [" + Thread.currentThread().getName() + "] -> AFTER starting new thread, and BEFORE starting another thread.");
        Thread anotherThread = new Thread(() -> System.out.println(identifierPrefix + ": Another thread [" + Thread.currentThread().getName() + "] running."));
        // sleep() instructs the OS to not schedule the thread for specified period of time.
        // Thread does not consume any CPU when its sleeping.
        //Thread.sleep(2000);
        //anotherThread.start();
        //System.out.println("In thread [" + Thread.currentThread().getName() + "] -> AFTER starting ANOTHER thread.");
    }

    private static void createAndTunThreadDirectly(String identifierPrefix){
        Thread newThread = new NewThread();
        newThread.setName(identifierPrefix);
        newThread.start();
    }

    private static class NewThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello from NewThread [" +this.getName() + "]");
        }
    }

}
