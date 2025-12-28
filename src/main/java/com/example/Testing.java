package com.example;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.*;

public class Testing {
    public static void main(String[] args) {

        Question35.main();
        // Practice
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5));
        list.add(2);
        System.out.println(Arrays.toString(list.toArray()));

        int[][] arr = new int[][]{{1,3},{3,4},{6,7}};
        System.out.println(Arrays.deepToString(arr));
        List<List<Integer>> list1 = (Arrays.stream(arr).map(i->Arrays.stream(i).boxed().collect(Collectors.toList())).collect(Collectors.toList()));

        System.out.println(Arrays.toString(list1.toArray()));

        int[][] matrix = {{1,1,1},{1,0,1},{1,1,1}};
        int[][] rows = matrix, columns = matrix;
        for(int i=0;i<matrix.length;i++){
            if(Arrays.asList(matrix[i]).contains(0))
                rows[i] = new ArrayList<>(Collections.nCopies(matrix.length,0)).stream().mapToInt(Integer::intValue).toArray();
            else rows[i] = matrix[i];
        }
        System.out.println(Arrays.deepToString(rows));

        ArrayList<Integer> list2 = new ArrayList<>();
        list2.addAll(new ArrayList<>(Arrays.asList(7,8,6,5,34)));
        System.out.println(list2);

        System.out.println(add(2,3));
    }

    public static int add(int a, int b){
        return a+b;
    }

    public class Question35 {

        public static void main() {
            int a[] = {10, 20, 30};
            int i = 5, j = 0;

            try {

                System.out.println(a[3]);
                System.out.println(i / j);

            } catch (ArithmeticException e) {
                System.out.println("Line 1");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Line 2");
            } catch (Exception e) {
                System.out.println("Line 3");
            }
            // unreachable code (runtime exception)
//            catch (NumberFormatException e) {
//                System.out.println("Line 4");
//            }
        }
    }

}

class StackUsingQueue implements Iterable<Integer> {
    Queue<Integer> q1 = new LinkedList<>();
    Queue<Integer> q2 = new LinkedList<>();

    // Push element onto stack
    public void push(int x) {
        q2.add(x);

        while (!q1.isEmpty()) {
            q2.add(q1.poll());
        }

        // swap queues
        Queue<Integer> temp = q1;
        q1 = q2;
        q2 = temp;
    }

    public void pushAll(Collection<Integer> values) {
        for (int x : values) {
            push(x);
        }
    }

    // Pop element from stack
    public int pop() {
        if (q1.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return q1.poll();
    }

    public int top() {
        return q1.peek();
    }

    public boolean isEmpty() {
        return q1.isEmpty();
    }

    public void printStack() {
        for (int x : q1) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return q1.iterator();
    }

    @Override
    public String toString() {
        return "StackUsingQueue{" +
                "q1=" + q1+
                '}';
    }
}

class QueueUsingStack {
    private Deque<Integer> in = new ArrayDeque<>();
    private Deque<Integer> out = new ArrayDeque<>();

    // Enqueue → O(1)
    public void enqueue(int x) {
        in.push(x);
    }

    public void enqueueAll(Collection<Integer> values) {
        for (int x : values) {
            enqueue(x);
        }
    }


    // Dequeue → Amortized O(1)
    public int dequeue() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }

        if (out.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        return out.pop();
    }

    public int peek() {
        if (out.isEmpty()) {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
        }

        if (out.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        return out.peek();
    }

    public boolean isEmpty() {
        return in.isEmpty() && out.isEmpty();
    }

    @Override
    public String toString() {
        return "QueueUsingStack{" +
                "in=" + in +
                ", out=" + out +
                '}';
    }
}
