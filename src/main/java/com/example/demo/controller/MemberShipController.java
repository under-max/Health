package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class MemberShipController {

    private static final Map<String, String> map = new HashMap<>();

    public MemberShipController() {
        map.put("1", "java");
        map.put("2", "spring");
        map.put("3", "vue");
        map.put("4", "axios");
    }

    @GetMapping("/test/{id}")
    public String getItem(@PathVariable String id) {
        log.info("data={}", map.get(id));
        return map.get(id);
    }

    @GetMapping("/test")
    public List<String> items() {
        List<String> values = (List<String>) map.values();
        return values;
    }
}
