package poly.dao;

import poly.entity.ContactMessage;
import java.util.List;

/**
 * Interface DAO cho ContactMessage
 * @author Nghia
 */
public interface ContactMessageDAO {
    
    /**
     * Thêm tin nhắn liên hệ mới
     */
    void insert(ContactMessage message);
    
    /**
     * Cập nhật tin nhắn liên hệ
     */
    void update(ContactMessage message);
    
    /**
     * Xóa tin nhắn liên hệ
     */
    void delete(Integer messageId);
    
    /**
     * Lấy tất cả tin nhắn liên hệ
     */
    List<ContactMessage> selectAll();
    
    /**
     * Lấy tin nhắn theo ID
     */
    ContactMessage selectById(Integer messageId);
    
    /**
     * Lấy tin nhắn theo người dùng
     */
    List<ContactMessage> selectByUserId(Integer userId);
    
    /**
     * Lấy tin nhắn chưa đọc
     */
    List<ContactMessage> selectUnreadMessages();
    
    /**
     * Lấy tin nhắn đã đọc
     */
    List<ContactMessage> selectReadMessages();
    
    /**
     * Đánh dấu tin nhắn đã đọc
     */
    void markAsRead(Integer messageId);
    
    /**
     * Đánh dấu tất cả tin nhắn đã đọc
     */
    void markAllAsRead();
    
    /**
     * Trả lời tin nhắn
     */
    void respondToMessage(Integer messageId, String response, Integer adminUserId);
    
    /**
     * Đếm số tin nhắn chưa đọc
     */
    int countUnreadMessages();
    
    /**
     * Tìm kiếm tin nhắn theo từ khóa
     */
    List<ContactMessage> searchMessages(String keyword);
} 