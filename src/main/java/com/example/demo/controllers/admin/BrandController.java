package com.example.demo.controllers.admin;

import com.example.demo.entities.Brand;
import com.example.demo.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/brand-all")
    public String getAllBrand(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "sort", defaultValue = "name,asc") String sortField) {
        int pageSize = 8; // Number of items per page
        String[] sortParams = sortField.split(",");
        String sortFieldName = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(sortDirection, sortFieldName);

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Brand> brandPage = brandService.getAllBrand(pageable);

        model.addAttribute("sortField", sortFieldName);
        model.addAttribute("sortDirection", sortDirection);

        model.addAttribute("items", brandPage);

        return "admin/brand";
    }

    @GetMapping("/brand-create")
    public String viewAddBrand(Model model){
        Brand brand = new Brand();
        model.addAttribute("action", "/admin/brand-save");
        model.addAttribute("Brand", brand);
        return "admin/brand-create";
    }


    @PostMapping("/brand-save")
    public String addBrand(RedirectAttributes redirectAttributes,
                           @Validated @ModelAttribute("Brand") Brand brand) {
        try {
            // Check if the brand name already exists before proceeding
            if (brandService.existsByName(brand.getName())) {
                redirectAttributes.addFlashAttribute("duplicateName", "Tên nhãn hàng đã tồn tại");
                return "redirect:/admin/brand-create"; // Redirect to create page on duplicate name
            }

            // Attempt to create a new brand if it does not exist
            brandService.createBrand(brand);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm nhãn hàng mới thành công");
            return "redirect:/admin/brand-all"; // Redirect to the brand list upon success

        } catch (Exception e) {
            // Handle other exceptions, if any
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/brand-create"; // Redirect back to the creation page
        }
    }

    @PostMapping("/brand-update/{id}")
    public String update(@PathVariable("id") Long id,
                         @Validated @ModelAttribute("Brand") Brand brand, RedirectAttributes redirectAttributes) {
        if (brandService.existsById(id)) {
            try {
                brandService.updateBrand(id, brand);
                redirectAttributes.addFlashAttribute("successMessage", "Nhãn hàng đã được cập nhật thành công");

            }catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/admin/brand-edit/" + id;
            }
            return "redirect:/admin/brand-all";
        } else {
            return "404";
        }
    }

    @GetMapping("/brand-detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Optional<Brand> optional = brandService.findById(id);
        if (optional.isPresent()) {
            Brand brand = optional.get();
            model.addAttribute("Brand", brand);
            model.addAttribute("action", "/admin/brand-update/" + brand.getId());
            return "admin/brand-create";
        } else {
            return "404";
        }
    }

    @GetMapping("/brand-delete/{id}")
    public String delete(@PathVariable("id") Long id, ModelMap modelMap){
        brandService.delete(id);
        return "redirect:/admin/brand-all";
    }
    @GetMapping("/brand-restore/{id}")
    public String restore(@PathVariable("id") Long id, ModelMap modelMap){
        brandService.restore(id);
        return "redirect:/admin/brand-all"; // hoặc redirect về trang phù hợp
    }
}
