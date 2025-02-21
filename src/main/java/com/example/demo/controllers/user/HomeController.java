package com.example.demo.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {



    /**
     * Hiển thị trang chủ cho người dùng với quyền hợp lệ
     * @param model mô hình để truyền dữ liệu vào view
     * @return tên view nếu người dùng có quyền hợp lệ
     */
    @GetMapping("/home")
    public String getHomePage(Model model ) {
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        // Kiểm tra quyền của người dùng
        if (isAdminOrEmployee((List<GrantedAuthority>) authorities)) {
            return "redirect:/";
        } else {
            return "redirect:/admin";
        }
    }

    /**
     * Kiểm tra quyền của người dùng (User, Customer, Anonymous)
     * @param authorities danh sách quyền của người dùng
     * @return true nếu người dùng có quyền hợp lệ, ngược lại false
     */
    private boolean isAdminOrEmployee(List<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equalsIgnoreCase("ROLE_USER") ||
                        role.equalsIgnoreCase("ROLE_CUSTOMER") ||
                        role.equalsIgnoreCase("ROLE_ANONYMOUS"));
    }

    /**
     * Hiển thị trang chủ với các sản phẩm tìm kiếm
     * @param model mô hình để truyền dữ liệu vào view
//     * @param searchProductDto đối tượng chứa các tham số tìm kiếm
//     * @param pageable thông tin phân trang
     * @return tên view cho trang chủ
     */

    @GetMapping("/")
    public String getHomePageWithSearch(Model model){
return "user/home";
    }


}
