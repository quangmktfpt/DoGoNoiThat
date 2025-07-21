/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author quang
 */
public class ConnectionTest {
    public static void main(String[] args) {
        System.out.println("=== Bắt đầu kiểm thử kết nối ===");
        try {
            // Mở kết nối
            Connection conn = XJdbc.openConnection();

            // Kiểm tra trạng thái kết nối
            if (conn != null && !conn.isClosed()) {
                System.out.println("Kết nối thành công đến cơ sở dữ liệu!");
            } else {
                System.err.println("Kết nối thất bại hoặc đã bị đóng.");
            }
        } catch (SQLException | RuntimeException e) {
            System.err.println("Lỗi khi kết nối: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            XJdbc.closeConnection();
            System.out.println("=== Kết thúc kiểm thử kết nối ===");
        }
    }
}
