package com.clone.rottentomato.domain.example.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {


    @GetMapping("/")
    public String test(){
        return "rottentomato test";
    }
}
