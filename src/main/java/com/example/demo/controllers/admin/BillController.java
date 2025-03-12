package com.example.demo.controllers.admin;

import com.example.demo.dto.Bill.*;
import com.example.demo.entities.Bill;
import com.example.demo.entities.enumClass.BillStatus;
import com.example.demo.entities.enumClass.InvoiceType;
import com.example.demo.repositories.DiscountCodeRepository;
import com.example.demo.repositories.ProductDetailRepository;
import com.example.demo.services.BillService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @GetMapping("/list-in-store-invoice")
    public ResponseEntity<?> listInStoreInvoice() {

        var listBillId = billService.findAllInStoreInvoiceId();
        listBillId.forEach(id -> {

        });
        return null;

    }


    @GetMapping("/bill-list")
    public String getBill(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sort", defaultValue = "createDate,asc") String sortField,
            @RequestParam(name = "maDinhDanh", required = false) String maDinhDanh,
            @RequestParam(name = "ngayTaoStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayTaoStart,
            @RequestParam(name = "ngayTaoEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayTaoEnd,
            @RequestParam(name = "trangThai", required = false) String trangThai,
            @RequestParam(name = "loaiDon", required = false) String loaiDon,
            @RequestParam(name = "soDienThoai", required = false) String soDienThoai,
            @RequestParam(name = "hoVaTen", required = false) String hoVaTen
    ) {
        //update status các hóa đơn chờ xác nhận nhưng thiếu hàng
        billService.updateStatusChoHangVe();

        int pageSize = 8;
        String[] sortParams = sortField.split(",");
        String sortFieldName = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.ASC;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;
        }

        Sort sort = Sort.by(sortDirection, sortFieldName);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<BillDtoInterface> Bill;
        LocalDateTime convertedNgayTaoStart = null;
        LocalDateTime convertedNgayTaoEnd = null;
        if (ngayTaoStart != null || ngayTaoEnd != null || maDinhDanh != null || trangThai != null || loaiDon != null || hoVaTen != null || soDienThoai != null) {
            // Convert Date to LocalDateTime

            if (ngayTaoStart != null) {
                convertedNgayTaoStart = ngayTaoStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                model.addAttribute("ngayTaoStart", convertedNgayTaoStart.format(formatter));
            }
            if (ngayTaoEnd != null) {
                convertedNgayTaoEnd = ngayTaoEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                model.addAttribute("ngayTaoEnd", convertedNgayTaoEnd.format(formatter));
            }
            Bill = billService.searchListBill(maDinhDanh.trim(), convertedNgayTaoStart, convertedNgayTaoEnd, trangThai, loaiDon, soDienThoai.trim(), hoVaTen.trim(), pageable);
        } else {
            Bill = billService.findAll(pageable);
        }

        model.addAttribute("sortField", sortFieldName);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("items", Bill);

        model.addAttribute("maDinhDanh", maDinhDanh);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("loaiDon", loaiDon);
        model.addAttribute("soDienThoai", soDienThoai);
        model.addAttribute("hoVaTen", hoVaTen);
        model.addAttribute("sortField", sortField);
        model.addAttribute("billStatus", BillStatus.values());
        model.addAttribute("invoiceType", InvoiceType.values());
        return "admin/bill";
    }

    @GetMapping("/update-bill-status/{billId}")
    public String updateBillStatus2(Model model,
                                    @PathVariable Long billId,
                                    @RequestParam String trangThaiDonHang,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra tính hợp lệ của trạng thái trước khi cập nhật
            if (!isValidEnumValue(trangThaiDonHang)) {
                redirectAttributes.addFlashAttribute("error",
                        "Trạng thái đơn hàng không hợp lệ: " + trangThaiDonHang);
                return "redirect:/admin/getbill-detail/" + billId;
            }

            // Gọi service để cập nhật trạng thái
            Bill bill = billService.updateStatus(trangThaiDonHang, billId);
            redirectAttributes.addFlashAttribute("message",
                    "Hóa đơn " + bill.getCode() + " cập nhật trạng thái thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật trạng thái.");
        }

        return "redirect:/admin/getbill-detail/" + billId;
    }

    // Hàm kiểm tra giá trị Enum hợp lệ
    private boolean isValidEnumValue(String value) {
        for (BillStatus status : BillStatus.values()) {
            if (status.name().equals(value)) {
                return true;
            }
        }
        return false;
    }


    @GetMapping("/getbill-detail/{maHoaDon}")
    public String getBillDetail(Model model, @PathVariable("maHoaDon") Long maHoaDon) {

        BillDetailDtoInterface billDetailDtoInterface = billService.getBillDetail(maHoaDon);
        List<BillDetailProduct> billDetailProducts = billService.getBillDetailProduct(maHoaDon);
        Double total = Double.valueOf("0");
        for (BillDetailProduct billDetailProduct :
                billDetailProducts) {
            int q = billDetailProduct.getSoLuong();
            total += billDetailProduct.getGiaTien() * q;
        }
        model.addAttribute("billDetailProduct", billDetailProducts);
        model.addAttribute("billdetail", billDetailDtoInterface);
        model.addAttribute("total", total);
        return "admin/bill-detail";
    }

    @PostMapping("/api/bill-detail/check-bill")
    public ResponseEntity<?> checkBill(@RequestBody List<Map<String, Integer>> mapProductDetail) {
        if (mapProductDetail.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (Map<String, Integer> item : mapProductDetail) {
            Long productDetailId = Long.valueOf(item.get("productDetailId").toString());
            Integer quantity = Integer.valueOf(item.get("quantity").toString());
            var productDetail = productDetailRepository.findById(productDetailId);
            if (productDetail.isPresent()) {
                if (productDetail.get().getQuantity() < quantity) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            }
        }
        return new ResponseEntity<>(HttpStatus.OK);


    }

//    @GetMapping("/export-bill")
//    public void exportBill(
//            HttpServletResponse response,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "sort", defaultValue = "create_Date,desc") String sortField,
//            @RequestParam(name = "ngayTaoStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayTaoStart,
//            @RequestParam(name = "ngayTaoEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayTaoEnd,
//            UriComponentsBuilder uriBuilder
//    ) throws IOException {
//        int pageSize = 10;
//        String[] sortParams = sortField.split(",");
//        String sortFieldName = sortParams[0];
//        Sort.Direction sortDirection = Sort.Direction.ASC;
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
//            sortDirection = Sort.Direction.DESC;
//        }
//
//        Sort sort = Sort.by(sortDirection, sortFieldName);
//
//
//        Pageable pageable = PageRequest.of(page, pageSize, sort);
//        Page<BillDtoInterface> bills;
//        bills = billService.findAll(pageable);
//
//
//        String exportUrl = uriBuilder.path("/export-bill")
//                .queryParam("page", page)
//                .queryParam("sort", sortField)
//                .queryParam("ngayTaoStart", ngayTaoStart)
//                .queryParam("ngayTaoEnd", ngayTaoEnd)
//                .toUriString();
//
//        billService.exportToExcel(response, bills, exportUrl);
//    }
//
//    @GetMapping("/export-pdf/{maHoaDon}")
//    public String exportPdf(HttpServletResponse response, @PathVariable("maHoaDon") Long maHoaDon) throws DocumentException, IOException {
//        return billService.exportPdf(response, maHoaDon);
//    }
//
//    @GetMapping("admin/api/bill-detail/rollback-voucher/{voucherId}")
//    public ResponseEntity<?> rollbackvoucher(@PathVariable Long voucherId) {
//        if (voucherId == 0)
//            return ResponseEntity.badRequest().build();
//        Optional<DiscountCode> optVoucher = discountCodeRepository.findById(voucherId);
//        if (!optVoucher.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        var voucher=optVoucher.get();
//        voucher.setMaximumUsage(voucher.getMaximumUsage() + 1);
//        discountCodeRepository.save(voucher);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @GetMapping("/generate-pdf/{maHoaDon}")
    public ResponseEntity<String> generatePDF(@PathVariable Long maHoaDon) {
        // Your HTML content as a string
        String htmlContent = billService.getHtmlContent(maHoaDon);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=utf-8");

        return new ResponseEntity<>(htmlContent, headers, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping("/api/product/{billId}/bill")
    public ResponseEntity<List<BillDetailProduct>> getAllProductByBillId(@PathVariable Long billId) {
        return ResponseEntity.ok(billService.getBillDetailProduct(billId));
    }

    @ResponseBody
    @GetMapping("/api/bill/validToReturn")
    public Page<BillDto> getAllValidBillToReturn(Pageable pageable) {
        return billService.getAllValidBillToReturn(pageable);
    }

    @ResponseBody
    @GetMapping("/api/bill/validToReturn/search")
    public Page<BillDto> getAllValidBillToReturnSearch(SearchBillDto searchBillDto, Pageable pageable) {
        return billService.searchBillJson(searchBillDto, pageable);
    }

}
