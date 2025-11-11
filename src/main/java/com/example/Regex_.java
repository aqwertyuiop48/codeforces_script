// http://regex101.com

package main.java.com.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex_ {
    public static void main(String[] args) {
        System.out.printf("""
        %s
        %s
        %s
        %s
        %s
        %s
        %s
        %s
        %s
        %s
        """,
                Pattern.compile("hello").matcher("hel").find(),
                Pattern.compile("agent \\d\\d\\d").matcher("agent 007").find(),
                Pattern.compile("agent \\d{3}").matcher("agent 007").find(),
                Pattern.compile("agent \\D{3}").matcher("agent 007").find(),
                Pattern.compile("agent \\D{3}").matcher("agent qwe").find(),
                Pattern.compile("agent \\d{3,5}").matcher("agent 00007").find(),
                Pattern.compile("^agent \\d{3,5}").matcher("agent 00007").find(),
                Pattern.compile("^agent \\d{3,5}$").matcher("agent 00007").find(),
                Pattern.compile("^[a|A]gent \\d{3,5}$").matcher("agent 00007").find(),
                Pattern.compile("^[b|A]gent \\d{3,5}$").matcher("agent 00007").find()
        );

    }
}
