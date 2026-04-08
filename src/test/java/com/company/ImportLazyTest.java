package com.company;

import org.example.imports.ImportedConfiguration;
import org.example.imports.AnotherImportedConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.beans.factory.config.BeanDefinition;

import static org.junit.jupiter.api.Assertions.*;

class ImportLazyTest {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @SpringBootTest(properties = {
        "auto-scan.imports[0]=org.example.imports.ImportedConfiguration",
        "auto-scan.lazy-initialization=true",
        "auto-scan.dev-mode=true"
    })
    static class SingleImportTest {
        
        @Autowired
        private ConfigurableApplicationContext applicationContext;

        @Test
        void testImportedConfiguration() {
            // Test that the imported configuration is registered
            ImportedConfiguration.ImportedService importedService = 
                applicationContext.getBean(ImportedConfiguration.ImportedService.class);
            
            assertNotNull(importedService);
            assertEquals("Imported service from ImportedConfiguration", importedService.getMessage());
        }
    }

    @SpringBootTest(properties = {
        "auto-scan.imports[0]=org.example.imports.ImportedConfiguration",
        "auto-scan.imports[1]=org.example.imports.AnotherImportedConfiguration",
        "auto-scan.lazy-initialization=true",
        "auto-scan.dev-mode=true"
    })
    static class MultipleImportsTest {
        
        @Autowired
        private ConfigurableApplicationContext applicationContext;

        @Test
        void testMultipleImportedConfigurations() {
            // Test that both imported configurations are registered
            ImportedConfiguration.ImportedService importedService = 
                applicationContext.getBean(ImportedConfiguration.ImportedService.class);
            AnotherImportedConfiguration.AnotherImportedService anotherImportedService = 
                applicationContext.getBean(AnotherImportedConfiguration.AnotherImportedService.class);
            
            assertNotNull(importedService);
            assertEquals("Imported service from ImportedConfiguration", importedService.getMessage());
            
            assertNotNull(anotherImportedService);
            assertEquals("Another imported service from AnotherImportedConfiguration", anotherImportedService.getMessage());
        }
    }

    @SpringBootTest(properties = {
        "auto-scan.base-packages[0]=org.example.lazy",
        "auto-scan.lazy-initialization=true",
        "auto-scan.dev-mode=true"
    })
    static class GlobalLazyInitializationTest {
        
        @Autowired
        private ConfigurableApplicationContext applicationContext;

        @Test
        void testGlobalLazyInitialization() {
            // Reset initialization flags
            org.example.lazy.LazyService.reset();
            org.example.lazy.LazyController.reset();
            org.example.lazy.another.AnotherLazyService.reset();
            
            // Verify beans are not initialized at startup
            assertFalse(org.example.lazy.LazyService.isInitialized(), "LazyService should not be initialized at startup");
            assertFalse(org.example.lazy.LazyController.isInitialized(), "LazyController should not be initialized at startup");
            assertFalse(org.example.lazy.another.AnotherLazyService.isInitialized(), "AnotherLazyService should not be initialized at startup");
            
            // Access beans and verify they are initialized
            org.example.lazy.LazyService lazyService = applicationContext.getBean(org.example.lazy.LazyService.class);
            assertTrue(org.example.lazy.LazyService.isInitialized(), "LazyService should be initialized after access");
            assertEquals("Hello from LazyService", lazyService.getMessage());
            
            org.example.lazy.LazyController lazyController = applicationContext.getBean(org.example.lazy.LazyController.class);
            assertTrue(org.example.lazy.LazyController.isInitialized(), "LazyController should be initialized after access");
            assertEquals("Hello from LazyController", lazyController.getMessage());
            
            org.example.lazy.another.AnotherLazyService anotherLazyService = applicationContext.getBean(org.example.lazy.another.AnotherLazyService.class);
            assertTrue(org.example.lazy.another.AnotherLazyService.isInitialized(), "AnotherLazyService should be initialized after access");
            assertEquals("Hello from AnotherLazyService", anotherLazyService.getMessage());
        }
    }

    @SpringBootTest(properties = {
        "auto-scan.base-packages[0]=org.example.lazy",
        "auto-scan.lazy-packages[0]=org.example.lazy",
        "auto-scan.dev-mode=true"
    })
    static class PackageLazyInitializationTest {
        
        @Autowired
        private ConfigurableApplicationContext applicationContext;

        @Test
        void testPackageLazyInitialization() {
            // Reset initialization flags
            org.example.lazy.LazyService.reset();
            org.example.lazy.LazyController.reset();
            org.example.lazy.another.AnotherLazyService.reset();
            
            // Verify beans in lazy package are not initialized at startup
            assertFalse(org.example.lazy.LazyService.isInitialized(), "LazyService should not be initialized at startup");
            assertFalse(org.example.lazy.LazyController.isInitialized(), "LazyController should not be initialized at startup");
            
            // Access beans and verify they are initialized
            org.example.lazy.LazyService lazyService = applicationContext.getBean(org.example.lazy.LazyService.class);
            assertTrue(org.example.lazy.LazyService.isInitialized(), "LazyService should be initialized after access");
            assertEquals("Hello from LazyService", lazyService.getMessage());
            
            org.example.lazy.LazyController lazyController = applicationContext.getBean(org.example.lazy.LazyController.class);
            assertTrue(org.example.lazy.LazyController.isInitialized(), "LazyController should be initialized after access");
            assertEquals("Hello from LazyController", lazyController.getMessage());
        }
    }

    @SpringBootTest(properties = {
        "auto-scan.base-packages[0]=org.example.lazy",
        "auto-scan.lazy-classes[0]=org.example.lazy.LazyService",
        "auto-scan.dev-mode=true"
    })
    static class ClassLazyInitializationTest {
        
        @Autowired
        private ConfigurableApplicationContext applicationContext;

        @Test
        void testClassLazyInitialization() {
            // Reset initialization flags
            org.example.lazy.LazyService.reset();
            org.example.lazy.LazyController.reset();
            
            // Access non-lazy bean (should be initialized immediately)
            org.example.lazy.LazyController lazyController = applicationContext.getBean(org.example.lazy.LazyController.class);
            assertTrue(org.example.lazy.LazyController.isInitialized(), "LazyController should be initialized immediately");
            
            // Verify lazy bean is not initialized at startup
            assertFalse(org.example.lazy.LazyService.isInitialized(), "LazyService should not be initialized at startup");
            
            // Access lazy bean and verify it is initialized
            org.example.lazy.LazyService lazyService = applicationContext.getBean(org.example.lazy.LazyService.class);
            assertTrue(org.example.lazy.LazyService.isInitialized(), "LazyService should be initialized after access");
            assertEquals("Hello from LazyService", lazyService.getMessage());
        }
    }
}
