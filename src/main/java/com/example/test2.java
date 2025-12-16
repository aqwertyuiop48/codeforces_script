package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class test2 {
    public static void main(String[] args) throws InterruptedException {

        // Flattened nested list
        System.out.println(Arrays.stream(Arrays.deepToString(
                        new Object[]{
                                1,
                                new Object[]{2, 3},
                                new Object[]{4, new Object[]{5, 6}}
                        })
                .replaceAll("\\[","").replaceAll("\\]","").replaceAll(" ","").split(",")).map(i->Long.valueOf(i)).collect(Collectors.toList()));

        System.out.println("Hi");

        int[] bubble_arr = new int[]{5,4,5,3,2,1};
        // bubble sort
        System.out.println("=================== Bubble sort ==================");
        for(int i=0;i<bubble_arr.length-1;i++){
            int swap = 0;
            for(int j=i+1;j<bubble_arr.length;j++){
                if(bubble_arr[i] > bubble_arr[j]){
                    swap = 1;
                    int temp = bubble_arr[i];
                    bubble_arr[i] = bubble_arr[j];
                    bubble_arr[j] = temp;
                }

            }
            if(swap != 1) break;
        }
        System.out.println(Arrays.toString(bubble_arr));


        int[] selection_arr = new int[]{5,4,5,3,2,1};
        // selection sort
        System.out.println("=================== Selection sort ==================");
        for(int i=0;i<selection_arr.length;i++){
            int min = Arrays.stream(Arrays.copyOfRange(selection_arr,i,selection_arr.length)).min().getAsInt(),
                    min_index = IntStream.range(i,selection_arr.length).filter(j->selection_arr[j]==min).findFirst().orElse(-1),
                    temp = min;
            selection_arr[min_index] = selection_arr[i];
            selection_arr[i] = temp;
        }
        System.out.println(Arrays.toString(selection_arr));


        int[] insertion_arr = new int[]{5,4,5,3,2,1};
        // Insertion sort
        System.out.println("=================== Insertion sort ==================");

        List<Integer> sorted = new ArrayList<>();
        for(int x : insertion_arr){
            int pos = 0;
            while(pos < sorted.size() && sorted.get(pos)<x) pos++;
            sorted.add(pos,x);
        }
        System.out.println(Arrays.toString(sorted.toArray()));

        // Merge sort
        System.out.println("=================== Merge sort ==================");
        int[] merge_arr = new int[]{5,4,5,3,2,1};
        System.out.println(mergeSort(Arrays.stream(merge_arr).boxed().collect(Collectors.toList())));

        int[] merge_arr1 = new int[]{5,4,5,3,2,1};
        System.out.println(Arrays.toString(mergeSort1(Arrays.stream(merge_arr1).boxed().collect(Collectors.toList())).toArray()));
        System.out.println(Arrays.toString(mergeSort1(Arrays.stream(merge_arr1).boxed().collect(Collectors.toList())).stream().mapToInt(i->i.intValue()).toArray()));

        System.out.println("==============");

        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5));
        list.stream().forEach(i-> System.out.println(i));
        System.out.println("==============");
        list.parallelStream().forEach(i-> System.out.println(i));
        System.out.println("==============");

        // platform thread (memory becomes exhausted, context switching is difficult)
        List<Thread> platformThread = new ArrayList<>();
        for(int i=0;i<5;i++){
            Thread thread = new Thread(() ->{
                System.out.println(Thread.currentThread().threadId());
                System.out.println(Thread.currentThread().getName());
            });
            platformThread.add(thread);
            thread.start();
        }
        for(Thread i : platformThread) i.join();

        System.out.println("==============");
        // virtual thread
        List<Thread> virtualThread = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread t = Thread.ofVirtual()
                    .name("vt-",i)
                    .start(() -> {
                System.out.println(Thread.currentThread().threadId());
                System.out.println(Thread.currentThread().getName());
            });

            virtualThread.add(t);   // now you can store it
        }
        for(Thread i : virtualThread) i.join();

        // Threads

        // 1 (old style)
        new MyThread().start();
        // 2
        new Thread(new MyRunnable()).start();
        // 3 (anonymous classes)
        new Thread(new Runnable(){
            public void run(){
                System.out.println("Anonymous class thread:" + Thread.currentThread().getName());
            }
        }).start();
        // 4 (lambda)
        new Thread(() -> System.out.println("Lambda function thread:" + Thread.currentThread().getName())).start();


    }

    public static List<Integer> mergeSort(List<Integer> list) {
        if (list.size() <= 1) return list;  // already sorted

        int mid = list.size() / 2;

        List<Integer> left = mergeSort(list.subList(0, mid));
        List<Integer> right = mergeSort(list.subList(mid, list.size()));

        return merge(left, right);
    }

    public static List<Integer> mergeSort1(List<Integer> list) {
        if(list.size()<=1) return list;

        List<Integer> left = (list.subList(0, list.size()/2));
        List<Integer> right = (list.subList(list.size()/2, list.size()));
        List<Integer> result = new ArrayList<>();

        left = mergeSort1(new ArrayList<>(left));
        right = mergeSort1(new ArrayList<>(right));

        int i=0,j=0;
        while(i<left.size() && j<right.size()){
            if(left.get(i) <= right.get(j))result.add(left.get(i++));
            else result.add(right.get(j++));
        }
        while(i<left.size()) result.add(left.get(i++));
        while(j<right.size()) result.add(right.get(j++));

        return result;
    }

    private static List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            merged.add(left.get(i) <= right.get(j) ? left.get(i++) : right.get(j++));
        }

        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));

        return merged;
    }
}

class MyThread extends Thread{
    public void run() {
        System.out.println("Thread extended class:" + Thread.currentThread().getName());
    }
}

class MyRunnable implements Runnable{
    public void run() {
        System.out.println("Runnable implemented class:" + Thread.currentThread().getName());
    }
}
