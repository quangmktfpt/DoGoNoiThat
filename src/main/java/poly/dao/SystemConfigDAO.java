package poly.dao;

import poly.entity.SystemConfig;
import java.util.List;

public interface SystemConfigDAO {
    void insert(SystemConfig config);
    void update(SystemConfig config);
    void delete(String configKey);
    List<SystemConfig> selectAll();
    SystemConfig selectByKey(String configKey);
} 