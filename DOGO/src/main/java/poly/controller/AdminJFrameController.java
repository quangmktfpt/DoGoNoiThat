package poly.controller;

import poly.ui.AdminJFrame;
import poly.ui.manager.PhanQuyen_nghia;
import poly.ui.manager.QLNhaCungCap1;
import poly.ui.manager.CaiDatChung;
import poly.ui.manager.MaGiamGia1;

/**
 * Controller cho AdminJFrame
 * Xử lý các sự kiện và logic cho giao diện Admin
 */
public class AdminJFrameController {
    
    private AdminJFrame view;
    
    public AdminJFrameController(AdminJFrame view) {
        this.view = view;
    }
    
    /**
     * Mở màn hình phân quyền
     */
    public void openPhanQuyen() {
        PhanQuyen_nghia phanQuyen = new PhanQuyen_nghia(view, true);
        phanQuyen.setLocationRelativeTo(view);
        phanQuyen.setVisible(true);
    }
    
    /**
     * Mở màn hình quản lý nhà cung cấp
     */
    public void openQLNhaCungCap() {
        QLNhaCungCap1 qlncc = new QLNhaCungCap1(view, true);
        qlncc.setLocationRelativeTo(view);
        qlncc.setVisible(true);
    }
    
    /**
     * Mở màn hình cài đặt chung
     */
    public void openCaiDatChung() {
        CaiDatChung caidat = new CaiDatChung(view, true);
        caidat.setLocationRelativeTo(view);
        caidat.setVisible(true);
    }
    
    /**
     * Mở màn hình mã giảm giá
     */
    public void openMaGiamGia() {
        MaGiamGia1 magiamgia = new MaGiamGia1(view, true);
        magiamgia.setLocationRelativeTo(view);
        magiamgia.setVisible(true);
    }
} 