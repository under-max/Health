package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("/")
    public String check(){
        return "밥은먹고 다니니?";
    }

    @GetMapping("/items")
    public List<String> getItems() {
        List<String> items = new ArrayList<>();
        items.add("abc");
        items.add("123");
        items.add("456");
        return items;
    }
}
