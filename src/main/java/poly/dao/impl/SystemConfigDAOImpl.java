package poly.dao.impl;

import poly.dao.SystemConfigDAO;
import poly.entity.SystemConfig;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SystemConfigDAOImpl implements SystemConfigDAO {
    private final String INSERT_SQL = "INSERT INTO SystemConfig (ConfigKey, ConfigValue) VALUES (?, ?)";
    private final String UPDATE_SQL = "UPDATE SystemConfig SET ConfigValue=? WHERE ConfigKey=?";
    private final String DELETE_SQL = "DELETE FROM SystemConfig WHERE ConfigKey=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM SystemConfig";
    private final String SELECT_BY_KEY_SQL = "SELECT * FROM SystemConfig WHERE ConfigKey=?";

    @Override
    public void insert(SystemConfig config) {
        XJdbc.executeUpdate(INSERT_SQL, config.getConfigKey(), config.getConfigValue());
    }

    @Override
    public void update(SystemConfig config) {
        XJdbc.executeUpdate(UPDATE_SQL, config.getConfigValue(), config.getConfigKey());
    }

    @Override
    public void delete(String configKey) {
        XJdbc.executeUpdate(DELETE_SQL, configKey);
    }

    @Override
    public List<SystemConfig> selectAll() {
        // Sắp xếp theo ConfigKey để đảm bảo thứ tự và tránh lỗi SQL Server
        return selectBySql("SELECT * FROM SystemConfig ORDER BY ConfigKey");
    }

    @Override
    public SystemConfig selectByKey(String configKey) {
        List<SystemConfig> list = selectBySql(SELECT_BY_KEY_SQL, configKey);
        return list.isEmpty() ? null : list.get(0);
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