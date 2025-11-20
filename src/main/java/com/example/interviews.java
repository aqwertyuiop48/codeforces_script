package com.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;

public class interviews {
    public static void main_(String[] args) {
        System.out.println("======================================================================");
        String s = "uytrew";
        char[] arr = s.toCharArray();
        Arrays.sort(arr);
        System.out.println(new String(arr));

        // Hashmap of frequencies
        s = "banana";
        Map<String,Integer> freq = new HashMap<>() , freq1 = new HashMap<>();
        s.chars().forEach(ch -> freq.merge(String.valueOf((char)ch) ,1 , Integer::sum));
        System.out.println(freq);
        s.chars().forEach(ch -> freq1.put(String.valueOf((char)ch) , 1 + freq1.getOrDefault(String.valueOf((char)ch),0)));
        System.out.println(freq1);

        // Functional interfaces

        // Predicate
        Predicate<Integer> p1 = i -> i>0;
        System.out.println(p1.test(3));
        System.out.println(((Predicate<Integer>)(i -> i > 0)).test(-1));
        Predicate<Integer> p2 = i -> i<0;
        System.out.println(p2.test(5));
        // BiPredicate
        BiPredicate<Integer, Integer> bp1 = (i,j) -> i>j;
        System.out.println(bp1.test(3,8));

        // Function
        Function<Integer,Integer> f1 = i -> i+4;
        System.out.println(f1.apply(6));
        // BiFunction
        BiFunction<Integer,Integer, Integer> bf1 = (i,j) -> i+j;
        System.out.println(bf1.apply(1,-2));
        BiFunction<List<Double>, Double, List<Double>> bf2 = (i, j) -> {
            i.add(j);
            return i;
        };
        System.out.println(bf2.apply(Arrays.stream(new double[]{1,2,3,4}).mapToObj(Double::valueOf).collect(Collectors.toList()), (double) -2));

        // Supplier
        Supplier<String> s1 = () -> "555";
        System.out.println(s1.get());

        // Consumer
        Consumer<String> c1 = a -> System.out.println(a);
        c1.accept("qwerty");
        // BiConsumer
        BiConsumer<String, String> c2 = (a,b) -> System.out.println(a+b);
        c2.accept("qwerty","zxcvbnm");

        // custom
        @FunctionalInterface
        interface SI{double calculate(double p, double t, double r); default int g(){return 2;}}
        SI si = (p,t,r) -> p*t*r / 100;
        System.out.println(si.calculate(3,4,5));
        System.out.println(si.g());

        @FunctionalInterface
        interface S2{double calculate(double p, double r, double t);}
        S2 s2 = (p,t,r) -> (p*t*r)/100;
        System.out.println(s2.calculate(3,4,5));


        System.out.println("======================================================================");
    }
}
