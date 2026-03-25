# autoscan-spring-boot-starter

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
![Maven Central](https://img.shields.io/maven-central/v/org.itrys/autoscan-spring-boot-starter.svg?label=Maven)
[![GitHub release](https://img.shields.io/github/v/release/itrys/autoscan-spring-boot-starter?sort=semver)](https://github.com/itrys/autoscan-spring-boot-starter)
[![License](https://img.shields.io/badge/JDK-17+-4EB1BA.svg)](https://docs.oracle.com/javase/17/docs/index.html)
[![License](https://img.shields.io/badge/SpringBoot-3.2.0+-green.svg)](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/)
[![GitHub forks](https://img.shields.io/github/forks/itrys/autoscan-spring-boot-starter)](https://github.com/itrys/autoscan-spring-boot-starter/network)
[![GitHub stars](https://img.shields.io/github/stars/itrys/autoscan-spring-boot-starter)](https://github.com/itrys/autoscan-spring-boot-starter/stargazers)

<!-- Keep these links. Translations will automatically update with the README. -->
[Deutsch](https://zdoc.app/de/itrys/autoscan-spring-boot-starter) | 
[English](https://zdoc.app/en/itrys/autoscan-spring-boot-starter) | 
[Español](https://zdoc.app/es/itrys/autoscan-spring-boot-starter) | 
[français](https://zdoc.app/fr/itrys/autoscan-spring-boot-starter) | 
[日本語](https://zdoc.app/ja/itrys/autoscan-spring-boot-starter) | 
[한국어](https://zdoc.app/ko/itrys/autoscan-spring-boot-starter) | 
[Português](https://zdoc.app/pt/itrys/autoscan-spring-boot-starter) | 
[Русский](https://zdoc.app/ru/itrys/autoscan-spring-boot-starter) | 
[中文](https://zdoc.app/zh/itrys/autoscan-spring-boot-starter)

> A lightweight Starter that solves the cross-package scanning pain point in Spring Boot, making infrastructure development so simple.

## 📖 Introduction

In enterprise-level Spring Boot development, the package structure of technical infrastructure and business infrastructure is often fixed (e.g., `org.example.boot`, `org.example.business`), but business projects typically use the company's own domain packages (e.g., `com.company`). This causes the traditional `@ComponentScan` mechanism to be unable to scan both infrastructure packages and business packages simultaneously.

**autoscan-spring-boot-starter** perfectly solves this pain point by implementing the `ApplicationContextInitializer` interface to automatically scan configured base packages during the early stage of Spring container startup.

## ✨ Core Features

- 🚀 **Automatic Base Package Scanning** - Configure once in infrastructure projects, effective for all dependent projects
- 🎯 **Zero Configuration for Business Packages** - Leverages the default scanning mechanism of `@SpringBootApplication`
- 🏗️ **Multi-layer Infrastructure Support** - Business projects can also serve as infrastructure for other projects
- 🔧 **Non-invasive Design** - Does not change existing code structure
- 📊 **Developer Friendly** - Provides detailed scanning logs for easy debugging
- ⚡ **Lightweight** - No extra dependencies, perfectly compatible with Spring Boot
- 🌟 **Wildcard Package Support** - Supports single-level (`*`) and multi-level (`**`) wildcards for package paths
- 🚫 **Exclude Scanning** - Supports excluding specific packages and classes from scanning
- 🎨 **Custom Annotation Scanning** - Supports custom annotations for component scanning

## 🚀 Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>org.itrys</groupId>
    <artifactId>autoscan-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

### 2. Configure Base Packages

Configure the base packages to be automatically scanned in `application.yml`:

```yaml
auto-scan:
  base-packages:
    - org.example.boot        # Technical infrastructure
  business-packages:
    - org.example.business    # Business infrastructure
  dev-mode: true             # Development mode, output detailed logs
```

### 3. Start Application

```java
package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Automatically scans com.company package and its sub-packages
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}
```

After startup, the console will output scanning logs:

```
>>> [AutoScan] Initializing base package scanner...
>>> [AutoScan] Configured base packages: [org.example.boot]
>>> [AutoScan] Configured business packages: [org.example.business]
>>> [AutoScan] Final packages to scan: [org.example.boot, org.example.business]
>>> [AutoScan] Successfully registered 11 bean(s) from base packages.```

## 📚 Use Cases

### Case 1: Technical Infrastructure Project

**Positioning**: Provides core framework capabilities for all business projects to depend on

**Configuration**:
```yaml
auto-scan:
  base-packages:
    - org.example.boot        # Core framework package
    - org.example.common      # Common components package
    - org.example.security    # Security components package
```

### Case 2: Business Infrastructure Project

**Positioning**: Based on technical infrastructure, encapsulates common business capabilities

**Configuration**:
```yaml
auto-scan:
  base-packages:
    - org.example.boot        # Include technical infrastructure
    - org.example.framework   # Business framework package
    - org.example.security    # Security components package
  # business-packages is optional, only configure when serving as infrastructure for other projects
  business-packages:
    - org.example.business    # Common business package
```

### Case 3: Regular Business Project (Most Common)

**Positioning**: Develops specific business based on technical/business infrastructure

**Configuration**:
```yaml
auto-scan:
  base-packages:
    - org.example.boot        # Technical infrastructure
    - org.example.business    # Business infrastructure
```

**Note**: No need to configure `business-packages`, because `@SpringBootApplication` will automatically scan the package where the startup class is located!

## ⚙️ Configuration Details

### Configuration Items

| Configuration Item | Type | Required | Description |
|--------|------|------|------|
| `auto-scan.base-packages` | List<String> | Yes | Base package path list for scanning technical infrastructure, business infrastructure, etc. Supports wildcards (`*` for single level, `**` for multi-level) |
| `auto-scan.business-packages` | List<String> | No | Business package path list, only needed when this project serves as infrastructure for other projects. Supports wildcards |
| `auto-scan.dev-mode` | boolean | No | Development mode, outputs detailed scanning logs when set to `true`, defaults to auto-detection based on `spring.profiles.active` |
| `auto-scan.exclude-packages` | List<String> | No | Package path list to exclude from scanning |
| `auto-scan.exclude-classes` | List<String> | No | Class fully qualified name list to exclude from scanning |
| `auto-scan.include-annotations` | List<String> | No | Annotation fully qualified name list to include in scanning |

### Complete Configuration Example

```yaml
auto-scan:
  # Base package paths (required)
  # Supports wildcards: * for single level, ** for multi-level
  base-packages:
    - org.example.*        # Match all first-level packages under org.itrys
    - com.company.**     # Match all packages under com.company
  
  # Business package paths (optional)
  # Only needed when this project serves as infrastructure for other projects
  business-packages:
    - org.example.business
  
  # Exclude packages (optional)
  exclude-packages:
    - org.example.boot.test  # Exclude test packages
    - org.example.boot.example  # Exclude example packages
  
  # Exclude classes (optional)
  exclude-classes:
    - org.example.boot.example.DemoClass  # Exclude specific class
  
  # Include annotations (optional)
  include-annotations:
    - org.springframework.stereotype.Service
    - org.springframework.stereotype.Controller
    - org.example.annotation.CustomComponent  # Custom annotation
  
  # Development mode
  # true: Output detailed scanning logs
  # false: Silent mode
  # Default is auto-detected based on spring.profiles.active (dev/local/test = true)
  dev-mode: true
```

## 🏗️ Architecture Design

### Core Components

```
autoscan-spring-boot-starter
├── AutoScanApplicationContextInitializer
│   └── Implements ApplicationContextInitializer interface
│   └── Executes scanning during early Spring container startup
│   └── Supports wildcard resolution, exclude filtering, and custom annotations
│
├── AutoScanProperties
│   └── Configuration properties class
│   └── Supports base-packages, business-packages, dev-mode
│   └── Supports exclude-packages, exclude-classes, include-annotations
│
└── spring.factories
    └── Registers AutoScanApplicationContextInitializer
```

### Scanning Process

1. **Read Configuration** - Read `auto-scan.base-packages`, `auto-scan.business-packages`, `auto-scan.exclude-packages`, `auto-scan.exclude-classes`, and `auto-scan.include-annotations` from `application.yml`
2. **Resolve Wildcards** - Resolve wildcard patterns in package paths to actual package paths
3. **Build Scan List** - Merge base packages and business packages, remove duplicates
4. **Create Scanner** - Use `ClassPathBeanDefinitionScanner`
5. **Set Filters** - Add filters for `@Component`, `@Configuration`, and custom annotations
6. **Set Exclude Filters** - Add exclude filters for specified packages and classes
7. **Execute Scan** - Scan all configured package paths
8. **Register Components** - Register scanned components to Spring container

## 💡 Best Practices

### 1. Infrastructure Project Planning

**Technical Infrastructure** (`org.example.boot`):
```yaml
auto-scan:
  base-packages:
    - org.example.boot        # Core framework
    - org.example.common      # Common utilities
    - org.example.security    # Security components
```

**Business Infrastructure** (`com.company.framework`):
```yaml
auto-scan:
  base-packages:
    - org.example.boot        # Include technical infrastructure
    - org.example.core      # Business core
    - org.example.system    # System management
```

### 2. Business Project Development

```yaml
# Just include infrastructure, minimal configuration
auto-scan:
  base-packages:
    - org.example.boot
    - org.example.core
```

### 3. Multi-layer Infrastructure Architecture

```
Technical Infrastructure (org.example.boot)
    ↓
Business Infrastructure A (org.example.framework)
    ↓
Business Infrastructure B (org.example.platform)
    ↓
Specific Business Project (com.project.xxx)
```

Each infrastructure layer configures its own `base-packages`, upper layers automatically inherit.

## 🔍 FAQ

### Q1: Does it conflict with @ComponentScan?

**A**: No conflict. autoscan executes during the early stage of Spring container startup, earlier than `@ComponentScan`. Both can coexist, but it's recommended to use autoscan uniformly for base package scanning management.

### Q2: How to exclude certain packages from scanning?

**A**: Yes. Starting from version 1.1.0, you can configure `auto-scan.exclude-packages` to exclude specific packages and `auto-scan.exclude-classes` to exclude specific classes.

### Q3: Does it support Spring Boot 3.x?

**A**: Yes. autoscan-spring-boot-starter 1.1.0 is developed based on Spring Boot 3.2.0, fully compatible with Spring Boot 3.x/4.x.

### Q4: What if components cannot be scanned?

**A**:
1. Check if `auto-scan.base-packages` configuration is correct
2. Enable `dev-mode: true` to view scanning logs
3. Confirm if components are annotated with `@Component`, `@Configuration`, or other configured annotations
4. Check if package paths contain unsupported wildcard patterns (v1.1.0+ supports `*` and `**` wildcards)

### Q5: How to use wildcards in package paths?

**A**: Starting from version 1.1.0, you can use wildcards in package paths:
- `*` - Matches a single level of packages (e.g., `org.itrys.*` matches `org.example.boot`, `org.itrys.base`, etc.)
- `**` - Matches multiple levels of packages (e.g., `com.company.**` matches all packages under `com.company`)

### Q6: How to configure custom annotations for scanning?

**A**: Starting from version 1.1.0, you can configure `auto-scan.include-annotations` to specify custom annotations for scanning, such as `com.company.annotation.CustomComponent`.

## 📊 Performance Notes

- **Scanning Timing**: Executes during early Spring container startup, does not affect subsequent startup process
- **Scanning Scope**: Only scans configured package paths, avoids full classpath scanning
- **Memory Usage**: Only registers needed components, loads on demand
- **Caching Mechanism**: Spring caches scanning results, avoids repeated scanning

## 🤝 Contributing

Contributions, Issues, and suggestions are welcome!

### Submitting Issues

- Clearly describe the problem scenario
- Provide reproduction steps
- Attach relevant configuration and logs

### Submitting PRs

1. Fork this repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push branch (`git push origin feature/AmazingFeature`)
5. Create a Pull Request

## 📄 License

This project is licensed under the [Apache License 2.0](LICENSE).

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) - Excellent Java development framework

## 📞 Contact Us

- **GitHub**: [https://github.com/itrys/autoscan-spring-boot-starter](https://github.com/itrys/autoscan-spring-boot-starter)
- **Gitee**: [https://gitee.com/itrys/autoscan-spring-boot-starter](https://gitee.com/itrys/autoscan-spring-boot-starter)
- **Email**: contact@itrys.org

---

**If this project helps you, please give it a Star ⭐ to show your support!**
