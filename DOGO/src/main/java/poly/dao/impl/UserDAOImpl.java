package poly.dao.impl;

import poly.dao.UserDAO;
import poly.entity.User;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class UserDAOImpl implements UserDAO {
    private final String INSERT_SQL = "INSERT INTO Users (Username, PasswordHash, FullName, Phone, Address, Email, Role, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE Users SET FullName=?, Phone=?, Address=?, Email=?, Role=?, IsActive=? WHERE UserID=?";
    private final String DELETE_SQL = "DELETE FROM Users WHERE UserID=?";
    private final String SELECT_ALL_SQL = "SELECT * FROM Users";
    private final String SELECT_BY_ID_SQL = "SELECT * FROM Users WHERE UserID=?";
    private final String SELECT_BY_USERNAME_SQL = "SELECT * FROM Users WHERE Username=?";

    @Override
    public void insert(User user) {
        XJdbc.executeUpdate(INSERT_SQL, user.getUsername(), user.getPasswordHash(), user.getFullName(), user.getPhone(), user.getAddress(), user.getEmail(), user.getRole(), user.getIsActive());
    }

    @Override
    public void update(User user) {
        XJdbc.executeUpdate(UPDATE_SQL, user.getFullName(), user.getPhone(), user.getAddress(), user.getEmail(), user.getRole(), user.getIsActive(), user.getUserId());
    }

    @Override
    public void delete(Integer userId) {
        XJdbc.executeUpdate(DELETE_SQL, userId);
    }

    @Override
    public List<User> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public User selectById(Integer userId) {
        List<User> list = selectBySql(SELECT_BY_ID_SQL, userId);
        return list.isEmpty() ? null : list.get(0);
    }

    public User selectByUsername(String username) {
        List<User> list = selectBySql(SELECT_BY_USERNAME_SQL, username);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User login(String username, String password) {
        // Lưu ý: password nên hash trước khi so sánh nếu dùng hash
        String sql = "SELECT * FROM Users WHERE Username=? AND PasswordHash=? AND IsActive=1";
        List<User> list = selectBySql(sql, username, password);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<User> selectBySql(String sql, Object... args) {
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setFullName(rs.getString("FullName"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setEmail(rs.getString("Email"));
                user.setRole(rs.getObject("Role") != null ? rs.getBoolean("Role") : null);
                user.setIsActive(rs.getObject("IsActive") != null ? rs.getBoolean("IsActive") : null);
                user.setCreatedDate(rs.getTimestamp("CreatedDate") != null ? rs.getTimestamp("CreatedDate").toLocalDateTime() : null);
                list.add(user);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, email);
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setFullName(rs.getString("FullName"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setRole(rs.getBoolean("Role"));
                user.setIsActive(rs.getBoolean("IsActive"));
                java.sql.Timestamp ts = rs.getTimestamp("CreatedDate");
                if (ts != null) {
                    user.setCreatedDate(ts.toLocalDateTime());
                }
                user.setEmail(rs.getString("Email"));
                return user;
            }
            if (rs.getStatement() != null && rs.getStatement().getConnection() != null) {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean updatePassword(int userID, String newPasswordHash) {
        String sql = "UPDATE Users SET PasswordHash = ? WHERE UserID = ?";
        int result = XJdbc.executeUpdate(sql, newPasswordHash, userID);
        return result > 0;
    }

    @Override
    public User findByUsernameAndEmail(String username, String email) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Email = ?";
        List<User> list = selectBySql(sql, username, email);
        return list.isEmpty() ? null : list.get(0);
    }
} 