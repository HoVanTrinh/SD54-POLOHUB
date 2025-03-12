package com.example.demo.dto.Bill;

import com.example.demo.entities.enumClass.BillStatus;
import com.example.demo.entities.enumClass.InvoiceType;

import java.time.LocalDateTime;

public interface BillDtoInterface {
    Long getMaHoaDon();
    String getMaDinhDanh();
    String getHoVaTen();
    String getSoDienThoai();
    LocalDateTime getNgayTao();
    Double getTongTien();
    BillStatus getTrangThai();
    InvoiceType getLoaiDon();
    String getHinhThucThanhToan();
    String getMaGiaoDich();
    String getMaDoiTra();
}
