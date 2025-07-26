package poly.controller;

import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import java.util.List;

public interface ShoppingCartController extends CrudController<ShoppingCart> {
    // Inject DAO
    void setShoppingCartDAO(poly.dao.ShoppingCartDAO dao);

    // CartItem thao tác
    List<CartItem> getCartItems(int cartId);
    CartItem getCartItem(int cartId, String productId);
    void addCartItem(CartItem item);
    void updateCartItem(CartItem item);
    void deleteCartItem(int cartItemId);
    void deleteCartItemByProduct(int cartId, String productId);

    // Tổng hợp thao tác
    void addProductToCart(int userId, String productId, int quantity);
    void updateProductQuantity(int userId, String productId, int quantity);
    void removeProductFromCart(int userId, String productId);
    void clearCart(int userId);
} 