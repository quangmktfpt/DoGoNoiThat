package poly.dao.impl;


import poly.dao.UserDAO;
import poly.entity.User;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import poly.dao.UserDAO1;

public class UserDAOImpl1 implements UserDAO1 {

    private final String INSERT_SQL = 
        "INSERT INTO Users (Username, PasswordHash, FullName, Phone, Address, Email, Role, IsActive) "
      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private final String UPDATE_SQL = 
        "UPDATE Users SET PasswordHash=?, FullName=?, Phone=?, Address=?, Email=?, Role=?, IsActive=? WHERE UserID=?";

    private final String DELETE_SQL = 
        "DELETE FROM Users WHERE UserID=?";

    private final String SELECT_ALL_SQL = 
        "SELECT * FROM Users";

    private final String SELECT_BY_ID_SQL = 
        "SELECT * FROM Users WHERE UserID=?";

    private final String SELECT_BY_USERNAME_SQL = 
        "SELECT * FROM Users WHERE Username=?";

    @Override
    public void insert(User user) {
        XJdbc.executeUpdate(
            INSERT_SQL,
            user.getUsername(),
            user.getPasswordHash(),
            user.getFullName(),
            user.getPhone(),
            user.getAddress(),
            user.getEmail(),
            user.getRole(),
            user.getIsActive()
        );
    }

    @Override
    public void update(User user) {
        XJdbc.executeUpdate(
            UPDATE_SQL,
            user.getPasswordHash(),
            user.getFullName(),
            user.getPhone(),
            user.getAddress(),
            user.getEmail(),
            user.getRole(),
            user.getIsActive(),
            user.getUserId()
        );
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
public User findByUsername(String username) {
    String sql = "SELECT * FROM Users WHERE Username = ? AND Role = 0";
    try {
        ResultSet rs = XJdbc.executeQuery(sql, username);
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

            return user;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

  

  @Override
public User findById(int id) {
    String sql = "SELECT * FROM Users WHERE UserID = ?";
    try {
        ResultSet rs = XJdbc.executeQuery(sql, id);
        if (rs.next()) {
            User user = new User();
            user.setUsername(rs.getString("Username"));
            user.setFullName(rs.getString("FullName"));
            user.setPhone(rs.getString("Phone"));
            user.setAddress(rs.getString("Address"));
            return user;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}
  


