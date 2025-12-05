package com.example;

class Book{
    String title;
    String author;
    int price;

    public Book(String title, String author, int price){
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Book(){
        title = "Title";
        author = "Author";
        price = 200;
    }
}
public class Constructors {
    public static void main(String[] args) {
        Book b1 = new Book();
        System.out.println(b1.author + b1.title + b1.price);

        Book b2 = new Book("abc","xyz",300);
        System.out.println(b2.author + b2.title + b2.price);
    }
}
