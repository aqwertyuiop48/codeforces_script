package com.example;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class tests {
    public static void main(String[] args) throws Exception {

        /************************************** Accolite *********************************************/

        // 1
        System.out.println(Arrays.stream(new int[]{1, 2, 3, 4, 5}).sum());
        System.out.println(Arrays.stream(new int[]{1, 2, 3, 4, 5}).map(i->i).sum());
        System.out.println(Arrays.stream(new int[]{1, 2, 3, 4, 5}).filter(i->i==i).sum());
        System.out.println(MessageFormat.format("Hello, {0}! You are {1} years old.", "name", 25));
        System.out.println(String.format("%s , %d", "name", 25));

        String s = "Hello Worldhe !!";
        Map<String,Integer> counts = new LinkedHashMap<>();
        Arrays.stream(s.replaceAll(" ","").toLowerCase().split(""))
                .forEach(i -> counts.put(i, 1 + counts.getOrDefault(i,0)));
        System.out.println(counts.keySet().stream().filter(i -> counts.get(i)==1).limit(1).collect(Collectors.toList()).get(0));

        /* Given a string s, find the length of the longest substring without repeating characters.		String s = "abcabcbb";

        Output : 3
         */

        // 2
        s = "abcabcbbabcdefg";
        int max = 0;
        String sub = "";
        for(int i=0;i<s.length();i++){
            if(!sub.contains(s.substring(i,i+1))) {
                sub += s.substring(i,i+1);
                System.out.println(sub);
            }
            else{
                System.out.println(sub);
                max = Math.max(max , sub.length());
                sub = sub.substring(sub.indexOf(s.substring(i,i+1)) + 1) + s.substring(i,i+1);
                System.out.println(sub);
            }
            System.out.println("============================");
        }
        max = Math.max(max , sub.length());
        System.out.println(max);

        //3
        CompletableFuture<Void> task1 = CompletableFuture
                .runAsync(() -> {
                    System.out.println("Task 1: Hello from " + Thread.currentThread().getName());
                });

        CompletableFuture<Void> task2 = CompletableFuture
                .runAsync(() -> {
                    System.out.println("Task 2: Hi from " + Thread.currentThread().getName());
                });

        CompletableFuture<Void> task3 = CompletableFuture
                .runAsync(() -> {
                    System.out.println("Task 3: Hey from " + Thread.currentThread().getName());
                });

        // Wait for ALL to complete → then print
        CompletableFuture.allOf(task1, task2, task3)
                // Wait here
                .thenRun(() -> System.out.println("All Done!"))
                .join();  // ← This blocks main thread cleanly until all finish

        System.out.println("Main thread exiting...");

        //4
        FairBenchmark.main();

    }
}



class FairBenchmark {
    private static final int TASKS = 2000;
    private static final ExecutorService executor = Executors.newFixedThreadPool(TASKS);

    public static void main() throws Exception {
        testFuture();
        testCompletableFuture();
        executor.shutdown();
    }

    // Future: Parallel submit, but get() loop blocks sequentially
    static void testFuture() throws Exception {
        long start = System.nanoTime();

        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < TASKS; i++) {
            int taskId = i;
            futures.add(executor.submit(() -> {
                Thread.sleep(1000);
                return "Task " + taskId + " done";
            }));
        }

        // Sequential get() – but tasks already parallel
        for (Future<String> f : futures) {
            f.get();
        }

        long time = System.nanoTime() - start;
        System.out.println("Future (with fixed pool): " + time / 1_000_000 + " ms");
    }

    // CompletableFuture: Parallel with same pool, non-blocking join
    static void testCompletableFuture() throws Exception {
        long start = System.nanoTime();

        List<CompletableFuture<String>> cfs = new ArrayList<>();
        for (int i = 0; i < TASKS; i++) {
            int taskId = i;
            cfs.add(CompletableFuture.supplyAsync(() -> {
                try { Thread.sleep(1000); } catch (Exception e) {}
                return "Task " + taskId + " done";
            }, executor));  // Use same executor!
        }

        // Non-blocking allOf + join
        CompletableFuture.allOf(cfs.toArray(new CompletableFuture[0]))
                .join();

        long time = System.nanoTime() - start;
        System.out.println("CompletableFuture (with fixed pool): " + time / 1_000_000 + " ms");
    }
}