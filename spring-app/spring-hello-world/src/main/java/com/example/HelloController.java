package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String sayHello(Model model) {
        model.addAttribute("message", "Hello from Classic Spring!");
        return "hello";
    }
}