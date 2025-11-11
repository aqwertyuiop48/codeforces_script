package main.java.com.example;

import java.io.IOException;
import java.math.BigInteger;

public class MainJava {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("/**************************** KotlinInJava ****************************/");
        main.java.com.example.KotlinInJava.main(new String[]{});

        System.out.println("/**************************** FunctionalJavaDeclarative ****************************/");
        main.java.com.example.FunctionalJavaDeclarative.main(new String[]{});

        System.out.println("/**************************** Data Structures ****************************/");
        main.java.com.example.DataStructures.main(new String[]{});

        System.out.println("/**************************** Multithreading ****************************/");
        main.java.com.example.Multithreading.main(new String[]{});

        System.out.println("/**************************** Regex ****************************/");
        main.java.com.example.Regex_.main(new String[]{});

        // java.lang.NegativeArraySizeException: -1029996288 (Numeric overflow in expression)
        //BigInteger[] arr = new BigInteger[900 * 900 * 900 * 900];
    }
}
