package poly.util;

public class CurrentUserUtil {
    private static String currentUsername;
    // Nếu muốn lưu User object, có thể thêm static User currentUser;
    private static Integer currentUserId;

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUserId(Integer userId) { 
        currentUserId = userId; 
    }
    
    public static Integer getCurrentUserId() { 
        return currentUserId; 
    }

    public static int getCurrentUserID() {
        return currentUserId != null ? currentUserId : -1;
    }
} 