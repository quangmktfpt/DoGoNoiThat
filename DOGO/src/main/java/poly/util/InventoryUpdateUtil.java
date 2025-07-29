package poly.util;

import poly.entity.OrderDetail;
import poly.entity.OrderRequestItem;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Utility class để xử lý việc cập nhật kho khi xác nhận đơn hàng
 * @author Assistant
 */
public class InventoryUpdateUtil {
    
    /**
     * Kiểm tra tồn kho trước khi xác nhận đơn hàng
     * @param orderId ID đơn hàng
     * @return true nếu đủ hàng, false nếu thiếu hàng
     */
    public static boolean checkInventoryBeforeConfirmation(Integer orderId) {
        try {
            // Lấy chi tiết đơn hàng
            String selectOrderItemsSql = "SELECT od.ProductID, od.Quantity, p.ProductName, p.Quantity as AvailableQuantity " +
                                       "FROM OrderDetails od " +
                                       "JOIN Products p ON od.ProductID = p.ProductID " +
                                       "WHERE od.OrderID = ?";
            
            ResultSet rs = XJdbc.executeQuery(selectOrderItemsSql, orderId);
            
            StringBuilder insufficientItems = new StringBuilder();
            insufficientItems.append("⚠️ KIỂM TRA TỒN KHO TRƯỚC KHI XÁC NHẬN ĐƠN HÀNG\n\n");
            
            boolean hasInsufficientItems = false;
            
            while (rs.next()) {
                String productId = rs.getString("ProductID");
                String productName = rs.getString("ProductName");
                int requestedQuantity = rs.getInt("Quantity");
                int availableQuantity = rs.getInt("AvailableQuantity");
                
                if (requestedQuantity > availableQuantity) {
                    hasInsufficientItems = true;
                    insufficientItems.append("• ").append(productName)
                            .append(": Cần ").append(requestedQuantity)
                            .append(", Có ").append(availableQuantity)
                            .append(" ❌\n");
                }
            }
            rs.close();
            
            if (hasInsufficientItems) {
                insufficientItems.append("\n❌ Không thể xác nhận đơn hàng do thiếu hàng trong kho!");
                JOptionPane.showMessageDialog(null, 
                    insufficientItems.toString(),
                    "Thiếu hàng trong kho", 
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra tồn kho: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "❌ Lỗi khi kiểm tra tồn kho!\nLỗi: " + e.getMessage(),
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Trừ kho sau khi xác nhận đơn hàng
     * @param orderId ID đơn hàng
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean updateInventoryAfterConfirmation(Integer orderId) {
        try {
            // Lấy chi tiết đơn hàng
            String selectOrderItemsSql = "SELECT od.ProductID, od.Quantity, p.ProductName " +
                                       "FROM OrderDetails od " +
                                       "JOIN Products p ON od.ProductID = p.ProductID " +
                                       "WHERE od.OrderID = ?";
            
            ResultSet rs = XJdbc.executeQuery(selectOrderItemsSql, orderId);
            
            StringBuilder updateLog = new StringBuilder();
            updateLog.append("📦 CẬP NHẬT KHO SAU XÁC NHẬN ĐƠN HÀNG\n\n");
            updateLog.append("Mã đơn hàng: ").append(orderId).append("\n\n");
            
            while (rs.next()) {
                String productId = rs.getString("ProductID");
                String productName = rs.getString("ProductName");
                int quantity = rs.getInt("Quantity");
                
                // Cập nhật số lượng trong kho
                String updateSql = "UPDATE Products SET Quantity = Quantity - ? WHERE ProductID = ?";
                int updatedRows = XJdbc.executeUpdate(updateSql, quantity, productId);
                
                if (updatedRows > 0) {
                    // Lấy thông tin sản phẩm sau khi cập nhật
                    String selectSql = "SELECT Quantity FROM Products WHERE ProductID = ?";
                    ResultSet rs2 = XJdbc.executeQuery(selectSql, productId);
                    
                    if (rs2.next()) {
                        int newQuantity = rs2.getInt("Quantity");
                        
                        updateLog.append("• ").append(productName)
                                .append(": Đã trừ ").append(quantity)
                                .append(", Còn lại ").append(newQuantity)
                                .append(" ✅\n");
                        
                        System.out.println("✓ Đã trừ kho: " + productName + " - " + quantity + " sản phẩm");
                    }
                    rs2.close();
                } else {
                    updateLog.append("• ").append(productName)
                            .append(": Lỗi cập nhật kho ❌\n");
                    System.err.println("❌ Lỗi cập nhật kho cho sản phẩm: " + productName);
                }
            }
            rs.close();
            
            System.out.println(updateLog.toString());
            return true;
            
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật kho: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "❌ Lỗi khi cập nhật kho!\nLỗi: " + e.getMessage(),
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Trừ kho cho danh sách OrderRequestItem (cho đơn hàng mới)
     * @param items Danh sách sản phẩm cần trừ kho
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean updateInventoryForOrderRequest(List<OrderRequestItem> items) {
        StringBuilder updateLog = new StringBuilder();
        updateLog.append("📦 CẬP NHẬT KHO SAU ĐẶT HÀNG\n\n");
        
        for (OrderRequestItem item : items) {
            try {
                // Cập nhật số lượng trong kho
                String updateSql = "UPDATE Products SET Quantity = Quantity - ? WHERE ProductID = ?";
                int updatedRows = XJdbc.executeUpdate(updateSql, item.getQuantity(), item.getProductId());
                
                if (updatedRows > 0) {
                    // Lấy thông tin sản phẩm để hiển thị
                    String selectSql = "SELECT ProductName, Quantity FROM Products WHERE ProductID = ?";
                    ResultSet rs = XJdbc.executeQuery(selectSql, item.getProductId());
                    
                    if (rs.next()) {
                        String productName = rs.getString("ProductName");
                        int newQuantity = rs.getInt("Quantity");
                        
                        updateLog.append("• ").append(productName)
                                .append(": Đã trừ ").append(item.getQuantity())
                                .append(", Còn lại ").append(newQuantity)
                                .append(" ✅\n");
                        
                        System.out.println("✓ Đã trừ kho: " + productName + " - " + item.getQuantity() + " sản phẩm");
                    }
                    rs.close();
                } else {
                    updateLog.append("• Sản phẩm ID ").append(item.getProductId())
                            .append(": Lỗi cập nhật kho ❌\n");
                    System.err.println("❌ Lỗi cập nhật kho cho sản phẩm ID: " + item.getProductId());
                }
                
            } catch (Exception e) {
                updateLog.append("• Sản phẩm ID ").append(item.getProductId())
                        .append(": Lỗi cập nhật kho ❌\n");
                System.err.println("Lỗi khi cập nhật kho cho sản phẩm " + item.getProductId() + ": " + e.getMessage());
            }
        }
        
        System.out.println(updateLog.toString());
        return true;
    }
    
    /**
     * Kiểm tra tồn kho cho danh sách OrderRequestItem
     * @param items Danh sách sản phẩm cần kiểm tra
     * @return true nếu đủ hàng, false nếu thiếu hàng
     */
    public static boolean checkInventoryForOrderRequest(List<OrderRequestItem> items) {
        StringBuilder insufficientItems = new StringBuilder();
        insufficientItems.append("⚠️ KIỂM TRA TỒN KHO\n\n");
        
        boolean hasInsufficientItems = false;
        
        for (OrderRequestItem item : items) {
            try {
                // Lấy thông tin sản phẩm từ database
                String sql = "SELECT ProductName, Quantity FROM Products WHERE ProductID = ?";
                ResultSet rs = XJdbc.executeQuery(sql, item.getProductId());
                
                if (rs.next()) {
                    String productName = rs.getString("ProductName");
                    int availableQuantity = rs.getInt("Quantity");
                    int requestedQuantity = item.getQuantity();
                    
                    if (requestedQuantity > availableQuantity) {
                        hasInsufficientItems = true;
                        insufficientItems.append("• ").append(productName)
                                .append(": Cần ").append(requestedQuantity)
                                .append(", Có ").append(availableQuantity)
                                .append(" ❌\n");
                    }
                }
                rs.close();
            } catch (Exception e) {
                System.err.println("Lỗi khi kiểm tra tồn kho cho sản phẩm " + item.getProductId() + ": " + e.getMessage());
                hasInsufficientItems = true;
                insufficientItems.append("• Sản phẩm ID ").append(item.getProductId())
                        .append(": Lỗi kiểm tra tồn kho\n");
            }
        }
        
        if (hasInsufficientItems) {
            insufficientItems.append("\n❌ Không thể đặt hàng do thiếu hàng trong kho!");
            JOptionPane.showMessageDialog(null, 
                insufficientItems.toString(),
                "Thiếu hàng trong kho", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
} 