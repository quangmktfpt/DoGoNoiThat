package poly.dao.impl;

import poly.dao.ShoppingCartDAO;
import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAOImpl implements ShoppingCartDAO {
    @Override
    public ShoppingCart findByUserId(int userId) {
        String sql = "SELECT * FROM ShoppingCarts WHERE UserID = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, userId)) {
            if (rs.next()) {
                ShoppingCart cart = new ShoppingCart();
                cart.setCartId(rs.getInt("CartID"));
                cart.setUserId(rs.getInt("UserID"));
                cart.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
                return cart;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void insert(ShoppingCart cart) {
        String sql = "INSERT INTO ShoppingCarts (UserID, CreatedDate) VALUES (?, ?)";
        XJdbc.executeUpdate(sql, cart.getUserId(), cart.getCreatedDate());
    }

    @Override
    public void update(ShoppingCart cart) {
        String sql = "UPDATE ShoppingCarts SET UserID = ?, CreatedDate = ? WHERE CartID = ?";
        XJdbc.executeUpdate(sql, cart.getUserId(), cart.getCreatedDate(), cart.getCartId());
    }

    @Override
    public void delete(int cartId) {
        String sql = "DELETE FROM ShoppingCarts WHERE CartID = ?";
        XJdbc.executeUpdate(sql, cartId);
    }

    @Override
    public List<CartItem> findCartItemsByCartId(int cartId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT * FROM CartItems WHERE CartID = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, cartId)) {
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("CartItemID"));
                item.setCartId(rs.getInt("CartID"));
                item.setProductId(rs.getString("ProductID"));
                item.setQuantity(rs.getInt("Quantity"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public CartItem findCartItemByCartIdAndProductId(int cartId, String productId) {
        String sql = "SELECT * FROM CartItems WHERE CartID = ? AND ProductID = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, cartId, productId)) {
            if (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("CartItemID"));
                item.setCartId(rs.getInt("CartID"));
                item.setProductId(rs.getString("ProductID"));
                item.setQuantity(rs.getInt("Quantity"));
                return item;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void insertCartItem(CartItem item) {
        String sql = "INSERT INTO CartItems (CartID, ProductID, Quantity) VALUES (?, ?, ?)";
        XJdbc.executeUpdate(sql, item.getCartId(), item.getProductId(), item.getQuantity());
    }

    @Override
    public void updateCartItem(CartItem item) {
        String sql = "UPDATE CartItems SET Quantity = ? WHERE CartItemID = ?";
        XJdbc.executeUpdate(sql, item.getQuantity(), item.getCartItemId());
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM CartItems WHERE CartItemID = ?";
        XJdbc.executeUpdate(sql, cartItemId);
    }

    @Override
    public void deleteCartItemByCartIdAndProductId(int cartId, String productId) {
        String sql = "DELETE FROM CartItems WHERE CartID = ? AND ProductID = ?";
        XJdbc.executeUpdate(sql, cartId, productId);
    }

    // Các phương thức tổng hợp sẽ bổ sung sau nếu cần
    @Override
    public void addProductToCart(int userId, String productId, int quantity) {
        // 1. Kiểm tra user đã có giỏ hàng chưa
        ShoppingCart cart = findByUserId(userId);
        if (cart == null) {
            // Tạo giỏ hàng mới nếu chưa có
            cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setCreatedDate(java.time.LocalDateTime.now());
            insert(cart);
            // Lấy lại cart với ID đã được tạo
            cart = findByUserId(userId);
        }
        
        // 2. Kiểm tra sản phẩm đã có trong giỏ chưa
        CartItem existingItem = findCartItemByCartIdAndProductId(cart.getCartId(), productId);
        if (existingItem != null) {
            // Nếu đã có, tăng số lượng
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            updateCartItem(existingItem);
        } else {
            // Nếu chưa có, thêm mới
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getCartId());
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            insertCartItem(newItem);
        }
    }

    @Override
    public void updateProductQuantity(int userId, String productId, int quantity) {
        // TODO: Bổ sung logic cập nhật số lượng sản phẩm trong giỏ
    }

    @Override
    public void removeProductFromCart(int userId, String productId) {
        // TODO: Bổ sung logic xoá sản phẩm khỏi giỏ
    }

    @Override
    public void clearCart(int userId) {
        // TODO: Bổ sung logic xoá toàn bộ sản phẩm trong giỏ
    }
} 