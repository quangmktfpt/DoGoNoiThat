package poly.controller;

import poly.entity.SystemConfig;
import poly.dao.SystemConfigDAO;
import poly.dao.impl.SystemConfigDAOImpl;
import java.util.List;

public class SystemConfigControllerImpl implements SystemConfigController {
    private SystemConfigDAO dao = new SystemConfigDAOImpl();

    @Override
    public void addConfig(SystemConfig config) {
        dao.insert(config);
    }

    @Override
    public void updateConfig(SystemConfig config) {
        dao.update(config);
    }

    @Override
    public void deleteConfig(String configKey) {
        dao.delete(configKey);
    }

    @Override
    public List<SystemConfig> getAllConfigs() {
        return dao.selectAll();
    }

    @Override
    public SystemConfig getConfigByKey(String configKey) {
        return dao.selectByKey(configKey);
    }
} 