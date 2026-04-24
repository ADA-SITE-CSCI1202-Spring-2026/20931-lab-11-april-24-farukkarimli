public class DynamicScaling {

   
    static class MathTask implements Runnable {

        private final long start;
        private final long end;

        public MathTask(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            long result = 0;
            long j = 5;

            for (long i = start; i < end; i++) {
                result += i * i * i + i * j;
            }

            System.out.println(Thread.currentThread().getName()
                    + " finished. Partial result = " + result);
        }
    }

    
    public static long runExperiment(int threadCount) throws InterruptedException {

        long totalIterations = 10_000_000L;
        long workPerThread = totalIterations / threadCount;

        Thread[] threads = new Thread[threadCount];

        long startTime = System.currentTimeMillis();

        // Create & start threads
        for (int i = 0; i < threadCount; i++) {
            long start = i * workPerThread;
            long end = (i == threadCount - 1)
                    ? totalIterations
                    : start + workPerThread;

            threads[i] = new Thread(new MathTask(start, end));
            threads[i].start();
        }

        
        for (Thread t : threads) {
            t.join();
        }

        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    
    public static void main(String[] args) throws InterruptedException {

        
        int cores = Runtime.getRuntime().availableProcessors();

        System.out.println("Logical processors available: " + cores);

        
        long timeOneThread = runExperiment(1);
        System.out.println("\nTime using 1 thread: "
                + timeOneThread + " ms");

        
        long timeMaxThreads = runExperiment(cores);
        System.out.println("\nTime using " + cores + " threads: "
                + timeMaxThreads + " ms");

        // Comparison
        double speedup =
                (double) timeOneThread / timeMaxThreads;

        System.out.println("\nSpeedup: " + speedup + "x");
    }
}