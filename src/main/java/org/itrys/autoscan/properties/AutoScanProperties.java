package org.itrys.autoscan.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动扫描配置属性
 *
 * @author denghuafeng
 */
//@ConfigurationProperties(prefix = "auto-scan")
public class AutoScanProperties {

    /**
     * 基础包路径列表（技术底座、业务底座等）
     * 必须配置，用于扫描基础组件
     */
    private List<String> basePackages = new ArrayList<>();

    /**
     * 业务包路径列表（可选）
     * 仅当此项目作为其他项目的底座时才需要配置
     */
    private List<String> businessPackages = new ArrayList<>();

    /**
     * 开发模式（用于输出详细日志）
     */
    private boolean devMode = false;

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public List<String> getBusinessPackages() {
        return businessPackages;
    }

    public void setBusinessPackages(List<String> businessPackages) {
        this.businessPackages = businessPackages;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
