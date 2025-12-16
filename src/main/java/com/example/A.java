package com.example;

// Accolite

import java.util.LinkedHashMap;
import java.util.Map;

public class A{
    public A() {
        //print();
    }
    public void print(){
        System.out.println("A class");
    }
}

class B extends A{

    public B() {
        super.print();
    }

    @Override
    public void print(){
        System.out.println("B class");
    }
}

class C extends B{

    public C() {
        super.print();
    }

    @Override
    public void print(){
        System.out.println("C class");
    }

    public static void main(String[] args) {
        A a = new C();
        a.print();
    }

}

//ListNode node;
//ListNode curr = new ListNode();
//ListNode slow = new ListNode();
//
//// middle node
//// assuming initial length = total
//int total ;
//int counter = total;
//curr = node;
//slow = curr;
//while(curr != null){
//    slow = slow.next;
//    curr = curr.next.next;
//}
//
//sout(slow.val);
//
//// nth from the end
//
//slow1 = node;
//while(counter != total-n){
//    slow1 = slow1.next;
//    counter --;
//}
//sout(slow1.val);



// LRU
//class LRUCache(int capacity, float loadFactor, boolean access_order){
//
//    Map<String,Integer> map = new LinkedHashMap<>();
//
//    public LRUCache{
//        this(10,0.75f, true);
//    }
//
//    public void put(){
//        if((map.size()<= capacity || map.pollFirstEntry() != null)) {
//            map.put(k,map.getOrDefault(k,0) + 1);
//        }
//    }
//
//    public int get(){
//        return map.getOrDefault(k,-1);
//    }
//}