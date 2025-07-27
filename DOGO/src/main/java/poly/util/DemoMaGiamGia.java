package poly.util;

import poly.ui.manager.MaGiamGia1;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Demo chức năng quản lý mã giảm giá
 */
public class DemoMaGiamGia {
    
    public static void main(String[] args) {
        System.out.println("=== DEMO CHỨC NĂNG QUẢN LÝ MÃ GIẢM GIÁ ===\n");
        
        // Chạy trên EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                // Tạo frame chính
                JFrame mainFrame = new JFrame("Demo Quản lý Mã Giảm Giá");
                mainFrame.setSize(1000, 700);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setLocationRelativeTo(null);
                
                // Mở dialog quản lý mã giảm giá
                MaGiamGia1 dialog = new MaGiamGia1(mainFrame, true);
                dialog.setLocationRelativeTo(mainFrame);
                
                System.out.println("🎯 HƯỚNG DẪN SỬ DỤNG:");
                System.out.println("1. Double-click vào dòng trong bảng để xem chi tiết");
                System.out.println("2. Sử dụng các nút 'Chọn mục', 'Bỏ chọn mục', 'Xóa mục đã chọn'");
                System.out.println("3. Thử chức năng tìm kiếm");
                System.out.println("4. Thêm/sửa/xóa mã giảm giá");
                System.out.println();
                
                // Hiển thị dialog
                dialog.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi mở demo: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Hướng dẫn sử dụng chi tiết
     */
    public static void showInstructions() {
        System.out.println("📋 HƯỚNG DẪN CHI TIẾT:");
        System.out.println();
        System.out.println("🖱️  DOUBLE-CLICK BẢNG:");
        System.out.println("   - Nhấn 2 lần vào dòng trong bảng");
        System.out.println("   - Tự động chuyển sang tab 'Chỉnh sửa'");
        System.out.println("   - Hiển thị thông tin mã giảm giá");
        System.out.println("   - Hiện dialog thông báo");
        System.out.println();
        System.out.println("🔍 TÌM KIẾM:");
        System.out.println("   - Nhập từ khóa vào ô tìm kiếm");
        System.out.println("   - Click nút 'Tìm'");
        System.out.println("   - Xem kết quả trong bảng");
        System.out.println();
        System.out.println("✅ CHỌN MỤC:");
        System.out.println("   - Click 'Chọn mục' → chọn tất cả");
        System.out.println("   - Click 'Bỏ chọn mục' → bỏ chọn tất cả");
        System.out.println("   - Click 'Xóa mục đã chọn' → xóa các mục đã check");
        System.out.println();
        System.out.println("✏️  CHỈNH SỬA:");
        System.out.println("   - Chuyển sang tab 'Chỉnh sửa'");
        System.out.println("   - Nhập thông tin mã giảm giá");
        System.out.println("   - Click 'Thêm' / 'Cập nhật' / 'Xóa'");
        System.out.println();
    }
} 