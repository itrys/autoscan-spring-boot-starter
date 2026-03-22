package org.itrys.autoscan.context;

import jakarta.annotation.Nonnull;
import org.itrys.autoscan.properties.AutoScanProperties;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 自动扫描应用上下文初始化器
 * <p>
 * 自动扫描配置的基础包路径，支持技术底座和业务底座的组件扫描
 * 业务项目基于底座开发时，无需配置 business-packages，
 * 因为 @SpringBootApplication 会自动扫描启动类所在包
 *
 * @author AutoScan
 */
public class AutoScanApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    // 使用统一的前缀常量，与 AutoScanProperties 保持一致
    private static final String PREFIX = "auto-scan";

    @Override
    public void initialize(@Nonnull ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof BeanDefinitionRegistry registry)) {
            System.err.println(">>> [AutoScan] ApplicationContext is not a BeanDefinitionRegistry. Skip scanning.");
            return;
        }

        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        // 检查是否为开发环境
        List<String> devProfiles = Arrays.asList("dev", "local", "test");
        boolean isDev = Arrays.stream(environment.getActiveProfiles())
            .anyMatch(devProfiles::contains);

        // Use ConfigurationProperties binding instead of direct Binder usage
        AutoScanProperties properties = new AutoScanProperties();
        Binder.get(environment).bind(PREFIX, Bindable.ofInstance(properties));
        // 读取开发模式配置
        boolean devMode = properties.isDevMode()||isDev;

        if (devMode) {
            System.out.println(">>> [AutoScan] Initializing base package scanner...");
        }

        // 构建扫描包列表
        Set<String> packagesToScan = new LinkedHashSet<>();
        
        // 读取基础包配置（必须配置）
        List<String> basePackages = properties.getBasePackages();
        
        if (basePackages.isEmpty()) {
            if (devMode) {
                System.out.println(">>> [AutoScan] No base packages configured. Skip scanning.");
            }
            return;
        }
        
        if (devMode) {
            System.out.println(">>> [AutoScan] Configured base packages: " + basePackages);
        }
        
        // 添加基础包
        for (String pkg : basePackages) {
            if (pkg != null && !pkg.trim().isEmpty()) {
                packagesToScan.add(pkg.trim());
            }
        }
        
        // 读取业务包配置（可选，仅当此项目作为底座时才需要）
        List<String> businessPackages = properties.getBusinessPackages();
        
        if (!businessPackages.isEmpty() && devMode) {
            System.out.println(">>> [AutoScan] Configured business packages: " + businessPackages);
        }
        
        // 添加业务包（如果有配置）
        for (String pkg : businessPackages) {
            if (pkg != null && !pkg.trim().isEmpty()) {
                packagesToScan.add(pkg.trim());
            }
        }

        // 转为数组
        String[] scanPackages = packagesToScan.toArray(new String[0]);
        
        if (devMode) {
            System.out.println(">>> [AutoScan] Final packages to scan: " + Arrays.toString(scanPackages));
        }

        // 创建扫描器
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Configuration.class));

        // 执行扫描
        int scannedCount = scanner.scan(scanPackages);
        
        if (devMode) {
            System.out.println(">>> [AutoScan] Successfully registered " + scannedCount + " bean(s) from base packages.");
        }
    }
}
