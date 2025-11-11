package main.java.com.example;

/*
1. Dynamic array
2. Singly linked list
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

import io.reactivex.rxjava3.core.Observable;
import org.graalvm.polyglot.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// spark
import org.apache.spark.api.java.*;

// Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class DataStructures {
    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            System.out.println("Data structures!");

            System.out.println("/**************************************** Dynamic array **************************************/");
            dynamic_array();
            System.out.println("/**************************************** Singly Linked List **************************************/");
            linked_list();
            System.out.println("/**************************************** Lists **************************************/");
            lists();
            System.out.println("/**************************************** Sets **************************************/");
            sets();
            System.out.println("/**************************************** Queues **************************************/");
            queues();
            System.out.println("/**************************************** Dequeues **************************************/");
            dequeues();
            System.out.println("/**************************************** Maps **************************************/");
            maps();
            System.out.println("/**************************************** Stacks **************************************/");
            stacks();
            System.out.println("/**************************************** Dynamic programming **************************************/");
            dynamic_programming();
        }
        

        finally {
         //START : TO BE COMMENTED IN LOCAL
            JavaSparkContext sc = new JavaSparkContext("local", "test");
            System.out.println("/**************************************** Strings **************************************/");
            strings(sc);
            sc.stop();
            System.out.println("/**************************************** MySQL **************************************/");
            mysql_();
            System.out.println("/**************************************** MySQL JDBC **************************************/");
            mysql_jdbc();
         //END : TO BE COMMENTED IN LOCAL
                    }
    }

    // 181. Employees Earning More Than Their Managers
    public static void mysql_() throws IOException, InterruptedException {
            // Wrap your whole script in bash -c
            String script = """
            echo "Waiting for MySQL to be ready..."
            for i in {1..30}; do
              if mysqladmin ping -h 127.0.0.1 -u root --password=example --silent; then
                echo "MySQL is ready!"
                break
              fi
              echo "Waiting for MySQL..."
              sleep 2
            done

            sudo apt-get update && sudo apt-get install -y mysql-client

            echo "Connecting to MySQL and creating a table..."
            mysql -h 127.0.0.1 -u sample_user -psample_password sample_db -e "
            
            Create table If Not Exists Employee (id int, name varchar(255), salary int, managerId int);
            Truncate table Employee;
            INSERT INTO Employee (id, name, salary, managerId) VALUES (1, 'Joe', 70000, 3);
            INSERT INTO Employee (id, name, salary, managerId) VALUES (2, 'Henry', 80000, 4);
            INSERT INTO Employee (id, name, salary, managerId) VALUES (3, 'Sam', 60000, NULL);
            INSERT INTO Employee (id, name, salary, managerId) VALUES (4, 'Max', 90000, NULL);
            
            "

            echo "Querying the database..."
            mysql -h 127.0.0.1 -u sample_user -psample_password sample_db -e "
            
            select e.name as Employee from Employee e join Employee m on e.managerId = m.id and e.salary > m.salary; 
            
            "
        """;

            // Use ProcessBuilder with bash -c
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", script);
            pb.redirectErrorStream(true); // merge stdout and stderr

            Process process = pb.start();

            // Read the output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
    }

    public static void mysql_jdbc() {
        String url = "jdbc:mysql://127.0.0.1:3306/sample_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "sample_user";
        String password = "sample_password";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Create table if not exists
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Employee (
                    id INT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    salary INT NOT NULL,
                    managerId INT
                )
            """);

            // Truncate table
            stmt.execute("TRUNCATE TABLE Employee");

            // Insert records
            stmt.executeUpdate("INSERT INTO Employee (id, name, salary, managerId) VALUES (1, 'Joe', 70000, 3)");
            stmt.executeUpdate("INSERT INTO Employee (id, name, salary, managerId) VALUES (2, 'Henry', 80000, 4)");
            stmt.executeUpdate("INSERT INTO Employee (id, name, salary, managerId) VALUES (3, 'Sam', 60000, NULL)");
            stmt.executeUpdate("INSERT INTO Employee (id, name, salary, managerId) VALUES (4, 'Max', 90000, NULL)");

            // Query
            ResultSet rs = stmt.executeQuery("""
                SELECT e.name AS Employee
                FROM Employee e
                JOIN Employee m ON e.managerId = m.id
                WHERE e.salary > m.salary
            """);

            // Print results
            System.out.println("Employees earning more than their managers:");
            while (rs.next()) {
                System.out.println(rs.getString("Employee"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Dynamic programming
    public static void dynamic_programming() {
        dynamic_programming_ dynamic_programming_ = new dynamic_programming_();

        System.out.println("---------------------------------------------53. Maximum Subarray---------------------------------------------");
        for(var i:List.of(new int[]{-2,1,-3,4,-1,2,1,-5,4}, new int[]{1},new int[]{5,4,-1,7,8})){
            System.out.println(dynamic_programming_.maxSubArray(i));
            System.out.println(dynamic_programming_.maxSubArrayRx(i));
            System.out.println(dynamic_programming_.maxSubArrayRx1(i));
            System.out.println("---------------------------------------------");
        }
    }

    // Strings
    public static void strings(JavaSparkContext sc){
            Strings_ strings_ = new Strings_();
            System.out.println("---------------------------------------------12. Integer to Roman---------------------------------------------");
            System.out.println(strings_.intToRoman(3749));
            System.out.println(strings_.intToRoman(58));
            System.out.println(strings_.intToRoman(1994));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.intToRomanRx(3749));
            System.out.println(strings_.intToRomanRx(58));
            System.out.println(strings_.intToRomanRx(1994));

            System.out.println("---------------------------------------------13. Roman to Integer---------------------------------------------");
            System.out.println(strings_.romanToInt("III"));
            System.out.println(strings_.romanToInt("LVIII"));
            System.out.println(strings_.romanToInt("MCMXCIV"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.romanToIntRx("III"));
            System.out.println(strings_.romanToIntRx("LVIII"));
            System.out.println(strings_.romanToIntRx("MCMXCIV"));

            System.out.println("---------------------------------------------17. Letter Combinations of a Phone Number---------------------------------------------");
            System.out.println(strings_.letterCombinations("23"));
            System.out.println(strings_.letterCombinations(""));
            System.out.println(strings_.letterCombinations("2"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.letterCombinationsRx("23"));
            System.out.println(strings_.letterCombinationsRx(""));
            System.out.println(strings_.letterCombinationsRx("2"));

            System.out.println("--------------------------------------------- 3. Longest Substring Without Repeating Characters ---------------------------------------------");
            System.out.println(strings_.lengthOfLongestSubstring("abcabcbb"));
            System.out.println(strings_.lengthOfLongestSubstring("bbbbb"));
            System.out.println(strings_.lengthOfLongestSubstring("pwwkew"));
            System.out.println(strings_.lengthOfLongestSubstring("aabaab!bb"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.lengthOfLongestSubstringRx("abcabcbb"));
            System.out.println(strings_.lengthOfLongestSubstringRx("bbbbb"));
            System.out.println(strings_.lengthOfLongestSubstringRx("pwwkew"));
            System.out.println(strings_.lengthOfLongestSubstringRx("aabaab!bb"));

            System.out.println("--------------------------------------------- 5. Longest Palindromic Substring ---------------------------------------------");
            System.out.println(strings_.longestPalindrome("babad"));
            System.out.println(strings_.longestPalindrome("cbbd"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.longestPalindromeRx("babad"));
            System.out.println(strings_.longestPalindromeRx("cbbd"));

            System.out.println("--------------------------------------------- (Assessment question) 1122. Relative Sort Array ---------------------------------------------");
            System.out.println(Arrays.toString(strings_.relativeSortArray(new int[]{2, 3, 1, 3, 2, 4, 6, 7, 9, 2, 19}, new int[]{2, 1, 4, 3, 9, 6})));
            System.out.println(Arrays.toString(strings_.relativeSortArray(new int[]{28, 6, 22, 8, 44, 17}, new int[]{22, 28, 8, 6})));
            System.out.println("---------------------------------------------");
            System.out.println(Arrays.toString(strings_.relativeSortArrayRx(new int[]{2, 3, 1, 3, 2, 4, 6, 7, 9, 2, 19}, new int[]{2, 1, 4, 3, 9, 6})));
            System.out.println(Arrays.toString(strings_.relativeSortArrayRx(new int[]{28, 6, 22, 8, 44, 17}, new int[]{22, 28, 8, 6})));

            System.out.println("--------------------------------------------- 20. Valid Parentheses ---------------------------------------------");
            System.out.println(strings_.isValid("()"));
            System.out.println(strings_.isValid("()[]{}"));
            System.out.println(strings_.isValid("(]"));
            System.out.println(strings_.isValid("([])"));
            System.out.println(strings_.isValid("([)]"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.isValidRx("()"));
            System.out.println(strings_.isValidRx("()[]{}"));
            System.out.println(strings_.isValidRx("(]"));
            System.out.println(strings_.isValidRx("([])"));
            System.out.println(strings_.isValidRx("([)]"));

            System.out.println("--------------------------------------------- 38. Count and Say ---------------------------------------------");
            System.out.println(strings_.countAndSay(4));
            System.out.println(strings_.countAndSay(1));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.countAndSay1(4));
            System.out.println(strings_.countAndSay1(1));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.countAndSayRx(4));
            System.out.println(strings_.countAndSayRx(1));

            System.out.println("--------------------------------------------- 43. Multiply Strings ---------------------------------------------");
            System.out.println(strings_.multiply("2", "3"));
            System.out.println(strings_.multiply("123", "456"));

            System.out.println("--------------------------------------------- 49. Group Anagrams ---------------------------------------------");
            System.out.println(strings_.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
            System.out.println(strings_.groupAnagrams(new String[]{""}));
            System.out.println(strings_.groupAnagrams(new String[]{"a"}));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.groupAnagramsRx(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
            System.out.println(strings_.groupAnagramsRx(new String[]{""}));
            System.out.println(strings_.groupAnagramsRx(new String[]{"a"}));

            System.out.println("--------------------------------------------- 58. Length of Last Word ---------------------------------------------");
            System.out.println(strings_.lengthOfLastWord("Hello World"));
            System.out.println(strings_.lengthOfLastWord("   fly me   to   the moon  "));
            System.out.println(strings_.lengthOfLastWord("luffy is still joyboy"));

            System.out.println("--------------------------------------------- 67. Add Binary ---------------------------------------------");
            System.out.println(strings_.addBinary("11", "1"));
            System.out.println(strings_.addBinary("1010", "1011"));

            System.out.println("--------------------------------------------- 125. Valid Palindrome ---------------------------------------------");
            System.out.println(strings_.isPalindrome("A man, a plan, a canal: Panama"));
            System.out.println(strings_.isPalindrome("race a car"));
            System.out.println(strings_.isPalindrome(" "));

            System.out.println("--------------------------------------------- 151. Reverse Words in a String ---------------------------------------------");
            System.out.println(strings_.reverseWords("the sky is blue"));
            System.out.println(strings_.reverseWords("  hello world  "));
            System.out.println(strings_.reverseWords("a good   example"));

            System.out.println("--------------------------------------------- 165. Compare Version Numbers ---------------------------------------------");
            System.out.println(strings_.compareVersion("1.2", "1.10"));
            System.out.println(strings_.compareVersion("1.01", "1.001"));
            System.out.println(strings_.compareVersion("1.0", "1.0.0.0"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.compareVersionRx("1.2", "1.10"));
            System.out.println(strings_.compareVersionRx("1.01", "1.001"));
            System.out.println(strings_.compareVersionRx("1.0", "1.0.0.0"));

            System.out.println("--------------------------------------------- 168. Excel Sheet Column Title ---------------------------------------------");
            System.out.println(strings_.convertToTitle(1));
            System.out.println(strings_.convertToTitle(28));
            System.out.println(strings_.convertToTitle(701));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.convertToTitleRx(1));
            System.out.println(strings_.convertToTitleRx(28));
            System.out.println(strings_.convertToTitleRx(701));

            System.out.println("--------------------------------------------- 171. Excel Sheet Column Number ---------------------------------------------");
            System.out.println(strings_.titleToNumber("A"));
            System.out.println(strings_.titleToNumber("AB"));
            System.out.println(strings_.titleToNumber("ZY"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.titleToNumberRx("A"));
            System.out.println(strings_.titleToNumberRx("AB"));
            System.out.println(strings_.titleToNumberRx("ZY"));

            System.out.println("--------------------------------------------- 179. Largest Number ---------------------------------------------");
            System.out.println(strings_.largestNumber(new int[]{10, 2}));
            System.out.println(strings_.largestNumber(new int[]{3, 30, 34, 5, 9}));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.largestNumberRx(new int[]{10, 2}));
            System.out.println(strings_.largestNumberRx(new int[]{3, 30, 34, 5, 9}));

            System.out.println("--------------------------------------------- 187. Repeated DNA Sequences ---------------------------------------------");
            System.out.println(strings_.findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"));
            System.out.println(strings_.findRepeatedDnaSequences("AAAAAAAAAAAAA"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.findRepeatedDnaSequencesRx("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"));
            System.out.println(strings_.findRepeatedDnaSequencesRx("AAAAAAAAAAAAA"));

            System.out.println("--------------------------------------------- 205. Isomorphic Strings ---------------------------------------------");
            System.out.println(strings_.isIsomorphic("egg", "add"));
            System.out.println(strings_.isIsomorphic("foo", "bar"));
            System.out.println(strings_.isIsomorphic("paper", "title"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.isIsomorphic1("egg", "add"));
            System.out.println(strings_.isIsomorphic1("foo", "bar"));
            System.out.println(strings_.isIsomorphic1("paper", "title"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.isIsomorphicRx("egg", "add"));
            System.out.println(strings_.isIsomorphicRx("foo", "bar"));
            System.out.println(strings_.isIsomorphicRx("paper", "title"));

            System.out.println("--------------------------------------------- 227. Basic Calculator II ---------------------------------------------");
            System.out.println(strings_.calculate("3+2*2"));
            System.out.println(strings_.calculate(" 3/2 "));
            System.out.println(strings_.calculate(" 3+5 / 2 "));

            System.out.println("--------------------------------------------- 290. Word Pattern ---------------------------------------------");
            System.out.println(strings_.wordPattern("abba", "dog cat cat dog"));
            System.out.println(strings_.wordPattern("abba", "dog cat cat fish"));
            System.out.println(strings_.wordPattern("aaaa", "dog cat cat dog"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.wordPatternRx("abba", "dog cat cat dog"));
            System.out.println(strings_.wordPatternRx("abba", "dog cat cat fish"));
            System.out.println(strings_.wordPatternRx("aaaa", "dog cat cat dog"));

            System.out.println("--------------------------------------------- 318. Maximum Product of Word Lengths ---------------------------------------------");
            System.out.println(strings_.maxProduct(new String[]{"abcw", "baz", "foo", "bar", "xtfn", "abcdef"}));
            System.out.println(strings_.maxProduct(new String[]{"a", "ab", "abc", "d", "cd", "bcd", "abcd"}));
            System.out.println(strings_.maxProduct(new String[]{"a", "aa", "aaa", "aaaa"}));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.maxProductRx(new String[]{"abcw", "baz", "foo", "bar", "xtfn", "abcdef"}));
            System.out.println(strings_.maxProductRx(new String[]{"a", "ab", "abc", "d", "cd", "bcd", "abcd"}));
            System.out.println(strings_.maxProductRx(new String[]{"a", "aa", "aaa", "aaaa"}));

            System.out.println("--------------------------------------------- 345. Reverse Vowels of a String ---------------------------------------------");
            System.out.println(strings_.reverseVowels("IceCreAm"));
            System.out.println(strings_.reverseVowels("leetcode"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.reverseVowelsRx("IceCreAm"));
            System.out.println(strings_.reverseVowelsRx("leetcode"));

            System.out.println("--------------------------------------------- 383. Ransom Note ---------------------------------------------");
            System.out.println(strings_.canConstruct("a", "b"));
            System.out.println(strings_.canConstruct("aa", "ab"));
            System.out.println(strings_.canConstruct("aa", "aab"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.canConstructRx("a", "b"));
            System.out.println(strings_.canConstructRx("aa", "ab"));
            System.out.println(strings_.canConstructRx("aa", "aab"));

            System.out.println("--------------------------------------------- 392. Is Subsequence ---------------------------------------------");
            System.out.println(strings_.isSubsequence("abc", "ahbgdc"));
            System.out.println(strings_.isSubsequence("axc", "ahbgdc"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.isSubsequenceRx("abc", "ahbgdc"));
            System.out.println(strings_.isSubsequenceRx("axc", "ahbgdc"));

            System.out.println("--------------------------------------------- 409. Longest Palindrome ---------------------------------------------");
            System.out.println(strings_.longestPalindrome1("abccccdd"));
            System.out.println(strings_.longestPalindrome1("a"));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.longestPalindrome1Rx("abccccdd"));
            System.out.println(strings_.longestPalindrome1Rx("a"));

            System.out.println("--------------------------------------------- 434. Number of Segments in a String ---------------------------------------------");
            System.out.println(strings_.countSegments("Hello, my name is John"));
            System.out.println(strings_.countSegments("Hello"));

            System.out.println("--------------------------------------------- 451. Sort Characters By Frequency ---------------------------------------------");
            System.out.println(strings_.frequencySort("tree"));
            System.out.println(strings_.frequencySort("cccaaa"));
            System.out.println(strings_.frequencySort("Aabb"));

            System.out.println("--------------------------------------------- 443. String Compression ---------------------------------------------");
            System.out.println(strings_.compress(new char[]{'a', 'a', 'b', 'b', 'c', 'c', 'c'}));
            System.out.println(strings_.compress(new char[]{'a'}));
            System.out.println(strings_.compress(new char[]{'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'}));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.compressRx(new char[]{'a', 'a', 'b', 'b', 'c', 'c', 'c'}));
            System.out.println(strings_.compressRx(new char[]{'a'}));
            System.out.println(strings_.compressRx(new char[]{'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'}));

            System.out.println("--------------------------------------------- 482. License Key Formatting ---------------------------------------------");
            System.out.println(strings_.licenseKeyFormatting("5F3Z-2e-9-w", 4));
            System.out.println(strings_.licenseKeyFormatting("2-5g-3-J", 2));
            System.out.println("---------------------------------------------");
            System.out.println(strings_.licenseKeyFormattingRx("5F3Z-2e-9-w", 4));
            System.out.println(strings_.licenseKeyFormattingRx("2-5g-3-J", 2));

            System.out.println("--------------------------------------------- 500. Keyboard Row ---------------------------------------------");
            System.out.println(Arrays.toString(strings_.findWords(new String[]{"Hello", "Alaska", "Dad", "Peace"})));
            System.out.println(Arrays.toString(strings_.findWords(new String[]{"omk"})));
            System.out.println(Arrays.toString(strings_.findWords(new String[]{"adsdf", "sfd"})));
            System.out.println("---------------------------------------------");
            System.out.println(Arrays.toString(strings_.findWordsRx(new String[]{"Hello", "Alaska", "Dad", "Peace"})));
            System.out.println(Arrays.toString(strings_.findWordsRx(new String[]{"omk"})));
            System.out.println(Arrays.toString(strings_.findWordsRx(new String[]{"adsdf", "sfd"})));
            System.out.println("---------------------------------------------");
            System.out.println(Arrays.toString(strings_.findWordsSpark(sc,new String[]{"Hello", "Alaska", "Dad", "Peace"})));
            System.out.println(Arrays.toString(strings_.findWordsSpark(sc,new String[]{"omk"})));
            System.out.println(Arrays.toString(strings_.findWordsSpark(sc,new String[]{"adsdf", "sfd"})));

            System.out.println("--------------------------------------------- 524. Longest Word in Dictionary through Deleting ---------------------------------------------");
            System.out.println((strings_.findLongestWord("abpcplea", List.of(new String[]{"ale","apple","monkey","plea"}))));
            System.out.println((strings_.findLongestWord("abpcplea", List.of(new String[]{"a", "b", "c"}))));
            System.out.println("---------------------------------------------");
            System.out.println((strings_.findLongestWordRx("abpcplea", List.of(new String[]{"ale","apple","monkey","plea"}))));
            System.out.println((strings_.findLongestWordRx("abpcplea", List.of(new String[]{"a", "b", "c"}))));
            System.out.println("---------------------------------------------");
            System.out.println((strings_.findLongestWordSpark(sc,"abpcplea", List.of(new String[]{"ale","apple","monkey","plea"}))));
            System.out.println((strings_.findLongestWordSpark(sc,"abpcplea", List.of(new String[]{"a", "b", "c"}))));

            System.out.println("--------------------------------------------- 537. Complex Number Multiplication ---------------------------------------------");
            System.out.println((strings_.complexNumberMultiply("1+1i", "1+1i")));
            System.out.println((strings_.complexNumberMultiply("1+-1i", "1+-1i")));

            System.out.println("--------------------------------------------- 541. Reverse String II ---------------------------------------------");
            System.out.println((strings_.reverseStr("abcdefg", 2)));
            System.out.println((strings_.reverseStr("abcd", 2)));
            System.out.println("---------------------------------------------");
            System.out.println((strings_.reverseStrRx("abcdefg", 2)));
            System.out.println((strings_.reverseStrRx("abcd", 2)));
            System.out.println("---------------------------------------------");
            System.out.println((strings_.reverseStrSpark(sc, "abcdefg", 2)));
            System.out.println((strings_.reverseStrSpark(sc, "abcd", 2)));

            System.out.println("--------------------------------------------- 832. Flipping an Image ---------------------------------------------");
            System.out.println(Arrays.deepToString(strings_.flipAndInvertImage(new int[][]{{1,1,0},{1,0,1},{0,0,0}})));
            System.out.println(Arrays.deepToString(strings_.flipAndInvertImage(new int[][]{{1,1,0,0},{1,0,0,1},{0,1,1,1},{1,0,1,0}})));

            System.out.println("--------------------------------------------- 1002. Find Common Characters ---------------------------------------------");
            System.out.println(strings_.commonChars(new String[]{"bella","label","roller"}));
            System.out.println(strings_.commonChars(new String[]{"cool","lock","cook"}));

            System.out.println("--------------------------------------------- Functional Interface ---------------------------------------------");
            functional_interface();

            System.out.println("------------------------------------------------------------------------------------------");
        }

    // Lists : ordered, allow duplicates
    public static void lists() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("A"); // duplicates allowed
        System.out.println("ArrayList: " + arrayList);

        List<String> linkedList = new LinkedList<>();
        linkedList.add("X");
        linkedList.add("Y");
        linkedList.add("X"); // duplicates allowed
        linkedList = linkedList.reversed();
        Collections.sort(linkedList);
        linkedList.addFirst("A");
        linkedList.addLast("Z");
        System.out.println("LinkedList: " + linkedList);
    }

    // Sets : Unordered, ignore duplicates
    public static void sets() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add("A");
        hashSet.add("B");
        hashSet.add("A"); // duplicate ignored
        System.out.println(hashSet.equals(hashSet));
        System.out.println("HashSet: " + hashSet);

        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("X");
        linkedHashSet.add("Y");
        linkedHashSet.add("X"); // duplicates ignored
        System.out.println("LinkedHashSet (insertion order): " + linkedHashSet);

        Set<String> treeSet = new TreeSet<>();
        treeSet.add("Z");
        treeSet.add("Y");
        treeSet.add("X");
        treeSet.add("X"); // duplicates ignored
        System.out.println(treeSet.contains("A"));
        System.out.println("TreeSet (sorted): " + treeSet);
    }

    // Queue – FIFO behavior (or priority)
    public static void queues() {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(10);
        queue.add(20);
        queue.add(30);
        System.out.println("Queue: " + queue);
        System.out.println("Poll: " + queue.poll()); // removes head
        System.out.println("Queue after poll: " + queue);

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(50);
        pq.add(10);
        pq.add(30);
        System.out.println("PriorityQueue (min-heap): " + pq);
    }

    // Double-ended queue
    public static void dequeues() {
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("A");
        deque.addLast("B");
        deque.addFirst("C");
        System.out.println("Deque: " + deque);
        System.out.println("Remove first: " + deque.removeFirst());
        System.out.println("Deque after removal: " + deque);
    }

    public static void maps() {
        Map<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "One");
        hashMap.put(2, "Two");
        hashMap.put(3, "Three");
        System.out.println("HashMap: " + hashMap);

        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(3, "Three");
        linkedHashMap.put(1, "One");
        linkedHashMap.put(2, "Two");
        System.out.println("LinkedHashMap (insertion order): " + linkedHashMap);

        Map<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(3, "Three");
        treeMap.put(1, "One");
        treeMap.put(2, "Two");
        System.out.println("TreeMap (sorted by key): " + treeMap);

        System.out.println("---------------------------------------------1. Two Sum ---------------------------------------------");
        maps_ maps_ = new maps_();
        System.out.println(Arrays.binarySearch(new int[]{2, 7, 11, 15},7));
        System.out.println(Arrays.binarySearch(new int[]{2, 7, 11, 15},3));
        System.out.println(Arrays.toString(maps_.twoSum(new int[]{2, 7, 11, 15}, 9)));
        System.out.println(Arrays.toString(maps_.twoSum(new int[]{3,2,4}, 6)));
        System.out.println(Arrays.toString(maps_.twoSum(new int[]{3,3}, 6)));
        System.out.println("-------------------------------------");
        System.out.println(Arrays.toString(maps_.twoSumRx(new int[]{2, 7, 11, 15}, 9)));
        System.out.println(Arrays.toString(maps_.twoSumRx(new int[]{3,2,4}, 6)));
        System.out.println(Arrays.toString(maps_.twoSumRx(new int[]{3,3}, 6)));

        System.out.println("--------------------------------------------- 350. Intersection of Two Arrays II ---------------------------------------------");
        System.out.println(Arrays.toString(maps_.intersect(new int[]{1,2,2,1}, new int[]{2,2})));
        System.out.println(Arrays.toString(maps_.intersect(new int[]{4,9,5},new int[]{9,4,9,8,4})));
        System.out.println("-------------------------------------");

    }

    public static void stacks() {
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        System.out.println("Stack: " + stack);
        System.out.println("Pop: " + stack.pop());
        System.out.println("Stack after pop: " + stack);
    }


    public static void dynamic_array(){
        DynamicArray DynamicArray = new DynamicArray();
        DynamicArray.add(3);
        DynamicArray.add(4);
        DynamicArray.add(5);
        DynamicArray.add(2);
        DynamicArray.get(2);
        DynamicArray.set(2,7);
        DynamicArray.remove(30);
        DynamicArray.removeAt(2);
        //DynamicArray.set(2000,7000); // index out of bounds for length 16
        System.out.println(DynamicArray);
    }

    public static void linked_list(){

        // https://leetcode.com/problems/add-two-numbers/editorial/?envType=problem-list-v2&envId=linked-list (2. Add Two Numbers)
        System.out.println("---------------------------------------------2. Add Two Numbers ---------------------------------------------");

        LinkedList_ LinkedList_ = new LinkedList_();
        ListNode result = LinkedList_.mains(new ListNode(2, new ListNode(4, new ListNode(3))) , new ListNode(5, new ListNode(6, new ListNode(4))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        LinkedList_ = new LinkedList_();
        result = LinkedList_.mains(new ListNode(0),new ListNode(0));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        LinkedList_ = new LinkedList_();
        result = LinkedList_.mains(new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9))))))), new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9)))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        System.out.println("---------------------------------------------");

        LinkedList_ = new LinkedList_();
        result = LinkedList_.mainsRx(new ListNode(2, new ListNode(4, new ListNode(3))) , new ListNode(5, new ListNode(6, new ListNode(4))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        LinkedList_ = new LinkedList_();
        result = LinkedList_.mainsRx(new ListNode(0),new ListNode(0));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        LinkedList_ = new LinkedList_();
        result = LinkedList_.mainsRx(new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9))))))), new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9)))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        System.out.println("--------------------------------------------- 83. Remove Duplicates from Sorted List ---------------------------------------------");
        LinkedList_ = new LinkedList_();
        result = LinkedList_.deleteDuplicates(new ListNode(1, new ListNode(1, new ListNode(2))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        LinkedList_ = new LinkedList_();
        result = LinkedList_.deleteDuplicates(new ListNode(1, new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(3))))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        System.out.println("---------------------------------------------");

        LinkedList_ = new LinkedList_();
        result = LinkedList_.deleteDuplicatesRx(new ListNode(1, new ListNode(1, new ListNode(2))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        LinkedList_ = new LinkedList_();
        result = LinkedList_.deleteDuplicatesRx(new ListNode(1, new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(3))))));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();



        // https://leetcode.com/problems/remove-nth-node-from-end-of-list/?envType=problem-list-v2&envId=linked-list (19. Remove Nth Node From End of List)
        System.out.println("---------------------------------------------19. Remove Nth Node From End of List ---------------------------------------------");
        LinkedList_ = new LinkedList_();

        ListNode l1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        result = LinkedList_.nthnode(l1, BigInteger.valueOf(2));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        l1 = new ListNode(1);
        result = LinkedList_.nthnode(l1, BigInteger.valueOf(1));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println(); // ✅ prints nothing, since list becomes []

        l1 = new ListNode(1, new ListNode(2));
        result = LinkedList_.nthnode(l1, BigInteger.valueOf(1));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println(); // ✅ Expected Output: 1

        System.out.println("--------------------------------------------");

        l1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        result = LinkedList_.nthnodeRx(l1, BigInteger.valueOf(2));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println();

        l1 = new ListNode(1);
        result = LinkedList_.nthnodeRx(l1, BigInteger.valueOf(1));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println(); // ✅ prints nothing, since list becomes []

        l1 = new ListNode(1, new ListNode(2));
        result = LinkedList_.nthnodeRx(l1, BigInteger.valueOf(1));
        while (result != null) {System.out.print(result.val + (result.next != null ? " -> " : ""));result = result.next;}
        System.out.println(); // ✅ Expected Output: 1


    }

/*
A functional interface is an interface that has exactly one abstract method.
It can have:
- Any number of default or static methods.
- But only one abstract method.
*/
    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    public static void functional_interface() {
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        System.out.println(add.calculate(2, 3));     // Output: 5
        System.out.println(multiply.calculate(2, 3)); // Output: 6
    }

}


@SuppressWarnings("unchecked")
//https://www.youtube.com/watch?v=RBSGKlAvoiM
class DynamicArray<T> implements Iterable<T> {

    private T[] arr;
    private int len = 0; // length user thinks array is
    private int capacity = 0; // Actual array size

    public DynamicArray() {
        this(16);
    }

    public DynamicArray(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        this.capacity = capacity;
        arr = (T[]) new Object[capacity];
    }

    public int size() {
        return len;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public T get(int index) {
        return arr[index];
    }

    public void set(int index, T elem) {
        arr[index] = elem;
    }

    public void clear() {
        for (int i = 0; i < len; i++) arr[i] = null;
        len = 0;
    }

    public void add(T elem) {

        // Time to resize!
        if (len + 1 >= capacity) {
            if (capacity == 0) capacity = 1;
            else capacity *= 2; // double the size
            T[] new_arr = (T[]) new Object[capacity];
            for (int i = 0; i < len; i++) new_arr[i] = arr[i];
            arr = new_arr; // arr has extra nulls padded
        }

        arr[len++] = elem;
    }

    // Removes an element at the specified index in this array.
    public T removeAt(int rm_index) {
        if (rm_index >= len || rm_index < 0) throw new IndexOutOfBoundsException();
        T data = arr[rm_index];
        T[] new_arr = (T[]) new Object[len - 1];
        for (int i = 0, j = 0; i < len; i++, j++)
            if (i == rm_index) j--; // Skip over rm_index by fixing j temporarily
            else new_arr[j] = arr[i];
        arr = new_arr;
        capacity = --len;
        return data;
    }

    public boolean remove(Object obj) {
        int index = indexOf(obj);
        if (index == -1) return false;
        removeAt(index);
        return true;
    }

    public int indexOf(Object obj) {
        for (int i = 0; i < len; i++) {
            if (obj == null) {
                if (arr[i] == null) return i;
            } else {
                if (obj.equals(arr[i])) return i;
            }
        }
        return -1;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    // Iterator is still fast but not as fast as iterative for loop
    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < len;
            }

            @Override
            public T next() {
                return arr[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        if (len == 0) return "[]";
        else {
            StringBuilder sb = new StringBuilder(len).append("[");
            for (int i = 0; i < len - 1; i++) sb.append(arr[i] + ", ");
            return sb.append(arr[len - 1] + "]").toString();
        }
    }
}


class maps_ {
    public int[] twoSum(int[] nums, int target) {
        var hash_ = new HashMap<Integer,Integer>();
        for(int i = 0;i<nums.length;i++){
            if(hash_.containsKey(target - nums[i]))return new int[]{Math.min(i,hash_.get(target - nums[i])) , Math.max(i,hash_.get(target - nums[i]))};
            hash_.put(nums[i],i);
        }
        return new int[0];
    }

    public int[] twoSumRx(int[] nums, int target) {
        Map<Integer, Integer> seen = new ConcurrentHashMap<>();
        return Observable.range(0, nums.length)
                .filter(i -> seen.containsKey(target - nums[i]) || (seen.put(nums[i], i) == null && false))
                .take(1)
                .map(i -> new int[]{seen.get(target - nums[i]), i})
                .blockingFirst();
    }

    public int[] intersect(int[] nums1, int[] nums2) {
        var ans = new ArrayList<Integer>();
        for(var i:Arrays.stream(nums1).boxed().collect(Collectors.toSet()))ans.addAll(new ArrayList<>(Collections.nCopies((int) Math.min(Arrays.stream(nums1).filter(j->j==i).count() , Arrays.stream(nums2).filter(j->j==i).count()),i)));
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
}

@SuppressWarnings("unchecked") class LinkedList_ {

    // https://leetcode.com/problems/add-two-numbers/editorial/?envType=problem-list-v2&envId=linked-list (2. Add Two Numbers)
    public static ListNode mains(ListNode l1, ListNode l2) {var li1 = new ArrayList<BigInteger>();
        var li2 = new ArrayList<BigInteger>();
        ListNode current = l1;
        while(current != null){li1.add(BigInteger.valueOf(current.val));
            current = current.next;}
        current = l2;
        while(current != null){li2.add(BigInteger.valueOf(current.val));
            current = current.next;}
        var list_ = new ArrayList<>(Arrays.stream(new BigInteger(li1.reversed().stream().map(i -> String.valueOf(i)).collect(Collectors.joining())).add(new BigInteger(li2.reversed().stream().map(i -> String.valueOf(i)).collect(Collectors.joining()))).toString().split("")).toList().stream().map(i -> new BigInteger(i)).toList()).reversed();
        ListNode l3 = new ListNode();
        current = l3;
        for(int i = 0; i < list_.size(); i++){current.next = new ListNode(list_.get(i).intValue());
            current = current.next;}
        return l3.next;}

    public static ListNode mainsRx(ListNode l1, ListNode l2) {
        // Convert l1 to BigInteger
        BigInteger num1 = Observable.<ListNode>create(emitter -> {
                    ListNode current = l1;  // local copy for lambda
                    while (current != null) {
                        emitter.onNext(current);
                        current = current.next;
                    }
                    emitter.onComplete();
                })
                .map(node -> BigInteger.valueOf(node.val))
                .toList()
                .map(list -> new BigInteger(
                        list.stream()
                                .map(BigInteger::toString)
                                .reduce("", (a, b) -> b + a) // reverse + join
                ))
                .blockingGet();

        // Convert l2 to BigInteger
        BigInteger num2 = Observable.<ListNode>create(emitter -> {
                    ListNode current = l2;  // local copy for lambda
                    while (current != null) {
                        emitter.onNext(current);
                        current = current.next;
                    }
                    emitter.onComplete();
                })
                .map(node -> BigInteger.valueOf(node.val))
                .toList()
                .map(list -> new BigInteger(
                        list.stream()
                                .map(BigInteger::toString)
                                .reduce("", (a, b) -> b + a) // reverse + join
                ))
                .blockingGet();

        BigInteger sum = num1.add(num2);

        // Convert sum to linked list in one RxJava chain
        return Observable.fromArray(sum.toString().split(""))
                .map(BigInteger::new)
                .reduce(new ListNode(), (prev, digit) -> {
                    ListNode node = new ListNode(digit.intValue());
                    node.next = prev.next; // insert at front
                    prev.next = node;
                    return prev;
                })
                .blockingGet()
                .next; // skip dummy head
    }

    // https://leetcode.com/problems/remove-nth-node-from-end-of-list/?envType=problem-list-v2&envId=linked-list (19. Remove Nth Node From End of List)
    public static ListNode nthnode(ListNode l1, BigInteger n) {
        var li1 = new ArrayList<BigInteger>();
        ListNode current = l1;
        while(current != null){li1.add(BigInteger.valueOf(current.val));
            current = current.next;}
        var list1_ = new ArrayList<BigInteger>();
        list1_.addAll(li1.subList(0,li1.size()-Integer.valueOf(String.valueOf(n))));
        list1_.addAll(li1.subList(li1.size()-Integer.valueOf(String.valueOf(n)) + 1, li1.size()));
        ListNode l3 = new ListNode();
        current = l3;
        for(int i = 0; i < list1_.size(); i++){current.next = new ListNode(list1_.get(i).intValue());
            current = current.next;}
        return l3.next;}

    public static ListNode nthnodeRx(ListNode l1, BigInteger n) {
        // Convert linked list to List<BigInteger>
        List<BigInteger> nodes = new ArrayList<>();
        Observable.<ListNode>create(emitter -> {
                    ListNode current = l1; // effectively final
                    while (current != null) {
                        emitter.onNext(current);
                        current = current.next;
                    }
                    emitter.onComplete();
                })
                .map(node -> BigInteger.valueOf(node.val))
                .toList()
                .blockingSubscribe(nodes::addAll);

        // Index to remove
        int removeIdx = nodes.size() - n.intValue();

        // Build linked list using dummy head and array wrapper for current
        ListNode dummy = new ListNode();
        ListNode[] current = new ListNode[] { dummy };

        Observable.range(0, nodes.size())
                .filter(i -> i != removeIdx) // skip nth from end
                .map(nodes::get)
                .blockingForEach(val -> {
                    current[0].next = new ListNode(val.intValue());
                    current[0] = current[0].next; // mutation is fine inside array
                });

        return dummy.next;
    }

    public static ListNode deleteDuplicates(ListNode head) {
        if(head==null)return head;
        ListNode curr = head;
        while(curr.next !=null){
            if(curr.val == curr.next.val) curr.next = curr.next.next;
            else curr = curr.next;
        }
        return head;

    }

    public static ListNode deleteDuplicatesRx(ListNode head) {
        return Observable.generate(() -> head, (curr, emitter) -> {
                    if (curr != null) { emitter.onNext(curr.val); return curr.next; }
                    else { emitter.onComplete(); return null; }
                })
                .distinctUntilChanged()
                .reduce(new ListNode(0), (dummy, v) -> {
                    ListNode tail = dummy;
                    while (tail.next != null) tail = tail.next;
                    tail.next = new ListNode((int) v);
                    return dummy;
                })
                .map(dummy -> dummy.next)
                .blockingGet();
    }

}


class Strings_{

    // https://leetcode.com/problems/basic-calculator-ii/description/?envType=problem-list-v2&envId=string (227. Basic Calculator II)
    public int calculate(String expression) {
        return (int) Context.create("js").eval("js", expression).asDouble();
    }

    public boolean wordPattern(String pattern, String s) {
        var hash_ = new HashMap<String,String>();
        if(!s.contains(" ") && pattern.length()>1)return false;
        var list =  s.split(" ");
        var newList = new ArrayList<String>();
        for(int i=0;i<pattern.length();i++){
            if(!hash_.containsKey(pattern.substring(i,i+1))){
                if(!hash_.values().contains(list[i]))
                    hash_.put(pattern.substring(i,i+1),list[i]);
                else
                    hash_.put(pattern.substring(i,i+1),"-");
            }
        }
        for(int i=0;i<pattern.length();i++){
            newList.add(hash_.get(pattern.substring(i,i+1)));
        }
        return String.join(" ",newList).equals(s);
    }


    public boolean wordPatternRx(String p, String s) {
        String[] w = s.split(" ");
        if (w.length != p.length()) return false;
        Map<Character,String> map = new HashMap<>();
        return Observable.zip(
                        Observable.fromIterable(() -> p.chars().mapToObj(c -> (char) c).iterator()),
                        Observable.fromArray(w),
                        (ch, word) -> map.computeIfAbsent(ch, k -> word)
                )
                .reduce((a, b) -> a + " " + b)
                .map(res -> res.equals(s) && new HashSet<>(map.values()).size() == map.size())
                .blockingGet();
    }


    // https://leetcode.com/problems/integer-to-roman/ (12. Integer to Roman)
    public static String intToRoman(int num) {
        Map<Integer, String> romanMap = new HashMap<>(Map.of( 3000, "MMM", 2000, "MM", 1000, "M", 900,  "CM", 800,  "DCCC", 700,  "DCC", 600,  "DC", 500,  "D", 400,  "CD", 300,  "CCC" ));
        romanMap.putAll(Map.of(200,  "CC", 100,"C", 90,"XC", 80,"LXXX", 70,"LXX", 60,"LX", 50,"L", 40,"XL", 30,"XXX", 20,"XX"));
        romanMap.putAll(Map.of(10,"X", 9,"IX", 8,"VIII", 7,"VII", 6,"VI", 5,"V", 4,"IV", 3,"III", 2,"II", 1,"I"));
        String ans = "";
        int num1 = num;
        while(num1 !=0){
            int first_digit = (num1 / (int)Math.pow(10,(String.valueOf(num1).length()-1)));
            ans += romanMap.get((int)(first_digit*(Math.pow(10,(String.valueOf(num1).length())-1))));
            if(String.valueOf(num1).length() > 1) num1 = Integer.parseInt(String.valueOf(num1).substring(1));
            else break;
        }
        return (ans);
    }

    public static String intToRomanRx(int num) {
        Map<Integer, String> romanMap = new HashMap<>(Map.of( 3000, "MMM", 2000, "MM", 1000, "M", 900,  "CM", 800,  "DCCC", 700,  "DCC", 600,  "DC", 500,  "D", 400,  "CD", 300,  "CCC" ));
        romanMap.putAll(Map.of(200,  "CC", 100,"C", 90,"XC", 80,"LXXX", 70,"LXX", 60,"LX", 50,"L", 40,"XL", 30,"XXX", 20,"XX"));
        romanMap.putAll(Map.of(10,"X", 9,"IX", 8,"VIII", 7,"VII", 6,"VI", 5,"V", 4,"IV", 3,"III", 2,"II", 1,"I"));
        String numStr = String.valueOf(num);
        return Observable.range(0, numStr.length())
                .map(i -> {
                    int digit = Character.getNumericValue(numStr.charAt(i));
                    int placeValue = digit * (int) Math.pow(10, numStr.length() - i - 1);
                    return romanMap.getOrDefault(placeValue, "");
                })
                .reduce("", String::concat)
                .blockingGet();
    }

    // https://leetcode.com/problems/roman-to-integer/description/ (13. Roman to Integer)
    public int romanToInt(String s) {
        Map<Integer, String> romanMap = new HashMap<>(Map.of( 3000, "MMM", 2000, "MM", 1000, "M", 900,  "CM", 800,  "DCCC", 700,  "DCC", 600,  "DC", 500,  "D", 400,  "CD", 300,  "CCC" ));
        romanMap.putAll(Map.of(200,  "CC", 100,"C", 90,"XC", 80,"LXXX", 70,"LXX", 60,"LX", 50,"L", 40,"XL", 30,"XXX", 20,"XX"));
        romanMap.putAll(Map.of(10,"X", 9,"IX", 8,"VIII", 7,"VII", 6,"VI", 5,"V", 4,"IV", 3,"III", 2,"II", 1,"I"));

        Map<String, Integer> reverse = new HashMap<>();
        for(var i:romanMap.entrySet()){
            reverse.put(i.getValue() , i.getKey());
        }
        int num = 0;
        while(s.length()>0){
            for(int i=s.length();i>=0;i--){
                if(reverse.containsKey(s.substring(0,i))){
                    num += reverse.get(s.substring(0,i));
                    s = s.substring(i);
                    break;
                }
            }
        }return num;
    }

    public int romanToIntRx(String s) {
        Map<Integer, String> romanMap = new HashMap<>(Map.of( 3000, "MMM", 2000, "MM", 1000, "M", 900,  "CM", 800,  "DCCC", 700,  "DCC", 600,  "DC", 500,  "D", 400,  "CD", 300,  "CCC" ));
        romanMap.putAll(Map.of(200,  "CC", 100,"C", 90,"XC", 80,"LXXX", 70,"LXX", 60,"LX", 50,"L", 40,"XL", 30,"XXX", 20,"XX"));
        romanMap.putAll(Map.of(10,"X", 9,"IX", 8,"VIII", 7,"VII", 6,"VI", 5,"V", 4,"IV", 3,"III", 2,"II", 1,"I"));

        // Reverse map for lookup
        Map<String, Integer> reverse = new HashMap<>();
        romanMap.forEach((k, v) -> reverse.put(v, k));

        // Observable generator for Roman parsing
        return Observable.generate(
                        () -> s, // state: remaining string
                        (state, emitter) -> {
                            if (state.isEmpty()) {
                                emitter.onComplete();
                            } else {
                                // find the longest matching prefix
                                int matchLen = 0;
                                for (int i = state.length(); i > 0; i--) {
                                    if (reverse.containsKey(state.substring(0, i))) {
                                        matchLen = i;
                                        break;
                                    }
                                }
                                String token = state.substring(0, matchLen);
                                emitter.onNext(reverse.get(token));
                                state = state.substring(matchLen);
                            }
                            return state; // new state
                        }
                )
                .map(Integer.class::cast)
                .reduce(0, Integer::sum)
                .blockingGet();
    }

    //https://leetcode.com/problems/letter-combinations-of-a-phone-number/description/ (17. Letter Combinations of a Phone Number)
    public List<String> letterCombinations(String digits) {
        if(digits.equals("")) return new ArrayList<String>();
        var hash_ = Map.of("2","abc","3","def","4","ghi","5","jkl","6","mno","7","pqrs","8","tuv","9","wxyz");
        var s = new ArrayList<String>();

        int[] arr_ = new int[]{1,2,3,4};
        Integer[] arr1 = Arrays.stream(arr_).boxed().toArray(Integer[]::new);
        Arrays.sort(arr1,Comparator.reverseOrder());
        System.out.println(Arrays.toString(arr1));

        System.out.println("Rounded:" + Math.round(2.5)); // 3
        System.out.println("Ceil:" + (int)Math.ceil(3.5)); // 4
        System.out.println("Floor:" + (int)Math.floor(3.5)); // 3
        System.out.println("Rounded:" + Math.round(1.5)); // 2
        System.out.println("Rounded:" + Math.round(3.5)); // 4
        for(int i=0;i<digits.length();i++)if(hash_.containsKey(String.valueOf(digits.charAt(i)))) s.add(hash_.get(String.valueOf(digits.charAt(i))));
        return s.stream().map(s_ -> s_.chars().mapToObj(c -> "" + (char)c).toList()).reduce(List.of(""),(acc, charsList) -> acc.stream().flatMap(prefix -> charsList.stream().map(ch -> prefix + ch)).toList());
    }

    public static List<String> letterCombinationsRx(String digits) {
        if (digits.isEmpty()) return new ArrayList<>();

        Map<String, String> hash_ = Map.of(
                "2","abc","3","def","4","ghi","5","jkl","6","mno",
                "7","pqrs","8","tuv","9","wxyz"
        );

        // Convert each digit to its corresponding letters list
        List<List<String>> digitLetters = new ArrayList<>();
        for (char ch : digits.toCharArray()) {
            String d = String.valueOf(ch);
            if (hash_.containsKey(d)) {
                List<String> letters = new ArrayList<>();
                for (char c : hash_.get(d).toCharArray()) {
                    letters.add(String.valueOf(c));
                }
                digitLetters.add(letters);
            }
        }

        // RxJava: generate all combinations
        return Observable.fromIterable(digitLetters)
                .reduce(
                        Collections.singletonList(""), // start with empty prefix
                        (acc, letters) ->
                                Observable.fromIterable(acc)
                                        .flatMap(prefix ->
                                                Observable.fromIterable(letters)
                                                        .map(letter -> prefix + letter)
                                        )
                                        .toList()
                                        .blockingGet()
                )
                .blockingGet();
    }

    public int lengthOfLongestSubstring(String s) {var li = new ArrayList<String>();
        String s1 = "";
        for(int i=0;i<s.length();i++){if(!s1.contains(String.valueOf(s.charAt(i)))) s1 = s1 + s.charAt(i);
        else{li.add(s1);
            s1 = s1.substring( s1.indexOf(s.charAt(i))+1, s1.length()) + s.charAt(i);}}
        li.add(s1);

        System.out.println("-----String sorting------");
        char[] t = "hello".toCharArray();
        Arrays.sort(t);
        System.out.println(new String(t));

        return Collections.max(li.stream().map(i->i.length()).collect(Collectors.toList())); }


    public static int lengthOfLongestSubstringRx(String s) {
        return Observable.defer(() -> {
                    // 🔹 this block is executed when the Observable is subscribed
                    System.out.println("-----String sorting------");
                    char[] t = "hello".toCharArray();
                    Arrays.sort(t);
                    System.out.println(new String(t));
                    return Observable.range(0, s.length());
                })
                .scan(
                        new AbstractMap.SimpleEntry<>("", 0),
                        (state, i) -> {
                            String current = state.getKey();
                            char ch = s.charAt(i);

                            if (!current.contains(String.valueOf(ch))) {
                                current = current + ch;
                            } else {
                                current = current.substring(current.indexOf(ch) + 1) + ch;
                            }

                            int maxLen = Math.max(state.getValue(), current.length());
                            return new AbstractMap.SimpleEntry<>(current, maxLen);
                        }
                )
                .map(Map.Entry::getValue)
                .reduce(0, Math::max)
                .blockingGet();
    }

    public String longestPalindrome(String s) {if(s.length()==1) return s;
        String li = "";
        for(int i=0;i<s.length();i++){for(int j=s.length();j>=(i+1);j--){String substring = s.substring(i,j);
            if ((new Object() {boolean check(String s) {int l = 0, r = s.length() - 1;
                while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false;
                return true;
            }}).check(substring) && substring.length() > li.length()) li = substring;}}
        return li;}

    public static String longestPalindromeRx(String s) {
        if (s.length() == 1) return s;

        return Observable.range(0, s.length()) // start index
                .flatMap(i ->
                        Observable.range(1, s.length() - i) // substring length
                                .map(len -> s.substring(i, i + len))
                )
                .filter(substring ->
                        (new Object() {
                            boolean check(String str) {
                                int l = 0, r = str.length() - 1;
                                while (l < r) {
                                    if (str.charAt(l++) != str.charAt(r--)) return false;
                                }
                                return true;
                            }
                        }).check(substring)
                )
                .reduce("", (best, candidate) ->
                        candidate.length() > best.length() ? candidate : best
                )
                .blockingGet();
    }

    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        var Arr1 = Arrays.stream(arr1).boxed().collect(Collectors.toList());
        var Arr2 = Arrays.stream(arr2).boxed().collect(Collectors.toList());

        List<Integer> notInArr2 = new ArrayList<>();
        List<Integer> inArr2 = new ArrayList<>();
        for(int i=0;i<Arr1.size();i++)if(!Arr2.contains(Arr1.get(i))) notInArr2.add(Arr1.get(i));
        for(int i=0;i<Arr1.size();i++)if(Arr2.contains(Arr1.get(i))) inArr2.add(Arr1.get(i));
        notInArr2 = notInArr2.stream().sorted().collect(Collectors.toList());

        List<Integer> answer = new ArrayList<>();
        List<Integer> setArr1 = new ArrayList<>(Arr1.stream().collect(Collectors.toCollection(LinkedHashSet::new)));
        for(int i=0;i<Arr2.size();i++)for(int j=0;j<Collections.frequency(inArr2,Arr2.get(i));j++)answer.add(Arr2.get(i));
        answer.addAll(notInArr2);
        return answer.stream().mapToInt(Integer -> Integer.intValue()).toArray();
    }

    public static int[] relativeSortArrayRx(int[] arr1, int[] arr2) {
        List<Integer> arr1List = Arrays.stream(arr1).boxed().collect(Collectors.toList());
        List<Integer> arr2List = Arrays.stream(arr2).boxed().collect(Collectors.toList());

        // Build answer observable
        List<Integer> answer = new ArrayList<>();

        // Elements in arr2 order
        Observable.fromIterable(arr2List)
                .flatMap(num ->
                        Observable.range(0, Collections.frequency(arr1List, num))
                                .map(i -> num)
                )
                .doOnNext(answer::add)
                .blockingSubscribe();

        // Elements not in arr2 (sorted)
        Observable.fromIterable(arr1List)
                .filter(num -> !arr2List.contains(num))
                .sorted()
                .doOnNext(answer::add)
                .blockingSubscribe();

        // Convert to int[]
        return answer.stream().mapToInt(Integer::intValue).toArray();
    }

    public boolean isValid(String s) {
        var stack = new ArrayList<>();
        for(int i=0;i<s.length();i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{') stack.add(s.charAt(i));
            else if ((s.charAt(i) == ']' && (stack.size() == 0 || !stack.get(stack.size() - 1).equals('['))) || (s.charAt(i) == '}' && (stack.size() == 0 || !stack.get(stack.size() - 1).equals('{'))) || (s.charAt(i) == ')' && (stack.size() == 0 || !stack.get(stack.size() - 1).equals('(')))) return false;
            else stack.remove(stack.size() - 1);
        }
        return stack.size()==0?true:false;
    }

    public static boolean isValidRx(String s) {
        return Observable.fromIterable(() -> s.chars().mapToObj(c -> (char) c).iterator())
                .collect(
                        ArrayList<Character>::new, // mutable stack
                        (stack, ch) -> {
                            if (ch == '(' || ch == '[' || ch == '{') {
                                stack.add(ch);
                            } else {
                                if (stack.isEmpty()) {
                                    stack.add('#'); // marker for invalid
                                    return;
                                }
                                char last = stack.get(stack.size() - 1);
                                if ((ch == ')' && last != '(') ||
                                        (ch == ']' && last != '[') ||
                                        (ch == '}' && last != '{')) {
                                    stack.add('#'); // invalid marker
                                } else {
                                    stack.remove(stack.size() - 1);
                                }
                            }
                        }
                )
                // ✅ final check: valid if stack is empty AND no '#' marker
                .map(stack -> !stack.contains('#') && stack.isEmpty())
                .blockingGet();
    }


    public String expand(String s){
        String ss = String.valueOf(s.charAt(0));
        List<String> li = new ArrayList<>();
        for(int i=0;i<s.length()-1;i++){
            if(String.valueOf(s.charAt(i)).equals(String.valueOf(s.charAt(i+1)))) ss += String.valueOf(s.charAt(i));
            else if(!String.valueOf(s.charAt(i)).equals(String.valueOf(s.charAt(i+1)))){
                li.add(ss);
                ss = String.valueOf(s.charAt(i+1));}
        }
        li.add(ss);

        String one_loop_string = "";
        for(int i=0;i<li.size();i++){
            one_loop_string += (String.valueOf(li.get(i).length()) + String.valueOf(li.get(i).charAt(0)));
        }
        return one_loop_string;
    }

    public String countAndSay(int n) {
        if(n==1)return "1";

        var arr = new ArrayList<String>();
        arr.add("1");
        for(int j=1;j<n;j++){
            arr.add(expand(arr.get(j-1)));
        }

        return (arr.get(arr.size()-1));
    }

    public static String countAndSayRx(int n) {
        return Observable.range(1, n - 1)
                .scan("1", (s, i) ->
                        Observable.fromIterable(
                                        Pattern.compile("(\\d)\\1*")
                                                .matcher(s)
                                                .results()
                                                .map(mr -> mr.group())
                                                .toList()
                                )
                                .map(g -> g.length() + "" + g.charAt(0))
                                .reduce(new StringBuilder(), StringBuilder::append)
                                .map(StringBuilder::toString)
                                .blockingGet()
                )
                .blockingLast();
    }


    public String countAndSay1(int n) {
        if(n==1) return "1";
        String groupString = "1";
        for(int i=1;i<n;i++){
            List<String> groups = new ArrayList<String>();
            Matcher matcher = Pattern.compile("(\\d)\\1*").matcher(groupString);
            while(matcher.find())groups.add(matcher.group());
            groups = groups.stream().map(j -> String.valueOf(j.length())+j.substring(0,1)).collect(Collectors.toList());
            groupString = (String.join("",groups));
        }
        return groupString;
    }


    public String multiply(String num1, String num2) {
        return String.valueOf(new BigInteger(num1).multiply(new BigInteger(num2)));
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        List<String> anagram = new ArrayList<>();
        for(int i=0;i<strs.length;i++){
            anagram.add(Arrays.stream(strs[i].split("")).sorted().collect(Collectors.joining()));
        }

        Map<String,List<String>> hash_ = new HashMap<>();
        for(int i=0;i<anagram.size();i++){
            if(!hash_.containsKey(anagram.get(i))) {
                hash_.put(anagram.get(i),new ArrayList<>());
                hash_.get(anagram.get(i)).add(strs[i]);
            }
            else hash_.get(anagram.get(i)).add(strs[i]);
        }
        List<List<String>> anagram1 = new ArrayList<>();
        for(var i:hash_.values()) anagram1.add(i);
        return anagram1;
    }

    public List<List<String>> groupAnagramsRx(String[] strs) {
        return Observable.fromArray(strs)
                // Map each string to its sorted key
                .groupBy(s -> Arrays.stream(s.split(""))
                        .sorted()
                        .collect(Collectors.joining()))
                // For each group, collect the original strings into a list
                .flatMapSingle(group -> group.toList())
                // Collect all lists into a single list
                .toList()
                .blockingGet();
    }

    public String reverseWords(String s) {
        return (String.join(" ",Arrays.stream(s.split(" ")).filter(i->i.trim().length()>0).collect(Collectors.toList()).reversed()));
    }

    public int lengthOfLastWord(String s) {
        return (Arrays.stream(s.split(" ")).filter(i->i.length()>0).toList().getLast().length());
    }

    public String addBinary(String a, String b) {
        return (new BigInteger(a,2).add(new BigInteger(b,2))).toString(2);
    }

    public boolean isPalindrome(String s) {
        String st = s.toLowerCase().replaceAll("[^A-Za-z0-9]","");
        return st.equals(new StringBuilder(st).reverse().toString());
    }

    public int compareVersion(String version1, String version2) {
        // In Java, String.split takes a regular expression, not a plain string. The dot (".") in regex means “any character”. So, we use Pattern.quote
        var vr1 = new ArrayList<>(List.of(version1.split(Pattern.quote("."))));
        var vr2 = new ArrayList<>(List.of(version2.split(Pattern.quote("."))));
        if(Integer.valueOf(vr1.get(0)) > Integer.valueOf(vr2.get(0)))return 1;
        else if(Integer.valueOf(vr1.get(0)) < Integer.valueOf(vr2.get(0)))return -1;
        else{

            if(vr1.size() > vr2.size()) vr2.addAll(Collections.nCopies(vr1.size() - vr2.size(),"0"));
            else vr1.addAll(Collections.nCopies(vr2.size() - vr1.size(),"0"));
            for(int i=0;i<vr1.size();i++){
                if(Integer.valueOf(vr1.get(i)) > Integer.valueOf(vr2.get(i))) return 1;
                else if(Integer.valueOf(vr1.get(i)) < Integer.valueOf(vr2.get(i))) return -1;
            }
            return 0;
        }
    }

    public static int compareVersionRx(String version1, String version2) {
        // Convert dotted strings into streams of integers
        Observable<Integer> v1 = Observable.fromArray(version1.split(Pattern.quote(".")))
                .map(Integer::parseInt);
        Observable<Integer> v2 = Observable.fromArray(version2.split(Pattern.quote(".")))
                .map(Integer::parseInt);

        // Normalize lengths by padding with zeros
        int maxLen = Math.max(version1.split("\\.").length, version2.split("\\.").length);

        v1 = v1.concatWith(Observable.range(0, maxLen).skip(version1.split("\\.").length).map(i -> 0));
        v2 = v2.concatWith(Observable.range(0, maxLen).skip(version2.split("\\.").length).map(i -> 0));

        // Compare zipped elements
        return Observable.zip(v1, v2, Integer::compare)
                .filter(cmp -> cmp != 0) // keep only first non-zero
                .first(0)                // return first diff, else 0
                .blockingGet();
    }

    public String convertToTitle(int col) {
        String columnTitle = "";
        while(col > 0){
            col -=1;
            columnTitle = (char)(col%26 + (int)('A')) + columnTitle;
            col /=26;
        }
        return columnTitle;
    }

    public static String convertToTitleRx(int col) {
        return Observable.generate(
                        () -> col,
                        (state, emitter) -> {
                            if (state > 0) {
                                int next = state - 1;
                                emitter.onNext((char)(next % 26 + 'A'));
                                return next / 26;
                            } else {
                                emitter.onComplete();
                                return state;
                            }
                        })
                .map(Object::toString)
                .reduce("", (a, b) -> b + a)
                .blockingGet();
    }



    public int titleToNumber(String c) {
        Map<Character,Integer> hash_ = new HashMap<>();
        for(int i=65;i<91;i++)hash_.put((char)i, i-64);
        if(hash_.containsKey(c))return hash_.get(c);
        c = (new StringBuilder(c)).reverse().toString();
        int sum_ = 0;
        for(int i = 0;i<c.length();i++) sum_ += (hash_.get(c.charAt(i)) * Math.pow(26,i));

        return sum_;
    }

    public static int titleToNumberRx(String c) {
        return Observable.fromIterable(() -> new StringBuilder(c).reverse().toString().chars()
                        .mapToObj(ch -> (char) ch)
                        .iterator())
                .zipWith(
                        Observable.range(0, c.length()),
                        (ch, i) -> new AbstractMap.SimpleEntry<>(ch, i)
                )
                .map(entry -> (entry.getKey() - 'A' + 1) * (int) Math.pow(26, entry.getValue()))
                .reduce(0, Integer::sum)
                .blockingGet();
    }

    public List<String> findRepeatedDnaSequences(String s) {
        if(s.length()<10)return new ArrayList<String>();
        Map<String,Integer> hash_ = new HashMap<>();
        for(int i=0;i<s.length()-10+1;i++){
            if(!hash_.containsKey(s.substring(i,i+10))) hash_.put(s.substring(i,i+10),1);
            else hash_.put(s.substring(i,i+10), hash_.get(s.substring(i,i+10))+1);
        }
        return new ArrayList<String>(hash_.keySet()).stream().filter(i->hash_.get(i)>1).collect(Collectors.toList());
    }

    public static List<String> findRepeatedDnaSequencesRx(String s) {
        if (s.length() < 10) return new ArrayList<>();

        return Observable.range(0, s.length() - 10 + 1)
                .map(i -> s.substring(i, i + 10))          // extract every substring of length 10
                .collect(HashMap<String, Integer>::new,    // use a HashMap collector
                        (map, sub) -> map.put(sub, map.getOrDefault(sub, 0) + 1))
                .map(map -> map.entrySet().stream()
                        .filter(e -> e.getValue() > 1)     // keep only sequences that appear > 1
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList()))
                .blockingGet();
    }


    public boolean isIsomorphic(String s, String t) {
        String t1="" , t2 = "";
        List<String> s1 = new ArrayList<>(List.of(s.split("")).stream().collect(Collectors.toCollection(LinkedHashSet::new))) , s2 = new ArrayList<>(List.of(t.split("")).stream().collect(Collectors.toCollection(LinkedHashSet::new)));
        System.out.println(s1);
        System.out.println(s2);
        for(int i=0;i<s.length();i++) t1 += s1.indexOf(String.valueOf(s.charAt(i)));
        for(int i=0;i<t.length();i++) t2 += s2.indexOf(String.valueOf(t.charAt(i)));
        return t1.equals(t2);
    }

    public boolean isIsomorphic1(String s, String t) {
        var hash_ = new HashMap<String,String>();
        String new_ = "";
        for(int i=0;i<s.length();i++){
            if(!hash_.containsKey(s.substring(i,i+1))){
                if(!hash_.values().contains(t.substring(i,i+1))) hash_.put(s.substring(i,i+1) , t.substring(i,i+1));
                else hash_.put(s.substring(i,i+1) , "-");
            }
        }
        for(int i=0;i<s.length();i++){
            new_ += hash_.get(s.substring(i,i+1));
        }

        return new_.equals(t);
    }

    public static boolean isIsomorphicRx(String s, String t) {
        if (s.length() != t.length()) return false;

        // Build the mapping index sequence for s
        String t1 = Observable.fromArray(s.split(""))
                .toList() // keep order
                .flatMap(list ->
                        Observable.fromIterable(list)
                                .map(ch -> list.indexOf(ch)) // position in list
                                .collect(StringBuilder::new,
                                        (sb, idx) -> sb.append(idx))
                                .map(StringBuilder::toString))
                .blockingGet();

        // Build the mapping index sequence for t
        String t2 = Observable.fromArray(t.split(""))
                .toList()
                .flatMap(list ->
                        Observable.fromIterable(list)
                                .map(ch -> list.indexOf(ch))
                                .collect(StringBuilder::new,
                                        (sb, idx) -> sb.append(idx))
                                .map(StringBuilder::toString))
                .blockingGet();

        return t1.equals(t2);
    }

    public String largestNumber(int[] nums) {
        var arrays = new ArrayList<String>(Arrays.stream(nums).boxed().map(i->String.valueOf(i)).sorted().collect(Collectors.toList()));
        Collections.reverse(arrays);

        boolean swap = true;
        while(swap){
            swap = false;
            for(int i=0;i<arrays.size()-1;i++){
                if((arrays.get(i)+arrays.get(i+1)).compareTo(arrays.get(i+1)+arrays.get(i)) <0){
                    swap = true;
                    String temp = arrays.get(i);
                    arrays.set(i, arrays.get(i+1));
                    arrays.set(i+1, temp);
                }
            }
        }
        System.out.println(arrays);
        return String.valueOf(new BigInteger(String.join("",arrays)));
    }

    public static String largestNumberRx(int[] nums) {
        return Observable.fromIterable(Arrays.stream(nums).boxed().toList())
                .map(String::valueOf)
                // Custom comparator: (x+y) vs (y+x)
                .toSortedList((a, b) -> (b + a).compareTo(a + b))
                .map(list -> {
                    // Edge case: if the largest is "0", avoid "000..."
                    if (list.get(0).equals("0")) return "0";
                    return String.valueOf(new BigInteger(String.join("", list)));
                })
                .blockingGet();
    }

    public int maxProductRx(String[] words) {return Observable.range(0, words.length).flatMap(i -> Observable.range(i+1, words.length-i-1).map(j -> {return (words[i].chars().reduce(0, (acc, c) -> acc | 1<<(c-'a')) & words[j].chars().reduce(0, (acc, c) -> acc | 1<<(c-'a'))) == 0 ? words[i].length() * words[j].length() : 0;
                        })).reduce(Integer::max).blockingGet();}

    public int maxProduct(String[] words) {
        int n = words.length;
        int[] masks = new int[n];
        int[] lengths = new int[n];

        // Precompute masks and lengths
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i].toCharArray()) {
                mask |= 1 << (c - 'a'); // set bit for each char
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }

        int maxProduct = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((masks[i] & masks[j]) == 0) { // no common letters
                    maxProduct = Math.max(maxProduct, lengths[i] * lengths[j]);
                }
            }
        }

        return maxProduct;
    }

    public String reverseVowels(String s) {
        String temp = "";
        for(int i=0;i<s.length();i++){
            int ascii = (int) Character.toLowerCase(s.charAt(i));
            if(ascii == 97 || ascii == 101 ||ascii == 105 ||ascii == 111 ||ascii == 117){
                temp += s.substring(i,i+1);
                s = s.substring(0,i) + "_" + s.substring(i+1);
            }
        }
        if(temp.length() ==0)return s;
        temp = new StringBuilder(temp).reverse().toString();
        int count = 0;
        for(int i=0;i<s.length();i++){
            if(s.substring(i,i+1).equals("_")){
                s = s.substring(0,i) + temp.substring(count,count+1) + s.substring(i+1);
                count += 1;
            }
        }
        return s;
    }

    public String reverseVowelsRx(String s) {
        return Observable.range(0, s.length())
                .map(s::charAt)
                .filter(c -> s.indexOf(c) >= 0)
                .toList()
                .map(l -> { Collections.reverse(l);
                    return l; })
                .flatMapObservable(vowels ->
                        Observable.range(0, s.length())
                                .map(i -> {
                                    return s.indexOf(s.charAt(i)) >= 0 ? vowels.remove(0).toString() : Character.toString(s.charAt(i));
                                })
                )
                .reduce(new StringBuilder(), StringBuilder::append)
                .map(StringBuilder::toString)
                .blockingGet();
    }

    public boolean canConstruct(String ransomNote, String magazine) {
        var hash_magazine = new HashMap<String,Integer>();
        var hash_ransomNote = new HashMap<String,Integer>();

        for(int i=0;i<magazine.length();i++){
            if(!hash_magazine.containsKey(magazine.substring(i,i+1))) hash_magazine.put(magazine.substring(i,i+1),1);
            else hash_magazine.put(magazine.substring(i,i+1),1 + hash_magazine.get(magazine.substring(i,i+1)));
        }
        for(int i=0;i<ransomNote.length();i++){
            if(!hash_ransomNote.containsKey(ransomNote.substring(i,i+1))) hash_ransomNote.put(ransomNote.substring(i,i+1),1);
            else hash_ransomNote.put(ransomNote.substring(i,i+1),1 + hash_ransomNote.get(ransomNote.substring(i,i+1)));
        }

        var keyList_magazine = new ArrayList<String>(hash_magazine.keySet());
        var keyList_ransomNote = new ArrayList<String>(hash_ransomNote.keySet());

        for(int i=0;i<keyList_ransomNote.size();i++){
            if(!keyList_magazine.contains(keyList_ransomNote.get(i)) || hash_ransomNote.get(keyList_ransomNote.get(i)) > hash_magazine.get(keyList_ransomNote.get(i))) return false;
        }
        return true;
    }

    public boolean canConstructRx(String ransomNote, String magazine) {
        Map<String, Long> hash_magazine =
                Observable.range(0, magazine.length())
                        .map(i -> magazine.substring(i, i + 1))
                        .toList()
                        .blockingGet()
                        .stream()
                        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        Map<String, Long> hash_ransomNote =
                Observable.range(0, ransomNote.length())
                        .map(i -> ransomNote.substring(i, i + 1))
                        .toList()
                        .blockingGet()
                        .stream()
                        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        return Observable.fromIterable(hash_ransomNote.entrySet())
                .all(e -> hash_magazine.getOrDefault(e.getKey(), 0L) >= e.getValue())
                .blockingGet();
    }


    public boolean isSubsequence(String s, String t) {
        int i=0,j=0;
        while(i<s.length() && j<t.length()){
            if(s.substring(i,i+1).equals(t.substring(j,j+1))){i++;j++;}
            else j++;
        }
        return i==s.length();
    }

    public boolean isSubsequenceRx(String s, String t) {
        return Observable.fromIterable(() -> t.chars().mapToObj(c -> (char) c).iterator())
                .reduce(0, (i, c) -> i < s.length() && s.charAt(i) == c ? i + 1 : i)
                .map(i -> i == s.length())
                .blockingGet();
    }

    public int longestPalindrome1(String s) {
        var hash_ = new HashMap<String,Integer>();
        for(int i=0;i<s.length();i++){
            if(!hash_.containsKey(s.substring(i,i+1))) hash_.put(s.substring(i,i+1),1);
            else hash_.put(s.substring(i,i+1),1 + hash_.get(s.substring(i,i+1)));
        }
        var even = hash_.values().stream().filter(i->i%2==0).sorted().collect(Collectors.toList());
        var odd = hash_.values().stream().filter(i->i%2!=0).sorted().collect(Collectors.toList());

        int sum = 0;
        for(int i=0;i<even.size();i++) sum+= even.get(i);
        for(int i=0;i<odd.size()-1;i++) sum+= (odd.get(i)-1);
        if(odd.size()>0) sum += odd.get(odd.size()-1);
        return sum;

    }

    public int longestPalindrome1Rx(String s) {
        return Observable.fromArray(s.split(""))                  // stream characters
                .groupBy(c -> c)                                      // group by char
                .flatMapSingle(g -> g.count().map(Long::intValue))    // get frequency counts
                .reduce(0, (sum, cnt) -> sum + (cnt % 2 == 0 ? cnt : cnt - 1)) // add evens, add odds-1
                .map(sum -> sum + (s.length() > sum ? 1 : 0))         // if leftover odd, center +1
                .blockingGet();                                       // final result
    }

    public int countSegments(String s) {return new ArrayList<>(Arrays.stream(s.split(" ")).filter(i->i.length()>0).collect(Collectors.toList())).size();}

    public String frequencySort(String s) {
        return s.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(c -> c, Collectors.counting())).entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).map(e -> String.valueOf(e.getKey()).repeat(e.getValue().intValue())).collect(Collectors.joining());
    }

    public int compress(char[] chars) {
        String result = Pattern.compile("(.)\\1*").matcher(new String(chars)).replaceAll(mr ->  Matcher.quoteReplacement(mr.group(1) + (mr.group().length() > 1 ? mr.group().length() : "")));
        for(int i=0;i<result.toCharArray().length;i++)chars[i] = result.charAt(i);
        System.out.println(Arrays.toString(Arrays.copyOfRange(chars,0,result.toCharArray().length)));
        return result.length();
    }

    public int compressRx(char[] chars) {
        String s = new String(chars);

        String result = Observable.fromArray(s.split(""))
                .scan(new StringBuilder(), (sb, c) -> {
                    if (sb.length() == 0 || sb.charAt(sb.length() - 1) == c.charAt(0)) {
                        sb.append(c);
                    } else {
                        sb.append(c);
                    }
                    return sb;
                })
                .lastElement()
                .map(sb -> {
                    AtomicInteger count = new AtomicInteger();
                    StringBuilder compressed = new StringBuilder();
                    char prev = 0;
                    for (char ch : sb.toString().toCharArray()) {
                        if (ch == prev) {
                            count.incrementAndGet();
                        } else {
                            if (prev != 0) {
                                compressed.append(prev);
                                if (count.get() > 1) compressed.append(count.get());
                            }
                            prev = ch;
                            count.set(1);
                        }
                    }
                    // append last character
                    compressed.append(prev);
                    if (count.get() > 1) compressed.append(count.get());
                    return compressed.toString();
                })
                .blockingGet();

        // copy result back to chars array
        for (int i = 0; i < result.length(); i++) chars[i] = result.charAt(i);
        System.out.println(Arrays.toString(Arrays.copyOfRange(chars,0,result.toCharArray().length)));
        return result.length();
    }

    public String licenseKeyFormatting(String s, int k) {
        s = String.join("",(s.toUpperCase().split("-")));
        s = new StringBuilder(s).reverse().toString();
        var list = new ArrayList<String>();
        int count = 0;
        for(int i=0;i<(s.length()-k+1);i+=k){
            list.add(new StringBuilder(s.substring(i,i+k)).reverse().toString());
            count +=k;
        }
        if(s.substring(count).length()>0)list.add(new StringBuilder(s.substring(count)).reverse().toString());
        Collections.reverse(list);
        return (String.join("-",list));
    }

    public String licenseKeyFormattingRx(String s, int k) {
        String reversed = new StringBuilder(s.toUpperCase().replace("-", "")).reverse().toString();
        return Observable.range(0, (reversed.length() + k - 1) / k)
                .map(i -> reversed.substring(i * k, Math.min((i + 1) * k, reversed.length())))
                .map(chunk -> new StringBuilder(chunk).reverse().toString())
                .toList()
                .map(list -> {
                    Collections.reverse(list);
                    return String.join("-", list);
                })
                .blockingGet();
    }

    public String[] findWords(String[] words) {

        var hash_ = new HashMap<String,String>();
        String row1 = "qwertyuiop", row2 = "asdfghjkl", row3 = "zxcvbnm";
        for(var i:row1.toCharArray()) hash_.put(String.valueOf(i),"1");
        for(var i:row2.toCharArray()) hash_.put(String.valueOf(i),"2");
        for(var i:row3.toCharArray()) hash_.put(String.valueOf(i),"3");

        var stringList = new ArrayList<String>();
        for(int i=0;i<words.length;i++){
            var s = String.join("", Arrays.stream(words[i].toLowerCase().split("")).map(j->hash_.get(j)).collect(Collectors.toList()));
            if(s.equals(s.substring(0,1).repeat(s.length()))) stringList.add(words[i]);
        }

        String[] ans = new String[stringList.size()];
        for(int i=0;i<stringList.size();i++) ans[i] = stringList.get(i);

        return ans;
    }

    public String[] findWordsRx(String[] words) {
        var hash_ = new HashMap<String,String>() {{
            "qwertyuiop".chars().forEach(c -> put(String.valueOf((char)c), "1"));
            "asdfghjkl".chars().forEach(c -> put(String.valueOf((char)c), "2"));
            "zxcvbnm".chars().forEach(c -> put(String.valueOf((char)c), "3"));
        }};

        return Observable.fromArray(words)
                .filter(w ->
                {
                    String s = Observable.fromArray(w.toLowerCase().split(""))
                            .map(hash_::get)
                            .collect(StringBuilder::new, StringBuilder::append)
                            .blockingGet()
                            .toString();
                    return s.equals(s.substring(0,1).repeat(s.length()));
                })
                .toList()
                .map(l -> l.toArray(new String[0]))
                .blockingGet();
    }

    public static String[] findWordsSpark(JavaSparkContext sc, String[] words) {
        // build hash map for keyboard rows
        Map<String, String> hash_ = new HashMap<>();
        "qwertyuiop".chars().forEach(c -> hash_.put(String.valueOf((char) c), "1"));
        "asdfghjkl".chars().forEach(c -> hash_.put(String.valueOf((char) c), "2"));
        "zxcvbnm".chars().forEach(c -> hash_.put(String.valueOf((char) c), "3"));

        // parallelize words into an RDD
        JavaRDD<String> rdd = sc.parallelize(Arrays.asList(words));

        // filter words that can be typed from one row
        List<String> result = rdd.filter(w -> {
                    String s = w.toLowerCase()
                            .chars()
                            .mapToObj(c -> hash_.get(String.valueOf((char) c)))
                            .collect(Collectors.joining());
                    return s.chars().allMatch(ch -> ch == s.charAt(0));
                })
                .collect();

        return result.toArray(new String[0]);
    }

    public String findLongestWord(String s, List<String> d) {
        String ans = "";
        var ansList = new ArrayList<String>();
        for(int i=0;i<d.size();i++)if((
                new Object(){
                    public boolean isSubsequence(String s, String t) {
                        int i=0,j=0;
                        while(i<s.length() && j<t.length()){
                            if(s.substring(i,i+1).equals(t.substring(j,j+1))){i++;j++;}
                            else j++;
                        }
                        return i==s.length();
                    }
                }
        ).isSubsequence(d.get(i),s)) if(d.get(i).length() > ans.length() || (d.get(i).length() == ans.length() && d.get(i).compareTo(ans)<0))ans = d.get(i);
        return ans;
    }

    public String findLongestWordRx(String s, List<String> d) {
        return Observable.fromIterable(d)
                .filter(t -> {
                    int i = 0, j = 0;
                    while (i < t.length() && j < s.length()) {
                        if (t.charAt(i) == s.charAt(j)) i++;
                        j++;
                    }
                    return i == t.length();
                })
                .reduce("", (a, b) ->
                        b.length() > a.length() || (b.length() == a.length() && b.compareTo(a) < 0) ? b : a
                )
                .blockingGet();
    }

    public static String findLongestWordSpark(JavaSparkContext sc, String s, List<String> d) {
        return sc.parallelize(d)
                .filter(t -> {
                    int i = 0, j = 0;
                    while (i < t.length() && j < s.length()) {
                        if (t.charAt(i) == s.charAt(j)) i++;
                        j++;
                    }
                    return i == t.length();
                })
                .reduce((a, b) ->
                        b.length() > a.length() ||
                                (b.length() == a.length() && b.compareTo(a) < 0) ? b : a
                );
    }

    public String complexNumberMultiply(String num1, String num2) {

        int a = Integer.valueOf(num1.split("\\+")[0]);
        int b = Integer.valueOf(num1.split("\\+")[1].substring(0,num1.split("\\+")[1].length()-1));
        int c = Integer.valueOf(num2.split("\\+")[0]);
        int d = Integer.valueOf(num2.split("\\+")[1].substring(0,num2.split("\\+")[1].length()-1));

        return (String.format("%d+%di",(a*c)+(b*d)*(-1),((a*d)+(b*c))));
    }

    public String reverseStr(String s, int k) {if(s.length()<k) s = new StringBuilder(s).reverse().toString();
    else for(int i=0;i<(s.length());i+=(2*k)) s = s.substring(0,i) + new StringBuilder(s.substring(i, Math.min(i+k,s.length()))).reverse().toString() +  s.substring(Math.min(i+k,s.length()));
        return (s);}


    public int[][] flipAndInvertImage(int[][] image) {
        List<Integer> list;
        for(int i=0;i<image.length;i++){
            list = Arrays.stream(image[i]).boxed().collect(Collectors.toList());
            Collections.reverse(list);
            image[i] = list.stream().map(j->j==0?2:j).map(j->j==1?0:j).map(j->j==2?1:j).mapToInt(Integer::intValue).toArray();
        }
        return image;
    }

    public String reverseStrRx(String s, int k) {
        return Observable.range(0, (s.length() + 2 * k - 1) / (2 * k))
                .map(b -> new StringBuilder(s.substring(b * 2 * k, Math.min(b * 2 * k + k, s.length()))).reverse()
                        .append(s, Math.min(b * 2 * k + k, s.length()), Math.min(b * 2 * k + 2 * k, s.length()))
                        .toString())
                .reduce("", (a, b) -> a + b)
                .blockingGet();
    }

    public List<String> commonChars(String[] words) {
        Map<Character, Integer> freqMap = words[0].chars().mapToObj(c -> (char) c).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        Arrays.stream(words, 1, words.length).map(w -> w.chars().mapToObj(c -> (char) c).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum))).forEach(currFreq -> freqMap.replaceAll((k, v) -> Math.min(v, currFreq.getOrDefault(k, 0))));
        return freqMap.entrySet().stream().flatMap(e -> Collections.nCopies(e.getValue(), String.valueOf(e.getKey())).stream()).collect(Collectors.toList());
    }

    public String reverseStrSpark(JavaSparkContext sc, String s, int k) {
        JavaRDD<String> parts = sc.parallelize(
                IntStream.range(0, (s.length() + 2 * k - 1) / (2 * k)).boxed().toList()
        ).map(b -> new StringBuilder(s.substring(b * 2 * k, Math.min(b * 2 * k + k, s.length()))).reverse()
                .append(s, Math.min(b * 2 * k + k, s.length()), Math.min(b * 2 * k + 2 * k, s.length()))
                .toString());

        return String.join("", parts.collect());
    }


}

class dynamic_programming_{
    public int maxSubArray(int[] nums) {
        int maxSoFar = nums[0];
        int curr = nums[0];
        for (int i = 1; i < nums.length; i++) {
            curr = Math.max(curr + nums[i], nums[i]);
            maxSoFar = Math.max(maxSoFar,curr);
        }
        return maxSoFar;
    }

    public int maxSubArrayRx(int[] nums) {
        return Observable.fromArray(Arrays.stream(nums).boxed().toArray(Integer[]::new))
                .skip(1) // ← avoid double-processing nums[0]
                .reduce(new int[]{nums[0], nums[0]},
                        (acc, x) -> new int[]{Math.max(acc[0] + x, x),
                                Math.max(acc[1], Math.max(acc[0] + x, x))})
                .map(a -> a[1])
                .blockingGet();
    }

    public int maxSubArrayRx1(int[] nums) {
        return Observable.fromArray(Arrays.stream(nums).boxed().toArray(Integer[]::new))
                .scan(new int[]{0, Integer.MIN_VALUE}, (acc, x) -> {
                    int curr = Math.max(acc[0] + x, x) , best = Math.max(acc[1], curr);
                    return new int[]{curr, best};
                })
                .takeLast(1)
                .singleOrError()   // convert Observable → Single
                .map(a -> a[1])
                .blockingGet();
    }


}