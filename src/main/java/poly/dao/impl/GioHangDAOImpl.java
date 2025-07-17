package poly.dao.impl;

import poly.dao.GioHangDAO;
import poly.entity.CartItem;
import poly.util.XJdbc;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GioHangDAOImpl implements GioHangDAO {
    @Override
    public void insertCartItem(int userId, String productId, int quantity) {
        String sql = "INSERT INTO CartItems (CartID, ProductID, Quantity) " +
                     "SELECT sc.CartID, ?, ? FROM ShoppingCarts sc WHERE sc.UserID = ?";
        XJdbc.executeUpdate(sql, productId, quantity, userId);
    }

    @Override
    public void updateCartItem(int userId, String productId, int quantity) {
        String sql = "UPDATE ci SET ci.Quantity = ? FROM CartItems ci JOIN ShoppingCarts sc ON ci.CartID = sc.CartID WHERE sc.UserID = ? AND ci.ProductID = ?";
        XJdbc.executeUpdate(sql, quantity, userId, productId);
    }

    @Override
    public void deleteCartItem(int userId, String productId) {
        String sql = "DELETE ci FROM CartItems ci JOIN ShoppingCarts sc ON ci.CartID = sc.CartID WHERE sc.UserID = ? AND ci.ProductID = ?";
        XJdbc.executeUpdate(sql, userId, productId);
    }

    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> result = new ArrayList<>();
        String sql = "SELECT ci.CartItemID, ci.CartID, ci.ProductID, ci.Quantity " +
                     "FROM CartItems ci " +
                     "JOIN ShoppingCarts sc ON ci.CartID = sc.CartID " +
                     "WHERE sc.UserID = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, userId)) {
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("CartItemID"));
                item.setCartId(rs.getInt("CartID"));
                item.setProductId(rs.getString("ProductID"));
                item.setQuantity(rs.getInt("Quantity"));
                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
} 