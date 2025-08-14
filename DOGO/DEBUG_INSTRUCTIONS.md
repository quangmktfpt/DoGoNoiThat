# Hướng Dẫn Debug Địa Chỉ Đơn Hàng

## Vấn Đề Hiện Tại

Đơn hàng 32 vẫn hiển thị "Địa chỉ mặc định" thay vì địa chỉ thực tế "Quang tets 14 lần ..."

## Các Bước Debug

### 1. **Chạy SQL Debug**

Chạy file `DEBUG_ORDER_32.sql` trong SQL Server Management Studio để kiểm tra dữ liệu:

```sql
-- Kiểm tra đơn hàng 32
SELECT OrderID, UserID, DeliveryAddressID FROM Orders WHERE OrderID = 32;

-- Kiểm tra địa chỉ có OrderID = 32
SELECT AddressID, OrderID, AddressLine1, CustomerName FROM Addresses WHERE OrderID = 32;
```

### 2. **Test Trong Ứng Dụng**

1. Mở màn hình theo dõi đơn hàng
2. Tìm đơn hàng có OrderID = 32
3. Nhấn "Xem Chi Tiết"
4. **Quan sát console output**

### 3. **Console Output Mong Đợi**

Khi nhấn "Xem Chi Tiết", console sẽ hiển thị:

```
🔍 DEBUG - Tìm địa chỉ theo OrderID: 32
🔍 DEBUG - AddressDAO.selectByOrderId(32)
🔍 DEBUG - SQL: SELECT * FROM Addresses WHERE OrderID = ?
🔍 DEBUG - Kết quả: 1 records found
🔍 DEBUG - AddressID: 34
🔍 DEBUG - AddressLine1: Quang tets 14 lần ...
🔍 DEBUG - OrderID: 32
🔍 DEBUG - Kết quả tìm theo OrderID: FOUND
🔍 DEBUG - AddressID: 34
🔍 DEBUG - AddressLine1: Quang tets 14 lần ...
🔍 DEBUG - CustomerName: Quang tets 14 lần ...
```

### 4. **Các Trường Hợp Có Thể Xảy Ra**

#### **Trường Hợp A: OrderID = NULL**
```
🔍 DEBUG - OrderID is NULL
```
**Nguyên nhân**: `currentOrder.getOrderId()` trả về null
**Giải pháp**: Kiểm tra cách load đơn hàng trong `TDDonHangJDialog_nghia`

#### **Trường Hợp B: Không tìm thấy địa chỉ theo OrderID**
```
🔍 DEBUG - Tìm địa chỉ theo OrderID: 32
🔍 DEBUG - Kết quả: 0 records found
🔍 DEBUG - Kết quả tìm theo OrderID: NOT FOUND
```
**Nguyên nhân**: Không có địa chỉ nào có OrderID = 32
**Giải pháp**: Kiểm tra database

#### **Trường Hợp C: Tìm thấy địa chỉ nhưng hiển thị sai**
```
🔍 DEBUG - Kết quả tìm theo OrderID: FOUND
🔍 DEBUG - AddressLine1: Quang tets 14 lần ...
```
Nhưng vẫn hiển thị "Địa chỉ mặc định"
**Nguyên nhân**: Logic hiển thị có vấn đề
**Giải pháp**: Kiểm tra phần code hiển thị

### 5. **Kiểm Tra Logic Load Đơn Hàng**

Trong `TDDonHangJDialog_nghia`, kiểm tra phương thức `edit()`:

```java
private void edit() {
    try {
        Integer id = (Integer) jTable1.getValueAt(this.index, 0);
        currentOrder = orderDAO.selectById(id);
        // Kiểm tra xem currentOrder có OrderID không
        System.out.println("🔍 DEBUG - CurrentOrder OrderID: " + currentOrder.getOrderId());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### 6. **Kiểm Tra SQL Query**

Thêm debug vào `selectBySql()` trong `AddressDAOImpl`:

```java
@Override
public List<Address> selectBySql(String sql, Object... args) {
    List<Address> list = new ArrayList<>();
    try {
        System.out.println("🔍 DEBUG - Executing SQL: " + sql);
        System.out.println("🔍 DEBUG - Parameters: " + java.util.Arrays.toString(args));
        
        ResultSet rs = XJdbc.executeQuery(sql, args);
        while (rs.next()) {
            Address address = new Address();
            address.setAddressId(rs.getInt("AddressID"));
            address.setUserId(rs.getInt("UserID"));
            address.setAddressLine1(rs.getString("AddressLine1"));
            address.setCity(rs.getString("City"));
            address.setCountry(rs.getString("Country"));
            address.setPhone(rs.getString("Phone"));
            address.setCustomerName(rs.getString("CustomerName"));
            address.setIsDefault(rs.getBoolean("IsDefault"));
            address.setCouponId(rs.getString("CouponID"));
            address.setOrderId(rs.getObject("OrderID") != null ? rs.getInt("OrderID") : null);
            address.setCreatedDate(rs.getTimestamp("CreatedDate").toLocalDateTime());
            
            System.out.println("🔍 DEBUG - Mapped Address: " + address.getAddressLine1());
            list.add(address);
        }
        rs.close();
    } catch (Exception e) {
        System.err.println("🔍 DEBUG - Error in selectBySql: " + e.getMessage());
        e.printStackTrace();
    }
    return list;
}
```

### 7. **Kiểm Tra Entity Mapping**

Đảm bảo `OrderID` được map đúng trong `selectBySql()`:

```java
address.setOrderId(rs.getObject("OrderID") != null ? rs.getInt("OrderID") : null);
```

### 8. **Test Trực Tiếp Database**

Chạy SQL trực tiếp để kiểm tra:

```sql
-- Test query giống như trong code
SELECT * FROM Addresses WHERE OrderID = 32;

-- Kiểm tra cột OrderID có tồn tại không
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Addresses' AND COLUMN_NAME = 'OrderID';
```

## Kết Quả Mong Đợi

Sau khi debug, chúng ta sẽ biết:

1. **OrderID có được load đúng không**
2. **SQL query có trả về kết quả không**
3. **Entity có được map đúng không**
4. **Logic hiển thị có hoạt động đúng không**

## Bước Tiếp Theo

1. Chạy ứng dụng và test đơn hàng 32
2. Quan sát console output
3. So sánh với kết quả mong đợi
4. Xác định nguyên nhân cụ thể
5. Sửa lỗi tương ứng

Hãy chạy test và cho tôi biết console output để tôi có thể giúp xác định nguyên nhân chính xác!
