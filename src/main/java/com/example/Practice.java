package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Practice {
    public static void main(String[] args) throws IOException {
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
            }
}
