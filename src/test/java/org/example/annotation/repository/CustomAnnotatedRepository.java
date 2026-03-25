package org.example.annotation.repository;

import org.example.annotation.CustomComponent;

@CustomComponent
public class CustomAnnotatedRepository {
    public String getMessage() {
        return "Hello from CustomAnnotatedRepository!";
    }
}
