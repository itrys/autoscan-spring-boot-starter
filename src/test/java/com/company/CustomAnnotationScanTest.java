package com.company;

import org.example.annotation.service.CustomAnnotatedService;
import org.example.annotation.repository.CustomAnnotatedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "auto-scan.base-packages[0]=org.example.annotation",
    "auto-scan.include-annotations[0]=org.example.annotation.CustomComponent",
    "auto-scan.dev-mode=true"
})
class CustomAnnotationScanTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testCustomAnnotatedServiceScanned() {
        CustomAnnotatedService service = applicationContext.getBean(CustomAnnotatedService.class);
        assertNotNull(service, "CustomAnnotatedService should be scanned and registered");
        assertEquals("Hello from CustomAnnotatedService!", service.getMessage());
    }

    @Test
    void testCustomAnnotatedRepositoryScanned() {
        CustomAnnotatedRepository repository = applicationContext.getBean(CustomAnnotatedRepository.class);
        assertNotNull(repository, "CustomAnnotatedRepository should be scanned and registered");
        assertEquals("Hello from CustomAnnotatedRepository!", repository.getMessage());
    }

    @Test
    void testCustomAnnotationFiltering() {
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        
        boolean hasCustomService = false;
        boolean hasCustomRepository = false;
        
        for (String beanName : beanNames) {
            if (beanName.contains("customAnnotatedService")) {
                hasCustomService = true;
            }
            if (beanName.contains("customAnnotatedRepository")) {
                hasCustomRepository = true;
            }
        }
        
        assertTrue(hasCustomService, "Should find CustomAnnotatedService bean");
        assertTrue(hasCustomRepository, "Should find CustomAnnotatedRepository bean");
    }

    @Test
    void testAnnotationBasedScanning() {
        int customAnnotatedBeans = 0;
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        
        for (String beanName : beanNames) {
            if (beanName.contains("customAnnotated")) {
                customAnnotatedBeans++;
            }
        }
        
        assertTrue(customAnnotatedBeans >= 2, "Should find at least 2 custom annotated beans");
    }
}
