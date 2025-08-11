# Gợi Ý Cải Tiến Màn Hình KhachJFrame

## ✅ Vấn Đề Hiện Tại
- **Màn hình trống:** Chỉ có hình ảnh phòng khách ở giữa
- **Thiếu thông tin hữu ích:** Không có thông tin nổi bật cho khách hàng
- **Thiếu tương tác:** Không có các tính năng nhanh

## 🎯 Các Gợi Ý Cải Tiến

### 1. **Dashboard Thông Tin Tổng Quan**
```
┌─────────────────────────────────────────────────────────┐
│                    FURNITURE STORE                      │
├─────────────────────────────────────────────────────────┤
│ [Menu] [Duyệt SP] [Giỏ hàng] [Thanh toán] [Đơn hàng]   │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │   Chào mừng │  │  Sản phẩm   │  │   Đơn hàng  │     │
│  │   [Tên KH]  │  │   Nổi bật   │  │   Gần đây   │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│                                                     │   │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │   Khuyến    │  │   Thống kê  │  │   Hỗ trợ    │     │
│  │   mãi HOT   │  │   Mua hàng   │  │   Nhanh     │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│                                                     │   │
│  ┌─────────────────────────────────────────────────┐   │
│  │              Sản phẩm đề xuất                   │   │
│  │  [SP1] [SP2] [SP3] [SP4] [SP5] [SP6]           │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### 2. **Các Thành Phần Chi Tiết**

#### **A. Panel Chào Mừng**
```java
// Panel chào mừng với thông tin khách hàng
JPanel welcomePanel = new JPanel();
welcomePanel.setBorder(BorderFactory.createTitledBorder("Chào mừng"));
welcomePanel.add(new JLabel("Xin chào, " + customerName));
welcomePanel.add(new JLabel("Điểm tích lũy: " + loyaltyPoints));
```

#### **B. Panel Sản Phẩm Nổi Bật**
```java
// Hiển thị 3-4 sản phẩm bán chạy nhất
JPanel featuredPanel = new JPanel();
featuredPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm nổi bật"));
// Grid layout với hình ảnh và tên sản phẩm
```

#### **C. Panel Khuyến Mãi**
```java
// Hiển thị các khuyến mãi đang diễn ra
JPanel promotionPanel = new JPanel();
promotionPanel.setBorder(BorderFactory.createTitledBorder("Khuyến mãi HOT"));
// Danh sách mã giảm giá, flash sale, etc.
```

#### **D. Panel Thống Kê**
```java
// Thống kê đơn hàng gần đây
JPanel statsPanel = new JPanel();
statsPanel.setBorder(BorderFactory.createTitledBorder("Thống kê"));
statsPanel.add(new JLabel("Đơn hàng gần đây: " + recentOrders));
statsPanel.add(new JLabel("Tổng chi tiêu: " + totalSpent));
```

#### **E. Panel Hỗ Trợ Nhanh**
```java
// Các liên kết hỗ trợ nhanh
JPanel supportPanel = new JPanel();
supportPanel.setBorder(BorderFactory.createTitledBorder("Hỗ trợ nhanh"));
supportPanel.add(new JButton("Chat với CSKH"));
supportPanel.add(new JButton("Hướng dẫn mua hàng"));
supportPanel.add(new JButton("Chính sách đổi trả"));
```

#### **F. Panel Sản Phẩm Đề Xuất**
```java
// Hiển thị sản phẩm đề xuất dựa trên lịch sử
JPanel recommendationPanel = new JPanel();
recommendationPanel.setBorder(BorderFactory.createTitledBorder("Đề xuất cho bạn"));
// Scrollable panel với sản phẩm
```

### 3. **Tính Năng Tương Tác**

#### **A. Nút Tác Vụ Nhanh**
```java
// Các nút tác vụ nhanh
JButton quickViewCart = new JButton("Xem giỏ hàng (" + cartItemCount + ")");
JButton quickCheckout = new JButton("Thanh toán nhanh");
JButton viewOrders = new JButton("Đơn hàng của tôi");
```

#### **B. Thông Báo Real-time**
```java
// Panel thông báo
JPanel notificationPanel = new JPanel();
notificationPanel.add(new JLabel("🔔 " + notificationCount + " thông báo mới"));
```

#### **C. Tìm Kiếm Nhanh**
```java
// Ô tìm kiếm sản phẩm
JTextField searchField = new JTextField("Tìm kiếm sản phẩm...");
JButton searchButton = new JButton("🔍");
```

### 4. **Layout Chi Tiết**

#### **Layout Chính:**
```java
// Sử dụng GridBagLayout hoặc MigLayout
setLayout(new GridBagLayout());
GridBagConstraints gbc = new GridBagConstraints();

// Hàng 1: Welcome + Featured + Orders
gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.3;
add(welcomePanel, gbc);

gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 0.4;
add(featuredPanel, gbc);

gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.3;
add(ordersPanel, gbc);

// Hàng 2: Promotion + Stats + Support
gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.3;
add(promotionPanel, gbc);

gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 0.4;
add(statsPanel, gbc);

gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.3;
add(supportPanel, gbc);

// Hàng 3: Recommendations (full width)
gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1.0;
add(recommendationPanel, gbc);
```

### 5. **Dữ Liệu Cần Lấy**

#### **A. Thông Tin Khách Hàng**
```java
// Lấy từ CurrentUserUtil
String customerName = CurrentUserUtil.getCurrentUsername();
int loyaltyPoints = getLoyaltyPoints(customerName);
```

#### **B. Sản Phẩm Nổi Bật**
```java
// Query từ database
String sql = "SELECT TOP 4 * FROM Products ORDER BY SalesCount DESC";
List<Product> featuredProducts = productDAO.getFeaturedProducts();
```

#### **C. Đơn Hàng Gần Đây**
```java
// Query từ database
String sql = "SELECT TOP 3 * FROM Orders WHERE UserID = ? ORDER BY OrderDate DESC";
List<Order> recentOrders = orderDAO.getRecentOrders(userId);
```

#### **D. Khuyến Mãi**
```java
// Query từ database
String sql = "SELECT * FROM Coupons WHERE IsActive = 1 AND ExpiryDate > GETDATE()";
List<Coupon> activeCoupons = couponDAO.getActiveCoupons();
```

### 6. **Màu Sắc Và Theme**

#### **A. Màu Sắc Chủ Đạo**
```java
// Màu chủ đạo
Color primaryColor = new Color(204, 204, 255); // Màu tím nhạt hiện tại
Color secondaryColor = new Color(153, 51, 0);  // Màu nâu đỏ
Color accentColor = new Color(255, 193, 7);    // Màu vàng
Color backgroundColor = new Color(248, 249, 250); // Màu xám nhạt
```

#### **B. Border Và Shadow**
```java
// Border cho các panel
panel.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createTitledBorder(title),
    BorderFactory.createEmptyBorder(10, 10, 10, 10)
));
```

### 7. **Responsive Design**

#### **A. Kích Thước Linh Hoạt**
```java
// Sử dụng weight trong GridBagLayout
gbc.weightx = 0.3; // Panel nhỏ
gbc.weightx = 0.4; // Panel vừa
gbc.weightx = 1.0; // Panel lớn
```

#### **B. Scrollable Content**
```java
// Wrap trong JScrollPane nếu cần
JScrollPane scrollPane = new JScrollPane(mainPanel);
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
```

## 🚀 Lợi Ích Của Cải Tiến

### ✅ **Cho Khách Hàng:**
- **Thông tin nhanh:** Xem ngay sản phẩm nổi bật, khuyến mãi
- **Tương tác dễ dàng:** Nút tác vụ nhanh, tìm kiếm
- **Trải nghiệm tốt:** Giao diện đẹp, thông tin hữu ích
- **Cá nhân hóa:** Sản phẩm đề xuất theo sở thích

### ✅ **Cho Hệ Thống:**
- **Tăng doanh số:** Khuyến mãi nổi bật, sản phẩm hot
- **Giảm tải CSKH:** Hỗ trợ nhanh, hướng dẫn tự động
- **Dữ liệu tốt:** Thống kê mua hàng, hành vi khách hàng
- **UX/UI hiện đại:** Giao diện dashboard chuyên nghiệp

## 📋 Bước Triển Khai

### **Bước 1: Thiết Kế Layout**
1. **Tạo các panel** theo gợi ý
2. **Thiết kế layout** với GridBagLayout
3. **Test responsive** với các kích thước màn hình

### **Bước 2: Lấy Dữ Liệu**
1. **Tạo các method** trong DAO để lấy dữ liệu
2. **Test query** database
3. **Xử lý exception** và fallback

### **Bước 3: Tích Hợp**
1. **Thay thế hình ảnh** hiện tại bằng dashboard
2. **Thêm event listener** cho các nút
3. **Test toàn bộ** tính năng

### **Bước 4: Tối Ưu**
1. **Caching dữ liệu** để tăng tốc
2. **Lazy loading** cho sản phẩm đề xuất
3. **Error handling** đầy đủ

## 💡 Gợi Ý Thêm

### **A. Animation Và Hiệu Ứng**
```java
// Hiệu ứng hover cho sản phẩm
productPanel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseEntered(MouseEvent e) {
        productPanel.setBorder(BorderFactory.createLineBorder(accentColor, 2));
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        productPanel.setBorder(BorderFactory.createEmptyBorder());
    }
});
```

### **B. Auto-refresh**
```java
// Tự động cập nhật dữ liệu mỗi 5 phút
Timer refreshTimer = new Timer(300000, e -> refreshDashboardData());
refreshTimer.start();
```

### **C. Dark Mode**
```java
// Tùy chọn chế độ tối
if (isDarkMode) {
    setBackground(darkBackgroundColor);
    setForeground(darkForegroundColor);
}
```

**Bạn muốn tôi triển khai phần nào trước? Tôi có thể bắt đầu với panel chào mừng và sản phẩm nổi bật!**
