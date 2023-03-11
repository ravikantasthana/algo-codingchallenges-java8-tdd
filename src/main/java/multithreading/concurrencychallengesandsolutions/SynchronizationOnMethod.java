package multithreading.concurrencychallengesandsolutions;

public class SynchronizationOnMethod {

    public static void main(String[] args) throws InterruptedException {

        InventoryCounter inventoryCounter = new InventoryCounter();
        Thread incrementingThread = new IncrementingThread(inventoryCounter);
        Thread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("Number of Inventory Items = " + inventoryCounter.getItems());
    }

    private static class IncrementingThread extends Thread {

        private final InventoryCounter inventoryCounter;

        public IncrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0 ; i < 10_000; i++){
                inventoryCounter.increment();
            }
        }
    }

    private static class DecrementingThread extends Thread {

        private final InventoryCounter inventoryCounter;

        public DecrementingThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10_000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    private static class InventoryCounter {

        private int items;
        public synchronized void increment(){
            items++;
        }

        public synchronized void decrement(){
            items--;
        }

        public synchronized int getItems() {
            return items;
        }
    }

}
