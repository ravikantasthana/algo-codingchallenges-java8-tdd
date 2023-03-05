package multithreading.fundamentals;

public class UncaughtExceptionHandling {

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            throw new RuntimeException("Intentional Exception");
        });

        thread.setName("Misbehaving thread");

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.printf("A critical error occurred in thread [%s], the error is '%s'%n", t.getName(), e.getMessage());
            }
        });

        thread.start();
    }

}
