//https://www.youtube.com/watch?v=rPSL1alFIjI
// code : https://github.com/amigoscode/java-functional-programming


package main.java.com.example;

// declarative - what to do
// imperative - how to do

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
/*
1. Valid declarations
2. Functional programming terminology / terminologies
3. Streams
 */

public class FunctionalJavaDeclarative {
    public static void main(String[] args) {

        /************************************  1. Valid declarations  ************************************/
        // valid (to improve readability)
        int oneMillion = 1_000_000;
        long creditCard = 1234_5678_9012_3456L;
        double pi = 3.14_15_92;
        long hexBytes = 0xFF_EC_DE_5E;

        /*
        invalid

        int x = _1000;      // ❌ underscore at start
        int y = 1000_;      // ❌ underscore at end
        double d = 100_.0;  // ❌ next to decimal
        */

        new ArrayList<>(Arrays.asList("A","AA","B","C","D","E")).stream()
                .filter(i->i.startsWith("A"))
                .distinct()
                .map(i->(int)i.charAt(0))
                .map(i->String.valueOf(i).repeat(3))
                .sorted()
                .distinct()
                .parallel()
                .limit(100)
                .forEach(System.out::println);


        /************************************  2. Functional programming terminology / terminologies  ************************************/
        // inline static helper
        Function<String, Integer> strlen =
                s -> s.trim().toLowerCase().toUpperCase().substring(0,s.length()).concat(s).repeat(3).length()
        ;
        System.out.println(strlen.apply("Hello"));



        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        // Lambda functions
        names.forEach(name -> {
            System.out.println(name);
        });
        // method references
        names.forEach(System.out::println);
        // Collection processing
        names.stream().filter(i->i.startsWith("A")).map(j->j.toUpperCase()).toList().forEach(System.out::println);

        // Functional Interfaces
        Predicate<String> isShort = s -> s.length() < 5;
        Function<String, Integer> length = String::length;
        Supplier<Double> random = Math::random;
        Consumer<String> printer = System.out::println;

        System.out.println(isShort.test("Hi"));  // true  (Predicate)
        System.out.println(length.apply("Hello")); // 5   (Function)
        printer.accept("Hello Functional Java!");  //     (Consumer)

        // parallel streams
        System.out.println(IntStream.range(1,1000000).parallel().sum());

        // currying / higher-order functions
        Function<Integer,Function<Integer,Integer>> adder = x -> y -> x+y;
        System.out.println(adder.apply(5).apply(10));


 /************************************  3. Streams  ************************************/

 System.out.println(
         Stream.of(names).filter(i->i==i).distinct().map(i->i)
                 .collect(Collectors.toList())
                 .stream().toList()
                 .stream().toList()
                 .stream().toList()

 );

 System.out.println("ln(10):" + Math.log(10));
 System.out.println(Math.log(32) / Math.log(2));
 System.out.println(Math.log(2147483647) / Math.log(2));

 // Lists
         var list1 = new ArrayList<>(List.of(1,2,3,4,5));
         var list2 = new ArrayList<>(list1);
        Collections.reverse(list2);
        System.out.println("list2:" + list2);
        System.out.println(list2.equals(list1));
        Collections.reverse(list1);
        System.out.println(list2.equals(list1));
    }
}
