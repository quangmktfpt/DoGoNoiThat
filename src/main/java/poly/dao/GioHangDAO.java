package poly.dao;

import poly.entity.CartItem;
import java.util.List;

public interface GioHangDAO {
    void insertCartItem(int userId, String productId, int quantity);
    void updateCartItem(int userId, String productId, int quantity);
    void deleteCartItem(int userId, String productId);
    List<CartItem> getCartItemsByUserId(int userId);
} 