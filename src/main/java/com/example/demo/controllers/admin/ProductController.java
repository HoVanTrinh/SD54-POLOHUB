package com.example.demo.controllers.admin;


import com.example.demo.dto.product.CreateProductDetailsForm;
import com.example.demo.dto.product.ProductSearchDto;
import com.example.demo.entities.*;
import com.example.demo.repositories.ColorRepo;
import com.example.demo.repositories.MaterialRepository;
import com.example.demo.repositories.SizeRepository;
import com.example.demo.services.*;
import com.example.demo.untils.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Value("${upload.directory}")
    private String uploadDirectory; // Đường dẫn đến thư mục lưu ảnh, được cấu hình trong application.properties
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private BrandService brandService;

@Autowired
private MaterialRepository materialRepository;
    @Autowired
    private SizeService sizeService;

    @Autowired
    private MaterialService materialService;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ColorService colorService;
    @Autowired
    private ColorRepo colorRepo;
    @Autowired
    private ImageService imageService;

    @Autowired
    FileUploadUtil fileUploadUtil;
//    @GetMapping("/product-all")
//    public String getAllProduct(Model model,
//                                @RequestParam(name = "page", defaultValue = "0") int page,
//                                @RequestParam(name = "sort", defaultValue = "createDate,desc") String sortField,
//                                @RequestParam(name = "maSanPham", required = false) String maSanPham,
//                                @RequestParam(name = "tenSanPham", required = false) String tenSanPham,
//                                @RequestParam(name = "nhanHang", required = false) Long nhanHang,
//                                @RequestParam(name = "chatLieu", required = false) Long chatLieu,
//                                @RequestParam(name = "trangThai", required = false) Integer trangThai,
//                                @RequestParam(name = "quantity", required = false) Integer quantity
//                               )
//                                 {
//
//        int pageSize = 8;
//        String[] sortParams = sortField.split(",");
//        String sortFieldName = sortParams[0];
//        Sort.Direction sortDirection = Sort.Direction.ASC;
//
//        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
//            sortDirection = Sort.Direction.DESC;
//        }
//
//        Sort sort = Sort.by(sortDirection, sortFieldName);
//        Pageable pageable = PageRequest.of(page, pageSize, sort);
//
//        Page<ProductSearchDto> listProducts = productService.listSearchProduct(maSanPham, tenSanPham, nhanHang, chatLieu, trangThai,quantity,pageable);
//                                     // Tính tổng số lượng từ danh sách sản phẩm
//
//
//        model.addAttribute("maSanPham", maSanPham);
//        model.addAttribute("tenSanPham", tenSanPham);
//        model.addAttribute("nhanHang", nhanHang);
//        model.addAttribute("chatLieu", chatLieu);
//        model.addAttribute("quantity", quantity);
//        model.addAttribute("trangThai", trangThai);
//        model.addAttribute("sortDirection", sortDirection);
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("items", listProducts);
//
//
//        return "admin/product";
//    }
@GetMapping("/product-all")
public String getAllProduct(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "sort", defaultValue = "createDate,desc") String sortField,
                            @RequestParam(name = "maSanPham", required = false) String maSanPham,
                            @RequestParam(name = "tenSanPham", required = false) String tenSanPham,
                            @RequestParam(name = "nhanHang", required = false) Long nhanHang,
                            @RequestParam(name = "chatLieu", required = false) Long chatLieu,
                            @RequestParam(name = "trangThai", required = false) Integer trangThai,
                            @RequestParam(name = "quantity", required = false) Integer quantity) {

    int pageSize = 8;
    String[] sortParams = sortField.split(",");
    String sortFieldName = sortParams[0];
    Sort.Direction sortDirection = Sort.Direction.ASC;

    if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
        sortDirection = Sort.Direction.DESC;
    }

    Sort sort = Sort.by(sortDirection, sortFieldName);
    Pageable pageable = PageRequest.of(page, pageSize, sort);

    // Tìm kiếm sản phẩm bằng các tham số từ request
    Page<ProductSearchDto> listProducts = productService.listSearchProduct(maSanPham, tenSanPham, nhanHang, chatLieu, trangThai, quantity, pageable);

    // Tính tổng số lượng từ danh sách sản phẩm
    long totalQuantity = listProducts.getContent()
            .stream()
            .mapToLong(ProductSearchDto::getQuantity) // Giả sử ProductSearchDto có phương thức getQuantity
            .sum();

    // Thêm các thuộc tính vào model để sử dụng trong view
    model.addAttribute("maSanPham", maSanPham);
    model.addAttribute("tenSanPham", tenSanPham);
    model.addAttribute("nhanHang", nhanHang);
    model.addAttribute("chatLieu", chatLieu);
    model.addAttribute("quantity", quantity);
    model.addAttribute("trangThai", trangThai);
    model.addAttribute("sortDirection", sortDirection);
    model.addAttribute("sortField", sortField);
    model.addAttribute("items", listProducts);
    model.addAttribute("totalQuantity", totalQuantity); // Thêm tổng số lượng vào model

    return "admin/product"; // Trả về view tương ứng
}
    @GetMapping("/product-create")
    public String viewAddProduct(Model model, HttpSession session) {
        Product product = new Product();
         product.setName("Áo Polo Nam");

        model.addAttribute("action", "/admin/product-create/save-part1");
        model.addAttribute("product", product);

        return "/admin/product-create";
    }

//    @PostMapping("/product-create/save-part1")
//    public String handlePart1(@ModelAttribute("product") Product product, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
//        if(productService.existsByCode(product.getCode())) {
//            redirectAttributes.addFlashAttribute("duplicateCode", "Mã sản phẩm đã tồn tại");
//            return "redirect:/admin/product-create";
//        }
//        String randomString = UUID.randomUUID().toString();
//
//        session.setAttribute("randomCreateKey", randomString);
//
//        session.setAttribute("createProductPart1" + randomString, product);
//        return "redirect:/admin/product-create/part2";
//    }
@PostMapping("/product-create/save-part1")
public String handlePart1(@ModelAttribute("product") Product product, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
    if (productService.existsByCode(product.getCode())) {
        redirectAttributes.addFlashAttribute("duplicateCode", "Mã sản phẩm đã tồn tại");
        return "redirect:/admin/product-create";
    }

    // Kiểm tra nếu tên sản phẩm đã tồn tại
    if (productService.existsByName(product.getName())) {
        redirectAttributes.addFlashAttribute("duplicateName", "Tên sản phẩm đã tồn tại");
        return "redirect:/admin/product-create";
    }

    String randomString = UUID.randomUUID().toString();
    session.setAttribute("randomCreateKey", randomString);
    session.setAttribute("createProductPart1" + randomString, product);
    return "redirect:/admin/product-create/part2";
}

    @GetMapping("/product-create/part2")
    public String viewAddProductPart2(Model model, HttpSession session) {
        String randomCreateKey = (String) session.getAttribute("randomCreateKey");

        Product part1Data = (Product) session.getAttribute("createProductPart1" + randomCreateKey);
        if (part1Data == null) {
            // Nếu dữ liệu phần 1 không tồn tại, điều hướng người dùng trở lại phần 1
            return "redirect:/admin/product-create";
        }

        CreateProductDetailsForm createProductDetailsForm = new CreateProductDetailsForm();
        List<ProductDetail> productDetails = new ArrayList<>();
        productDetails.add(new ProductDetail());
        createProductDetailsForm.setProductDetailList(productDetails);
        model.addAttribute("form", createProductDetailsForm);
        return "/admin/product-create-detail";
    }

    @PostMapping("/product-save")
    @Transactional(rollbackOn = Exception.class)
    public String handlePart2(@ModelAttribute("form") CreateProductDetailsForm form, HttpSession session, @RequestParam("files") List<MultipartFile> files, RedirectAttributes redirectAttributes) throws IOException {
        // Kiểm tra xem dữ liệu từ phần 1 đã tồn tại trong session hay chưa
        String randomCreateKey = (String) session.getAttribute("randomCreateKey");
        Product part1Data = (Product) session.getAttribute("createProductPart1" + randomCreateKey);
        if (part1Data == null) {
            // Nếu dữ liệu phần 1 không tồn tại, điều hướng người dùng trở lại phần 1
//            return "redirect:/admin/product-create";
        } else  {
            List<ProductDetail> productDetails = form.getProductDetailList();
            for (ProductDetail productDetail : productDetails) {
                productDetail.setProduct(part1Data);
            }
            part1Data.setProductDetails(productDetails);
            List<Image> images = new ArrayList<>();
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    String fileNameAfter = FileUploadUtil.saveFile(uploadDirectory, fileName, file);
                    images.add(new Image(null, fileNameAfter, LocalDateTime.now(), LocalDateTime.now(), "uploads/"+fileNameAfter, file.getContentType(), part1Data));
                }
            }
            part1Data.setImage(images);
            productService.add(part1Data);
        }

        session.removeAttribute("randomCreateKey");
        session.removeAttribute("createProductPart1" + randomCreateKey);

        redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm " + part1Data.getCode() + " thành công");
        return "redirect:/admin/product-all"; // Trả về trang thành công
    }

    @GetMapping("/product-edit/{productCode}")
    public String viewEditProduct(Model model, @PathVariable String productCode) {
        Product product = productService.getProductByCode(productCode);
        if(product == null) {
            return "/error/404";
        }

        model.addAttribute("action", "/admin/product-edit/save-part1");
        model.addAttribute("product", product);
        return "admin/product-edit";
    }

    @PostMapping("/product-edit/save-part1")
    public String handleSaveEditPart1(@ModelAttribute("product") Product product, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String randomString = UUID.randomUUID().toString();

        session.setAttribute("randomUpdateKey", randomString);

        session.setAttribute("editProductPart1" + randomString, product);

            return "redirect:/admin/product-edit/part2";

    }

    @GetMapping("/product-edit/part2")
    public String viewEditProductPart2(Model model, HttpSession session) {
        String randomUpdateKey = (String) session.getAttribute("randomUpdateKey");

        Product part1Data = (Product) session.getAttribute("editProductPart1" + randomUpdateKey);
        if (part1Data == null) {
            // Nếu dữ liệu phần 1 không tồn tại, điều hướng người dùng trở lại phần 1
            return "redirect:/admin/product-all";
        }

        Product product = productService.getProductById(part1Data.getId()).orElseThrow(null);
        List<ProductDetail> productDetails = product.getProductDetails();

        CreateProductDetailsForm createProductDetailsForm = new CreateProductDetailsForm();
        createProductDetailsForm.setProductDetailList(productDetails);
        model.addAttribute("listImages", product.getImage());
        model.addAttribute("form", createProductDetailsForm);
        return "/admin/product-edit-detail";
    }

    @PostMapping("/product-save-edit")
    @Transactional(rollbackOn = Exception.class)
    public String handleSaveEditPart2(@ModelAttribute("form") CreateProductDetailsForm form, HttpSession session,
                                      @RequestParam(value = "imageRemoveIds", required = false) List<Long> imageRemoveIds,
                                      @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                      RedirectAttributes redirectAttributes) throws IOException {
        String randomUpdateKey = (String) session.getAttribute("randomUpdateKey");
        Product part1Data = (Product) session.getAttribute("editProductPart1" + randomUpdateKey);
        if (part1Data == null) {
            return "redirect:/admin/product-all";
        }

        List<ProductDetail> productDetails = form.getProductDetailList();
        for (ProductDetail productDetail : productDetails) {
            productDetail.setProduct(part1Data);
        }
        part1Data.setProductDetails(productDetails);

        // Initialize imageRemoveIds if null to avoid NullPointerException
        imageRemoveIds = (imageRemoveIds == null) ? new ArrayList<>() : imageRemoveIds;

        List<Image> images = new ArrayList<>();
        List<Image> beforeImages = imageService.getAllImagesByProductId(part1Data.getId());

        for (Image image : beforeImages) {
            if (!imageRemoveIds.contains(image.getId())) {
                images.add(image);
            } else {
                FileUploadUtil.deleteFile(image.getLink());
            }
        }

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String fileNameAfter = FileUploadUtil.saveFile(uploadDirectory, fileName, file);
                images.add(new Image(null, fileNameAfter, LocalDateTime.now(), LocalDateTime.now(), "uploads/" + fileNameAfter, file.getContentType(), part1Data));
            }
        }

        imageService.removeImageByIds(imageRemoveIds);
        part1Data.setImage(images);
        part1Data.setUpdatedDate(LocalDateTime.now());
        productService.save(part1Data);

        // Clean up session attributes
        session.removeAttribute("randomUpdateKey");
        session.removeAttribute("editProductPart1" + randomUpdateKey);
        redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm được chỉnh sửa thành công");
        String productCode = (part1Data.getCode() != null) ? part1Data.getCode().trim() : "defaultCode";
        return "redirect:/admin/chi-tiet-san-pham/" + productCode;

//        return "redirect:/admin/chi-tiet-san-pham/" + part1Data.getCode();
    }

    @ModelAttribute("listSize")
    public List<Size> getSize() {
        return sizeRepository.findAllByDeleteFlagFalse();
    }

    @ModelAttribute("listColor")
    public List<Color> getColor() {
        return

                colorRepo.findAllByDeleteFlagFalse();
    }



    @ModelAttribute("listBrand")
    public List<Brand> getBrand() {

    return brandService.findAllByDeleteFlagFalse();
    }

    @ModelAttribute("listMaterial")
    public List<Material> getMaterial() {
        return materialRepository.findAllByDeleteFlagFalse();

    }

    @GetMapping("/product-delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Model model) {
        try{
            Product product = productService.delete(id);
            model.addAttribute("successMessage", "Sản phẩm có mã " + product.getCode() + " đã xóa thành công");
        }catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/product-all";
    }

}
