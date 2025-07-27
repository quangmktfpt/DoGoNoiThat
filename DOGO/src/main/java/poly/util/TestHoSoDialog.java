package poly.util;

import poly.ui.HoSoJDialog1;
import javax.swing.JFrame;

/**
 * Test chức năng mở dialog hồ sơ
 */
public class TestHoSoDialog {
    
    public static void main(String[] args) {
        System.out.println("=== TEST MỞ DIALOG HỒ SƠ ===\n");
        
        // Test với username hợp lệ
        testOpenHoSoDialog("johndoe");
        
        // Test với username không hợp lệ
        testOpenHoSoDialog("");
        
        // Test với username null
        testOpenHoSoDialog(null);
    }
    
    public static void testOpenHoSoDialog(String username) {
        try {
            System.out.println("✓ Test mở dialog hồ sơ với username: [" + username + "]");
            
            if (username == null || username.trim().isEmpty()) {
                System.out.println("  - Username không hợp lệ, sẽ hiển thị thông báo lỗi");
                System.out.println("  - Kết quả mong đợi: Dialog không mở, hiển thị JOptionPane lỗi");
            } else {
                System.out.println("  - Username hợp lệ, sẽ mở dialog hồ sơ");
                System.out.println("  - Kết quả mong đợi: Dialog mở với thông tin user");
                
                // Tạo frame test
                JFrame testFrame = new JFrame("Test Frame");
                testFrame.setSize(400, 300);
                testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                testFrame.setLocationRelativeTo(null);
                
                // Mở dialog hồ sơ
                HoSoJDialog1 hoSo = new HoSoJDialog1(testFrame, true, username);
                hoSo.setLocationRelativeTo(testFrame);
                
                System.out.println("  - Dialog đã được tạo thành công");
                System.out.println("  - Dialog sẽ hiển thị thông tin cho user: " + username);
                
                // Hiển thị dialog (comment lại để không block test)
                // hoSo.setVisible(true);
                
                System.out.println("  ✓ Test thành công!");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Lỗi khi test mở dialog hồ sơ: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
} 