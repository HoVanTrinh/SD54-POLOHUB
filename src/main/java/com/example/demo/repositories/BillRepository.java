package com.example.demo.repositories;

import com.example.demo.dto.Bill.*;
import com.example.demo.dto.Statistic.OrderStatistic;
import com.example.demo.entities.Bill;
import com.example.demo.entities.enumClass.BillStatus;
import com.example.demo.entities.enumClass.InvoiceType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

@Query(value = "SELECT DISTINCT b.id AS maHoaDon,b.code AS maDinhDanh, a.name AS hoVaTen, a.phoneNumber " +
        "AS soDienThoai,b.createDate AS ngayTao, b.amount AS tongTien, b.status AS trangThai, b.invoiceType " +
        "AS loaiDon, pm.name AS hinhThucThanhToan " +
        "FROM Bill b " +
        "JOIN Payment pmt on b.id = pmt.bill.id " +
        "LEFT JOIN Customer a ON b.customer.id = a.id " +
        "LEFT JOIN BillDetail bd ON b.id = bd.bill.id " +
        "LEFT JOIN PaymentMethod pm ON b.paymentMethod.id = pm.id  WHERE b.status = 'CHO_XAC_NHAN'")
Page<BillDtoInterface> listBill(Pageable pageable);


    @Query(value = "SELECT DISTINCT b.id AS maHoaDon, b.code AS maDinhDanh, a.name AS hoVaTen, a.phoneNumber AS soDienThoai, " +
            "b.createDate AS ngayTao, b.amount AS tongTien, b.status AS trangThai, b.invoiceType AS loaiDon, " +
            "pm.name AS hinhThucThanhToan " +
            "FROM Bill b " +
            "LEFT JOIN Customer a ON b.customer.id = a.id " +
            "LEFT JOIN BillDetail bd ON b.id = bd.bill.id " +
            "LEFT JOIN PaymentMethod pm ON b.paymentMethod.id = pm.id " +
            "WHERE (:maDinhDanh IS NULL OR b.code LIKE CONCAT('%', :maDinhDanh, '%')) " +
            "AND (:ngayTaoStart IS NULL OR :ngayTaoEnd IS NULL OR (b.createDate BETWEEN :ngayTaoStart AND :ngayTaoEnd)) " +
            "AND (:trangThai IS NULL OR b.status = :trangThai) " +
            "AND (:loaiDon IS NULL OR b.invoiceType = :loaiDon) " +
            "AND (:soDienThoai IS NULL OR a.phoneNumber IS NULL OR a.phoneNumber LIKE CONCAT('%', :soDienThoai, '%')) " +
            "AND (:hoVaTen IS NULL OR a.name IS NULL OR a.name LIKE CONCAT('%', :hoVaTen, '%'))")
    Page<BillDtoInterface> listSearchBill(
            @Param("maDinhDanh") String maDinhDanh,
            @Param("ngayTaoStart") LocalDateTime ngayTaoStart,
            @Param("ngayTaoEnd") LocalDateTime ngayTaoEnd,
            @Param("trangThai") BillStatus trangThai,
            @Param("loaiDon") InvoiceType loaiDon,
            @Param("soDienThoai") String soDienThoai,
            @Param("hoVaTen") String hoVaTen,
            Pageable pageable);

    @Modifying
    @Query(value = "UPDATE bill SET status=:status WHERE id=:id", nativeQuery = true)
    @Transactional
    int updateStatus(@Param("status") String status,@Param("id") Long id);


    @Query(value = "select distinct b.id as maDonHang,b.code as maDinhDanh,b.billing_address as diaChi," +
            " b.amount as tongTien,b.promotion_price as tienKhuyenMai,a.name as tenKhachHang," +
            "a.phone_number as soDienThoai,a.email as email, b.status as trangThaiDonHang, pmt.order_id as maGiaoDich, " +
            "pm.name as phuongThucThanhToan,b.invoice_type as loaiHoaDon, dc.code as voucherName, dc.type as discountType, dc.discount_amount as discountAmount, dc.percentage as discountPercent, dc.end_date as discountEndDate, dc.maximum_usage as discountUsage, dc.maximum_amount as discountMaximumAmount, dc.minimum_amount_in_cart as discountMinimumAmountInCart, b.create_date as createdDate " +
            "from bill b full join customer a on b.customer_id=a.id full join discount_code dc on b.discount_code_id=dc.id" +
            " full join bill_detail bd on b.id=bd.bill_id join payment pmt on b.id = pmt.bill_id left join payment_method pm on b.payment_method_id=pm.id where b.id=:maHoaDon",nativeQuery = true)
    BillDetailDtoInterface getBillDetail(@Param("maHoaDon") Long maHoaDon);

    @Query(value = "select pd.id, bd.id as billDetailId, p.id as productId, p.name as tenSanPham,c.name as tenMau, s.name as kichCo, bd.moment_price as giaTien,bd.quantity as soLuong, bd.moment_price*bd.quantity as tongTien, bd.is_promotional_product as isPromotionalProduct,  (\n" +
            "           SELECT top(1) link\n" +
            "           FROM image\n" +
            "           WHERE p.id = image.product_id\n" +

            "       ) AS imageUrl, " +
            " b.promotion_price*(bd.moment_price/b.amount) as promotionPerProduct " +
            "from bill b join bill_detail bd on b.id=bd.bill_id join" +
            " product_detail pd on bd.product_detail_id =pd.id join" +
            " product p on pd.product_id=p.id join color c on pd.color_id=c.id join size s on pd.size_id = s.id " +
            "where b.id=:maHoaDon",nativeQuery = true)
    List<BillDetailProduct> getBillDetailProduct(@Param("maHoaDon") Long maHoaDon);

    Bill findTopByOrderByIdDesc();

    Page<Bill> findAllByStatusAndCustomer_Account_Id(BillStatus status, Long id, Pageable pageable);

    Page<Bill> findByCustomer_Account_Id(Long id, Pageable pageable);

    @Query(value = "select * from bill b where DATEDIFF(DAY, b.create_date, GETDATE()) <= 7 and b.status='HOAN_THANH' and b.invoice_type='OFFLINE'", nativeQuery = true)    Page<Bill> findValidBillToReturn(Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(bill.amount), 0) +COALESCE(SUM(-bill.promotion_price), 0)  AS total\n" +
            "FROM bill\n" +
            "         LEFT JOIN bill_return billReturn ON bill.id = billReturn.bill_id\n" +
            "WHERE bill.status = 'HOAN_THANH' ;\n", nativeQuery = true)
    Double calculateTotalRevenue();

    @Query(value = "SELECT " +
            "COALESCE(SUM(b.amount), 0) + COALESCE(SUM(-b.promotion_price), 0) AS total " +
            "FROM bill b " +
            "WHERE (b.status = 'HOAN_THANH') " +
            "AND (b.create_date BETWEEN CONVERT(DATETIME, :startDate, 120) AND CONVERT(DATETIME, :endDate, 120))", nativeQuery = true)
    Double calculateTotalRevenueFromDate(String startDate, String endDate);


    @Query(value = "SELECT CONVERT(DATE, b.create_date) AS date, " +
            "COALESCE(SUM(b.amount), 0) + COALESCE(SUM(-b.promotion_price), 0) AS revenue " +
            "FROM bill b " +
            "WHERE (YEAR(b.create_date) = :year AND MONTH(b.create_date) = :month AND (b.status = 'HOAN_THANH' OR b.status = 'TRA_HANG')) " +
            "GROUP BY CONVERT(DATE, b.create_date) " +
            "ORDER BY CONVERT(DATE, b.create_date);", nativeQuery = true)
    List<Object[]> statisticRevenueDayInMonth(String month, String year);

    @Query(value = "SELECT MONTH(b.create_date) AS month, " +
            "COALESCE(SUM(b.amount), 0) + COALESCE(SUM(-b.promotion_price), 0) AS revenue " +
            "FROM bill b " +
            "WHERE YEAR(b.create_date) = :year " +
            "AND b.status NOT IN ('HUY') " +  // Trừ các trạng thái 'HUY'
            "GROUP BY YEAR(b.create_date), MONTH(b.create_date) " +
            "ORDER BY MONTH(b.create_date)", nativeQuery = true)
    List<Object[]> statisticRevenueMonthInYear(String year);



    @Query("select b from Bill b where b.status = 'HOAN_THANH' AND b.createDate between :fromDate and :toDate")
    List<Bill> findAllCompletedByDate(@Param("fromDate") LocalDateTime fromDate,@Param("toDate") LocalDateTime toDate);

    @Query("select count(b) from Bill b where b.status='CHO_XAC_NHAN'")
    int getTotalBillStatusWaiting();
    @Query("select count(b.status) from Bill b where b.status != 'HUY'AND b.invoiceType = 'ONLINE'")
    int getTotalBillStatusWaiting2();

    @Query(value = "SELECT \n" +
            "FORMAT(b.create_date, 'MM-yyyy') AS date,\n" +
            "COALESCE(SUM(b.amount), 0) + COALESCE(SUM(-b.promotion_price), 0) AS revenue\n" +
            "FROM bill b\n" +
            "WHERE (b.status = 'HOAN_THANH') AND (b.create_date BETWEEN :fromDate AND :toDate) \n" +
            "GROUP BY \n" +
            "FORMAT(b.create_date, 'MM-yyyy')\n" +
            "ORDER BY \n" +
            "FORMAT(b.create_date, 'MM-yyyy');", nativeQuery = true)
    List<Object[]> statisticRevenueFormMonth(String fromDate, String toDate);

    @Query(value = "SELECT CONVERT(varchar, b.create_date, 23) AS date, " +
            "COALESCE(SUM(b.amount), 0) + COALESCE(SUM(-b.promotion_price), 0) AS revenue " +
            "FROM bill b " +
            "WHERE (b.status = 'HOAN_THANH') AND " +
            "b.create_date BETWEEN CONVERT(DATETIME, :fromDate, 120) AND CONVERT(DATETIME, :toDate, 120) " +
            "GROUP BY CONVERT(varchar, b.create_date, 23) " +
            "ORDER BY CONVERT(varchar, b.create_date, 23)", nativeQuery = true)
    List<Object[]> statisticRevenueDaily(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

    @Query(value = "select status, count(b.status) as quantity, sum(b.amount) as revenue from bill b group by b.status", nativeQuery = true)
    List<OrderStatistic> statisticOrder();

//    @Query(value = "select b.code as billCode, b.id as billId, pm.order_id as orderId, c.name as customerName, b.update_date as cancelDate, b.amount as totalAmount, pm.status_exchange as statusExchange from bill b left join customer c on b.customer_id = c.id  left join payment pm on pm.bill_id = b.id \n" +
//            "join payment_method pme on pme.id = b.payment_method_id where b.status='HUY' and pme.name='CHUYEN_KHOAN' order by b.update_date desc", nativeQuery = true)
//    List<RefundDto> findListNeedRefund();

    @Query(value = "SELECT DISTINCT b.id AS maHoaDon, b.code AS maDinhDanh, a.name AS hoVaTen, a.phone_Number AS soDienThoai, " +
            "b.create_Date AS ngayTao, b.amount AS tongTien, b.status AS trangThai, b.invoice_Type AS loaiDon, " +
            "pm.name AS hinhThucThanhToan, pmt.order_Id AS maGiaoDich " +
            "FROM Bill b " +
            "JOIN Payment pmt ON b.id = pmt.bill_id " +
            "LEFT JOIN Customer a ON b.customer_id = a.id " +
            "LEFT JOIN Bill_Detail bd ON b.id = bd.bill_id " +
            "LEFT JOIN Payment_Method pm ON b.payment_Method_id = pm.id", nativeQuery = true)
    List<BillDtoInterface> listBill();

    @Query(value = "SELECT DISTINCT b.id AS maHoaDon, b.code AS maDinhDanh, a.name AS hoVaTen, a.phoneNumber AS soDienThoai, " +
            "b.createDate AS ngayTao, b.amount AS tongTien, b.status AS trangThai, b.invoiceType AS loaiDon, " +
            "pm.name AS hinhThucThanhToan " +
            "FROM Bill b " +
            "LEFT JOIN Customer a ON b.customer.id = a.id " +
            "LEFT JOIN BillDetail bd ON b.id = bd.bill.id " +
            "LEFT JOIN PaymentMethod pm ON b.paymentMethod.id = pm.id " +
            "WHERE (:maDinhDanh IS NULL OR b.code LIKE CONCAT('%', :maDinhDanh, '%')) " +
            "AND (:ngayTaoStart IS NULL OR :ngayTaoEnd IS NULL OR (b.createDate BETWEEN :ngayTaoStart AND :ngayTaoEnd)) " +
            "AND (:trangThai IS NULL OR b.status = :trangThai) " +
            "AND (:loaiDon IS NULL OR b.invoiceType = :loaiDon) " +
            "AND (:soDienThoai IS NULL OR a.phoneNumber IS NULL OR a.phoneNumber LIKE CONCAT('%', :soDienThoai, '%')) " +
            "AND (:hoVaTen IS NULL OR a.name IS NULL OR a.name LIKE CONCAT('%', :hoVaTen, '%'))", nativeQuery = true)
    List<BillDtoInterface> listSearchBill(
            @Param("maDinhDanh") String maDinhDanh,
            @Param("ngayTaoStart") LocalDateTime ngayTaoStart,
            @Param("ngayTaoEnd") LocalDateTime ngayTaoEnd,
            @Param("trangThai") BillStatus trangThai,
            @Param("loaiDon") InvoiceType loaiDon,
            @Param("soDienThoai") String soDienThoai,
            @Param("hoVaTen") String hoVaTen);
    @Query("Select b.id from Bill b where b.status=?1")
    List<Long> findAllByStatus(BillStatus billStatus);

    @Query(value = "select * from Bill", nativeQuery = true)
    List<InStoreInvoiceDetail> findAllInStoreInvoiceDetail(List<Long> ids);
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE BILL
            SET STATUS='CHO_HANG_VE'
            WHERE ID IN (SELECT BILL.ID
                         FROM BILL
                                  LEFT JOIN BILL_DETAIL ON BILL.ID = BILL_DETAIL.BILL_ID
                                  LEFT JOIN PRODUCT_DETAIL ON BILL_DETAIL.PRODUCT_DETAIL_ID = PRODUCT_DETAIL.ID
            
                         WHERE BILL.STATUS = 'CHO_XAC_NHAN'
                           AND PRODUCT_DETAIL.QUANTITY < BILL_DETAIL.QUANTITY)
            
    """, nativeQuery = true)
    void updateStatusChoHangVe();
}
