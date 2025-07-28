/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package poly.ui;

import poly.controller.ShoppingCartController;
import poly.dao.ShoppingCartDAO;
import poly.dao.impl.ShoppingCartDAOImpl;
import poly.dao.impl.ProductDAOImpl;
import poly.entity.ShoppingCart;
import poly.entity.CartItem;
import poly.entity.Product;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import poly.util.CurrentUserUtil;

/**
 *
 * @author Nghia
 */
public class GioHangJDialog extends javax.swing.JDialog implements ShoppingCartController {
    private ShoppingCartDAO shoppingCartDAO = new ShoppingCartDAOImpl();
    private ProductDAOImpl productDAO = new ProductDAOImpl();
    private ShoppingCart currentCart;
    private List<CartItem> cartItems;
    private DefaultTableModel tableModel;

    @Override
    public void setShoppingCartDAO(ShoppingCartDAO dao) {
        this.shoppingCartDAO = dao;
    }

    @Override
    public List<CartItem> getCartItems(int cartId) {
        return shoppingCartDAO.findCartItemsByCartId(cartId);
    }

    @Override
    public CartItem getCartItem(int cartId, String productId) {
        return shoppingCartDAO.findCartItemByCartIdAndProductId(cartId, productId);
    }

    @Override
    public void addCartItem(CartItem item) {
        shoppingCartDAO.insertCartItem(item);
    }

    @Override
    public void updateCartItem(CartItem item) {
        shoppingCartDAO.updateCartItem(item);
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        shoppingCartDAO.deleteCartItem(cartItemId);
    }

    @Override
    public void deleteCartItemByProduct(int cartId, String productId) {
        shoppingCartDAO.deleteCartItemByCartIdAndProductId(cartId, productId);
    }

    @Override
    public void addProductToCart(int userId, String productId, int quantity) {
        shoppingCartDAO.addProductToCart(userId, productId, quantity);
    }

    @Override
    public void updateProductQuantity(int userId, String productId, int quantity) {
        shoppingCartDAO.updateProductQuantity(userId, productId, quantity);
    }

    @Override
    public void removeProductFromCart(int userId, String productId) {
        shoppingCartDAO.removeProductFromCart(userId, productId);
    }

    @Override
    public void clearCart(int userId) {
        shoppingCartDAO.clearCart(userId);
    }

    // CRUD ShoppingCart (dùng CrudController<ShoppingCart> method)
    @Override
    public void open() {
        loadCartData();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void setForm(ShoppingCart entity) {
        this.currentCart = entity;
    }

    @Override
    public ShoppingCart getForm() {
        return this.currentCart;
    }

    @Override
    public void fillToTable() {
        loadCartData();
    }

    @Override
    public void edit() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            // Lấy productId từ CartItem thay vì từ bảng
            CartItem selectedItem = cartItems.get(selectedRow);
            String productId = selectedItem.getProductId();
            
            // Kiểm tra số lượng tồn kho hiện tại
            Product currentProduct = productDAO.selectById(productId);
            if (currentProduct == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin sản phẩm!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int currentStock = currentProduct.getQuantity() != null ? currentProduct.getQuantity() : 0;
            int currentQuantity = selectedItem.getQuantity();
            
            String quantityStr = JOptionPane.showInputDialog(this, 
                "Nhập số lượng mới (Trong kho hiện tại: " + currentStock + "):", 
                "Cập nhật số lượng", JOptionPane.QUESTION_MESSAGE);
            
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                try {
                    int newQuantity = Integer.parseInt(quantityStr.trim());
                    if (newQuantity <= 0) {
                        JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Kiểm tra số lượng tồn kho
                    if (newQuantity > currentStock) {
                        JOptionPane.showMessageDialog(this, 
                            "Số lượng vượt quá trong kho sẵn có! (Trong kho: " + currentStock + ")", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Cập nhật trực tiếp CartItem
                    selectedItem.setQuantity(newQuantity);
                    updateCartItem(selectedItem);
                    loadCartData(); // Reload dữ liệu
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void create() {
        // Không cần tạo mới giỏ hàng
    }

    @Override
    public void update() {
        // Không cần update giỏ hàng
    }

    @Override
    public void delete() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            CartItem selectedItem = cartItems.get(selectedRow);
            String productId = selectedItem.getProductId();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (currentCart != null) {
                    deleteCartItemByProduct(currentCart.getCartId(), productId);
                    loadCartData(); // Reload dữ liệu
                    JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm khỏi giỏ hàng!", 
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void clear() {
        Integer userId = CurrentUserUtil.getCurrentUserId();
        if (userId == null) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng nhập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa toàn bộ giỏ hàng?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            clearCart(userId);
            loadCartData();
            JOptionPane.showMessageDialog(this, "Đã xóa toàn bộ giỏ hàng!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void setEditable(boolean editable) {
        // Không cần implement cho giỏ hàng
    }

    @Override
    public void checkAll() {}
    @Override
    public void uncheckAll() {}
    @Override
    public void deleteCheckedItems() {}
    @Override
    public void moveFirst() {}
    @Override
    public void movePrevious() {}
    @Override
    public void moveNext() {}
    @Override
    public void moveLast() {}
    @Override
    public void moveTo(int rowIndex) {}

    // Phương thức hỗ trợ
    private void loadCartData() {
        // Lấy giỏ hàng của user đang đăng nhập hiện tại
        Integer userId = CurrentUserUtil.getCurrentUserId();
        if (userId == null) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng nhập!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            // Hiển thị bảng trống
            tableModel = new DefaultTableModel();
            tableModel.addColumn("Sản Phẩm");
            tableModel.addColumn("Số Lượng");
            tableModel.addColumn("Giá");
            tableModel.addColumn("Tổng Cộng");
            jTable1.setModel(tableModel);
            return;
        }
        currentCart = shoppingCartDAO.findByUserId(userId);
        if (currentCart != null) {
            cartItems = getCartItems(currentCart.getCartId());
            displayCartItems();
        } else {
            // Hiển thị bảng trống nếu chưa có giỏ hàng
            tableModel = new DefaultTableModel();
            tableModel.addColumn("Sản Phẩm");
            tableModel.addColumn("Số Lượng");
            tableModel.addColumn("Giá");
            tableModel.addColumn("Tổng Cộng");
            jTable1.setModel(tableModel);
        }
    }

    private void displayCartItems() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Sản Phẩm");
        tableModel.addColumn("Số Lượng");
        tableModel.addColumn("Giá");
        tableModel.addColumn("Tổng Cộng");
        
        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                Product product = productDAO.selectById(item.getProductId());
                if (product != null) {
                    BigDecimal unitPrice = product.getUnitPrice();
                    BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(item.getQuantity()));
                    
                    tableModel.addRow(new Object[]{
                        product.getProductName(),
                        item.getQuantity(),
                        unitPrice.toString() + " ₫",
                        totalPrice.toString() + " ₫"
                    });
                }
            }
        }
        
        jTable1.setModel(tableModel);
    }

    /**
     * Creates new form GioHangJDialog
     */
    public GioHangJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // Nút xóa khỏi giỏ
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete();
            }
        });

        // Nút thanh toán (chưa implement)
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblCartTitle = new javax.swing.JLabel();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Sản Phẩm", "Số Lượng", "Giá", "Tổng Cộng"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        lblCartTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCartTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCartTitle.setText("Giỏ hàng của bạn");

        btnCapNhat.setText("Cập nhật số lượng");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setText("Xoá khỏi giỏ");

        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblCartTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(btnCapNhat)
                .addGap(84, 84, 84)
                .addComponent(btnXoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThanhToan)
                .addGap(67, 67, 67))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblCartTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapNhat)
                    .addComponent(btnXoa)
                    .addComponent(btnThanhToan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.open();
    }//GEN-LAST:event_formWindowOpened

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        edit();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // Kiểm tra đăng nhập
        Integer userId = CurrentUserUtil.getCurrentUserId();
        if (userId == null) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng nhập! Vui lòng đăng nhập để thanh toán.", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Mở màn hình Đặt Hàng
        try {
            DatHangJDialog datHangDialog = new DatHangJDialog((java.awt.Frame) this.getOwner(), true);
            datHangDialog.setVisible(true);
            
            // Sau khi đóng màn hình đặt hàng, reload lại giỏ hàng
            loadCartData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi mở màn hình đặt hàng: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GioHangJDialog dialog = new GioHangJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnXoa;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCartTitle;
    // End of variables declaration//GEN-END:variables
}
