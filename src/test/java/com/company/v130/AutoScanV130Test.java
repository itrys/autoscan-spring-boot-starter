package com.company.v130;

import org.example.boot.TestComponent;
import org.example.boot.TestController;
import org.example.business.TestBusinessService;
import org.example.exclude.test.ExcludedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AutoScan v1.3.0 features
 * Tests regex-based package filtering and environment-based scanning
 */
public class AutoScanV130Test {

    @SpringBootTest(classes = AutoScanV130Application.class, properties = {
            "spring.profiles.active=dev",
            "auto-scan.base-packages=org.example.*",
            "auto-scan.exclude-packages-regex[0]=org/example/exclude/test/.*",
            "auto-scan.include-packages-regex[0]=org/example/boot/.*",
            "auto-scan.include-packages-regex[1]=org/example/business/.*",
            "auto-scan.dev-mode=true"
    })
    public static class DevEnvironmentRegexTest {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        public void testDevEnvironmentWithRegexFiltering() {
            // Should include boot components
            assertTrue(applicationContext.containsBean("testComponent"));
            assertTrue(applicationContext.containsBean("testController"));

            // Should include business components
            assertTrue(applicationContext.containsBean("testBusinessService"));

            // Should exclude test components matching regex
            assertFalse(applicationContext.containsBean("excludedService"));
        }
    }

    @SpringBootTest(classes = AutoScanV130Application.class, properties = {
            "spring.profiles.active=prod",
            "auto-scan.base-packages=org.example.boot",
            "auto-scan.exclude-packages-regex[0]=org/example/exclude/.*",
            "auto-scan.dev-mode=false"
    })
    public static class ProdEnvironmentRegexTest {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        public void testProdEnvironmentWithRegexFiltering() {
            // Should include boot components
            assertTrue(applicationContext.containsBean("testComponent"));
            assertTrue(applicationContext.containsBean("testController"));

            // Should not include business components in prod
            assertFalse(applicationContext.containsBean("testBusinessService"));

            // Should exclude test components matching regex
            assertFalse(applicationContext.containsBean("excludedService"));
        }
    }

    @SpringBootTest(classes = AutoScanV130Application.class, properties = {
            "spring.profiles.active=test",
            "auto-scan.base-packages=org.example.*",
            "auto-scan.include-packages-regex[0]=.*Controller",
            "auto-scan.dev-mode=true"
    })
    public static class TestEnvironmentRegexTest {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        public void testTestEnvironmentWithRegexFiltering() {
            // Should include controllers matching regex
            assertTrue(applicationContext.containsBean("testController"));

            // Should not include non-controller components
            assertFalse(applicationContext.containsBean("testComponent"));
            assertFalse(applicationContext.containsBean("testBusinessService"));
        }
    }
}
