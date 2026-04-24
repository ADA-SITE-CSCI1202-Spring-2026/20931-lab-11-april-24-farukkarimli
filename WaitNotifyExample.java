public class WaitNotifyExample {

    
    // Shared Resource (Monitor)
    
    static class SharedResource {

        private int value;
        private boolean bChanged = false;

        // Consumer method
        public synchronized int get() {

            // Wait while no new value exists
            while (!bChanged) {
                try {
                    wait(); // release lock & sleep
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Consumed: " + value);

            bChanged = false;
            return value;
        }

        // Producer method
        public synchronized void set(int newValue) {

            value = newValue;
            bChanged = true;

            System.out.println("Produced: " + value);

            // Wake up waiting consumer
            notify();
        }
    }

    
    // Producer Thread
    
    static class Producer implements Runnable {

        private final SharedResource resource;

        public Producer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                resource.set(i);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   
    // Consumer Thread
    
    static class Consumer implements Runnable {

        private final SharedResource resource;

        public Consumer(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                resource.get();

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Main Method
    
    public static void main(String[] args) {

        SharedResource resource = new SharedResource();

        Thread producer = new Thread(new Producer(resource));
        Thread consumer = new Thread(new Consumer(resource));

        producer.start();
        consumer.start();
    }
}
