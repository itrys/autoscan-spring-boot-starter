package org.example.annotation.service;

import org.example.annotation.CustomComponent;

@CustomComponent
public class CustomAnnotatedService {
    public String getMessage() {
        return "Hello from CustomAnnotatedService!";
    }
}
