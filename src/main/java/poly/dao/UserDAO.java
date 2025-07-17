<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
=======
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
package poly.dao;

import poly.entity.User;

<<<<<<< HEAD
public interface UserDAO extends  CrudDao<User, String>{
    
}
=======
public interface UserDAO extends CrudDAO<User, Integer> {
    // Đăng nhập, trả về User nếu thành công, null nếu thất bại
    User login(String username, String password);
} 
>>>>>>> eed6712 (đây là code của phần Caidatchung.java HotroJDialog.java GioHangJDialog.java)
