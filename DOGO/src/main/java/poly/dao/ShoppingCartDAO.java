package poly.dao;

import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import java.util.List;

public interface ShoppingCartDAO {
    // Thao tác với ShoppingCart
    ShoppingCart findByUserId(int userId);
    void insert(ShoppingCart cart);
    void update(ShoppingCart cart);
    void delete(int cartId);
    
    // Thao tác với CartItem
    List<CartItem> findCartItemsByCartId(int cartId);
    CartItem findCartItemByCartIdAndProductId(int cartId, String productId);
    void insertCartItem(CartItem item);
    void updateCartItem(CartItem item);
    void deleteCartItem(int cartItemId);
    void deleteCartItemByCartIdAndProductId(int cartId, String productId);
    
    // Thao tác tổng hợp
    void addProductToCart(int userId, String productId, int quantity);
    void updateProductQuantity(int userId, String productId, int quantity);
    void removeProductFromCart(int userId, String productId);
    void clearCart(int userId);
} 