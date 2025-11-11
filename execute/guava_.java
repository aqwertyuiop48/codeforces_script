import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.primitives.*;
import com.google.common.io.*;
import com.google.common.cache.*;
import com.google.common.util.concurrent.*;
import com.google.common.math.*;
import com.google.common.hash.*;
import com.google.common.graph.*;

public class guava_ {
    public static void main(String[] args) {
        // Example using Guava's ImmutableList and Joiner
        ImmutableList<String> fruits = ImmutableList.of("apple", "banana", "cherry");
        String result = Joiner.on(", ").join(fruits);
        System.out.println("Fruits: " + result);

        ImmutableList<String> list = ImmutableList.of("apple", "banana", "cherry");
        System.out.println("ImmutableList: " + list);

        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("fruit", "apple");
        multimap.put("fruit", "banana");
        multimap.put("vegetable", "carrot");
        System.out.println("Multimap: " + multimap);

        BiMap<String, String> biMap = HashBiMap.create();
        biMap.put("username1", "email1@example.com");
        biMap.put("username2", "email2@example.com");
        System.out.println("BiMap: " + biMap);
        System.out.println("Inverse lookup: " + biMap.inverse().get("email1@example.com"));
    }
}
