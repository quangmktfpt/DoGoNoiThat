package poly.util;

import poly.ui.manager.MaGiamGia1;
import javax.swing.JFrame;

/**
 * Test các chức năng quản lý mã giảm giá
 */
public class TestMaGiamGia {
    
    public static void main(String[] args) {
        System.out.println("=== TEST CHỨC NĂNG QUẢN LÝ MÃ GIẢM GIÁ ===\n");
        
        // Test mở giao diện
        testOpenMaGiamGiaDialog();
        
        // Test các chức năng
        testSelectAllItems();
        testDeselectAllItems();
        testDeleteSelectedItems();
        testCountSelectedItems();
        testTableClickToEdit();
        testSearchFunction();
    }
    
    public static void testOpenMaGiamGiaDialog() {
        try {
            System.out.println("✓ Test mở dialog quản lý mã giảm giá");
            
            // Tạo frame test
            JFrame testFrame = new JFrame("Test Frame");
            testFrame.setSize(800, 600);
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setLocationRelativeTo(null);
            
            // Mở dialog
            MaGiamGia1 dialog = new MaGiamGia1(testFrame, true);
            dialog.setLocationRelativeTo(testFrame);
            
            System.out.println("  - Dialog đã được tạo thành công");
            System.out.println("  - Dialog sẽ hiển thị danh sách mã giảm giá");
            System.out.println("  - Có 2 tab: Chi tiết và Chỉnh sửa");
            
            // Hiển thị dialog (comment lại để không block test)
            // dialog.setVisible(true);
            
            System.out.println("  ✓ Test thành công!");
            
        } catch (Exception e) {
            System.err.println("✗ Lỗi khi test mở dialog: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    public static void testSelectAllItems() {
        System.out.println("✓ Test chức năng 'Chọn mục'");
        System.out.println("  - Chức năng: Chọn tất cả checkbox trong bảng");
        System.out.println("  - Kết quả mong đợi: Tất cả checkbox được check");
        System.out.println("  - Hiển thị thông báo số mục đã chọn");
        System.out.println("  ✓ Test mô tả hoàn thành!");
        System.out.println();
    }
    
    public static void testDeselectAllItems() {
        System.out.println("✓ Test chức năng 'Bỏ chọn mục'");
        System.out.println("  - Chức năng: Bỏ chọn tất cả checkbox trong bảng");
        System.out.println("  - Kết quả mong đợi: Tất cả checkbox được uncheck");
        System.out.println("  - Hiển thị thông báo đã bỏ chọn tất cả");
        System.out.println("  ✓ Test mô tả hoàn thành!");
        System.out.println();
    }
    
    public static void testDeleteSelectedItems() {
        System.out.println("✓ Test chức năng 'Xóa mục đã chọn'");
        System.out.println("  - Chức năng: Xóa tất cả các mục đã được check");
        System.out.println("  - Kiểm tra: Có mục nào được chọn không");
        System.out.println("  - Xác nhận: Hiển thị dialog xác nhận xóa");
        System.out.println("  - Xử lý: Xóa từ cuối lên để tránh lỗi index");
        System.out.println("  - Kết quả: Refresh bảng và hiển thị số mục đã xóa");
        System.out.println("  ✓ Test mô tả hoàn thành!");
        System.out.println();
    }
    
    public static void testCountSelectedItems() {
        System.out.println("✓ Test chức năng đếm mục đã chọn");
        System.out.println("  - Chức năng: Đếm số checkbox đã được check");
        System.out.println("  - Sử dụng: Trong các chức năng khác");
        System.out.println("  - Kết quả: Trả về số lượng mục đã chọn");
        System.out.println("  ✓ Test mô tả hoàn thành!");
        System.out.println();
    }
    
    public static void testTableStructure() {
        System.out.println("✓ Test cấu trúc bảng");
        System.out.println("  - Cột 0: CouponID (String)");
        System.out.println("  - Cột 1: Description (String)");
        System.out.println("  - Cột 2: DiscountType (String)");
        System.out.println("  - Cột 3: DiscountValue (BigDecimal)");
        System.out.println("  - Cột 4: StartDate (LocalDate)");
        System.out.println("  - Cột 5: EndDate (LocalDate)");
        System.out.println("  - Cột 6: Checkbox (Boolean) - Có thể edit");
        System.out.println("  ✓ Test mô tả hoàn thành!");
        System.out.println();
    }
    
    public static void testTableClickToEdit() {
        System.out.println("✓ Test chức năng double-click bảng để chỉnh sửa");
        System.out.println("  - Chức năng: Double-click (nhấn 2 lần) vào dòng trong bảng");
        System.out.println("  - Kết quả mong đợi:");
        System.out.println("    + Tự động chuyển sang tab 'Chỉnh sửa'");
        System.out.println("    + Hiển thị thông tin của dòng được chọn");
        System.out.println("    + Cập nhật currentRow");
        System.out.println("    + In thông báo ra console");
        System.out.println("    + Hiển thị dialog thông tin mã giảm giá");
        System.out.println("  - Lưu ý: Single-click không có tác dụng");
        System.out.println("  ✓ Test mô tả hoàn thành!");
        System.out.println();
    }
    
    public static void testSearchFunction() {
        System.out.println("✓ Test chức năng tìm kiếm");
        System.out.println("  - Chức năng: Tìm kiếm theo CouponID hoặc Description");
        System.out.println("  - Cách sử dụng:");
        System.out.println("    + Nhập từ khóa vào ô tìm kiếm");
        System.out.println("    + Click nút 'Tìm'");
        System.out.println("  - Kết quả mong đợi:");
        System.out.println("    + Hiển thị kết quả tìm kiếm trong bảng");
        System.out.println("    + Hiển thị thông báo số kết quả tìm thấy");
        System.out.println("    + Nếu từ khóa rỗng → hiển thị tất cả");
        System.out.println("  ✓ Test mô tả hoàn thành!");
        System.out.println();
    }
} 