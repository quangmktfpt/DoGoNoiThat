/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author quang
 */
public class CategorySelectTest {
     public static void main(String[] args) {
        System.out.println("=== Danh sách Category ===");
        String sql = "SELECT category_id, name, description FROM categories";
        try (ResultSet rs = XJdbc.executeQuery(sql)) {
            System.out.printf("%-5s | %-20s | %s%n", "ID", "Name", "Description");
            System.out.println("-----------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                System.out.printf("%-5d | %-20s | %s%n", id, name, desc);
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi khi truy vấn danh mục: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
