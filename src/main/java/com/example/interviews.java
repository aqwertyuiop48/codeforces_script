package com.example;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class interviews {
    public static void main(String[] args) {
        System.out.println("======================================================================");
        String s = "uytrew";
        char[] arr0 = s.toCharArray();
        Arrays.sort(arr0);
        System.out.println(new String(arr0));

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
        //Predicate joining
        System.out.println(p2.and(p1).test(55));
        System.out.println(p2.or(p1).test(55));
        System.out.println(p2.negate().test(55));
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
        // example
        Function<Integer, Integer> double_ = i -> 2*i,
                square_ = i -> i*i;
        System.out.println(double_.apply(square_.apply(2)));

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

        // records
        record Person(String name, int age) { }
        Person p1_ = new Person("qwerty",25);
        Person p2_ = new Person("trewq",52);
        System.out.println(p1_.name + p2_.name);

        System.out.println("======================================================================");

        // String vs StringBuilder vs StringBuffer
        /*
        StringBuilder and StringBuffer do NOT override the equals() method.
        They inherit equals() from Object.class, which only checks reference equality.
         */
        String s1_ = "Hello", s2_ = new String(s1_);
        System.out.println(s1_.equals(s2_)); // true
        System.out.println(s1_==s2_); // false
        StringBuilder sb1 = new StringBuilder("Hello"), sb2 = new StringBuilder(sb1);
        System.out.println(sb1);
        System.out.println(sb2);
        System.out.println(sb1.equals(sb2)); // false
        System.out.println(sb1 == sb2); // false
        StringBuffer sbf1 = new StringBuffer("Hello"), sbf2 = new StringBuffer(sbf1);
        System.out.println(sbf1);
        System.out.println(sbf2);
        System.out.println(sbf1.equals(sbf2)); // false
        System.out.println(sbf1 == sbf2); // false

        // diamond problem  in java introduced by default methods can be resolved this way
        interface Mom {
            default void love() {
                System.out.println("Mom's love");
            }
        }

        interface Dad {
            default void love() {
                System.out.println("Dad's love");
            }
        }

        class Kid implements Mom, Dad {
            @Override
            public void love() {
                Mom.super.love();   // Choose Mom's love
                Dad.super.love();
                System.out.println("I love both!");
            }
        }
        new Kid().love();

        // parallel streams
        List<Integer> list =  new ArrayList<>(Arrays.asList(1,2,3,4));
        System.out.println(System.identityHashCode(list));
        List<Integer>list2 = list.stream().collect(Collectors.toList());
        System.out.println(list == list2);
        System.out.println(list.equals(list2));
        System.out.println(System.identityHashCode(list));
        list.stream()
                .sorted(Collections.reverseOrder())
                .forEach(x -> System.out.println(x));
        Stream.of(1,2,3,4,5).map(i->i+1).forEach(System.out::println);
        list.stream().forEach(System.out::print);
        System.out.println();
        list.parallelStream().forEach(System.out::print);

        // hashmap, hashtable, streams : map vs flatmap
        s = "qwerty dfg egrgh qwerty vbnmhg";
        Map<String,Integer> map = new LinkedHashMap<>();
        Arrays.stream(s.split(" ")).forEach(i -> map.put(i, 1 + map.getOrDefault(i,0)));
        System.out.println(map.keySet().stream().filter(i->map.get(i)>1).collect(Collectors.toList()));

        Map<String,Integer> table = new Hashtable<>();
        Arrays.stream(s.split(" ")).forEach(i -> table.put(i, 1 + table.getOrDefault(i,0)));

        Employee employee = new Employee(2,"qwerty",new ArrayList<>(Arrays.asList("a","b","c")));
        Employee employee2 = new Employee(1,"qwertyz",new ArrayList<>(Arrays.asList("a","b","d")));
        Employee employee3 = new Employee(3,"qwertyz",new ArrayList<>(Arrays.asList("a","b","b")));
        List<Employee> emList = new ArrayList<>(Arrays.asList(employee, employee2,employee3));
        System.out.println(emList.stream().filter(i->i.getCities().contains("d")).collect(Collectors.toSet()));

        // get by name
        System.out.println(emList.stream().collect(Collectors.groupingBy(Employee::getName)).get("qwertyz"));
        System.out.println(emList.stream().filter(i->i.getName().equals("qwertyz")).collect(Collectors.toList()));

        // skip limit
        int[] arr = {3,4,6,1,0,3,6},  arr2 = {3,4,6,1,0,3,6,7,8,9};
        // deep copy
        int[] arr1 = new int[arr.length];
        System.arraycopy(arr,0,arr1,0,arr.length);
        // deep copy
        int[] arr3 = arr1;

        //2nd largest element
        //1
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr3));
        System.out.println(arr[arr.length-2]);
        //2
        System.out.println(Arrays.stream(arr).boxed().sorted().collect(Collectors.toList()).get(arr.length-2));
        //3
        System.out.println(Arrays.stream(arr).boxed().sorted(Collections.reverseOrder()).skip(1).limit(1).toList().get(0));


    }
}

class Employee{
    int id;
    String name;
    List<String> cities;

    public Employee(int id, String name, List<String> cities) {
        this.id = id;
        this.name = name;
        this.cities = cities;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && Objects.equals(name, employee.name) && Objects.equals(cities, employee.cities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cities);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cities=" + cities +
                '}';
    }
}

