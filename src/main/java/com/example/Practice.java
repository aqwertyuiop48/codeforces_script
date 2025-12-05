package com.example;

import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Practice {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Hi");

        // anonymous inner class
        new Runnable(){
            @Override
            public void run(){
                System.out.println("Running anonymous inner class.");
            }
        }.run();

        // Files

        Path path = Path.of("test.txt");
        // clear file
        Files.writeString(path,"");
        // write into file
        Files.writeString(path, """
        Hello World
        Hello World1
        Hello World11
        Hello World111
        Hello World1111
        """);

        List<String> list = Files.readAllLines(path);
        list.sort(Comparator.comparingInt(String::length).reversed());
        Files.write(path,list);
        System.out.println(Files.readString(path));
        Files.writeString(path, """
        appended line
        """, StandardOpenOption.APPEND);
        Files.write(path, list, StandardOpenOption.APPEND);
        System.out.println(Files.readString(path));

        // shallow copy (modifies even original)
        List<Integer> l1 = new ArrayList<>(Arrays.asList(1,2,4)) , l2 = l1;
        l2.set(1,90);

        // deep copy (does not modify original)
        List<Integer> l3 = new ArrayList<>(l1);
        l3.set(1,900);
        System.out.println(l3);
        System.out.println(l2);
        System.out.println(l1);

        List<Integer> _l1 = new ArrayList<>(Arrays.asList(1,2,4)) ,
                _l2 = _l1, // shallow copy
                _l3 = new ArrayList<>(_l1), // deep copy
                _l4 = (List<Integer>) ((ArrayList<Integer>) _l1).clone();  // deep copy

        _l1.set(1,90);
        System.out.println(_l2);
        System.out.println(_l1);
        System.out.println(_l3);
        System.out.println(_l4);

        // linked list
        Queue<Integer> linked_list = new LinkedList<>();
        linked_list.addAll(Arrays.asList(2,3,4,5,6));
        System.out.println(linked_list);
        System.out.println(linked_list.peek());
        System.out.println(linked_list);
        System.out.println(linked_list.poll());
        System.out.println(linked_list);

        // thread with lambda
        Counter count = new Counter(), countSynchronized = new Counter();
        new Thread(()-> System.out.println("Thread running!")).run();

        // without synchronized keyword
        Thread t1 = new Thread(()-> {for (int i=0;i<1000;i++) count.increment();});
        Thread t2 = new Thread(()-> {for (int i=0;i<1000;i++) count.increment();});

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count.count);

        // synchronized keyword (blocking other threads)
        Thread t3 = new Thread(()-> {for (int i=0;i<1000;i++) countSynchronized.incrementSynchronized();});
        Thread t4 = new Thread(()-> {for (int i=0;i<1000;i++) countSynchronized.incrementSynchronized();});

        t3.start();
        t4.start();
        t3.join();
        t4.join();
        System.out.println(countSynchronized.countSynchronized);

        // Transient , serializable
//        transient prevents a field from being serialized.
//        After deserialization, that field becomes null/0/false.
        User u1 = new User("John", "secret123");

        // ------------ Serialization --------------
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("user.ser")
        );
        oos.writeObject(u1);
        oos.close();
        System.out.println("Serialized: " + u1.name + " " + u1.password);

        // ------------ Deserialization ------------
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("user.ser")
        );
        User u2 = (User) ois.readObject();
        ois.close();
        System.out.println("Deserialized: " + u2.name + " " + u2.password);


    }
}

class Counter {
    int count = 0, countSynchronized = 0;

    void increment() {
        count++;          // NOT synchronized → race condition
    }
    synchronized void incrementSynchronized() {
        countSynchronized++;          // NOT synchronized → race condition
    }
}

class User implements Serializable {
    String name;
    transient String password; // will NOT be saved

    User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}