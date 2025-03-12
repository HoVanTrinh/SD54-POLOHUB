package com.example.demo.dto.Bill;

import com.example.demo.entities.enumClass.BillStatus;
import com.example.demo.entities.enumClass.InvoiceType;

import java.time.LocalDateTime;
import java.util.Date;

public interface BillDetailDtoInterface {
    String getMaDonHang();

    String getMaDinhDanh();
    String getDiaChi();

    Double getTongTien();

    Double getTienKhuyenMai();

    String getTenKhachHang();

    String getSoDienThoai();

    String getEmail();

    BillStatus getTrangThaiDonHang();

    String getPhuongThucThanhToan();

    String getMaGiaoDich();

    InvoiceType getLoaiHoaDon();

    String getVoucherName();

    LocalDateTime getCreatedDate();

    Integer getDiscountType();
    Double getDiscountAmount();
    Double getDiscountPercent();
    Date getDiscountEndDate();
    Double getDiscountMaximumAmount();
    Integer getDiscountUsage();
    Double getDiscountMinimumAmountInCart();
}
