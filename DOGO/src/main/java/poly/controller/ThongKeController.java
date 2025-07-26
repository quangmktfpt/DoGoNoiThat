package poly.controller;

import java.time.LocalDateTime;
import java.util.List;
import poly.entity.Order;
import poly.entity.Product;

public interface ThongKeController {
    // Thống kê doanh thu theo khoảng thời gian
    List<Object[]> thongKeDoanhThu(LocalDateTime from, LocalDateTime to);

    // Thống kê lợi nhuận theo khoảng thời gian
    List<Object[]> thongKeLoiNhuan(LocalDateTime from, LocalDateTime to);

    // Thống kê sản phẩm bán chạy nhất
    List<Object[]> thongKeSanPhamBanChay(int top, String categoryId);

    // Thống kê tồn kho sản phẩm
    List<Object[]> thongKeTonKho();

    // Thống kê đơn hàng theo trạng thái và thời gian
    List<Order> thongKeDonHang(LocalDateTime from, LocalDateTime to, String status);

    // Xuất báo cáo (có thể trả về file hoặc dữ liệu)
    byte[] xuatBaoCao(String loaiBaoCao, String dinhDang, LocalDateTime from, LocalDateTime to);
} 