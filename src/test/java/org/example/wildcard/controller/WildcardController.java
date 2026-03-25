package org.example.wildcard.controller;

import org.springframework.stereotype.Controller;

@Controller
public class WildcardController {
    public String getMessage() {
        return "Hello from WildcardController!";
    }
}
