/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.dao.impl;

import java.util.List;
import poly.dao.UserDAO;
import poly.entity.User;
import poly.util.XJdbc;
import poly.util.XQuery;

/**
 *
 * @author Admin
 */
public class UserDAOImpl implements UserDAO{
     String createSql = "INSERT INTO Users(Username, PasswordHash,) VALUES (?, ?)";
    String updateSql = "UPDATE Users SET PasswordHash=?, WHERE Username=?";
    String deleteSql = "DELETE FROM Users WHERE Username=?";
    String findAllSql = "SELECT * FROM Users";
    String findByUsernameSql = "SELECT * FROM Users WHERE Username=?";

    @Override
    public User create(User entity) {
        Object[] values = {
            entity.getUsername(),
            entity.getPasswordHash(),
         
           
         
        };
        XJdbc.executeUpdate(createSql, values);
        return entity;
    }

    @Override
    public void update(User entity) {
        Object[] values = {
            entity.getPasswordHash(),
          
            entity.getUsername()
        };
        XJdbc.executeUpdate(updateSql, values);
    }

    @Override
    public void deleteById(String username) {
      XJdbc.executeUpdate(deleteSql, username);
    }

    @Override
    public List<User> findAll() {
        return XQuery.getBeanList(User.class, findAllSql);
 }

    @Override
    public User findById(String username) {
        return XQuery.getSingleBean(User.class, findByUsernameSql, username);
  }
}
