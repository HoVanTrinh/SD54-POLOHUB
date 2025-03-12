package com.example.demo.services.serviceImpl;

import com.example.demo.dto.Bill.*;
import com.example.demo.dto.CustomerDto.CustomerDto;
import com.example.demo.entities.Account;
import com.example.demo.entities.Bill;
import com.example.demo.entities.BillDetail;
import com.example.demo.entities.ProductDetail;
import com.example.demo.entities.enumClass.BillStatus;
import com.example.demo.entities.enumClass.InvoiceType;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ShopApiException;
import com.example.demo.repositories.BillRepository;
import com.example.demo.repositories.ProductDetailRepository;
import com.example.demo.repositories.Specification.BillSpecification;
import com.example.demo.services.BillService;
import com.example.demo.untils.UserLoginUtil;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Override
    public List<Long> findAllInStoreInvoiceId() {
        return billRepository.findAllByStatus(BillStatus.TAI_QUAY);
    }
    @Override
    public List<InStoreInvoiceDto> findAllInStoreInvoice(List<Long> ids) {
        Map<Long,InStoreInvoiceDto> map = new HashMap<>();
        billRepository.findAllInStoreInvoiceDetail(ids).forEach(invoiceDetail -> {
//                    if(map.add)
                }
        );
        return null;
    }

    @Override
    public Page<BillDtoInterface> findAll(Pageable pageable){
        return billRepository.listBill(pageable);
    }

    @Override
    public List<BillDtoInterface> findAll() {
        return billRepository.listBill();
    }

    @Override
    public Page<BillDtoInterface> searchListBill(String maDinhDanh,
                                                 LocalDateTime ngayTaoStart,
                                                 LocalDateTime ngayTaoEnd,
                                                 String trangThai,
                                                 String loaiDon,
                                                 String soDienThoai,
                                                 String hoVaTen,
                                                 Pageable pageable) {
        BillStatus status = null;
        InvoiceType invoiceType = null;

        try {
            status = BillStatus.valueOf(trangThai);
        } catch (IllegalArgumentException e) {

        }
        try {
            invoiceType = InvoiceType.valueOf(loaiDon);
        } catch (IllegalArgumentException e) {

        }
        return billRepository.listSearchBill( maDinhDanh,
                ngayTaoStart,
                ngayTaoEnd,
                status,
                invoiceType,
                soDienThoai,
                hoVaTen,
                pageable);
    }

    @Override
    public List<BillDtoInterface> searchListBill(String maDinhDanh, LocalDateTime ngayTaoStart, LocalDateTime ngayTaoEnd, String trangThai, String loaiDon, String soDienThoai, String hoVaTen) {
        BillStatus status = null;
        InvoiceType invoiceType = null;

        try {
            status = BillStatus.valueOf(trangThai);
        } catch (IllegalArgumentException e) {

        }
        try {
            invoiceType = InvoiceType.valueOf(loaiDon);
        } catch (IllegalArgumentException e) {

        }
        return billRepository.listSearchBill( maDinhDanh,
                ngayTaoStart,
                ngayTaoEnd,
                status,
                invoiceType,
                soDienThoai,
                hoVaTen);
    }

    @Override
    public Bill updateStatus(String status, Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bill có mã: " + id));

        BillStatus newStatus = BillStatus.valueOf(status);
        BillStatus currentStatus = bill.getStatus();

        if (newStatus == BillStatus.CHO_LAY_HANG && currentStatus == BillStatus.CHO_XAC_NHAN) {
            try {
                deductProductQuantitiesOnStatusChange(id);
            } catch (ShopApiException e) {
                bill.setStatus(BillStatus.CHO_HANG_VE);
                bill.setCreateDate(LocalDateTime.now());
                return billRepository.save(bill);
            }
        }

        if (newStatus == BillStatus.HUY || newStatus == BillStatus.THAT_BAI) {
            if (currentStatus != BillStatus.CHO_HANG_VE && currentStatus != BillStatus.CHO_XAC_NHAN) {
                List<BillDetailProduct> billDetailProducts = billRepository.getBillDetailProduct(id);
                billDetailProducts.forEach(item -> {
                    ProductDetail productDetail = productDetailRepository.findById(item.getId())
                            .orElseThrow(() -> new NotFoundException("Không tìm thấy thuộc tính sản phẩm: " + item.getId()));
                    productDetail.setQuantity(productDetail.getQuantity() + item.getSoLuong());
                    productDetailRepository.save(productDetail);
                });
            }
        }

        bill.setStatus(newStatus);
        bill.setUpdateDate(LocalDateTime.now());
        return billRepository.save(bill);
    }

    @Override
    public BillDetailDtoInterface getBillDetail(Long maHoaDon) {
        return billRepository.getBillDetail(maHoaDon);
    }

    @Override
    public Page<Bill> getBillByStatus(String status, Pageable pageable) {
        Account account = UserLoginUtil.getCurrentLogin();
        BillStatus status1 = null;

        try {
            status1 = BillStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            // Handle the case where the input string does not match any enum constant
            // You can log an error, return a default value, or perform other error handling here.
        }
        return billRepository.findAllByStatusAndCustomer_Account_Id(status1, account.getId(), pageable);
    }

    @Override
    public List<BillDetailProduct> getBillDetailProduct(Long maHoaDon) {
        return billRepository.getBillDetailProduct(maHoaDon);
    }

    public void exportToExcel(HttpServletResponse response, Page<BillDtoInterface> bills, String exportUrl) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bills.xlsx");

        // Tạo workbook Excel và các sheet, row, cell tương ứng
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Get the current date in the "dd-MM-yyyy" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());

        // Create a sheet with the name "bill_dd-mm-yyyy"
        XSSFSheet sheet = workbook.createSheet("bill_" + currentDate);

        // Tạo tiêu đề cột
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã Hóa Đơn");
        headerRow.createCell(1).setCellValue("Họ và Tên");
        headerRow.createCell(2).setCellValue("Số điện thoại");
        headerRow.createCell(3).setCellValue("Ngày đặt");
        headerRow.createCell(4).setCellValue("Tổng tiền");
        headerRow.createCell(5).setCellValue("Trạng thái");
        headerRow.createCell(6).setCellValue("Loại đơn");
        headerRow.createCell(7).setCellValue("Hình thực thanh toán");

        int rowNum = 1;
        for (BillDtoInterface bill : bills) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bill.getMaDinhDanh());
            row.createCell(1).setCellValue(bill.getHoVaTen());
            row.createCell(2).setCellValue(bill.getSoDienThoai());
            XSSFCell dateCell = row.createCell(3);
            XSSFCellStyle dateCellStyle = workbook.createCellStyle();
            XSSFDataFormat dataFormat = workbook.createDataFormat();
            dateCellStyle.setDataFormat(dataFormat.getFormat("dd/MM/yyyy"));
            dateCell.setCellStyle(dateCellStyle);
            dateCell.setCellValue(bill.getNgayTao());
            XSSFCell totalCell = row.createCell(4);
            totalCell.setCellValue(bill.getTongTien());

            XSSFCellStyle totalCellStyle = workbook.createCellStyle();
            XSSFDataFormat dataFormat1 = workbook.createDataFormat();
            totalCellStyle.setDataFormat(dataFormat1.getFormat("#,###"));
            totalCell.setCellStyle(totalCellStyle);
            String trangThaiText = "";
            switch (bill.getTrangThai()) {
                case CHO_XAC_NHAN:
                    trangThaiText = "Chờ xác nhận";
                    break;
                case CHO_LAY_HANG:
                    trangThaiText = "Đang xử lý";
                    break;
                case CHO_GIAO_HANG:
                    trangThaiText = "Chờ giao hàng";
                    break;
                case HOAN_THANH:
                    trangThaiText = "Hoàn thành";
                    break;
                case HUY:
                    trangThaiText = "Hủy";
                    break;
                default:
                    trangThaiText = "  ";
            }
            String loaiDonText = "";
            switch (bill.getLoaiDon()) {
                case OFFLINE:
                    loaiDonText = "Tại quầy";
                    break;
                case ONLINE:
                    loaiDonText = "Trực tuyến";
                    break;
                default:
                    loaiDonText = "  ";
            }
            row.createCell(5).setCellValue(trangThaiText);
            row.createCell(6).setCellValue(loaiDonText);
            row.createCell(7).setCellValue(bill.getHinhThucThanhToan());

            XSSFCell linkCell = row.createCell(8);
            XSSFRichTextString linkText = new XSSFRichTextString(" ");
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFHyperlink hyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
            hyperlink.setAddress(exportUrl);
            linkCell.setHyperlink(hyperlink);
            linkCell.setCellValue(linkText);
        }

        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
        }
    }

    @Override
    public String exportPdf(HttpServletResponse response, Long maHoaDon) throws DocumentException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("/static/fonts/time-new-roman/SVN-Times New Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        String htmlContent = getHtmlContent(maHoaDon);
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        try (OutputStream outputStream = response.getOutputStream()) {
            renderer.createPDF(outputStream, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHtmlContent(Long maHoaDon) {
        BillDetailDtoInterface billDetailDtoInterface = billRepository.getBillDetail(maHoaDon);
        List<BillDetailProduct> billDetailProduct = billRepository.getBillDetailProduct(maHoaDon);
        String email = billDetailDtoInterface.getEmail();
        if (email == null) {
            email = "";
        }
        String address = billDetailDtoInterface.getDiaChi();
        if (address == null) {
            address = "";
        }

        String customerName = billDetailDtoInterface.getTenKhachHang();
        if (customerName == null) {
            customerName = "";
        }

        String customerPhone = billDetailDtoInterface.getSoDienThoai();
        if (customerPhone == null) {
            customerPhone = "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String htmlContent = "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>\n" +
                "    <title>Hóa đơn bán hàng</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: SVN-Times New Roman;\n" +
                "            background-color: #f8f9fa;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "        }\n" +
                "        .invoice-container {\n" +
                "            width: 60%; /* Thu nhỏ khung hóa đơn */\n" +
                "            max-width: 600px;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1, h3, h4, h5 {\n" +
                "            text-align: center;\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        th, td {\n" +
                "            padding: 10px;\n" +
                "            text-align: left;\n" +
                "            border: 1px solid #ddd;\n" +
                "        }\n" +
                "        th {\n" +
                "            background-color: #f2f2f2;\n" +
                "        }\n" +
                "        .summary {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 16px;\n" +
                "            text-align: right;\n" + // Thêm căn lề phải
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"invoice-container\">\n" +
                "        <h1>POLO HUB</h1>\n" +
                "        <h4>Toà nhà Fpoly Trinh Van Bo</h3>\n" +
                "        <h4>Điện thoại : 190090090-66668888 </h3>\n" +
                "        <h5>Mã hóa đơn: " + billDetailDtoInterface.getMaDinhDanh() + "</h5>\n" +
                "        <h5>Ngày thanh toán: " + billDetailDtoInterface.getCreatedDate().format(formatter) + "</h5>\n" +
                "        <h5>Họ và tên: " + customerName + "</h5>\n" +
                "        <h5>Số điện thoại: " + customerPhone + "</h5>\n" +
                "        <table>\n" +
                "            <tr>\n" +
                "                <th>Tên sản phẩm</th>\n" +
                "                <th>Màu sắc</th>\n" +
                "                <th>Kích cỡ</th>\n" +
                "                <th>Giá tiền</th>\n" +
                "                <th>Số lượng</th>\n" +
                "                <th>Tổng tiền</th>\n" +
                "            </tr>\n";

        Double totalMoney = 0.0;
        for (BillDetailProduct item : billDetailProduct) {
            totalMoney += item.getGiaTien() * item.getSoLuong();
        }

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        for (BillDetailProduct item : billDetailProduct) {
            htmlContent += "<tr>\n" +
                    "<td>" + item.getTenSanPham() + "</td>\n" +
                    "<td>" + item.getTenMau() + "</td>\n" +
                    "<td>" + item.getKichCo() + "</td>\n" +
                    "<td>" + currencyFormatter.format(item.getGiaTien()) + "</td>\n" +
                    "<td>" + item.getSoLuong() + "</td>\n" +
                    "<td>" + currencyFormatter.format(item.getTongTien()) + "</td>\n" +
                    "</tr>\n";
        }

        htmlContent += "</table>\n" +
                "        <div class=\"summary\">\n" +
                "            <p>Tổng tiền: " + currencyFormatter.format(totalMoney) + "</p>\n" +
                "            <p>Tiền giảm giá: " + currencyFormatter.format(billDetailDtoInterface.getTienKhuyenMai()) + "</p>\n" +
                "            <p><strong>Tổng tiền thanh toán: " + currencyFormatter.format(totalMoney - billDetailDtoInterface.getTienKhuyenMai()) + "</strong></p>\n" +
                "        </div>\n" +
                "        <h3 style=\"text-align: center; margin-top: 30px;\">Xin chân thành cảm ơn Quý khách!</h3>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return htmlContent;
    }

    @Override
    public Page<Bill> getBillByAccount(Pageable pageable) {
        Account account = UserLoginUtil.getCurrentLogin();
        return billRepository.findByCustomer_Account_Id(account.getId(), pageable);
    }

    @Override
    public void deleteById(Long id) {
        billRepository.deleteById(id);
    }

    @Override
    public Page<BillDto> searchBillJson(SearchBillDto searchBillDto, Pageable pageable) {
        Specification<Bill> spec = new BillSpecification(searchBillDto);
        Page<Bill> bills = billRepository.findAll(spec, pageable);
        return bills.map(this::convertToDto);
    }

    @Override
    public Page<BillDto> getAllValidBillToReturn(Pageable pageable) {
        return billRepository.findValidBillToReturn(pageable).map(this::convertToDto);
    }

    private BillDto convertToDto(Bill bill) {
        BillDto billDto = new BillDto();
        BillDto.Voucher voucher=  billDto.new Voucher();
        voucher.setId(bill.getDiscountCode()==null?null:bill.getDiscountCode().getId());
        voucher.setMinimumValueForDiscount(bill.getDiscountCode()==null?null:bill.getDiscountCode().getMinimumAmountInCart());
        voucher.setBillDiscount(bill.getPromotionPrice());
        billDto.setId(bill.getId());
        billDto.setCode(bill.getCode());
        billDto.setCreateDate(bill.getCreateDate());
        billDto.setStatus(bill.getStatus());
        billDto.setUpdateDate(bill.getUpdateDate());
        billDto.setVoucher(voucher.getId()==null?null:voucher);
        CustomerDto customer = new CustomerDto();
        if(bill.getCustomer() != null) {
            customer.setName(bill.getCustomer().getName());
            customer.setId(bill.getCustomer().getId());
            customer.setCode(bill.getCustomer().getCode());
            customer.setCode(bill.getCustomer().getCode());
        }
        billDto.setCustomer(customer);
        Double total = Double.valueOf(0);
        for (BillDetail billDetail:
                bill.getBillDetail()) {
            total += billDetail.getQuantity() * billDetail.getMomentPrice();
        }
        billDto.setTotalAmount(total);
        return billDto;
    }

    @Override
    public void deductProductQuantitiesOnStatusChange(Long billId) {
        List<BillDetailProduct> billDetailProducts = billRepository.getBillDetailProduct(billId);

        billDetailProducts.forEach(item -> {
            ProductDetail productDetail = productDetailRepository.findById(item.getId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy thuộc tính sản phẩm: " + item.getId()));

            if (productDetail.getQuantity() < item.getSoLuong()) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST,
                        "Không đủ số lượng sản phẩm trong kho: " + productDetail.getProduct().getName());
            }

            productDetail.setQuantity(productDetail.getQuantity() - item.getSoLuong());
            productDetailRepository.save(productDetail);
        });
    }

    @Override
    public void updateStatusChoHangVe() {
        billRepository.updateStatusChoHangVe();
//        listCheck.forEach(bill->{
//
//        });
    }

}
