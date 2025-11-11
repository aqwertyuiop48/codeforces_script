package com.example.controller;

import io.micronaut.http.annotation.*;

@Controller("/hello")
public class HelloController {

    @Get("/")   // GET /hello
    public String index() {
        return "Hello from Micronaut!";
    }


    @Get("/{name}")   // GET /hello/{name}
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
