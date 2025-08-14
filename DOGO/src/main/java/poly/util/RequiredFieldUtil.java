package poly.util;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Utility class để quản lý các trường bắt buộc (required fields)
 * @author System
 */
public class RequiredFieldUtil {
    
    /**
     * Thêm dấu * cho label
     */
    public static void addRequiredIndicator(JLabel label) {
        if (label != null && !label.getText().contains("*")) {
            label.setText(label.getText() + " *");
            label.setForeground(new Color(51, 51, 51));
        }
    }
    
    /**
     * Thêm dấu * cho label với text tùy chỉnh
     */
    public static void addRequiredIndicator(JLabel label, String text) {
        if (label != null) {
            label.setText(text + " *");
            label.setForeground(new Color(51, 51, 51));
        }
    }
    
    /**
     * Thêm tooltip cho text field
     */
    public static void addTooltip(JTextField textField, String tooltip) {
        if (textField != null) {
            textField.setToolTipText(tooltip);
        }
    }
    
    /**
     * Thêm tooltip cho password field
     */
    public static void addTooltip(JPasswordField passwordField, String tooltip) {
        if (passwordField != null) {
            passwordField.setToolTipText(tooltip);
        }
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailPattern, email);
    }
    
    /**
     * Validate Vietnamese phone number
     */
    public static boolean isValidVietnamesePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String phonePattern = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
        return Pattern.matches(phonePattern, phone);
    }
    
    /**
     * Validate username format
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_]+$");
    }
    
    /**
     * Validate required field
     */
    public static String validateRequiredField(String value, String fieldName, int minLength) {
        if (value == null || value.trim().isEmpty()) {
            return "❌ " + fieldName + " không được để trống!";
        }
        if (value.trim().length() < minLength) {
            return "❌ " + fieldName + " phải có ít nhất " + minLength + " ký tự!";
        }
        return null;
    }
    
    /**
     * Validate required field with custom validation
     */
    public static String validateRequiredField(String value, String fieldName, int minLength, 
                                             String customValidationMessage, boolean customValidation) {
        String basicValidation = validateRequiredField(value, fieldName, minLength);
        if (basicValidation != null) {
            return basicValidation;
        }
        
        if (!customValidation) {
            return "❌ " + customValidationMessage;
        }
        
        return null;
    }
    
    /**
     * Hiển thị thông báo lỗi validation
     */
    public static void showValidationError(Component parent, String errorMessage) {
        if (errorMessage != null && !errorMessage.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, 
                "📋 Vui lòng kiểm tra và sửa các lỗi sau:\n\n" + errorMessage, 
                "Lỗi thông tin", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Tạo StringBuilder để build error message
     */
    public static StringBuilder createErrorMessageBuilder() {
        return new StringBuilder();
    }
    
    /**
     * Thêm lỗi vào error message builder
     */
    public static void addError(StringBuilder errorBuilder, String error) {
        if (errorBuilder != null && error != null) {
            errorBuilder.append(error).append("\n");
        }
    }
    
    /**
     * Kiểm tra và hiển thị lỗi từ error message builder
     */
    public static boolean hasErrorsAndShow(StringBuilder errorBuilder, Component parent) {
        if (errorBuilder != null && errorBuilder.length() > 0) {
            showValidationError(parent, errorBuilder.toString());
            return true;
        }
        return false;
    }
}
