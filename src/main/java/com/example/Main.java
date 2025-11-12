package com.example;

import java.lang.*;
import java.util.*;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        for (int i = 1; i <= 5; i++)
            System.out.println("""
                    i = """ + i);

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        int n = sc.nextInt();
        int[] arr = new int[n];
        for(int i=0;i<n;i++)arr[i] = sc.nextInt();
        System.out.println(t);
        System.out.println(n);
        System.out.println(
                Arrays.toString(arr)
        );

        var list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        System.out.println(list);
    }
}