# Thêm Cột OrderID Vào Bảng Addresses

## Tổng Quan

Để giải quyết vấn đề hiển thị địa chỉ "mặc định" thay vì địa chỉ thực tế từ form đặt hàng, chúng ta đã thêm cột `OrderID` vào bảng `Addresses` để tạo liên kết trực tiếp giữa đơn hàng và địa chỉ giao hàng.

## Vấn Đề Ban Đầu

- Bảng `Addresses` không có cột `OrderID`
- Chỉ có `DeliveryAddressID` trong bảng `Orders` 
- Không thể lấy địa chỉ thực tế từ form đặt hàng
- Hiển thị "Địa chỉ mặc định" thay vì địa chỉ thực tế

## Giải Pháp

### 1. **Thêm Cột OrderID Vào Database**

Chạy file `ADD_ORDERID_TO_ADDRESSES.sql`:

```sql
-- 1. Thêm cột OrderID vào bảng Addresses
ALTER TABLE Addresses 
ADD OrderID INT NULL;

-- 2. Thêm khóa ngoại để liên kết với bảng Orders
ALTER TABLE Addresses 
ADD CONSTRAINT FK_Addresses_Orders 
FOREIGN KEY (OrderID) REFERENCES Orders(OrderID);

-- 3. Tạo index để tối ưu hiệu suất truy vấn
CREATE INDEX IX_Addresses_OrderID ON Addresses(OrderID);
```

### 2. **Cập Nhật Entity Address**

Thêm trường `orderId` vào class `Address`:

```java
private Integer orderId;

public Integer getOrderId() {
    return orderId;
}

public void setOrderId(Integer orderId) {
    this.orderId = orderId;
}
```

### 3. **Cập Nhật AddressDAO**

Thêm phương thức mới:

```java
// Interface
Address selectByOrderId(Integer orderId);

// Implementation
private final String SELECT_BY_ORDER_SQL = "SELECT * FROM Addresses WHERE OrderID = ?";

@Override
public Address selectByOrderId(Integer orderId) {
    List<Address> list = selectBySql(SELECT_BY_ORDER_SQL, orderId);
    return list.isEmpty() ? null : list.get(0);
}
```

### 4. **Cập Nhật DatHangJDialog**

Sửa phương thức xử lý nút "Xác nhận" để trực tiếp insert vào bảng `Addresses`:

```java
// Sau khi tạo đơn hàng thành công
orderRequestDAO.insert(orderToSubmit);
int orderId = orderToSubmit.getOrderId();

// Insert địa chỉ giao hàng vào bảng Addresses với OrderID
try {
    AddressDAO addressDAO = new AddressDAOImpl();
    Address deliveryAddress = new Address();
    deliveryAddress.setUserId(currentUser.getUserId());
    deliveryAddress.setAddressLine1(currentOrder.getAddress());
    deliveryAddress.setCity(currentOrder.getCity());
    deliveryAddress.setCountry(currentOrder.getCountry());
    deliveryAddress.setPhone(currentOrder.getPhone());
    deliveryAddress.setCustomerName(currentOrder.getCustomerName());
    deliveryAddress.setIsDefault(false);
    deliveryAddress.setCouponId(currentOrder.getCouponId());
    deliveryAddress.setOrderId(orderId); // Liên kết với đơn hàng vừa tạo
    deliveryAddress.setCreatedDate(java.time.LocalDateTime.now());
    
    // Insert địa chỉ
    addressDAO.insert(deliveryAddress);
    System.out.println("✓ Đã lưu địa chỉ giao hàng với OrderID: " + orderId);
} catch (Exception e) {
    System.err.println("⚠️ Lỗi khi lưu địa chỉ giao hàng: " + e.getMessage());
}
```

### 5. **Cập Nhật OrderRequestDAOImpl (Tùy chọn)**

Nếu muốn giữ logic cũ trong `OrderRequestDAOImpl`, có thể cập nhật để hỗ trợ `OrderID`:

```java
// Trong phương thức createDeliveryAddress()
private Integer createDeliveryAddress(OrderRequest orderRequest) {
    try {
        Address newAddress = new Address();
        // ... set các trường khác ...
        newAddress.setOrderId(null); // Sẽ được cập nhật sau
        
        addressDAO.insert(newAddress);
        
        List<Address> addresses = addressDAO.selectByUserId(orderRequest.getUserId());
        if (!addresses.isEmpty()) {
            return addresses.get(0).getAddressId();
        }
    } catch (Exception e) {
        // ... xử lý lỗi ...
    }
    return null;
}
```

### 6. **Cập Nhật Logic Hiển Thị**

Sửa phương thức `viewOrderDetails()` để ưu tiên lấy theo `OrderID`:

```java
// Ưu tiên lấy từ bảng Addresses theo OrderID (thông tin thực tế từ form đặt hàng)
poly.entity.Address address = null;

// Thử lấy theo OrderID trước (cách mới)
if (currentOrder.getOrderId() != null) {
    address = addressDAO.selectByOrderId(currentOrder.getOrderId());
}

// Nếu không tìm thấy theo OrderID, thử theo DeliveryAddressID (cách cũ)
if (address == null && currentOrder.getDeliveryAddressId() != null) {
    address = addressDAO.selectById(currentOrder.getDeliveryAddressId());
}
```

## Luồng Dữ Liệu Mới

### **Khi Đặt Hàng:**
1. Tạo đơn hàng → lưu vào `Orders` với `DeliveryAddressID`
2. Lấy `OrderID` vừa tạo
3. Tạo địa chỉ mới từ form đặt hàng → lưu vào `Addresses` với `OrderID`

### **Khi Xem Chi Tiết:**
1. Lấy theo `OrderID` từ bảng `Addresses` (ưu tiên cao nhất)
2. Fallback: lấy theo `DeliveryAddressID` (cách cũ)
3. Fallback: lấy từ `OrderRequest` (nếu có)
4. Fallback: lấy từ `Users` (cuối cùng)

## Lợi Ích

### ✅ **Giải Quyết Vấn Đề:**
- Hiển thị địa chỉ thực tế từ form đặt hàng
- Không còn hiển thị "Địa chỉ mặc định"
- Liên kết trực tiếp giữa đơn hàng và địa chỉ

### ✅ **Cải Thiện Hiệu Suất:**
- Index trên cột `OrderID`
- Truy vấn nhanh hơn
- Giảm JOIN phức tạp

### ✅ **Dễ Bảo Trì:**
- Logic rõ ràng, dễ hiểu
- Fallback gracefully
- Debug thông tin chi tiết

## Kiểm Tra Kết Quả

### **Chạy SQL để kiểm tra:**
```sql
-- Kiểm tra cấu trúc bảng
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses'
ORDER BY ORDINAL_POSITION;

-- Kiểm tra khóa ngoại
SELECT fk.name as FK_Name, 
       OBJECT_NAME(fk.parent_object_id) as Table_Name,
       COL_NAME(fkc.parent_object_id, fkc.parent_column_id) as Column_Name
FROM sys.foreign_keys fk
INNER JOIN sys.foreign_key_columns fkc ON fk.object_id = fkc.constraint_object_id
WHERE OBJECT_NAME(fk.parent_object_id) = 'Addresses';

-- Kiểm tra dữ liệu
SELECT a.AddressID, a.OrderID, a.AddressLine1, a.CustomerName, o.OrderID as Order_OrderID
FROM Addresses a
LEFT JOIN Orders o ON a.OrderID = o.OrderID
WHERE a.OrderID IS NOT NULL;
```

## Rollback (Nếu Cần)

```sql
-- Xóa khóa ngoại
ALTER TABLE Addresses 
DROP CONSTRAINT FK_Addresses_Orders;

-- Xóa index
DROP INDEX IX_Addresses_OrderID ON Addresses;

-- Xóa cột OrderID
ALTER TABLE Addresses 
DROP COLUMN OrderID;
```

## Kết Luận

Với việc thêm cột `OrderID` vào bảng `Addresses` và cập nhật logic trong `DatHangJDialog`, chúng ta đã:
- ✅ Tạo liên kết trực tiếp giữa đơn hàng và địa chỉ
- ✅ Insert địa chỉ thực tế từ form đặt hàng vào database
- ✅ Hiển thị địa chỉ thực tế khi xem chi tiết đơn hàng
- ✅ Cải thiện hiệu suất truy vấn
- ✅ Duy trì tính tương thích ngược với logic cũ

### **Luồng Hoàn Chỉnh:**
1. **Đặt hàng**: Form → `DatHangJDialog` → Insert vào `Addresses` với `OrderID`
2. **Xem chi tiết**: `TDDonHangJDialog_nghia` → Lấy từ `Addresses` theo `OrderID` → Hiển thị địa chỉ thực tế

Bây giờ khi đặt hàng và xem chi tiết, hệ thống sẽ hiển thị địa chỉ thực tế mà khách hàng đã nhập! 🎉
