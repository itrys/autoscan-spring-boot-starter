package com.company;

import org.example.boot.TestComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class EnabledSwitchTest {

    @SpringBootTest(properties = {
        "auto-scan.base-packages[0]=org.example.boot",
        "auto-scan.enabled=true",
        "auto-scan.dev-mode=true"
    })
    static class AutoScanEnabledTest {
        
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testAutoScanEnabled() {
            // Test that AutoScan is enabled and beans are scanned
            TestComponent testComponent = applicationContext.getBean(TestComponent.class);
            assertNotNull(testComponent);
            assertEquals("Hello from TestComponent", testComponent.getMessage());
        }
    }

    @SpringBootTest(properties = {
        "auto-scan.base-packages[0]=org.example.boot",
        "auto-scan.enabled=false",
        "auto-scan.dev-mode=true"
    })
    static class AutoScanDisabledTest {
        
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testAutoScanDisabled() {
            // Test that AutoScan is disabled and beans are not scanned
            assertThrows(Exception.class, () -> {
                applicationContext.getBean(TestComponent.class);
            }, "TestComponent should not be available when AutoScan is disabled");
        }
    }

    @SpringBootTest(properties = {
        "auto-scan.base-packages[0]=org.example.boot",
        // enabled not specified (should default to true)
        "auto-scan.dev-mode=true"
    })
    static class AutoScanDefaultEnabledTest {
        
        @Autowired
        private ApplicationContext applicationContext;

        @Test
        void testAutoScanDefaultEnabled() {
            // Test that AutoScan is enabled by default
            TestComponent testComponent = applicationContext.getBean(TestComponent.class);
            assertNotNull(testComponent);
            assertEquals("Hello from TestComponent", testComponent.getMessage());
        }
    }
}