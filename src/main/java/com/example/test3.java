package com.example;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class test3 {
    public static void main(String[] args) {

        System.out.println("========================== StackUsingQueue ===============================");
        StackUsingQueue stack = new StackUsingQueue();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        stack.printStack();

        System.out.println(stack.top());   // 30
        System.out.println(stack.pop());   // 30
        System.out.println(stack.pop());   // 20

        stack.push(40);
        stack.pushAll(List.of(100,200,300,400));

        System.out.println("=========================================================");
        for (int x : stack) System.out.print(x + " ");
        System.out.println();
        System.out.println(stack);

        System.out.println(stack.top());   // 40
        System.out.println(stack.pop());   // 40
        System.out.println(stack.pop());   // 10

        System.out.println(stack.isEmpty()); // true

        System.out.println("========================== QueueUsingStack ===============================");
        QueueUsingStack q = new QueueUsingStack();

        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);

        System.out.println(q.dequeue()); // 10
        System.out.println(q.dequeue()); // 20

        q.enqueue(40);
        q.enqueueAll(List.of(800,700, 600));
        System.out.println(q);

        System.out.println(q.dequeue()); // 30
        System.out.println(q.dequeue()); // 40

        System.out.println(q.peek());
        System.out.println(q.isEmpty());

        System.out.println("=========================================================");



    }
}
