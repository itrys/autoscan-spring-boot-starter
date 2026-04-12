package com.company;

import org.example.boot.TestComponent;
import org.example.boot.TestController;
import org.example.business.TestBusinessService;
import org.example.business.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for environment-based scanning configuration feature in v1.3.0
 */
public class EnvironmentScanTest {

    @SpringBootTest(classes = AutoScanCompanyApplication.class, properties = {
            "spring.profiles.active=dev",
            "auto-scan.base-packages=org.example.*",
            "auto-scan.dev-mode=true"
    })
    public static class DevEnvironmentTest {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        public void testDevEnvironmentScanning() {
            // Should include all components in dev environment
            assertTrue(applicationContext.containsBean("testComponent"));
            assertTrue(applicationContext.containsBean("testController"));
            assertTrue(applicationContext.containsBean("testBusinessService"));
            assertTrue(applicationContext.containsBean("testRepository"));
        }
    }

    @SpringBootTest(classes = AutoScanCompanyApplication.class, properties = {
            "spring.profiles.active=prod",
            "auto-scan.base-packages=org.example.boot,org.example.business",
            "auto-scan.exclude-packages-regex[0]=org/example/exclude/.*",
            "auto-scan.dev-mode=false"
    })
    public static class ProdEnvironmentTest {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        public void testProdEnvironmentScanning() {
            // Should include only specified packages in prod environment
            assertTrue(applicationContext.containsBean("testComponent"));
            assertTrue(applicationContext.containsBean("testController"));
            assertTrue(applicationContext.containsBean("testBusinessService"));
            assertTrue(applicationContext.containsBean("testRepository"));
        }
    }

    @SpringBootTest(classes = AutoScanCompanyApplication.class, properties = {
            "spring.profiles.active=test",
            "auto-scan.base-packages=org.example.boot",
            "auto-scan.dev-mode=true"
    })
    public static class TestEnvironmentTest {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        public void testTestEnvironmentScanning() {
            // Should only include boot components in test environment
            assertTrue(applicationContext.containsBean("testComponent"));
            assertTrue(applicationContext.containsBean("testController"));
            // Should not include business components in test environment
            assertFalse(applicationContext.containsBean("testBusinessService"));
            assertFalse(applicationContext.containsBean("testRepository"));
        }
    }
}
