package com.example.controller

import io.micronaut.http.annotation.*

@Controller("/hello")
class HelloController {

    @Get("/")   // GET /hello
    fun index(): String = "Hello from Micronaut!"

    @Get("/{name}")   // GET /hello/{name}
    fun greet(name: String): String = "Hello, $name!"
}
