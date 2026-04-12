package com.company;

import org.example.boot.TestComponent;
import org.example.boot.TestController;
import org.example.business.TestBusinessService;
import org.example.business.TestRepository;
import org.example.business.TestRestController;
import org.example.exclude.example.ExcludedComponent;
import org.example.exclude.test.ExcludedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for regex-based package filtering feature in v1.3.0
 */
@SpringBootTest(classes = AutoScanCompanyApplication.class, properties = {
        "auto-scan.base-packages=org.example.*",
        "auto-scan.exclude-packages-regex[0]=org/example/exclude/test/.*",
        "auto-scan.exclude-packages-regex[1]=org/example/exclude/example/.*",
        "auto-scan.include-packages-regex[0]=org/example/boot/.*",
        "auto-scan.include-packages-regex[1]=org/example/business/.*",
        "auto-scan.dev-mode=true"
})
public class RegexFilterTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testRegexBasedInclusion() {
        // Should include classes from org.example.boot
        assertTrue(applicationContext.containsBean("testComponent"));
        assertTrue(applicationContext.containsBean("testController"));

        // Should include classes from org.example.business
        assertTrue(applicationContext.containsBean("testBusinessService"));
        assertTrue(applicationContext.containsBean("testRepository"));
        assertTrue(applicationContext.containsBean("testRestController"));
    }

    @Test
    public void testRegexBasedExclusion() {
        // Should exclude classes matching exclude regex patterns
        assertFalse(applicationContext.containsBean("excludedService"));
        assertFalse(applicationContext.containsBean("excludedComponent"));
    }

    @Test
    public void testMixedRegexAndNormalFilters() {
        // Should still include normal classes not matching exclusion patterns
        assertTrue(applicationContext.containsBean("testComponent"));
        assertTrue(applicationContext.containsBean("testBusinessService"));
    }
}
