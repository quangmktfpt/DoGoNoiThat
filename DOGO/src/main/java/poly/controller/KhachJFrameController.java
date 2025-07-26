package poly.controller;

import poly.ui.KhachJFrame;
import poly.ui.DatHangJDialog;
import poly.ui.HoSoJDialog;
import poly.ui.HoTroJDialog;
import poly.ui.DanhGiaJDialog1;

import javax.swing.ImageIcon;

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
        HoSoJDialog hoSo = new HoSoJDialog(view, true);
        hoSo.setLocationRelativeTo(view);
        hoSo.setVisible(true);
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