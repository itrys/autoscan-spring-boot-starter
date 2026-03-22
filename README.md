# autoscan-spring-boot-starter

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
![Maven Central](https://img.shields.io/maven-central/v/org.itrys/autoscan-spring-boot-starter.svg?label=Maven)
[![License](https://img.shields.io/badge/JDK-17+-4EB1BA.svg)](https://docs.oracle.com/javase/17/docs/index.html)
[![License](https://img.shields.io/badge/SpringBoot-3.2.0+-green.svg)](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/)

> A lightweight Starter that solves the cross-package scanning pain point in Spring Boot, making infrastructure development so simple.

## 📖 Introduction

In enterprise-level Spring Boot development, the package structure of technical infrastructure and business infrastructure is often fixed (e.g., `org.itrys.boot`, `org.itrys.base`), but business projects typically use the company's own domain packages (e.g., `com.company.project`). This causes the traditional `@ComponentScan` mechanism to be unable to scan both infrastructure packages and business packages simultaneously.

**autoscan-spring-boot-starter** perfectly solves this pain point by implementing the `ApplicationContextInitializer` interface to automatically scan configured base packages during the early stage of Spring container startup.

## ✨ Core Features

- 🚀 **Automatic Base Package Scanning** - Configure once in infrastructure projects, effective for all dependent projects
- 🎯 **Zero Configuration for Business Packages** - Leverages the default scanning mechanism of `@SpringBootApplication`
- 🏗️ **Multi-layer Infrastructure Support** - Business projects can also serve as infrastructure for other projects
- 🔧 **Non-invasive Design** - Does not change existing code structure
- 📊 **Developer Friendly** - Provides detailed scanning logs for easy debugging
- ⚡ **Lightweight** - No extra dependencies, perfectly compatible with Spring Boot

## 🚀 Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>org.itrys</groupId>
    <artifactId>autoscan-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Configure Base Packages

Configure the base packages to be automatically scanned in `application.yml`:

```yaml
auto-scan:
  base-packages:
    - org.itrys.boot        # Technical infrastructure
    - org.itrys.base        # Business infrastructure
    - com.company.framework # Company framework
  dev-mode: true             # Development mode, output detailed logs
```

### 3. Start Application

```java
package com.company.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Automatically scans com.company.project package and its sub-packages
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}
```

After startup, the console will output scanning logs:

```
>>> [AutoScan] Initializing base package scanner...
>>> [AutoScan] Configured base packages: [org.itrys.boot, org.itrys.base, com.company.framework]
>>> [AutoScan] Final packages to scan: [org.itrys.boot, org.itrys.base, com.company.framework]
>>> [AutoScan] Successfully registered 58 bean(s) from base packages.
```

## 📚 Use Cases

### Case 1: Technical Infrastructure Project

**Positioning**: Provides core framework capabilities for all business projects to depend on

**Configuration**:
```yaml
auto-scan:
  base-packages:
    - org.itrys.boot        # Core framework package
    - org.itrys.common      # Common components package
    - org.itrys.security    # Security components package
```

### Case 2: Business Infrastructure Project

**Positioning**: Based on technical infrastructure, encapsulates common business capabilities

**Configuration**:
```yaml
auto-scan:
  base-packages:
    - org.itrys.boot        # Include technical infrastructure
    - com.company.framework # Business framework package
    - com.company.security  # Security components package
  # business-packages is optional, only configure when serving as infrastructure for other projects
  business-packages:
    - com.company.business  # Common business package
```

### Case 3: Regular Business Project (Most Common)

**Positioning**: Develops specific business based on technical/business infrastructure

**Configuration**:
```yaml
auto-scan:
  base-packages:
    - org.itrys.boot        # Technical infrastructure
    - com.company.framework # Business infrastructure
```

**Note**: No need to configure `business-packages`, because `@SpringBootApplication` will automatically scan the package where the startup class is located!

## ⚙️ Configuration Details

### Configuration Items

| Configuration Item | Type | Required | Description |
|--------|------|------|------|
| `auto-scan.base-packages` | List<String> | Yes | Base package path list for scanning technical infrastructure, business infrastructure, etc. |
| `auto-scan.business-packages` | List<String> | No | Business package path list, only needed when this project serves as infrastructure for other projects |
| `auto-scan.dev-mode` | boolean | No | Development mode, outputs detailed scanning logs when set to `true`, defaults to auto-detection based on `spring.profiles.active` |

### Complete Configuration Example

```yaml
auto-scan:
  # Base package paths (required)
  base-packages:
    - org.itrys.boot
    - org.itrys.base
    - com.company.framework
  
  # Business package paths (optional)
  # Only needed when this project serves as infrastructure for other projects
  business-packages:
    - com.company.business
  
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
│
├── AutoScanProperties
│   └── Configuration properties class
│   └── Supports base-packages, business-packages, dev-mode
│
└── spring.factories
    └── Registers AutoScanApplicationContextInitializer
```

### Scanning Process

1. **Read Configuration** - Read `auto-scan.base-packages` and `auto-scan.business-packages` from `application.yml`
2. **Build Scan List** - Merge base packages and business packages, remove duplicates
3. **Create Scanner** - Use `ClassPathBeanDefinitionScanner`
4. **Set Filters** - Only scan `@Component` and `@Configuration` annotations
5. **Execute Scan** - Scan all configured package paths
6. **Register Components** - Register scanned components to Spring container

## 💡 Best Practices

### 1. Infrastructure Project Planning

**Technical Infrastructure** (`org.itrys.boot`):
```yaml
auto-scan:
  base-packages:
    - org.itrys.boot        # Core framework
    - org.itrys.common      # Common utilities
    - org.itrys.security    # Security components
```

**Business Infrastructure** (`com.company.framework`):
```yaml
auto-scan:
  base-packages:
    - org.itrys.boot        # Include technical infrastructure
    - com.company.core      # Business core
    - com.company.system    # System management
```

### 2. Business Project Development

```yaml
# Just include infrastructure, minimal configuration
auto-scan:
  base-packages:
    - org.itrys.boot
    - com.company.core
```

### 3. Multi-layer Infrastructure Architecture

```
Technical Infrastructure (org.itrys.boot)
    ↓
Business Infrastructure A (com.company.framework)
    ↓
Business Infrastructure B (com.company.platform)
    ↓
Specific Business Project (com.company.project.xxx)
```

Each infrastructure layer configures its own `base-packages`, upper layers automatically inherit.

## 🔍 FAQ

### Q1: Does it conflict with @ComponentScan?

**A**: No conflict. autoscan executes during the early stage of Spring container startup, earlier than `@ComponentScan`. Both can coexist, but it's recommended to use autoscan uniformly for base package scanning management.

### Q2: How to exclude certain packages from scanning?

**A**: The current version only supports inclusion scanning, exclusion is not yet supported. You can control the scanning scope by precisely configuring `base-packages`.

### Q3: Does it support Spring Boot 3.x?

**A**: Yes. autoscan-spring-boot-starter 1.0.0 is developed based on Spring Boot 3.2.0, fully compatible with Spring Boot 3.x/4.x.

### Q4: What if components cannot be scanned?

**A**:
1. Check if `auto-scan.base-packages` configuration is correct
2. Enable `dev-mode: true` to view scanning logs
3. Confirm if components are annotated with `@Component` or `@Configuration`
4. Check if package paths contain wildcards (wildcards are not supported yet)

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
