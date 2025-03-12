package com.example.demo.services;

import com.example.demo.dto.Bill.*;
import com.example.demo.entities.Bill;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface BillService {
    List<Long> findAllInStoreInvoiceId();
    List<InStoreInvoiceDto> findAllInStoreInvoice(List<Long> ids);

    Page<BillDtoInterface> findAll(Pageable pageable);
    List<BillDtoInterface> findAll();

//    public Page<BillDtoInterface> searchListBill(String maDinhDanh,
//                                                 Date ngayTaoStart,
//                                                 Date ngayTaoEnd,
//                                                 Integer trangThai,
//                                                 Integer loaiDon,
//                                                 String soDienThoai,
//                                                 String hoVaTen,
//                                                 Pageable pageable);

    Page<BillDtoInterface> searchListBill(String maDinhDanh,
                                          LocalDateTime ngayTaoStart,
                                          LocalDateTime ngayTaoEnd,
                                          String trangThai,
                                          String loaiDon,
                                          String soDienThoai,
                                          String hoVaTen,
                                          Pageable pageable);
    List<BillDtoInterface> searchListBill(String maDinhDanh,
                                          LocalDateTime ngayTaoStart,
                                          LocalDateTime ngayTaoEnd,
                                          String trangThai,
                                          String loaiDon,
                                          String soDienThoai,
                                          String hoVaTen);

    Bill updateStatus(String status, Long id);

    BillDetailDtoInterface getBillDetail(Long maHoaDon);

    Page<Bill> getBillByStatus(String status, Pageable pageable);

    List<BillDetailProduct> getBillDetailProduct(Long maHoaDon);

    void exportToExcel(HttpServletResponse response, Page<BillDtoInterface> bills, String exportUrl) throws IOException;

    String exportPdf(HttpServletResponse response,Long maHoaDon) throws DocumentException, IOException;

    String getHtmlContent(Long maHoaDon);


    Page<Bill> getBillByAccount(Pageable pageable);

    void deleteById(Long id);

//    List<BillDto> getAllValidBillToReturn();

    Page<BillDto> searchBillJson(SearchBillDto searchBillDto, Pageable pageable);

    Page<BillDto> getAllValidBillToReturn( Pageable pageable);

    void deductProductQuantitiesOnStatusChange(Long billId);

    void updateStatusChoHangVe();
}
