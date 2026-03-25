package org.example.exclude.normal;

import org.springframework.stereotype.Service;

@Service
public class NormalService {
    public String getMessage() {
        return "Hello from NormalService!";
    }
}
