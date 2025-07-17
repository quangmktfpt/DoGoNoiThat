package poly.controller;

import poly.entity.CartItem;
import java.util.List;

public interface GioHangController {
    void themVaoGio(int userId, String productId, int soLuong);
    void capNhatSoLuong(int userId, String productId, int soLuongMoi);
    void xoaKhoiGio(int userId, String productId);
    List<CartItem> layDanhSachGioHang(int userId);
} 