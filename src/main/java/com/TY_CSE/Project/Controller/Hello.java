package com.TY_CSE.Project.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {


    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
