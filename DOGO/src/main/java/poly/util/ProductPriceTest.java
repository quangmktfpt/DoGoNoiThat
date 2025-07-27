package poly.util;

import poly.dao.ProductDAO;
import poly.dao.impl.ProductDAOImpl;
import poly.entity.Product;
import java.math.BigDecimal;

/**
 * Class test để kiểm tra việc lấy giá sản phẩm từ database
 */
public class ProductPriceTest {
    
    public static void main(String[] args) {
        testProductPriceRetrieval();
    }
    
    public static void testProductPriceRetrieval() {
        try {
            ProductDAO productDAO = new ProductDAOImpl();
            
            // Test lấy tất cả sản phẩm
            System.out.println("=== DANH SÁCH TẤT CẢ SẢN PHẨM ===");
            var allProducts = productDAO.selectAll();
            for (Product product : allProducts) {
                System.out.printf("ID: %s | Tên: %s | Giá: %s | Số lượng: %d%n", 
                    product.getProductId(), 
                    product.getProductName(), 
                    formatPrice(product.getUnitPrice()), 
                    product.getQuantity());
            }
            
            // Test lấy sản phẩm theo ID
            System.out.println("\n=== TEST LẤY SẢN PHẨM THEO ID ===");
            String[] testIds = {"PROD001", "PROD002", "PROD003", "PROD004", "PROD005"};
            for (String productId : testIds) {
                Product product = productDAO.selectById(productId);
                if (product != null) {
                    System.out.printf("ID: %s | Tên: %s | Giá: %s | Số lượng: %d%n", 
                        product.getProductId(), 
                        product.getProductName(), 
                        formatPrice(product.getUnitPrice()), 
                        product.getQuantity());
                } else {
                    System.out.printf("Không tìm thấy sản phẩm với ID: %s%n", productId);
                }
            }
            
            // Test tính toán giá trị đơn hàng
            System.out.println("\n=== TEST TÍNH TOÁN GIÁ TRỊ ĐƠN HÀNG ===");
            BigDecimal totalValue = BigDecimal.ZERO;
            for (Product product : allProducts) {
                if (product.getUnitPrice() != null && product.getQuantity() != null) {
                    BigDecimal itemValue = product.getUnitPrice().multiply(new BigDecimal(product.getQuantity()));
                    totalValue = totalValue.add(itemValue);
                    System.out.printf("Sản phẩm: %s | Giá: %s | SL: %d | Thành tiền: %s%n", 
                        product.getProductName(),
                        formatPrice(product.getUnitPrice()),
                        product.getQuantity(),
                        formatPrice(itemValue));
                }
            }
            System.out.printf("Tổng giá trị kho: %s%n", formatPrice(totalValue));
            
        } catch (Exception e) {
            System.err.println("Lỗi khi test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String formatPrice(BigDecimal price) {
        if (price == null) return "0 VND";
        return String.format("%,.0f VND", price);
    }
} 