package poly.util;

import poly.dao.impl.UserDAOImpl;
import poly.entity.User;
import java.util.List;

/**
 * Utility class để hash tất cả password chưa được hash trong database
 * 
 * @author quang
 */
public class PasswordHashFixer {
    
    public static void main(String[] args) {
        hashAllUnhashedPasswords();
    }
    
    /**
     * Hash tất cả password chưa được hash trong database
     */
    public static void hashAllUnhashedPasswords() {
        UserDAOImpl userDAO = new UserDAOImpl();
        List<User> users = userDAO.selectAll();
        
        System.out.println("Bắt đầu hash password...");
        int updatedCount = 0;
        
        for (User user : users) {
            String currentHash = user.getPasswordHash();
            
            // Kiểm tra xem password đã được hash chưa
            // Password đã hash sẽ có format: "salt:hash"
            if (currentHash != null && !currentHash.contains(":")) {
                System.out.println("Hash password cho user: " + user.getUsername());
                System.out.println("Password cũ: " + currentHash);
                
                // Hash password
                String hashedPassword = PasswordUtil.hashPassword(currentHash);
                user.setPasswordHash(hashedPassword);
                
                // Cập nhật vào database
                try {
                    userDAO.update(user);
                    System.out.println("Password mới: " + hashedPassword);
                    updatedCount++;
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật password cho user " + user.getUsername() + ": " + e.getMessage());
                }
            } else {
                System.out.println("User " + user.getUsername() + " đã có password được hash");
            }
        }
        
        System.out.println("Hoàn thành! Đã hash " + updatedCount + " password.");
    }
    
    /**
     * Hash password cho một user cụ thể
     */
    public static void hashPasswordForUser(String username, String plainPassword) {
        UserDAOImpl userDAO = new UserDAOImpl();
        User user = userDAO.selectByUsername(username);
        
        if (user == null) {
            System.err.println("Không tìm thấy user: " + username);
            return;
        }
        
        System.out.println("Hash password cho user: " + username);
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        user.setPasswordHash(hashedPassword);
        
        try {
            userDAO.update(user);
            System.out.println("Đã hash password thành công cho user: " + username);
            System.out.println("Password hash: " + hashedPassword);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật password: " + e.getMessage());
        }
    }
    
    /**
     * Test verify password
     */
    public static void testPasswordVerification(String username, String plainPassword) {
        UserDAOImpl userDAO = new UserDAOImpl();
        User user = userDAO.selectByUsername(username);
        
        if (user == null) {
            System.err.println("Không tìm thấy user: " + username);
            return;
        }
        
        System.out.println("Test password verification cho user: " + username);
        System.out.println("Plain password: " + plainPassword);
        System.out.println("Stored hash: " + user.getPasswordHash());
        
        boolean result = PasswordUtil.verifyPassword(plainPassword, user.getPasswordHash());
        System.out.println("Verification result: " + result);
    }
} 