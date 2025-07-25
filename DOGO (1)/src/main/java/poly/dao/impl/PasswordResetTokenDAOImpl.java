package poly.dao.impl;

import poly.dao.PasswordResetTokenDAO;
import poly.entity.PasswordResetToken;
import poly.util.XJdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {
    @Override
    public boolean insert(PasswordResetToken token) {
        String sql = "INSERT INTO PasswordResetTokens (UserID, Token, ExpiryTime, IsUsed, CreatedTime) VALUES (?, ?, ?, ?, ?)";
        int result = XJdbc.executeUpdate(sql, token.getUserID(), token.getToken(), new java.sql.Timestamp(token.getExpiryTime().getTime()), token.isUsed(), new java.sql.Timestamp(token.getCreatedTime().getTime()));
        return result > 0;
    }

    @Override
    public PasswordResetToken findValidToken(int userID, String token) {
        String sql = "SELECT * FROM PasswordResetTokens WHERE UserID = ? AND Token = ? AND IsUsed = 0 AND ExpiryTime >= SYSDATETIME()";
        List<PasswordResetToken> list = select(sql, userID, token);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean markTokenAsUsed(int tokenID) {
        String sql = "UPDATE PasswordResetTokens SET IsUsed = 1 WHERE TokenID = ?";
        int result = XJdbc.executeUpdate(sql, tokenID);
        return result > 0;
    }

    @Override
    public int deleteExpiredOrUsedTokens() {
        String sql = "DELETE FROM PasswordResetTokens WHERE IsUsed = 1 OR ExpiryTime < SYSDATETIME()";
        return XJdbc.executeUpdate(sql);
    }

    @Override
    public List<PasswordResetToken> findTokensByUser(int userID) {
        String sql = "SELECT * FROM PasswordResetTokens WHERE UserID = ? ORDER BY CreatedTime DESC";
        return select(sql, userID);
    }

    private List<PasswordResetToken> select(String sql, Object... args) {
        List<PasswordResetToken> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                PasswordResetToken token = new PasswordResetToken();
                token.setTokenID(rs.getInt("TokenID"));
                token.setUserID(rs.getInt("UserID"));
                token.setToken(rs.getString("Token"));
                token.setExpiryTime(rs.getTimestamp("ExpiryTime"));
                token.setUsed(rs.getBoolean("IsUsed"));
                token.setCreatedTime(rs.getTimestamp("CreatedTime"));
                list.add(token);
            }
            if (rs.getStatement() != null && rs.getStatement().getConnection() != null) {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
} 