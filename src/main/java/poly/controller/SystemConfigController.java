package poly.controller;

import poly.entity.SystemConfig;
import java.util.List;

public interface SystemConfigController {
    void addConfig(SystemConfig config);
    void updateConfig(SystemConfig config);
    void deleteConfig(String configKey);
    List<SystemConfig> getAllConfigs();
    SystemConfig getConfigByKey(String configKey);
} 