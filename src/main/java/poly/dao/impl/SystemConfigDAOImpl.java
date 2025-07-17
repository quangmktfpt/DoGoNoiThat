package poly.dao.impl;

import poly.dao.SystemConfigDAO;
import poly.entity.SystemConfig;
import poly.util.XJdbc;
import java.sql.*;
import java.util.*;

public class SystemConfigDAOImpl implements SystemConfigDAO {
    @Override
    public void insert(SystemConfig config) {
        String sql = "INSERT INTO SystemConfig (ConfigKey, ConfigValue) VALUES (?, ?)";
        XJdbc.executeUpdate(sql, config.getConfigKey(), config.getConfigValue());
    }

    @Override
    public void update(SystemConfig config) {
        String sql = "UPDATE SystemConfig SET ConfigValue = ? WHERE ConfigKey = ?";
        XJdbc.executeUpdate(sql, config.getConfigValue(), config.getConfigKey());
    }

    @Override
    public void delete(String configKey) {
        String sql = "DELETE FROM SystemConfig WHERE ConfigKey = ?";
        XJdbc.executeUpdate(sql, configKey);
    }

    @Override
    public SystemConfig findByKey(String configKey) {
        String sql = "SELECT * FROM SystemConfig WHERE ConfigKey = ?";
        List<SystemConfig> list = selectBySql(sql, configKey);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<SystemConfig> findAll() {
        String sql = "SELECT * FROM SystemConfig";
        return selectBySql(sql);
    }

    private List<SystemConfig> selectBySql(String sql, Object... args) {
        List<SystemConfig> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                SystemConfig config = new SystemConfig();
                config.setConfigKey(rs.getString("ConfigKey"));
                config.setConfigValue(rs.getString("ConfigValue"));
                list.add(config);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 