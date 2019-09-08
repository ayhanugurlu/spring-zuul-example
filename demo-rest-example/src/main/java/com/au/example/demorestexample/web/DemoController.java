package com.au.example.demorestexample.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class DemoController {

    @GetMapping(path = "/demo/{input}")
    public List<String> demoGet(@PathVariable Integer input){
        log.debug("getPerformanceMeasurementsByTransaction({})",input);
        List<String> response = new ArrayList<>();
        response.add("response");
        return response;
    }

}
