package poly.util;

import poly.dao.impl.UserDAOImpl;
import poly.entity.User;

/**
 * Script để hash password cho user admin
 * 
 * @author quang
 */
public class FixAdminPassword {
    
    public static void main(String[] args) {
        // Hash password cho user admin
        hashAdminPassword();
        
        // Test đăng nhập
        testAdminLogin();
    }
    
    /**
     * Hash password cho user admin
     */
    public static void hashAdminPassword() {
        UserDAOImpl userDAO = new UserDAOImpl();
        User adminUser = userDAO.selectByUsername("admin");
        
        if (adminUser == null) {
            System.err.println("Không tìm thấy user admin!");
            return;
        }
        
        System.out.println("Tìm thấy user admin:");
        System.out.println("Username: " + adminUser.getUsername());
        System.out.println("Password hiện tại: " + adminUser.getPasswordHash());
        
        // Hash password "123456"
        String hashedPassword = PasswordUtil.hashPassword("123456");
        adminUser.setPasswordHash(hashedPassword);
        
        try {
            userDAO.update(adminUser);
            System.out.println("Đã hash password thành công!");
            System.out.println("Password hash mới: " + hashedPassword);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật password: " + e.getMessage());
        }
    }
    
    /**
     * Test đăng nhập với user admin
     */
    public static void testAdminLogin() {
        UserDAOImpl userDAO = new UserDAOImpl();
        
        // Test login với password "123456"
        User user = userDAO.login("admin", "123456");
        
        if (user != null) {
            System.out.println("✅ Đăng nhập thành công!");
            System.out.println("Username: " + user.getUsername());
            System.out.println("IsActive: " + user.getIsActive());
            System.out.println("Role: " + user.getRole());
        } else {
            System.err.println("❌ Đăng nhập thất bại!");
        }
    }
} 