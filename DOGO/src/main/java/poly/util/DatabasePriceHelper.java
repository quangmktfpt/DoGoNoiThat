package poly.util;

import poly.dao.ProductDAO;
import poly.dao.impl.ProductDAOImpl;
import poly.entity.Product;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class để kiểm tra và lấy giá sản phẩm từ database
 */
public class DatabasePriceHelper {
    
    /**
     * Kiểm tra kết nối database và lấy thông tin sản phẩm
     */
    public static void checkDatabaseConnection() {
        try {
            System.out.println("=== KIỂM TRA KẾT NỐI DATABASE ===");
            
            // Test kết nối cơ bản
            ResultSet rs = XJdbc.executeQuery("SELECT 1 as test");
            if (rs.next()) {
                System.out.println("✓ Kết nối database thành công");
            }
            
            // Kiểm tra bảng Products
            rs = XJdbc.executeQuery("SELECT COUNT(*) as count FROM Products");
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("✓ Bảng Products có " + count + " sản phẩm");
            }
            
            // Kiểm tra cấu trúc bảng Products
            rs = XJdbc.executeQuery("SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Products' ORDER BY ORDINAL_POSITION");
            System.out.println("✓ Cấu trúc bảng Products:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString("COLUMN_NAME") + " (" + rs.getString("DATA_TYPE") + ")");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy và hiển thị thông tin giá sản phẩm
     */
    public static void displayProductPrices() {
        try {
            System.out.println("\n=== THÔNG TIN GIÁ SẢN PHẨM ===");
            
            ProductDAO productDAO = new ProductDAOImpl();
            List<Product> products = productDAO.selectAll();
            
            if (products.isEmpty()) {
                System.out.println("Không có sản phẩm nào trong database");
                return;
            }
            
            BigDecimal totalValue = BigDecimal.ZERO;
            for (Product product : products) {
                String priceStr = formatPrice(product.getUnitPrice());
                String quantityStr = product.getQuantity() != null ? product.getQuantity().toString() : "N/A";
                
                System.out.printf("ID: %-8s | Tên: %-25s | Giá: %-12s | SL: %-5s%n", 
                    product.getProductId(), 
                    truncateString(product.getProductName(), 25), 
                    priceStr, 
                    quantityStr);
                
                if (product.getUnitPrice() != null && product.getQuantity() != null) {
                    BigDecimal itemValue = product.getUnitPrice().multiply(new BigDecimal(product.getQuantity()));
                    totalValue = totalValue.add(itemValue);
                }
            }
            
            System.out.println("\n" + "=".repeat(70));
            System.out.printf("Tổng giá trị kho: %s%n", formatPrice(totalValue));
            
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test lấy sản phẩm theo ID cụ thể
     */
    public static void testGetProductById(String productId) {
        try {
            System.out.println("\n=== TEST LẤY SẢN PHẨM THEO ID: " + productId + " ===");
            
            ProductDAO productDAO = new ProductDAOImpl();
            Product product = productDAO.selectById(productId);
            
            if (product != null) {
                System.out.println("✓ Tìm thấy sản phẩm:");
                System.out.println("  - ID: " + product.getProductId());
                System.out.println("  - Tên: " + product.getProductName());
                System.out.println("  - Giá: " + formatPrice(product.getUnitPrice()));
                System.out.println("  - Số lượng: " + product.getQuantity());
                System.out.println("  - Danh mục: " + product.getCategoryId());
                
                if (product.getUnitPrice() != null && product.getQuantity() != null) {
                    BigDecimal totalValue = product.getUnitPrice().multiply(new BigDecimal(product.getQuantity()));
                    System.out.println("  - Giá trị tổng: " + formatPrice(totalValue));
                }
            } else {
                System.out.println("✗ Không tìm thấy sản phẩm với ID: " + productId);
            }
            
        } catch (Exception e) {
            System.err.println("Lỗi khi test lấy sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra dữ liệu giỏ hàng và tính toán giá trị
     */
    public static void checkCartData(Integer userId) {
        try {
            System.out.println("\n=== KIỂM TRA DỮ LIỆU GIỎ HÀNG CHO USER: " + userId + " ===");
            
            // Lấy thông tin giỏ hàng
            poly.dao.ShoppingCartDAO cartDAO = new poly.dao.impl.ShoppingCartDAOImpl();
            poly.entity.ShoppingCart cart = cartDAO.findByUserId(userId);
            
            if (cart == null) {
                System.out.println("✗ Không tìm thấy giỏ hàng cho user: " + userId);
                return;
            }
            
            System.out.println("✓ Tìm thấy giỏ hàng ID: " + cart.getCartId());
            
            // Lấy danh sách sản phẩm trong giỏ hàng
            List<poly.entity.CartItem> cartItems = cartDAO.findCartItemsByCartId(cart.getCartId());
            
            if (cartItems.isEmpty()) {
                System.out.println("✗ Giỏ hàng trống");
                return;
            }
            
            System.out.println("✓ Có " + cartItems.size() + " sản phẩm trong giỏ hàng:");
            
            ProductDAO productDAO = new ProductDAOImpl();
            BigDecimal cartTotal = BigDecimal.ZERO;
            
            for (poly.entity.CartItem cartItem : cartItems) {
                Product product = productDAO.selectById(cartItem.getProductId());
                
                if (product != null) {
                    BigDecimal itemTotal = product.getUnitPrice().multiply(new BigDecimal(cartItem.getQuantity()));
                    cartTotal = cartTotal.add(itemTotal);
                    
                    System.out.printf("  - %s (x%d) = %s%n", 
                        product.getProductName(),
                        cartItem.getQuantity(),
                        formatPrice(itemTotal));
                } else {
                    System.out.println("  - Không tìm thấy sản phẩm: " + cartItem.getProductId());
                }
            }
            
            System.out.println("✓ Tổng giá trị giỏ hàng: " + formatPrice(cartTotal));
            
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra giỏ hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String formatPrice(BigDecimal price) {
        if (price == null) return "0 VND";
        return String.format("%,.0f VND", price);
    }
    
    private static String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    public static void main(String[] args) {
        // Chạy tất cả các test
        checkDatabaseConnection();
        displayProductPrices();
        testGetProductById("PROD001");
        testGetProductById("PROD002");
        testGetProductById("PROD003");
        
        // Test với user ID cụ thể (thay đổi theo user thực tế)
        checkCartData(1);
    }
} 