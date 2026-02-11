package com.example.HearingsDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @GetMapping("/")
    public String greeting() {
        return "Hearings API is ONLINE";
    }

}
