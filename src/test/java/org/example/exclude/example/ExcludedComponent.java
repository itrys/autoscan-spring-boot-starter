package org.example.exclude.example;

import org.springframework.stereotype.Component;

@Component
public class ExcludedComponent {
    public String getMessage() {
        return "Hello from ExcludedComponent!";
    }
}
