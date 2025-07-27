package poly.controller;

import poly.ui.KhachJFrame;
import poly.ui.DatHangJDialog;
import poly.ui.HoSoJDialog;
import poly.ui.HoSoJDialog1;
import poly.ui.HoTroJDialog;
import poly.ui.DanhGiaJDialog1;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Controller cho KhachJFrame
 * Xử lý các sự kiện và logic cho giao diện Khách hàng
 */
public class KhachJFrameController {
    
    private KhachJFrame view;
    
    public KhachJFrameController(KhachJFrame view) {
        this.view = view;
    }
    
    /**
     * Mở màn hình thanh toán
     */
    public void openThanhToan() {
        DatHangJDialog datHang = new DatHangJDialog(view, true);
        datHang.setLocationRelativeTo(view);
        datHang.setVisible(true);
    }
    
    /**
     * Mở màn hình hồ sơ
     */
    public void openHoSo() {
        // Lấy username từ CurrentUserUtil hoặc từ view
        String username = getCurrentUsername();
        if (username != null && !username.trim().isEmpty()) {
            HoSoJDialog1 hoSo = new HoSoJDialog1(view, true, username);
            hoSo.setLocationRelativeTo(view);
            hoSo.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view, 
                "Không thể xác định người dùng hiện tại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Lấy username của người dùng hiện tại
     */
    private String getCurrentUsername() {
        try {
            // Thử lấy từ CurrentUserUtil trước
            return poly.util.CurrentUserUtil.getCurrentUsername();
        } catch (Exception e) {
            // Nếu không có, có thể lấy từ view hoặc database
            System.err.println("Không thể lấy username từ CurrentUserUtil: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Mở màn hình hỗ trợ
     */
    public void openHoTro() {
        HoTroJDialog hoTro = new HoTroJDialog(view, true);
        hoTro.setLocationRelativeTo(view);
        hoTro.setVisible(true);
    }

    /**
     * Mở dialog đánh giá sản phẩm
     * @param tenSanPham tên sản phẩm
     * @param hinhAnh icon sản phẩm
     * @param daMua true nếu khách đã mua sản phẩm
     */
    public void openDanhGia(String tenSanPham, ImageIcon hinhAnh, boolean daMua) {
        DanhGiaJDialog1 dlg = new DanhGiaJDialog1(view, tenSanPham, hinhAnh, daMua);
        dlg.setVisible(true);
    }
} 