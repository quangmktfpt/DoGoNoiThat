package poly.util;

import poly.dao.ProductDAO;
import poly.dao.impl.ProductDAOImpl;
import poly.entity.Product;
import java.math.BigDecimal;

/**
 * Class test để kiểm tra việc lấy giá sản phẩm từ database
 */
public class TestProductPrices {
    
    public static void main(String[] args) {
        testProductPrices();
    }
    
    public static void testProductPrices() {
        try {
            System.out.println("=== TEST LẤY GIÁ SẢN PHẨM TỪ DATABASE ===");
            
            ProductDAO productDAO = new ProductDAOImpl();
            
            // Test lấy sản phẩm cụ thể
            String[] productIds = {"PROD001", "PROD003"};
            String[] expectedNames = {"Classic Wooden Chair", "Comfort Sofa"};
            BigDecimal[] expectedPrices = {new BigDecimal("49.99"), new BigDecimal("499.99")};
            
            for (int i = 0; i < productIds.length; i++) {
                String productId = productIds[i];
                Product product = productDAO.selectById(productId);
                
                if (product != null) {
                    System.out.println("✓ Sản phẩm " + productId + ":");
                    System.out.println("  - Tên: " + product.getProductName());
                    System.out.println("  - Giá: " + formatPrice(product.getUnitPrice()));
                    System.out.println("  - Số lượng: " + product.getQuantity());
                    
                    // Kiểm tra giá có đúng không
                    if (product.getUnitPrice() != null && product.getUnitPrice().compareTo(expectedPrices[i]) == 0) {
                        System.out.println("  ✓ Giá chính xác: " + formatPrice(expectedPrices[i]));
                    } else {
                        System.out.println("  ✗ Giá không đúng! Mong đợi: " + formatPrice(expectedPrices[i]));
                    }
                    
                    // Tính tổng giá trị
                    if (product.getUnitPrice() != null && product.getQuantity() != null) {
                        BigDecimal totalValue = product.getUnitPrice().multiply(new BigDecimal(product.getQuantity()));
                        System.out.println("  - Tổng giá trị: " + formatPrice(totalValue));
                    }
                    
                } else {
                    System.out.println("✗ Không tìm thấy sản phẩm: " + productId);
                }
                System.out.println();
            }
            
            // Test tính toán đơn hàng mẫu
            System.out.println("=== TÍNH TOÁN ĐƠN HÀNG MẪU ===");
            
            Product chair = productDAO.selectById("PROD001");
            Product sofa = productDAO.selectById("PROD003");
            
            if (chair != null && sofa != null) {
                BigDecimal chairTotal = chair.getUnitPrice().multiply(new BigDecimal(2)); // 2 cái ghế
                BigDecimal sofaTotal = sofa.getUnitPrice().multiply(new BigDecimal(1)); // 1 cái sofa
                BigDecimal subtotal = chairTotal.add(sofaTotal);
                
                System.out.println("Classic Wooden Chair (x2): " + formatPrice(chairTotal));
                System.out.println("Comfort Sofa (x1): " + formatPrice(sofaTotal));
                System.out.println("Tổng tạm tính: " + formatPrice(subtotal));
                
                // Tính phí vận chuyển cho Hà Nội
                BigDecimal shippingFee = new BigDecimal("15000");
                System.out.println("Phí vận chuyển (Hà Nội): " + formatPrice(shippingFee));
                
                // Tính giảm giá CP20 (20%)
                BigDecimal discount = subtotal.multiply(new BigDecimal("0.20"));
                System.out.println("Giảm giá CP20 (20%): " + formatPrice(discount));
                
                // Tổng cộng
                BigDecimal total = subtotal.add(shippingFee).subtract(discount);
                System.out.println("Tổng cộng: " + formatPrice(total));
                
            } else {
                System.out.println("✗ Không thể tính toán đơn hàng - thiếu dữ liệu sản phẩm");
            }
            
        } catch (Exception e) {
            System.err.println("Lỗi khi test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String formatPrice(BigDecimal price) {
        if (price == null) return "0 ₫";
        return String.format("%,.0f ₫", price);
    }
} 