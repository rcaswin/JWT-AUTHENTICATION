package com.example.SpringSecurity.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/msg")
public class MsgController {

    @GetMapping("/")
    public String msg(){
        return "Hello Aswin!!!";
    }
}
