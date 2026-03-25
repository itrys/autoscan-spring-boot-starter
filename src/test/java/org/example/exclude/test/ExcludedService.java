package org.example.exclude.test;

import org.springframework.stereotype.Service;

@Service
public class ExcludedService {
    public String getMessage() {
        return "Hello from ExcludedService!";
    }
}
