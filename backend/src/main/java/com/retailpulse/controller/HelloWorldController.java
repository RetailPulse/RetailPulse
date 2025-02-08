package com.retailpulse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author WilliamSiling
 * @create 1/2/2025 9:16 pm
 */
@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public Authentication hello(Authentication authentication) {
        return authentication;
    }

}
