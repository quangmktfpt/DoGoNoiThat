package poly.dao;

import poly.entity.User;

public interface UserDAO1 extends CrudDAO<User, Integer> {
       User login(String username, String password);
    User findByUsername(String username);
    User findById(int id);

} 