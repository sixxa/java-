package com.sixa.springbootpr1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @RequestMapping("/")
    public String greet(){
        return "Hello, welcome";
    }
}
